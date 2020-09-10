using UnityEngine;
using UnityEngine.Events;
using System.Collections;
using Vuforia;
#if UNITY_EDITOR
#pragma warning disable 0414
#endif

public class RegionCapture : MonoBehaviour
{
    public bool UseCustomBackgroundMaterial;
    public Material CustomBackgroundMaterial;
    private Material RegionCaptureMaterial;

    public bool UseBackgroundPlane = true;
	public GameObject BackgroundPlane;

    public Texture VideoBackgroundTexure;

	public UnityEvent OutOfBounds;
	public UnityEvent ReturnInBounds;

	private bool PlaneIsOutOfBounds;
	private bool OutOfBounds_State;
    private bool ResetBackgroundTextures;

	public bool Check_OutOfBounds;

	public bool HideFromARCamera;
	public bool FlipX, FlipY, Rotate90;

    private bool UseARFoundation;

    public Camera ARCamera;
    public GameObject imageTargetBehaviour;
	Mesh RegionMesh;
	Vector3[] vertices;
	Vector2[] uvs, uvs_tmp;
	float KX, KY;

    private int tex_counter;

    private void OnApplicationFocus(bool focus)
    {
        if (!focus) ResetBackgroundTextures = true;

        if (focus && ResetBackgroundTextures)
        {
            StartCoroutine(ResetYUVTextures());
            ResetBackgroundTextures = false;
        }
    }

    private IEnumerator Start()
    {
        KX = 1.0f;
        KY = 1.0f;

        RegionCaptureMaterial = GetComponent<Renderer>().material;

        if (!UseCustomBackgroundMaterial) CustomBackgroundMaterial = null;
        else if (CustomBackgroundMaterial == null)
        {
            System.Type checkArFoundation = System.Type.GetType("UnityEngine.XR.ARFoundation.ARCameraBackground, Unity.XR.ARFoundation");

            if (checkArFoundation != null)
            {
                var checkObjectInScene = FindObjectOfType(checkArFoundation);
                if (checkObjectInScene)
                {
                    UseARFoundation = true;
                    if (!Application.isEditor) CustomBackgroundMaterial = checkArFoundation.GetProperty("material").GetValue(checkObjectInScene) as Material;
                }
            }
        }

        yield return StartCoroutine(Initialize());
        StartCoroutine(Check_StateLoop());
    }

    private IEnumerator Initialize()
	{
        while (true)
        {
            string _notify = "";

            if (!ARCamera)
            {
                GameObject camObj;
                if ((camObj = GameObject.Find("ARCamera")) || (camObj = GameObject.FindWithTag("MainCamera")))
                {
                    ARCamera = camObj.GetComponent<Camera>();
                }
                else _notify = "ARCamera could not be found";
            }
if(!imageTargetBehaviour){
    _notify = "Image Target could not be found";
}else{

      GetRealScale r = imageTargetBehaviour.GetComponent<GetRealScale>();
transform.localScale=r.targetScale;
}
            if (ARCamera)
            {
                if (HideFromARCamera)
                    ARCamera.cullingMask &= ~(1 << gameObject.layer);
            }

            if (!RegionMesh)
            {
                if (GetComponent<MeshFilter>())
                {
                    RegionMesh = GetComponent<MeshFilter>().mesh;
                    vertices = RegionMesh.vertices;
                    uvs = new Vector2[vertices.Length];
                    uvs_tmp = new Vector2[vertices.Length];
                }
                else _notify = "MeshFilter could not be found";
            }

            if (VideoBackgroundTexure)
            {
                RegionCaptureMaterial.mainTexture = VideoBackgroundTexure;
            }

            if (UseBackgroundPlane)
            {
                if (!BackgroundPlane) BackgroundPlane = GameObject.Find("BackgroundPlane");

                if (BackgroundPlane && BackgroundPlane.GetComponent<MeshFilter>() && BackgroundPlane.GetComponent<MeshRenderer>())
                {
                    CustomBackgroundMaterial = BackgroundPlane.GetComponent<MeshRenderer>().sharedMaterial;
                }
                else _notify = "Searching BackgroundPlane";
            }

            if (CustomBackgroundMaterial)
            {
                GetYUVTextures();
            }

            if (UseCustomBackgroundMaterial)
            {
                 if (!CustomBackgroundMaterial) _notify = "Searching CustomBackground material";
                 else if (tex_counter == 0) _notify = "CustomBackground material has no textures";
            }
            else
            {
                if (!CustomBackgroundMaterial) _notify = "Searching BackgroundPlane material";
                else if (tex_counter == 0) _notify = "BackgroundPlane material has no textures";
            }

            if (ARCamera && RegionMesh && (VideoBackgroundTexure || tex_counter > 0))
            {
                break;              /// Exit from init cycle
            }

            Debug.Log("Restart Initialize - " + _notify);
            yield return new WaitForEndOfFrame();
        }
	}

