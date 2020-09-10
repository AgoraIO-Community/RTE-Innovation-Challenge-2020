using UnityEngine;

public class RC_Get_Texture : MonoBehaviour {

	public Camera RenderCamera;
	[Space(20)]
	public bool FreezeEnable = false;
    private bool SkinnedMesh;

    private RenderTexture _rt;

    void Start () 
	{
		if (FreezeEnable && RenderCamera) RenderCamera.enabled = false;
        if (GetComponent<SkinnedMeshRenderer>()) SkinnedMesh = true;
    }

	private void Update() 
	{
        if (!RenderCamera) return;

        if (!_rt && RenderCamera.targetTexture)
        {
            _rt = RenderCamera.targetTexture;
Debug.Log("Width:"+_rt.width+"Height:"+_rt.height);
            if (SkinnedMesh) GetComponent<SkinnedMeshRenderer>().material.SetTexture("_MainTex", RenderCamera.targetTexture);
            else GetComponent<MeshRenderer>().material.SetTexture("_MainTex", RenderCamera.targetTexture);
         
        }

        if (_rt && _rt != RenderCamera.targetTexture) Destroy(_rt);
    }
		
	void onGUI () 
	{
		if (RenderCamera)
		{
            if (FreezeEnable) RenderCamera.enabled = false;
			else RenderCamera.enabled = true;
		}
	}
}