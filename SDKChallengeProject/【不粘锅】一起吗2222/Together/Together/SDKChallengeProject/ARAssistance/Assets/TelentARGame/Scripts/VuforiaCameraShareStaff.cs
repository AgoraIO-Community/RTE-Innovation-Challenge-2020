using System.Collections.Generic;
using UnityEngine;
using agora_gaming_rtc;
using UnityEngine.UI;
using System.Globalization;
using System.Runtime.InteropServices;
using System.Collections;
using agora_utilities;
using System.Threading;
using Vuforia;

public class VuforiaCameraShareStaff : MonoBehaviour
{
    public ClientObserverManager clientObserver;
    public CameraImageAccess cameraImageAccess;
    //切换专家模式按钮
    //public Button switchExpert;
    public Text toast;
    #region PRIVATE_MEMBERS
    private PIXEL_FORMAT mPixelFormat = PIXEL_FORMAT.UNKNOWN_FORMAT;
    private bool mFormatRegistered = false;
    #endregion // PRIVATE_MEMBERS

    #region MONOBEHAVIOUR_METHODS

    Texture2D mTexture;
    Rect mRect;
    ShareCameraType currentCameraType;
    private string currentChannel = "";
    public bool useCam = true;
    bool isOnly = true;
    //   public IRtcEngine mRtcEngine;
    // public InputField dd;
    int i = 10;
    void Start()
    {

#if UNITY_EDITOR
        mPixelFormat = PIXEL_FORMAT.GRAYSCALE; // Need Grayscale for Editor
#else
           mPixelFormat = PIXEL_FORMAT.RGB888; // Use RGB888 for mobile
#endif
        // Register Vuforia life-cycle callbacks:
        VuforiaARController.Instance.RegisterVuforiaStartedCallback(OnVuforiaStarted);
        VuforiaARController.Instance.RegisterTrackablesUpdatedCallback(OnTrackablesUpdated);
        VuforiaARController.Instance.RegisterOnPauseCallback(OnPause);
        #endregion // MONOBEHAVIOUR_METHODS
        // 创建 Texture。
        //    mTexture = new Texture2D(640, 480, TextureFormat.RGBA32, false);
    }

    public void JoinRoom(string name)
    {
        SetCam(TelentDrawController.instance.shareCamType);
        JoinChannel(name);

    }
    //是否只存在一个画板
    public void SetOnlyOne(bool only)
    {
        isOnly = only;
        if (isOnly)
            useCam = true;
    }
    public void SwitchCamera()
    {
        if (isOnly)
        {
            useCam=isOnly;
            if (ToastManager.instance)
                ToastManager.instance.AddToast(ToastType.Other, "请先定义环境图");
        }
        else
            useCam = !useCam;
    }
    void JoinChannel(string channelName)
    {

        if (VideoShareEngine.mRtcEngine == null)
        {
            if (ToastManager.instance)
                ToastManager.instance.AddToast(ToastType.Error, "mRtcEngine 为空");
            return;
        }

        // 启用视频模块。
        VideoShareEngine.EnableVideo();
        // 启用视频观测器。
        VideoShareEngine.mRtcEngine.EnableVideoObserver();
        // set callbacks (optional)
        VideoShareEngine.mRtcEngine.OnJoinChannelSuccess = onJoinChannelSuccess;
        VideoShareEngine.mRtcEngine.OnUserJoined = onUserJoined;
        VideoShareEngine.mRtcEngine.OnUserOffline = onUserOffline;
        VideoShareEngine.mRtcEngine.OnReJoinChannelSuccess = onReJoinChannelSuccess;
        VideoShareEngine.mRtcEngine.OnRemoteVideoStateChanged = onRemoteVideoStateChanged;
        // join channel
        int tt = VideoShareEngine.mRtcEngine.JoinChannel(channelName, null, 0);
        if (ToastManager.instance)
            ToastManager.instance.AddToast(ToastType.Other, "加入频道返回：" + tt);
        // Optional: if a data stream is required, here is a good place to create it
        int streamID = VideoShareEngine.mRtcEngine.CreateDataStream(true, true);
        Debug.Log("initializeEngine done, data stream id = " + streamID);
    }
    void SetCam(ShareCameraType shareCamType)
    {
        if (VideoShareEngine.mRtcEngine == null)
            return;
        if (shareCamType == ShareCameraType.Screen)
        {
            // 配置外部视频源。
            VideoShareEngine.mRtcEngine.SetExternalVideoSource(true, false);
            // 创建需共享的屏幕区域。
            //  mRect = new Rect(0, 0, Screen.width, Screen.height);
            // 创建 Texture。
            //   mTexture = new Texture2D((int)mRect.width, (int)mRect.height, TextureFormat.RGBA32, false);
            currentCameraType = ShareCameraType.Screen;
            Debug.Log("设置成功：" + currentCameraType);
        }
        else
        {
            if (currentCameraType != shareCamType)
            {
                VideoShareEngine.mRtcEngine.SwitchCamera();
                currentCameraType = shareCamType;
            }
        }

    }
    public string getSdkVersion()
    {
        string ver = IRtcEngine.GetSdkVersion();
        if (ver == "2.9.1.45")
        {
            ver = "2.9.2";  // A conversion for the current internal version#
        }
        else
        {
            if (ver == "2.9.1.46")
            {
                ver = "2.9.2.2";  // A conversion for the current internal version#
            }
        }
        return ver;
    }