    private void RCPreRender(Camera cam)
    {
        if (cam == ARCamera && RegionMesh)
            MeshUpdate();
    }

    private void OnEnable()
    {
        Camera.onPreRender += RCPreRender;
    }

    private void OnDisable()
    {
        Camera.onPreRender -= RCPreRender;
    }

    public IEnumerator ResetYUVTextures()
    {
        if (!CustomBackgroundMaterial || !RegionCaptureMaterial)
        {
            Debug.Log("ResetYUVTextures: CustomBackgroundMaterial or RegionCaptureMaterial not found");
            yield break;
        }

        string[] TextureName = CustomBackgroundMaterial.GetTexturePropertyNames();

        bool check_new_textures = false;

        int frame_counter = 0;

        while (!check_new_textures)
        {
            foreach (string tex_name in TextureName)
            {
                if (RegionCaptureMaterial.GetTexture(tex_name))
                    if (RegionCaptureMaterial.GetTexture(tex_name) != CustomBackgroundMaterial.GetTexture(tex_name) && frame_counter > 3)
                        check_new_textures = true;
            }
            yield return new WaitForEndOfFrame();
            frame_counter++;
            if (frame_counter > 30) check_new_textures = true;
        }

        foreach (string tex_name in TextureName)
        {
            if (
                    RegionCaptureMaterial.GetTexture(tex_name)
                    && RegionCaptureMaterial.GetTexture(tex_name) != CustomBackgroundMaterial.GetTexture(tex_name)
                )
                Destroy(RegionCaptureMaterial.GetTexture(tex_name));

            RegionCaptureMaterial.SetTexture(tex_name, CustomBackgroundMaterial.GetTexture(tex_name));
        }
    }

    public void GetYUVTextures()
    {
        tex_counter = 0;

        if (CustomBackgroundMaterial && RegionCaptureMaterial)
        {
            string[] TextureName = CustomBackgroundMaterial.GetTexturePropertyNames();

            foreach (string tex_name in TextureName)
            {
                if (CustomBackgroundMaterial.GetTexture(tex_name))
                {
                    tex_counter++;
                }
            }

            if (tex_counter > 0)
            {
                RegionCaptureMaterial = new Material(CustomBackgroundMaterial);

                foreach (string tex_name in TextureName)
                {
                    if (CustomBackgroundMaterial.GetTexture(tex_name))
                    {
                        RegionCaptureMaterial.SetTexture(tex_name, CustomBackgroundMaterial.GetTexture(tex_name));
                    }
                }
                GetComponent<Renderer>().material = RegionCaptureMaterial;
            }
        }
    }

    private IEnumerator Check_StateLoop()			// 10 frames per second
	{
        while (true)
        {
            if (!UseCustomBackgroundMaterial && UseBackgroundPlane && BackgroundPlane)
                FindBackgroundPlaneBounds(BackgroundPlane);
            if (Check_OutOfBounds) On_OutOfBounds();
            yield return new WaitForSeconds(0.1f);
        }
    }

