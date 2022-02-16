using UnityEngine;
using System.IO;
using System.Collections;

#if UNITY_EDITOR
using UnityEditor;
#pragma warning disable 0219
#endif

public class RenderTextureCamera : MonoBehaviour
{
	[Space(20)]
	public int TextureResolution = 512;

    private string screensPath;
    private int TextureResolutionX;
	private int TextureResolutionY;

	private Camera Render_Texture_Camera;
	private RenderTexture CameraOutputTexture;

    public RenderTexture GetRenderTexture()
    {

        return CameraOutputTexture;
    }

	void Start() 
	{
		Render_Texture_Camera = GetComponent<Camera>();
		StartRenderingToTexture();
	}

    void StartRenderingToTexture()      // Note: RenderTexture will be delayed by one frame
    {
        if (transform.lossyScale.x >= transform.lossyScale.y)
        {
            TextureResolutionX = TextureResolution;
            TextureResolutionY = (int)(TextureResolution * transform.lossyScale.y / transform.lossyScale.x);
        }

        if (transform.lossyScale.x < transform.lossyScale.y)
        {
            TextureResolutionX = (int)(TextureResolution * transform.lossyScale.x / transform.lossyScale.y);
            TextureResolutionY = TextureResolution;
        }

        if (CameraOutputTexture)
        {
            Render_Texture_Camera.targetTexture = null;
            CameraOutputTexture.Release();
            CameraOutputTexture = null;
        }

        CameraOutputTexture = new RenderTexture(TextureResolutionX, TextureResolutionY, 0);
        CameraOutputTexture.Create();
        Render_Texture_Camera.targetTexture = CameraOutputTexture;

        if (transform.parent) 
        gameObject.layer = transform.parent.gameObject.layer;
      //  Render_Texture_Camera.cullingMask = 1 << gameObject.layer;
    }
	
    public void MakeScreen() 
	{
        StartRenderingToTexture();  // Restart

        if (screensPath == null) 
		{
            #if UNITY_ANDROID && !UNITY_EDITOR
			screensPath = Application.temporaryCachePath;       // Also you can create a custom folder, like: screensPath = "/sdcard/DCIM/RegionCapture";

            #elif UNITY_IPHONE && !UNITY_EDITOR
            screensPath = Application.temporaryCachePath;       // Also you can use persistent DataPath on IOS: screensPath = Application.persistentDataPath;

            #else
            screensPath = Application.dataPath + "/Screens";    // Editor Mode

            #endif
            if (!Directory.Exists(screensPath))
            Directory.CreateDirectory(screensPath);
        }
        StartCoroutine(TakeScreen());
    }

    private IEnumerator TakeScreen() 
	{
        yield return new WaitForEndOfFrame();

        Texture2D FrameTexture = new Texture2D(CameraOutputTexture.width, CameraOutputTexture.height, TextureFormat.RGBA32, false);
        RenderTexture.active = CameraOutputTexture;
        FrameTexture.ReadPixels(new Rect(0, 0, CameraOutputTexture.width, CameraOutputTexture.height), 0, 0);
        RenderTexture.active = null;

        FrameTexture.Apply();
		saveImg(FrameTexture.EncodeToPNG());
    }

    private string saveImg(byte[] imgPng)
    {
        string fileName = screensPath + "/screen_" + System.DateTime.Now.ToString("dd_MM_HH_mm_ss") + ".png";

        Debug.Log("write to " + fileName);

        File.WriteAllBytes(fileName, imgPng);

		#if UNITY_EDITOR
		AssetDatabase.Refresh();
		#endif

        return fileName;
    }
}