    //是否开启视频
    public void MultLocalVideo()
    {
        int temp = VideoShareEngine.MultLocalVideo();
        if (temp == 0)
        {
            if (ToastManager.instance)
                ToastManager.instance.AddToast(ToastType.Info, "操作成功");
        }
        else
        {
            if (ToastManager.instance)
                ToastManager.instance.AddToast(ToastType.Error, "操作失败");
        }
    }
    public void SetMuteLocalAudio()
    {
        int temp = VideoShareEngine.MuteLocalAudio();
        if (temp == 0)
        {
            if (ToastManager.instance)
                ToastManager.instance.AddToast(ToastType.Info, "操作成功");
        }
        else
        {
            if (ToastManager.instance)
                ToastManager.instance.AddToast(ToastType.Error, "操作失败");
        }
    }
    public void onRemoteVideoStateChanged(uint uid, REMOTE_VIDEO_STATE state, REMOTE_VIDEO_STATE_REASON reason, int elapsed)
    {
        //        Debug.LogError("RemoteVideo:" + uid + "reason:" + reason.ToString());
        switch (reason)
        {
            case REMOTE_VIDEO_STATE_REASON.REMOTE_VIDEO_STATE_REASON_REMOTE_MUTED:
                clientObserver.removeObserver(uid);
                break;
            case REMOTE_VIDEO_STATE_REASON.REMOTE_VIDEO_STATE_REASON_REMOTE_UNMUTED:
                //   Debug.LogError("UnMult");
                try
                {
                    clientObserver.AddObserver(uid);

                }
                catch (System.Exception e)
                {
                    Debug.LogError(e.Message);
                }

                break;
        }

    }
    public void leave()
    {
        Debug.Log("calling leave");
        VideoShareEngine.leave();
        //返回主场景
        if (TelentDrawController.instance)
            TelentDrawController.instance.LoadGame("Main");
    }

    // unload agora engine
    public void unloadEngine()
    {
        Debug.Log("calling unloadEngine");

        VideoShareEngine.unloadEngine();
    }
    // implement engine callbacks
    private void onJoinChannelSuccess(string channelName, uint uid, int elapsed)
    {
        Debug.Log("JoinChannelSuccessHandler: uid = " + uid);
        GameObject textVersionGameObject = GameObject.Find("VersionText");
        //   textVersionGameObject.GetComponent<Text>().text = "SDK Version : " + getSdkVersion();
        //  onSceneHelloVideoLoaded();
    }
    public void onReJoinChannelSuccess(string channelName, uint uid, int elapsed)
    {
        Debug.Log("OnReJoinChannelSuccessHandler  channelName = " + channelName + " ,uid = " + uid + " ,elapsed = " + elapsed);
    }

    // When a remote user joined, this delegate will be called. Typically
    // create a GameObject to render video on it
    private void onUserJoined(uint uid, int elapsed)
    {
        Debug.Log("onUserJoined: uid = " + uid + " elapsed = " + elapsed);
        // this is called in main thread
        clientObserver.AddObserver(uid);

    }

    private const float Offset = 100;
    // When remote user is offline, this delegate will be called. Typically
    // delete the GameObject for this user
    private void onUserOffline(uint uid, USER_OFFLINE_REASON reason)
    {

        // remove video stream
        Debug.Log("onUserOffline: uid = " + uid + " reason = " + reason);
        // this is called in main thread
        clientObserver.removeObserver(uid);
    }


