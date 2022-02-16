using System.Threading;
using UnityEngine;
using System.IO;
using System.Collections;

public class TextureUtility
{
    public class ThreadData
    {
        public int start;
        public int end;
        public ThreadData(int s, int e)
        {
            start = s;
            end = e;
        }
    }

    private static Color[] texColors;
    private static Color[] newColors;
    private static int w;
    private static float ratioX;
    private static float ratioY;
    private static int w2;
    private static int finishCount;
    private static Mutex mutex;

    public static void ScalePoint(Texture2D tex, int newWidth, int newHeight)
    {
        ThreadedScale(tex, newWidth, newHeight, false);
    }

    public static void ScaleBilinear(Texture2D tex, int newWidth, int newHeight)
    {
        ThreadedScale(tex, newWidth, newHeight, true);
    }

    private static void ThreadedScale(Texture2D tex, int newWidth, int newHeight, bool useBilinear)
    {
        texColors = tex.GetPixels();
        newColors = new Color[newWidth * newHeight];
        if (useBilinear)
        {
            ratioX = 1.0f / ((float)newWidth / (tex.width - 1));
            ratioY = 1.0f / ((float)newHeight / (tex.height - 1));
        }
        else
        {
            ratioX = ((float)tex.width) / newWidth;
            ratioY = ((float)tex.height) / newHeight;
        }
        w = tex.width;
        w2 = newWidth;
        var cores = Mathf.Min(SystemInfo.processorCount, newHeight);
        var slice = newHeight / cores;

        finishCount = 0;
        if (mutex == null)
        {
            mutex = new Mutex(false);
        }
        if (cores > 1)
        {
            int i = 0;
            ThreadData threadData;
            for (i = 0; i < cores - 1; i++)
            {
                threadData = new ThreadData(slice * i, slice * (i + 1));
                ParameterizedThreadStart ts = useBilinear ? new ParameterizedThreadStart(BilinearScale) : new ParameterizedThreadStart(PointScale);
                Thread thread = new Thread(ts);
                thread.Start(threadData);
            }
            threadData = new ThreadData(slice * i, newHeight);
            if (useBilinear)
            {
                BilinearScale(threadData);
            }
            else
            {
                PointScale(threadData);
            }
            while (finishCount < cores)
            {
                Thread.Sleep(1);
            }
        }
        else
        {
            ThreadData threadData = new ThreadData(0, newHeight);
            if (useBilinear)
            {
                BilinearScale(threadData);
            }
            else
            {
                PointScale(threadData);
            }
        }

        tex.Resize(newWidth, newHeight);
        tex.SetPixels(newColors);
        tex.Apply();
    }

    public static void BilinearScale(System.Object obj)
    {
        ThreadData threadData = (ThreadData)obj;
        for (var y = threadData.start; y < threadData.end; y++)
        {
            int yFloor = (int)Mathf.Floor(y * ratioY);
            var y1 = yFloor * w;
            var y2 = (yFloor + 1) * w;
            var yw = y * w2;

            for (var x = 0; x < w2; x++)
            {
                int xFloor = (int)Mathf.Floor(x * ratioX);
                var xLerp = x * ratioX - xFloor;
                newColors[yw + x] = ColorLerpUnclamped(ColorLerpUnclamped(texColors[y1 + xFloor], texColors[y1 + xFloor + 1], xLerp),
                ColorLerpUnclamped(texColors[y2 + xFloor], texColors[y2 + xFloor + 1], xLerp),
                y * ratioY - yFloor);
            }
        }

        mutex.WaitOne();
        finishCount++;
        mutex.ReleaseMutex();
    }

    public static void PointScale(System.Object obj)
    {
        ThreadData threadData = (ThreadData)obj;
        for (var y = threadData.start; y < threadData.end; y++)
        {
            var thisY = (int)(ratioY * y) * w;
            var yw = y * w2;
            for (var x = 0; x < w2; x++)
            {
                newColors[yw + x] = texColors[(int)(thisY + ratioX * x)];
            }
        }

        mutex.WaitOne();
        finishCount++;
        mutex.ReleaseMutex();
    }

    private static Color ColorLerpUnclamped(Color c1, Color c2, float value)
    {
        return new Color(c1.r + (c2.r - c1.r) * value,
        c1.g + (c2.g - c1.g) * value,
        c1.b + (c2.b - c1.b) * value,
        c1.a + (c2.a - c1.a) * value);
    }

    public static Texture2D LoadTexture(string filePath)
    {
        Texture2D tex = null;
        byte[] fileData;

        if (File.Exists(filePath))
        {
            fileData = File.ReadAllBytes(filePath);
            tex = new Texture2D(2, 2, TextureFormat.ARGB32, false);
            tex.LoadImage(fileData);
        }
        return tex;
    }

