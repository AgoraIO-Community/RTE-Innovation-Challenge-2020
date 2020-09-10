using UnityEngine;
using System.Collections;
using Vuforia;
using UnityEngine.UI;
using agora_gaming_rtc;
using System.Runtime.InteropServices;

public class CameraImageAccess : MonoBehaviour
{
    public RenderTextureCamera RenderCamera;
    public RawImage rawImage;
   public Texture2D screenShot ;
       private RenderTexture _rt;
       public bool mAccessCameraImage=false;
    void Start()
    {

    }

    public Texture2D GetRawTexture()
    {
            return screenShot;
    }
    IEnumerator TakeScreen()
    {
        yield return new WaitForEndOfFrame();

  if(RenderCamera==null)
       yield return 0;
        _rt = RenderCamera.GetRenderTexture();
      
//        Debug.Log(_rt.width+"||"+_rt.height);
        if (screenShot == null ||screenShot.width != _rt.width)
            screenShot = new Texture2D(_rt.width, _rt.height, TextureFormat.BGRA32, false);
        RenderTexture.active = _rt;
        screenShot.ReadPixels(new Rect(0, 0, _rt.width, _rt.height), 0, 0);
        RenderTexture.active = null;
        screenShot.Apply();
rawImage.texture=screenShot;
    }
    void Update()
    {

        if (mAccessCameraImage)
            StartCoroutine(TakeScreen());
        if (!RenderCamera) return;
    }
    
}