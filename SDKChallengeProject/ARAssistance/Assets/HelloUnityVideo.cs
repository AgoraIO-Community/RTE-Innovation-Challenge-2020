using UnityEngine;
using UnityEngine.UI;

using agora_gaming_rtc;
using agora_utilities;
using System.Runtime.InteropServices;
using System.Collections;


// this is an example of using Agora Unity SDK
// It demonstrates:
// How to enable video
// How to join/leave channel
// 
public class HelloUnityVideo:MonoBehaviour
{
    Texture2D mTexture;
    Rect mRect;
    bool share;
    int i = 200;
    // instance of agora engine
    private IRtcEngine mRtcEngine;

    // load agora engine
    public void loadEngine(string appId)
    {
        // start sdk
        Debug.Log("initializeEngine");

        if (mRtcEngine != null)
        {
            Debug.Log("Engine exists. Please unload it first!");
            return;
        }

        Debug.Log("ScreenShare Activated");
        mRtcEngine = IRtcEngine.GetEngine(appId);
        // ������־����ȼ���
        mRtcEngine.SetLogFilter(LOG_FILTER.DEBUG | LOG_FILTER.INFO | LOG_FILTER.WARNING | LOG_FILTER.ERROR | LOG_FILTER.CRITICAL);
        // ������Ƶģ�顣
        mRtcEngine.EnableVideo();
        // ������Ƶ�۲�����
        mRtcEngine.EnableVideoObserver();
        // �����ⲿ��ƵԴ��
        mRtcEngine.SetExternalVideoSource(true, false);
        // �����蹲������Ļ����
        mRect = new Rect(0, 0, Screen.width, Screen.height);
        // ���� Texture��
        mTexture = new Texture2D((int)mRect.width, (int)mRect.height, TextureFormat.BGRA32, false);
    }

    public void join(string channel)
    {
        Debug.Log("calling join (channel = " + channel + ")");

        if (mRtcEngine == null)
            return;
        // set callbacks (optional)
        mRtcEngine.OnJoinChannelSuccess = onJoinChannelSuccess;
        mRtcEngine.OnUserJoined = onUserJoined;
        mRtcEngine.OnUserOffline = onUserOffline;
        // join channel
        mRtcEngine.JoinChannel(channel, null, 0);

        // Optional: if a data stream is required, here is a good place to create it
  //      int streamID = mRtcEngine.CreateDataStream(true, true);
      //  Debug.Log("initializeEngine done, data stream id = " + streamID);
    }
    void Update()
    {
     //   if (share)
       //     StartCoroutine(shareScreen());
    }
    IEnumerator shareScreen()
    {
        yield return new WaitForEndOfFrame();
        // ��ȡ��Ļ���ء�
        mTexture.ReadPixels(mRect, 0, 0);
        // Ӧ�����ء�
        mTexture.Apply();
        // ��ȡ Raw Texture ������Ӧ�õ��ֽ������С�
        byte[] bytes = mTexture.GetRawTextureData();

        int size = Marshal.SizeOf(bytes[0]) * bytes.Length;
        // ��ѯ�Ƿ���� IRtcEngine ʵ����
        IRtcEngine rtc = IRtcEngine.QueryEngine();
        if (rtc != null)
        {
            //   921600
            // �����ⲿ��Ƶ֡��
            ExternalVideoFrame externalVideoFrame = new ExternalVideoFrame();
            // ������Ƶ֡ buffer ���͡�
            externalVideoFrame.type = ExternalVideoFrame.VIDEO_BUFFER_TYPE.VIDEO_BUFFER_RAW_DATA;
            // �������ظ�ʽ��
            externalVideoFrame.format = ExternalVideoFrame.VIDEO_PIXEL_FORMAT.VIDEO_PIXEL_BGRA;
            // Ӧ��ԭʼ���ݡ�
            externalVideoFrame.buffer = bytes;
            // ������Ƶ֡���ȣ�pixel����
            externalVideoFrame.stride = (int)mRect.width;
            // ������Ƶ֡�߶ȣ�pixel����
            externalVideoFrame.height = (int)mRect.height;
            // ���ô��Ĳ��Ƴ���Ƶ֡�����ء�
            externalVideoFrame.cropLeft = 10;
            externalVideoFrame.cropTop = 10;
            externalVideoFrame.cropRight = 10;
            externalVideoFrame.cropBottom = 10;
            // ������Ƶ֡��ת�Ƕȣ� 0��90��180 �� 270��
            externalVideoFrame.rotation = 180;
            // ʹ����Ƶʱ������� i��
            externalVideoFrame.timestamp = i++;
            // �����ⲿ��Ƶ֡��
            int a = rtc.PushVideoFrame(externalVideoFrame);
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

        if (mRtcEngine == null)
            return;

        // leave channel
        mRtcEngine.LeaveChannel();
        // deregister video frame observers in native-c code
        mRtcEngine.DisableVideoObserver();
    }

    // unload agora engine
    public void unloadEngine()
    {
        Debug.Log("calling unloadEngine");

        // delete
        if (mRtcEngine != null)
        {
            IRtcEngine.Destroy();  // Place this call in ApplicationQuit
            mRtcEngine = null;
        }
    }


    public void EnableVideo(bool pauseVideo)
    {
        if (mRtcEngine != null)
        {
            if (!pauseVideo)
            {
                mRtcEngine.EnableVideo();
            }
            else
            {
                mRtcEngine.DisableVideo();
            }
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
        share = true;
   //     textVersionGameObject.GetComponent<Text>().text = "SDK Version : " + getSdkVersion();
    }

    // When a remote user joined, this delegate will be called. Typically
    // create a GameObject to render video on it
    private void onUserJoined(uint uid, int elapsed)
    {
        Debug.Log("onUserJoined: uid = " + uid + " elapsed = " + elapsed);
        // this is called in main thread

        // find a game object to render video stream from 'uid'
        GameObject go = GameObject.Find(uid.ToString());
        if (!ReferenceEquals(go, null))
        {
            return; // reuse
        }

        // create a GameObject and assign to this new user
        VideoSurface videoSurface = makeImageSurface(uid.ToString());
        if (!ReferenceEquals(videoSurface, null))
        {
            // configure videoSurface
            videoSurface.SetForUser(uid);
            videoSurface.SetEnable(true);
            videoSurface.SetVideoSurfaceType(AgoraVideoSurfaceType.RawImage);
            videoSurface.SetGameFps(30);
        }
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
        float yPos = Random.Range(3.0f, 5.0f);
        float xPos = Random.Range(-2.0f, 2.0f);
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
        // remove video stream
        Debug.Log("onUserOffline: uid = " + uid + " reason = " + reason);
        // this is called in main thread
        GameObject go = GameObject.Find(uid.ToString());
        if (!ReferenceEquals(go, null))
        {
            Object.Destroy(go);
        }
    }
}