    // Save ScreenShot
/*
    public static void SaveScreenShotAsync(string path, Vector2 size)
    {
        SWMain.sharedSWMain.StartCoroutine(TextureUtility.SaveScreenShot(path, size));
    }

    public static void SaveScreenShotAsync(string path, Rect rect, Vector2 size)
    {
        SWMain.sharedSWMain.StartCoroutine(TextureUtility.SaveScreenShot(path, rect, size));
    }
    
    public static IEnumerator SaveScreenShot(string path, Vector2 size)
    {
        Rect rect = new Rect(0, 0, Screen.width, Screen.height);
        yield return SWMain.sharedSWMain.StartCoroutine(TextureUtility.SaveScreenShot(path, rect, size));
    }
*/
    public static IEnumerator SaveScreenShot(string path, Rect rect, Vector2 size = default(Vector2))
    {
        // We should only read the screen bufferafter rendering is complete
        yield return new WaitForEndOfFrame();
        // 要保存图片的大小
        Texture2D tex = new Texture2D((int)rect.width, (int)rect.height, TextureFormat.RGB24, false);
        // 截取的区域
        tex.ReadPixels(rect, 0, 0);
        tex.Apply();
        // 把图片数据转换为byte数组
        if (size != default(Vector2))
        {
            ScaleBilinear(tex, (int)size.x, (int)size.y);
        }

        byte[] buffer = tex.EncodeToJPG(100);
        Object.Destroy(tex);
        // 然后保存为图片
        File.WriteAllBytes(path, buffer);
    }

    public static Texture2D Copy(Texture2D tex)
    {
        return new Texture2D(tex.width, tex.height, tex.format, false);
    }

    /// <summary>
    /// Applies sepia effect to the texture.
    /// </summary>
    /// <param name="tex"> Texture to process.</param>
    public static Texture2D SetSepia(Texture2D tex)
    {
        Texture2D t = Copy(tex);
        Color[] colors = tex.GetPixels();

        for (int i = 0; i < colors.Length; i++)
        {
            float alpha = colors[i].a;
            float grayScale = ((colors[i].r * .299f) + (colors[i].g * .587f) + (colors[i].b * .114f));
            Color c = new Color(grayScale, grayScale, grayScale);
            colors[i] = new Color(c.r * 1, c.g * 0.95f, c.b * 0.82f, alpha);
        }
        t.SetPixels(colors);
        t.Apply();
        return t;
    }

    /// <summary>
    /// Applies grayscale effect to the texture and changes colors to grayscale.
    /// </summary>
    /// <param name="tex"> Texture to process.</param>
    public static Texture2D SetGrayscale(Texture2D tex)
    {
        Texture2D t = Copy(tex);

        Color[] colors = tex.GetPixels();
        for (int i = 0; i < colors.Length; i++)
        {
            float val = (colors[i].r + colors[i].g + colors[i].b) / 3;
            colors[i] = new Color(val, val, val);
        }
        t.SetPixels(colors);
        t.Apply();
        return t;
    }

    /// <summary>
    /// Pixelates the texture.
    /// </summary>
    /// <param name="tex"> Texture to process.</param>
    /// <param name="size"> Size of the pixel.</param>
    public static Texture2D SetPixelate(Texture2D tex, int size)
    {
        Texture2D t = Copy(tex);
        Rect rectangle = new Rect(0, 0, tex.width, tex.height);
        for (int xx = (int)rectangle.x; xx < rectangle.x + rectangle.width && xx < tex.width; xx += size)
        {
            for (int yy = (int)rectangle.y; yy < rectangle.y + rectangle.height && yy < tex.height; yy += size)
            {
                int offsetX = size / 2;
                int offsetY = size / 2;
                while (xx + offsetX >= tex.width) offsetX--;
                while (yy + offsetY >= tex.height) offsetY--;
                Color pixel = tex.GetPixel(xx + offsetX, yy + offsetY);
                for (int x = xx; x < xx + size && x < tex.width; x++)
                    for (int y = yy; y < yy + size && y < tex.height; y++)
                        t.SetPixel(x, y, pixel);
            }
        }

        t.Apply();
        return t;
    }

    /// <summary>
    /// Inverts colors of the texture.
    /// </summary>
    /// <param name="tex"> Texture to process.</param>
    public static Texture2D SetNegative(Texture2D tex)
    {
        Texture2D t = Copy(tex);
        Color[] colors = tex.GetPixels();
        Color pixel;

        for (int i = 0; i < colors.Length; i++)
        {
            pixel = colors[i];
            colors[i] = new Color(1 - pixel.r, 1 - pixel.g, 1 - pixel.b);
        }
        t.SetPixels(colors);
        t.Apply();
        return t;
    }

