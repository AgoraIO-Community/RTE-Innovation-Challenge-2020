using System.Collections;
using UnityEngine;

public class RCGetCameraData : MonoBehaviour
{

    public RenderTextureCamera RenderCamera;
    [Space(20)]
    public bool FreezeEnable = false;
    private RenderTexture _rt;
     Texture2D screenShot = null;
    void Start()
    {

    }

    private void Update()
    {
        if (!RenderCamera) return;

        /* if (!_rt && RenderCamera.targetTexture)
         {
             _rt = RenderCamera.targetTexture;
             if (screenShot == null)
             {
                 screenShot = new Texture2D(_rt.width, _rt.height, TextureFormat.RGBA32, false);
             }

             // 激活这个renderTexture, 并从中中读取像素
             RenderTexture.active = RenderCamera.targetTexture;
             screenShot.ReadPixels(new Rect(0, 0, _rt.width, _rt.height), 0, 0);
             screenShot.Apply();
             RenderTexture.active=null;
         }
         if (_rt && _rt != RenderCamera.targetTexture) Destroy(_rt);*/
        StartCoroutine(TakeScreen());
    }
    IEnumerator TakeScreen()
    {
        yield return new WaitForEndOfFrame();

        _rt = RenderCamera.GetRenderTexture();
        screenShot = new Texture2D(_rt.width, _rt.height, TextureFormat.RGBA32, false);
        RenderTexture.active = _rt;
        screenShot.ReadPixels(new Rect(0, 0, _rt.width, _rt.height), 0, 0);
        RenderTexture.active = null;
        screenShot.Apply();

    }
    public Texture2D GetRawTexture()
    {
        if (screenShot != null)
            return screenShot;
        return null;
    }
    void onGUI()
    {
        if (RenderCamera)
        {
            if (FreezeEnable) RenderCamera.enabled = false;
            else RenderCamera.enabled = true;
        }
    }
}