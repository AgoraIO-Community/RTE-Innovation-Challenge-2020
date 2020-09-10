using System.Collections;
using System.Collections.Generic;
using agora_gaming_rtc;
using UnityEngine;

public class VideoShareEngine
{
    public static IRtcEngine mRtcEngine;
    public static bool isMult = false;
    public static bool isVideo = true;
    public static void initEngine(string appId, OnMediaEngineLoadSuccessHandler success)
    {
        Debug.Log("initVideoShareEngine");
        if (mRtcEngine != null)
        {
            Debug.Log("mRtcEngine exist");
            return;
        }
        else
        {
            mRtcEngine = IRtcEngine.GetEngine(appId);
            mRtcEngine.OnMediaEngineLoadSuccess = success;
            // 设置日志输出等级。
            mRtcEngine.SetLogFilter(LOG_FILTER.DEBUG | LOG_FILTER.INFO | LOG_FILTER.WARNING | LOG_FILTER.ERROR | LOG_FILTER.CRITICAL);
        }
    }
    public static int MuteLocalAudio()
    {
        if (mRtcEngine != null)
        {

            isMult = !isMult;
            int kk = mRtcEngine.MuteLocalAudioStream(isMult);
            return kk;
        }
        return -1;
    }
    public static void leave()
    {
        Debug.Log("calling leave");
        if (mRtcEngine == null){
         return;
                 Debug.Log("calling leave engine == null");
        }
   
        // leave channel
        mRtcEngine.LeaveChannel();
        // deregister video frame observers in native-c code
        mRtcEngine.DisableVideoObserver();
    }
    public static void EnableVideo()
    {
        if (mRtcEngine != null)
        {
            mRtcEngine.EnableVideo();
            isVideo = true;
        }
    }
    public static int MultLocalVideo()
    {
        if (mRtcEngine != null)
        {
            isVideo = !isVideo;
            //  mRtcEngine.EnableLocalVideo(isVideo);
            int ww = mRtcEngine.MuteLocalVideoStream(isVideo);
            return ww;
        }
        return -1;
    }
    public static void DisableVideo()
    {
        if (mRtcEngine != null)
        {
            mRtcEngine.DisableVideo();
            isVideo = false;
        }
    }
    // unload agora engine
    public static void unloadEngine()
    {
        Debug.Log("calling unloadEngine");

        // delete
        if (mRtcEngine != null)
        {
            IRtcEngine.Destroy();  // Place this call in ApplicationQuit
            mRtcEngine = null;
        }
    }
}
