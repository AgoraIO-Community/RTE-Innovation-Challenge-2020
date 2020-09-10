using System.Collections.Generic;
using UnityEngine;
using agora_gaming_rtc;
using UnityEngine.UI;
using System.Globalization;
using System.Runtime.InteropServices;
using System;
using System.Collections;

public class ShareScreen : MonoBehaviour
{
   Texture2D mTexture;
   Rect mRect;
   [SerializeField]
   private string appId = "Your_AppID";
   [SerializeField]
   private string channelName = "agora";
   public IRtcEngine mRtcEngine;
   int i = 100;

   void Start()
   {
       Debug.Log("ScreenShare Activated");
       mRtcEngine = IRtcEngine.GetEngine(appId);
       // 设置日志输出等级。
       mRtcEngine.SetLogFilter(LOG_FILTER.DEBUG | LOG_FILTER.INFO | LOG_FILTER.WARNING | LOG_FILTER.ERROR | LOG_FILTER.CRITICAL);
       // 启用视频模块。
       mRtcEngine.EnableVideo();
       // 启用视频观测器。
       mRtcEngine.EnableVideoObserver();
       // 配置外部视频源。
       mRtcEngine.SetExternalVideoSource(true, false);
       // 加入频道。
       mRtcEngine.JoinChannel(channelName, null, 0);
       // 创建需共享的屏幕区域。
       mRect = new Rect(0, 0, Screen.width, Screen.height);
       // 创建 Texture。
       mTexture = new Texture2D((int)mRect.width, (int)mRect.height, TextureFormat.BGRA32, false);
   }

   void Update()
   {
       StartCoroutine(shareScreen());
   }

   // 开始屏幕共享。
   IEnumerator shareScreen()
   {
       yield return new WaitForEndOfFrame();
       // 读取屏幕像素。
       mTexture.ReadPixels(mRect, 0, 0);
       // 应用像素。
       mTexture.Apply();
       // 获取 Raw Texture 并将其应用到字节数组中。
       byte[] bytes = mTexture.GetRawTextureData();
       // 为字节数组提供足够的空间。
       int size = Marshal.SizeOf(bytes[0]) * bytes.Length;
       // 查询是否存在 IRtcEngine 实例。
       IRtcEngine rtc = IRtcEngine.QueryEngine();
       if (rtc != null)
       {
           // 创建外部视频帧。
           ExternalVideoFrame externalVideoFrame = new ExternalVideoFrame();
           // 设置视频帧 buffer 类型。
           externalVideoFrame.type = ExternalVideoFrame.VIDEO_BUFFER_TYPE.VIDEO_BUFFER_RAW_DATA;
           // 设置像素格式。
           externalVideoFrame.format = ExternalVideoFrame.VIDEO_PIXEL_FORMAT.VIDEO_PIXEL_BGRA;
           // 应用原始数据。
           externalVideoFrame.buffer = bytes;
           // 设置视频帧宽度（pixel）。
           externalVideoFrame.stride = (int)mRect.width;
           // 设置视频帧高度（pixel）。
           externalVideoFrame.height = (int)mRect.height;
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
}