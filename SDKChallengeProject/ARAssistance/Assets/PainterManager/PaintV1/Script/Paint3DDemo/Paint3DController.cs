using UnityEngine;
using System.Collections;

public class Paint3DController : MonoBehaviour {

	public Texture pen ;
	[Range(0f,10f)]
	public float penScale = 1f;
	public Transform map ;
	public Material drawMat;
	public Color color;

	// Use this for initialization
	void Start () {
		if(map){
			foreach(MeshRenderer sr in map.GetComponentsInChildren<MeshRenderer>(true))
			{
				if(sr.material){
					RenderTexture rt = sr.material.GetTexture("_PaintTex") as RenderTexture;
					if(rt){
						RenderTexture.active = rt;
						GL.Clear(true,true,Color.clear);
						RenderTexture.active = null;
					}
				}
			}
		}
	}
	
	// Update is called once per frame
	void Update () {
		if(map){
			map.Rotate(0,Time.deltaTime*5f,0);
		}
		if(pen && Input.GetMouseButtonDown(0))
		{
			DrawOnMesh();
		}
//		else if(pen && Input.GetMouseButton(0))
//		{
//			DrawOnMesh();
//		}
	}

	void DrawOnMesh(){
		RaycastHit hit;
		if(Physics.Raycast( Camera.main.ScreenPointToRay(Input.mousePosition),out hit,100))
		{
			MeshRenderer render = hit.collider.GetComponent<MeshRenderer>();
			if(render && render.material){
				RenderTexture rt = render.material.GetTexture("_PaintTex") as RenderTexture;
				if(rt){
					Draw(rt,hit.textureCoord);
				}
			}
		}
	}

	public void Draw(RenderTexture rt , Vector2 uvPos){
		if(drawMat) drawMat.color = color;

		Vector3 screenPos = new Vector3(uvPos.x * rt.width, rt.height - uvPos.y * rt.height,0f);
		float w = pen.width*penScale;
		float h = pen.height*penScale;

		Rect rect = new Rect((screenPos.x-w*0.5f),(screenPos.y-h*0.5f),w,h);

		GL.PushMatrix();
		GL.LoadPixelMatrix(0, rt.width, rt.height, 0);
		RenderTexture.active = rt;
		Graphics.DrawTexture(rect,pen,drawMat);
		RenderTexture.active = null;
		GL.PopMatrix();
	}
}