    /// <summary>
    /// Sets the foggy effect.雾化效果
    /// </summary>
    /// <returns>texture processed.</returns>
    /// <param name="tex">texture to process.</param>
    public static Texture2D SetFoggy(Texture2D tex)
    {
        Texture2D t = Copy(tex);
        Color pixel;

        for (int x = 1; x < tex.width - 1; x++)
            for (int y = 1; y < tex.height - 1; y++)
            {
                int k = UnityEngine.Random.Range(0, 123456);
                //像素块大小
                int dx = x + k % 19;
                int dy = y + k % 19;
                if (dx >= tex.width)
                    dx = tex.width - 1;
                if (dy >= tex.height)
                    dy = tex.height - 1;
                pixel = tex.GetPixel(dx, dy);
                t.SetPixel(x, y, pixel);
            }

        t.Apply();

        return t;
    }

    /// <summary>
    /// Sets the soft effect.柔化效果
    /// </summary>
    /// <returns>texture processed.</returns>
    /// <param name="tex">texture to process.</param>
    public static Texture2D SetSoft(Texture2D tex)
    {
        Texture2D t = Copy(tex);
        int[] Gauss = { 1, 2, 1, 2, 4, 2, 1, 2, 1 };
        for (int x = 1; x < tex.width - 1; x++)
            for (int y = 1; y < tex.height - 1; y++)
            {
                float r = 0, g = 0, b = 0;
                int Index = 0;
                for (int col = -1; col <= 1; col++)
                    for (int row = -1; row <= 1; row++)
                    {
                        Color pixel = tex.GetPixel(x + row, y + col);
                        r += pixel.r * Gauss[Index];
                        g += pixel.g * Gauss[Index];
                        b += pixel.b * Gauss[Index];
                        Index++;
                    }
                r /= 16;
                g /= 16;
                b /= 16;
                //处理颜色值溢出
                r = r > 1 ? 1 : r;
                r = r < 0 ? 0 : r;
                g = g > 1 ? 1 : g;
                g = g < 0 ? 0 : g;
                b = b > 1 ? 1 : b;
                b = b < 0 ? 0 : b;
                t.SetPixel(x - 1, y - 1, new Color(r, g, b));
            }

        t.Apply();

        return t;
    }

    /// <summary>
    /// Sets the sharp.锐化效果
    /// </summary>
    /// <returns>The sharp.</returns>
    /// <param name="tex">Tex.</param>
    public static Texture2D SetSharp(Texture2D tex)
    {
        Texture2D t = Copy(tex);
        Color pixel;
        //拉普拉斯模板
        int[] Laplacian = { -1, -1, -1, -1, 9, -1, -1, -1, -1 };
        for (int x = 1; x < tex.width - 1; x++)
            for (int y = 1; y < tex.height - 1; y++)
            {
                float r = 0, g = 0, b = 0;
                int index = 0;
                for (int col = -1; col <= 1; col++)
                    for (int row = -1; row <= 1; row++)
                    {
                        pixel = tex.GetPixel(x + row, y + col); r += pixel.r * Laplacian[index];
                        g += pixel.g * Laplacian[index];
                        b += pixel.b * Laplacian[index];
                        index++;
                    }
                //处理颜色值溢出
                r = r > 1 ? 1 : r;
                r = r < 0 ? 0 : r;
                g = g > 1 ? 1 : g;
                g = g < 0 ? 0 : g;
                b = b > 1 ? 1 : b;
                b = b < 0 ? 0 : b;
                t.SetPixel(x - 1, y - 1, new Color(r, g, b));
            }

        t.Apply();
        return t;
    }

    /// <summary>
    /// Sets the relief.浮雕效果
    /// </summary>
    /// <returns>The relief.</returns>
    /// <param name="tex">Tex.</param>
    public static Texture2D SetRelief(Texture2D tex)
    {
        Texture2D t = Copy(tex);

        for (int x = 0; x < tex.width - 1; x++)
        {
            for (int y = 0; y < tex.height - 1; y++)
            {
                float r = 0, g = 0, b = 0;
                Color pixel1 = tex.GetPixel(x, y);
                Color pixel2 = tex.GetPixel(x + 1, y + 1);
                r = Mathf.Abs(pixel1.r - pixel2.r + 0.5f);
                g = Mathf.Abs(pixel1.g - pixel2.g + 0.5f);
                b = Mathf.Abs(pixel1.b - pixel2.b + 0.5f);
                if (r > 1)
                    r = 1;
                if (r < 0)
                    r = 0;
                if (g > 1)
                    g = 1;
                if (g < 0)
                    g = 0;
                if (b > 1)
                    b = 1;
                if (b < 0)
                    b = 0;
                t.SetPixel(x, y, new Color(r, g, b));
            }
        }

        t.Apply();
        return t;
    }

}