    void Update()
    {
        if (!VideoShareEngine.isVideo)
           return;
        if (useCam)
            ShareCam(mTexture);
        else
            ShareCam(cameraImageAccess.GetRawTexture());
    }
    void ShareCam(Texture2D tex)
    {
        if (tex == null)
            return;

        byte[] bytes = tex.GetRawTextureData();
//                    Debug.Log(tex.width+"|"+tex.height+"|"+bytes.Length);
        int size = Marshal.SizeOf(bytes[0]) * bytes.Length;
        // 查询是否存在 IRtcEngine 实例。
        IRtcEngine rtc = IRtcEngine.QueryEngine();
        if (rtc != null)
        {
            //   921600
            // 创建外部视频帧。

            ExternalVideoFrame externalVideoFrame = new ExternalVideoFrame();
            // 设置视频帧 buffer 类型。
            externalVideoFrame.type = ExternalVideoFrame.VIDEO_BUFFER_TYPE.VIDEO_BUFFER_RAW_DATA;
            // 设置像素格式。
            //     if(useCam)
            //    externalVideoFrame.format = ExternalVideoFrame.VIDEO_PIXEL_FORMAT.VIDEO_PIXEL_NV12;
            //   else
            externalVideoFrame.format = ExternalVideoFrame.VIDEO_PIXEL_FORMAT.VIDEO_PIXEL_BGRA;
            // 应用原始数据。
            externalVideoFrame.buffer = bytes;
            // 设置视频帧宽度（pixel）。
            externalVideoFrame.stride = tex.width;
            // 设置视频帧高度（pixel）。
            externalVideoFrame.height = tex.height;
            // 设置从哪侧移除视频帧的像素。
            externalVideoFrame.cropLeft = 0;
            externalVideoFrame.cropTop = 0;
            externalVideoFrame.cropRight = 0;
            externalVideoFrame.cropBottom = 0;
            // 设置视频帧旋转角度： 0、90、180 或 270。
            externalVideoFrame.rotation = 180;

            // 使用视频时间戳增加 i。
            externalVideoFrame.timestamp = i++;
            // 推送外部视频帧。
            int a = rtc.PushVideoFrame(externalVideoFrame);
        }
    }
    private void FixedUpdate()
    {

    }
    // 开始屏幕共享。
    /*IEnumerator shareScreen()
    {
        yield return new WaitForEndOfFrame();
        // 读取屏幕像素。
        mTexture.ReadPixels(mRect, 0, 0);
        // 应用像素。
        mTexture.Apply();
        int x = (int)mRect.width / 3;
        int y = (int)mRect.height / 3;
        TextureScale.Bilinear(mTexture, x, y);
        // 获取 Raw Texture 并将其应用到字节数组中。
        byte[] bytes = mTexture.GetRawTextureData();
    }*/
    private void OnVuforiaStarted()
    {
        // Vuforia has started, now register camera image format  
        if (CameraDevice.Instance.SetFrameFormat(mPixelFormat, true))
        {
            Debug.Log("Successfully registered pixel format " + mPixelFormat.ToString());
            mFormatRegistered = true;
        }
        else
        {
            Debug.LogError(
              "Failed to register pixel format " + mPixelFormat.ToString() +
              "\n the format may be unsupported by your device;" +
              "\n consider using a different pixel format.");

            mFormatRegistered = false;
        }
    }


    /// Called each time the Vuforia state is updated
    /// unitunity
    void OnTrackablesUpdated()
    {
        if (mFormatRegistered)
        {
            if (useCam)
            {
                Vuforia.Image image = CameraDevice.Instance.GetCameraImage(mPixelFormat);
                if (image != null)
                {
                    /*                      Debug.Log(
                                              "\nImage Format: " + image.PixelFormat +
                                              "\nImage Size:   " + image.Width + "x" + image.Height +
                                              "\nBuffer Size:  " + image.BufferWidth + "x" + image.BufferHeight +
                                              "\nImage Stride: " + image.Stride + "\n"
                                          );*/
                    if (mTexture == null || mTexture.width != image.Width)
                    {
                        mTexture = new Texture2D((int)image.Width, (int)image.Height, TextureFormat.RGBA32, false);
                    }
                    byte[] pixels = image.Pixels;
                    if (pixels != null && pixels.Length > 0)
                    {
                        // Debug.Log("\nImage pixels: " + pixels.Length);
                        /*   Debug.Log(
                               "\nImage pixels: " +
                               pixels[0] + ", " +
                               pixels[1] + ", " +
                               pixels[2] + ", ...\n"
                           );*/
                        //    toast.text = "x: " + x + "y:" + y + "b:" + bytes.Length;

                        // 转化为Texture2D
                      //  image.CopyToTexture(mTexture);
                        mTexture.SetPixels32(ImageToColor32(image));
                        mTexture.Apply();
                        //   byte[] bytes = mTexture.GetRawTextureData();
                        //    ShareCam(image.Width, image.Height, bytes);

                    }
                }
            }
        }
    }
     //RGBA8888 to RGBA32
    Color32[] ImageToColor32(Vuforia.Image a)
    {
        Color32[] r = new Color32[a.BufferWidth * a.BufferHeight];
        for (int i = 0; i < r.Length; i++)
        {
            r[i].b = a.Pixels[i * 3];
            r[i].g = a.Pixels[i * 3 + 1];
            r[i].r = a.Pixels[i * 3 + 2];
            r[i].a = 1;
        }
        return r;
    }
    /// 
    /// Called when app is paused / resumed
    /// 
    void OnPause(bool paused)
    {
        if (paused)
        {
            Debug.Log("App was paused");
            UnregisterFormat();
        }
        else
        {
            Debug.Log("App was resumed");
            RegisterFormat();
        }
    }
    /// 
    /// Register the camera pixel format
    /// 
    void RegisterFormat()
    {
        if (CameraDevice.Instance.SetFrameFormat(mPixelFormat, true))
        {
            Debug.Log("Successfully registered camera pixel format " + mPixelFormat.ToString());
            mFormatRegistered = true;
        }
        else
        {
            Debug.LogError("Failed to register camera pixel format " + mPixelFormat.ToString());
            mFormatRegistered = false;
        }
    }
    /// 
    /// Unregister the camera pixel format (e.g. call this when app is paused)
    /// 
    void UnregisterFormat()
    {
        Debug.Log("Unregistering camera pixel format " + mPixelFormat.ToString());
        CameraDevice.Instance.SetFrameFormat(mPixelFormat, false);
        mFormatRegistered = false;
    }
}