    private void MeshUpdate()
    {
        bool CheckComplete = false;

        float UV_kx = 1.0f;
        float UV_ky = 1.0f;

        if (!Application.isEditor && UseARFoundation)
        {
            var matrix = Matrix4x4.zero;

            matrix = CustomBackgroundMaterial.GetMatrix("_UnityDisplayTransform");
             
            #if UNITY_ANDROID
            if (Mathf.Abs(matrix[1, 0]) > 0.01f) UV_kx = matrix[1, 0];
            if (Mathf.Abs(matrix[1, 1]) > 0.01f) UV_ky = -Mathf.Abs(matrix[1, 1]);
            #endif

            #if UNITY_IOS
            if (Mathf.Abs(matrix[0, 1]) > 0.01f) UV_kx = -matrix[0, 1];
            if (Mathf.Abs(matrix[1, 1]) > 0.01f) UV_ky = Mathf.Abs(matrix[1, 1]);
            #endif
        }

        for (int i = 0; i < uvs.Length; i++)
        {
            uvs[i] = ARCamera.WorldToViewportPoint(transform.TransformPoint(vertices[i]));

            uvs[i].x = (uvs[i].x - 0.5f) * KX * UV_kx + 0.5f;
            uvs[i].y = (uvs[i].y - 0.5f) * KY * UV_ky + 0.5f;

            if (FlipX)
                uvs[i].x = 1.0f - uvs[i].x;
            if (FlipY)
                uvs[i].y = 1.0f - uvs[i].y;

            if (Rotate90)
            {
                uvs_tmp[i].x = uvs[i].y;
                uvs_tmp[i].y = uvs[i].x;

                uvs[i].x = uvs_tmp[i].x;
                uvs[i].y = uvs_tmp[i].y;
            }

            if (Check_OutOfBounds && !CheckComplete)
            {
                if (uvs[i].x > 1.0f || uvs[i].y > 1.0f || uvs[i].x < 0.0f || uvs[i].y < 0.0f)
                {
                    PlaneIsOutOfBounds = true;
                    CheckComplete = true;
                }
                else PlaneIsOutOfBounds = false;
            }
        }
        RegionMesh.uv = uvs;
        RegionMesh.uv2 = uvs;
        RegionMesh.uv3 = uvs;
        RegionMesh.uv4 = uvs;
    }

	private void On_OutOfBounds()
	{
        if (OutOfBounds_State == PlaneIsOutOfBounds) return;
        OutOfBounds_State = PlaneIsOutOfBounds;

        if (enabled)
        {
            var selectEvent = OutOfBounds_State ? OutOfBounds : ReturnInBounds;
            if (selectEvent != null) selectEvent.Invoke();
        }
	}

	private void FindBackgroundPlaneBounds(GameObject plane)
	{
		Vector3[] vertices_max_min = plane.GetComponent<MeshFilter>().mesh.vertices;
        Vector2[] uvs_max_min = new Vector2[vertices_max_min.Length];

        float max_x_tmp = 0;
		float max_y_tmp = 0;
		float min_x_tmp = 0;
		float min_y_tmp = 0;

		for (int i = 0; i < uvs_max_min.Length; i++)
		{
			uvs_max_min [i] = ARCamera.WorldToViewportPoint (plane.transform.TransformPoint(vertices_max_min[i]));

			if (uvs_max_min [i].x > max_x_tmp) max_x_tmp = uvs_max_min [i].x;
			if (uvs_max_min [i].y > max_y_tmp) max_y_tmp = uvs_max_min [i].y;
			if (uvs_max_min [i].x < min_x_tmp) min_x_tmp = uvs_max_min [i].x;
			if (uvs_max_min [i].y < min_y_tmp) min_y_tmp = uvs_max_min [i].y;
		}

		KX = (1.0f / (((max_x_tmp - 1.0f) * 2.0f) + 1.0f));
		KY = (1.0f / (((max_y_tmp - 1.0f) * 2.0f) + 1.0f));
	}
}