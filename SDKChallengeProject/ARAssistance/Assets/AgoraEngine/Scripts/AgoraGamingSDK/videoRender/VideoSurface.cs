using UnityEngine;
using UnityEngine.UI;
using System.Runtime.InteropServices;
using System;
namespace agora_gaming_rtc
{
    /* This example script demonstrates how to attach
    * video content to a GameObject
    * 
    * Agora engine outputs one local preview video and some
    * remote user video. User ID (int) is used to identify
    * these video streams. 0 is used for local preview video
    * stream, and other value stands for remote user video
    * stream.
    */

    /** The definition of AgoraVideoSurfaceType.
    */
    public enum AgoraVideoSurfaceType
    {
        /** 0: (Default) The renderer for rendering 3D GameObject, such as Cube、Cylinder and Plane.*/
        Renderer = 0,
        /** 1: The renderer for rendering Raw Image of the UI components. */
        RawImage = 1,
    };

    /** The definition of VideoSurface. */
    public class VideoSurface : MonoBehaviour
    {
        public bool resize = false;
    public bool expert = false;
        private System.IntPtr data = Marshal.AllocHGlobal(1920 * 1080 * 4);
        private int defWidth = 0;
        private int defHeight = 0;
        private Texture2D nativeTexture;
        private bool initRenderMode = false;
        private VideoRender videoRender = null;
        private uint gameFps = 4;
        private uint updateVideoFrameCount = 0;
        private bool _enableFlipHorizontal = false;
        private bool _enableFlipVertical = false;

        [SerializeField]
        AgoraVideoSurfaceType VideoSurfaceType = AgoraVideoSurfaceType.Renderer;

        /* only one of the following should be set, depends on VideoSurfaceType */
        private Renderer mRenderer = null;
        private RawImage mRawImage = null;
        private RectTransform mRectTrans = null;
        private bool _initialized = false;

        void Start()
        {
            // render video
            if (VideoSurfaceType == AgoraVideoSurfaceType.Renderer)
            {
                mRenderer = GetComponent<Renderer>();
            }

            if (mRenderer == null || VideoSurfaceType == AgoraVideoSurfaceType.RawImage)
            {
                mRawImage = GetComponent<RawImage>();
                mRectTrans = GetComponent<RectTransform>();
                if (mRawImage != null)
                {
                    // the variable may have been set to default enum but actually it is a RawImage
                    VideoSurfaceType = AgoraVideoSurfaceType.RawImage;
                }
            }

            if (mRawImage == null && mRenderer == null)
            {
                _initialized = false;
                Debug.LogError("Unable to find surface render in VideoSurface component.");
            }
            else
            {
#if UNITY_EDITOR
                // this only applies to Editor, in case of material is too dark
                UpdateShader();
#endif
                _initialized = true;
            }
        }

