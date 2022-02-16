using System.Collections.Generic;
using UnityEngine;
using agora_gaming_rtc;
using UnityEngine.UI;
using System.Globalization;
using System.Runtime.InteropServices;
using System.Collections;
using agora_utilities;
using System.Threading;

public class VideoShareController : MonoBehaviour
{
    public ClientObserverManager clientObserver;
    public Text toast;
    bool share = false;
    Texture2D mTexture;
    Rect mRect;
    ShareCameraType currentCameraType;
    private string currentChannel = "";
    Queue<byte[]> outImageBuffer;
    bool stop = false;
    //   public IRtcEngine mRtcEngine;
    // public InputField dd;
    int i = 100;

    public void JoinRoom(string name)
    {
        outImageBuffer = new Queue<byte[]>();
        SwitchCam(TelentDrawController.instance.shareCamType);
        JoinChannel(name);
        //  Thread tt = new Thread(PushVideo);
        //  tt.Start();
    }
    public void SetStaffVideoType(bool expert)
    {
        clientObserver.SetStaffVideoType(expert);
    }
    void JoinChannel(string channelName)
    {

        if (VideoShareEngine.mRtcEngine == null)
            return;
        if (currentChannel != "")
        {
            Debug.Log("请退出当前Channel" + currentChannel);
        }
        // 启用视频模块。
        VideoShareEngine.mRtcEngine.EnableVideo();
        // 启用视频观测器。
        VideoShareEngine.mRtcEngine.EnableVideoObserver();
        // set callbacks (optional)
        VideoShareEngine.mRtcEngine.OnJoinChannelSuccess = onJoinChannelSuccess;
        VideoShareEngine.mRtcEngine.OnUserJoined = onUserJoined;
        VideoShareEngine.mRtcEngine.OnUserOffline = onUserOffline;
        VideoShareEngine.mRtcEngine.OnReJoinChannelSuccess = onReJoinChannelSuccess;
        VideoShareEngine.mRtcEngine.OnRemoteVideoStateChanged = onRemoteVideoStateChanged;
        // join channel
        VideoShareEngine.mRtcEngine.JoinChannel(channelName, null, 0);
        // Optional: if a data stream is required, here is a good place to create it
        int streamID = VideoShareEngine.mRtcEngine.CreateDataStream(true, true);
        Debug.Log("initializeEngine done, data stream id = " + streamID);
    }
    public void SwitchCam()
    {
        /* if (currentCameraType == ShareCameraType.FrontCam)
         {
             currentCameraType = ShareCameraType.BackCam;
         }*/
        VideoShareEngine.mRtcEngine.SwitchCamera();
    }
    void SwitchCam(ShareCameraType shareCamType)
    {
        if (VideoShareEngine.mRtcEngine == null)
            return;
        if (shareCamType == ShareCameraType.Screen)
        {
            // 配置外部视频源。
            VideoShareEngine.mRtcEngine.SetExternalVideoSource(true, false);
            // 创建需共享的屏幕区域。
            mRect = new Rect(0, 0, Screen.width, Screen.height);
            // 创建 Texture。
            mTexture = new Texture2D((int)mRect.width, (int)mRect.height, TextureFormat.BGRA32, false);
            currentCameraType = ShareCameraType.Screen;
            share = true;
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
    public void leave()
    {
        Debug.Log("calling leave");

        VideoShareEngine.leave();
        //返回主场景
        if (TelentDrawController.instance)
            TelentDrawController.instance.LoadGame("Main");
        stop = true;
    }

    // unload agora engine
    public void unloadEngine()
    {
        Debug.Log("calling unloadEngine");

        VideoShareEngine.unloadEngine();
    }


    public void EnableVideo(bool pauseVideo)
    {
        if (!pauseVideo)
        {
            VideoShareEngine.EnableVideo();
        }
        else
        {
            VideoShareEngine.DisableVideo();
        }

    }

    // accessing GameObject in Scnene1
    // set video transform delegate for statically created GameObject
    public void onSceneHelloVideoLoaded()
    {
        // Attach the SDK Script VideoSurface for video rendering
        GameObject quad = GameObject.Find("Quad");
        if (ReferenceEquals(quad, null))
        {
            Debug.Log("BBBB: failed to find Quad");
            return;
        }
        else
        {
            quad.AddComponent<VideoSurface>();
        }

        GameObject cube = GameObject.Find("Cube");
        if (ReferenceEquals(cube, null))
        {
            Debug.Log("BBBB: failed to find Cube");
            return;
        }
        else
        {
            cube.AddComponent<VideoSurface>();
        }
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
        // find a game object to render video stream from 'uid'
     /*   GameObject go = GameObject.Find(uid.ToString());
        if (!ReferenceEquals(go, null))
        {
            return; // reuse
        }
     */
        // create a GameObject and assign to this new user
        /*   VideoSurface videoSurface = makeImageSurface(uid.ToString());
           if (!ReferenceEquals(videoSurface, null))
           {
               // configure videoSurface
               videoSurface.SetForUser(uid);
               videoSurface.SetEnable(true);
               videoSurface.SetVideoSurfaceType(AgoraVideoSurfaceType.RawImage);
               videoSurface.SetGameFps(30);
           }*/
    }

    public VideoSurface makePlaneSurface(string goName)
    {
        GameObject go = GameObject.CreatePrimitive(PrimitiveType.Plane);

        if (go == null)
        {
            return null;
        }
        go.name = goName;
        // set up transform
        go.transform.Rotate(-90.0f, 0.0f, 0.0f);
        float yPos = UnityEngine.Random.Range(3.0f, 5.0f);
        float xPos = UnityEngine.Random.Range(-2.0f, 2.0f);
        go.transform.position = new Vector3(xPos, yPos, 0f);
        go.transform.localScale = new Vector3(0.25f, 0.5f, .5f);

        // configure videoSurface
        VideoSurface videoSurface = go.AddComponent<VideoSurface>();
        return videoSurface;
    }

    private const float Offset = 100;
    public VideoSurface makeImageSurface(string goName)
    {
        GameObject go = new GameObject();

        if (go == null)
        {
            return null;
        }

        go.name = goName;

        // to be renderered onto
        go.AddComponent<RawImage>();

        // make the object draggable
        go.AddComponent<UIElementDragger>();
        GameObject canvas = GameObject.Find("Canvas");
        if (canvas != null)
        {
            go.transform.parent = canvas.transform;
        }
        // set up transform
        go.transform.Rotate(0f, 0.0f, 180.0f);
        float xPos = Random.Range(Offset - Screen.width / 2f, Screen.width / 2f - Offset);
        float yPos = Random.Range(Offset, Screen.height / 2f - Offset);
        go.transform.localPosition = new Vector3(xPos, yPos, 0f);
        go.transform.localScale = new Vector3(3f, 4f, 1f);

        // configure videoSurface
        VideoSurface videoSurface = go.AddComponent<VideoSurface>();
        return videoSurface;
    }
    // When remote user is offline, this delegate will be called. Typically
    // delete the GameObject for this user
    private void onUserOffline(uint uid, USER_OFFLINE_REASON reason)
    {
          if (ToastManager.instance)
            ToastManager.instance.AddToast(ToastType.Info, "用户退出\n"+uid);
        clientObserver.removeObserver(uid);
        // remove video stream
        Debug.Log("onUserOffline: uid = " + uid + " reason = " + reason);
        // this is called in main thread
        /*    GameObject go = GameObject.Find(uid.ToString());
            if (!ReferenceEquals(go, null))
            {
                Object.Destroy(go);
            }*/
    }
    /*private void OnDestroy()
    {
        unloadEngine();
    }

*/

    void Update()
    {
        if (share)
            StartCoroutine(shareScreen());
    }
    private void FixedUpdate()
    {

    }
    public void PushVideo()
    {
        while (!stop)
        {
            if (outImageBuffer.Count > 0)
            {
                byte[] bytes = outImageBuffer.Dequeue();
                //    toast.text = "x: " + x + "y:" + y + "b:" + bytes.Length;
                // 为字节数组提供足够的空间。
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
                    externalVideoFrame.format = ExternalVideoFrame.VIDEO_PIXEL_FORMAT.VIDEO_PIXEL_BGRA;
                    // 应用原始数据。
                    externalVideoFrame.buffer = bytes;
                    // 设置视频帧宽度（pixel）。
                    externalVideoFrame.stride = (int)mRect.width / 3;
                    // 设置视频帧高度（pixel）。
                    externalVideoFrame.height = (int)mRect.height / 3;
                    // 设置从哪侧移除视频帧的像素。
                    externalVideoFrame.cropLeft = 10;
                    externalVideoFrame.cropTop = 10;
                    externalVideoFrame.cropRight = 10;
                    externalVideoFrame.cropBottom = 10;
                    // 设置视频帧旋转角度： 0、90、180 或 270。
                    externalVideoFrame.rotation = 180;
                    // 使用视频时间戳增加 i。
                    externalVideoFrame.timestamp = i++;
                    // 推送外部视频帧。
                    int a = rtc.PushVideoFrame(externalVideoFrame);
                }
            }
            else
            {
                Thread.Sleep(10);
            }
        }
    }
    // 开始屏幕共享。
    IEnumerator shareScreen()
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
        outImageBuffer.Enqueue(bytes);
    }
}