        // Update is called once per frame
        void Update()
        {
#if UNITY_STANDALONE_WIN || UNITY_EDITOR || UNITY_EDITOR_OSX || UNITY_STANDALONE_OSX || UNITY_ANDROID || UNITY_IOS || UNITY_IPHONE
            if (updateVideoFrameCount >= gameFps)
            {
                updateVideoFrameCount = 0;
            }
            else
            {
                ++updateVideoFrameCount;
                return;
            }
            // process engine messages (TODO: put in some other place)
            IRtcEngine engine = GetEngine();
            if (engine == null || !_initialized)
                return;

            // render video
            uint uid = mUid;
            if (mEnable)
            {
                // create texture if not existent
                if (IsBlankTexture())
                {
                    int tmpi = videoRender.UpdateVideoRawData(uid, data, ref defWidth, ref defHeight);

                    if (tmpi == -1)
                        return;

                    if (defWidth > 0 && defHeight > 0)
                    {
                        try
                        {
                            // create Texture in the first time update data
                            nativeTexture = new Texture2D((int)defWidth, (int)defHeight, TextureFormat.RGBA32, false);
                            nativeTexture.LoadRawTextureData(data, (int)defWidth * (int)defHeight * 4);
                            FlipTextureHorizontal(nativeTexture);
                            FlipTextureVertically(nativeTexture);
                            ApplyTexture(nativeTexture);
                            nativeTexture.Apply();
                        }
                        catch (System.Exception e)
                        {
                            Debug.LogError("Exception e = " + e);
                        }
                    }
                }
                else
                {
                    int width = 0;
                    int height = 0;
                    int tmpi = videoRender.UpdateVideoRawData(uid, data, ref width, ref height);
            //                    Debug.LogError("Width:"+width+"Height:"+height);
                    if (tmpi == -1)
                        return;

                    try
                    {
                        if (width == defWidth && height == defHeight)
                        {
                            /*
                            *  if width and height don't change ,we only need to update data for texture, do not need to create Texture.
                            */
                            nativeTexture.LoadRawTextureData(data, (int)width * (int)height * 4);
                            FlipTextureHorizontal(nativeTexture);
                            FlipTextureVertically(nativeTexture);
                            nativeTexture.Apply();
                        }
                        else
                        {
                            /* 
                            * if width or height changed ,we need to resize texture.
                            */
                            defWidth = width;
                            defHeight = height;
                            nativeTexture.Resize(defWidth, defHeight);
                            nativeTexture.LoadRawTextureData(data, (int)width * (int)height * 4);
                            FlipTextureHorizontal(nativeTexture);
                            FlipTextureVertically(nativeTexture);
                            nativeTexture.Apply();
                        }
                        if (resize)
                        {
//                         Debug.LogError(nativeTexture.width + "||" + nativeTexture.height);
                            float rH = Screen.height;
                            float rW =nativeTexture.width* Screen.height*1f/nativeTexture.height;
                            if(!expert){
                            mRectTrans.sizeDelta = new Vector2(rW, rH);
                            mRectTrans.localScale=new Vector3(1,-1,1);
                            }
                            else{
                            mRectTrans.sizeDelta = new Vector2(rW,rH);     
                               mRectTrans.localScale=Vector3.one;
                            }                      
   
                        }
                    }
                    catch (System.Exception e)
                    {
                        Debug.LogError("Exception e = " + e);
                    }
                }
            }
            else
            {
                if (!IsBlankTexture())
                {
                    ApplyTexture(null);
                }
            }
#endif
        }

        void OnDestroy()
        {
            Debug.Log("VideoSurface OnDestroy");
            if (videoRender != null)
            {
                videoRender.RemoveUserVideoInfo(mUid);
            }

            if (data != IntPtr.Zero)
            {
                Marshal.FreeHGlobal(data);
                data = IntPtr.Zero;
            }
        }

        /** Sets the video rendering frame rate.
        * 
        * @note 
        * - Ensure that you call this method in the main thread.
        * - Ensure that you call this method before binding VideoSurface.cs.
        * 
        * @param fps The real video refreshing frame rate of the program.
        */
        public void SetGameFps(uint fps)
        {
            gameFps = fps / 15; // 15 fix me according to the real video frame rate.
        }

        // call this to render video stream from uid on this game object
        /** Sets the local/remote video.
        * 
        * @note 
        * - Ensure that you call this method in the main thread.
        * - Ensure that you call this method before binding VideoSurface.cs.
        * 
        * @param uid The ID of the remote user, which is retrieved from {@link agora_gaming_rtc.OnUserJoinedHandler OnUserJoinedHandler}. The default value is 0, which means you can see the local video.
        */
        public void SetForUser(uint uid)
        {
            mUid = uid;
            Debug.Log("Set uid " + uid + " for " + gameObject.name);
        }

        /** Enables/Disables the mirror mode when renders the Texture.
        * 
        * @note 
        * - Ensure that you call this method in the main thread.
        * - Ensure that you call this method before binding VideoSurface.cs.
        * 
        * @param enableFlipHorizontal Whether to enable the horizontal mirror mode of Texture.
        * - true: Enable.
        * - false: (Default) Disable.
        * @param enableFlipVertical Whether to enable the vertical mirror mode of Texture.
        * - true: Enable.
        * - false: (Default) Disable.
        */
        public void EnableFilpTextureApply(bool enableFlipHorizontal, bool enableFlipVertical)
        {
            _enableFlipHorizontal = enableFlipHorizontal;
            _enableFlipVertical = enableFlipVertical;
        }

        /** Set the video renderer type.
        * 
        * @param agoraVideoSurfaceType The renderer type, see AgoraVideoSurfaceType.
        */
        public void SetVideoSurfaceType(AgoraVideoSurfaceType agoraVideoSurfaceType)
        {
            VideoSurfaceType = agoraVideoSurfaceType;
        }

        /** Starts/Stops the video rendering.
        * 
        * @param enable Whether to start/stop the video rendering.
        * - true: (Default) Start.
        * - false: Stop.
        */
        public void SetEnable(bool enable)
        {
            mEnable = enable;
        }

        private void FlipTextureHorizontal(Texture2D original)
        {
            if (_enableFlipHorizontal)
            {
                var originalPixels = original.GetPixels();
                Color[] flipped_data = new Color[originalPixels.Length];
                int width = original.width;
                int height = original.height;
                for (int x = 0; x < width; x++)
                {
                    for (int y = 0; y < height; y++)
                    {
                        flipped_data[x + y * width] = originalPixels[width - 1 - x + y * width];
                    }
                }
                original.SetPixels(flipped_data);
            }
        }

        private void FlipTextureVertically(Texture2D original)
        {
            if (_enableFlipVertical)
            {
                var originalPixels = original.GetPixels();
                Color[] newPixels = new Color[originalPixels.Length];
                int width = original.width;
                int rows = original.height;

                for (int x = 0; x < width; x++)
                {
                    for (int y = 0; y < rows; y++)
                    {
                        newPixels[x + y * width] = originalPixels[x + (rows - y - 1) * width];
                    }
                }
                original.SetPixels(newPixels);
            }
        }

        private IRtcEngine GetEngine()
        {
            agora_gaming_rtc.IRtcEngine engine = agora_gaming_rtc.IRtcEngine.QueryEngine();
            if (!initRenderMode && engine != null)
            {
                videoRender = (VideoRender)engine.GetVideoRender();
                videoRender.SetVideoRenderMode(VIDEO_RENDER_MODE.RENDER_RAWDATA);
                videoRender.AddUserVideoInfo(mUid, 0);
                initRenderMode = true;
            }
            return engine;
        }

        private bool IsBlankTexture()
        {
            if (VideoSurfaceType == AgoraVideoSurfaceType.Renderer)
            {
                // if never assigned or assigned texture is not Texture2D, we will consider it blank and create a new one
                return (mRenderer.material.mainTexture == null || !(mRenderer.material.mainTexture is Texture2D));
            }
            else if (VideoSurfaceType == AgoraVideoSurfaceType.RawImage)
            {
                return (mRawImage.texture == null);
            }
            else
            {
                return true;
            }
        }

        /// <summary>
        ///    nativeTexture at the calling point should have created with image data.  This method
        ///    apply this reference to the surface renderer.
        /// </summary>
        private void ApplyTexture(Texture2D texture)
        {
            if (VideoSurfaceType == AgoraVideoSurfaceType.Renderer)
            {
                mRenderer.material.mainTexture = texture;
            }
            else if (VideoSurfaceType == AgoraVideoSurfaceType.RawImage)
            {
                mRawImage.texture = texture;


            }
        }

        /*
        * uid = 0, it means yourself but not others, you can get others uid by Agora Engine CallBack onUserJoined.
        */
        private uint mUid = 0;

        /*
        *if disabled, then no rendering happens
        */
        private bool mEnable = true;


        /*
        *    Updates Shader to unlit on Editor (some Editor version has the default material that can be too dark.
        */
        private void UpdateShader()
        {

            MeshRenderer mesh = GetComponent<MeshRenderer>();
            if (mesh != null)
            {
                mesh.material = new Material(Shader.Find("Unlit/Texture"));
            }
        }
    }
}