using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[ExecuteInEditMode]
public class ResizePainterQuad : MonoBehaviour {

	private Mesh _mesh;

	// Use this for initialization
	void Start () {
		ReiszeMesh();
	}

	#if UNITY_EDITOR
	void LateUpdate(){
		if(!Application.isPlaying){
			ReiszeMesh();
		}
	}
	#endif

	void ReiszeMesh(){
		Painter painter = GetComponent<Painter>();
		MeshFilter mFilter = GetComponent<MeshFilter>();
		if(painter && mFilter){
			float pw = painter.renderTexWidth;
			float ph = painter.renderTexHeight;

			if(_mesh==null){
				_mesh = CreateQuad(pw,ph);
				_mesh.hideFlags = HideFlags.DontSaveInEditor;
			}
			mFilter.mesh = _mesh;

			MeshCollider mc = GetComponent<MeshCollider>();
			if(mc){
				mc.sharedMesh = _mesh;
			}
		}
	}


	Mesh CreateQuad(float canvasWidth,float canvasHeight){
		Mesh m = new Mesh();
		m.vertices = new Vector3[]{
			new Vector3(canvasWidth*0.005f ,canvasHeight*0.005f ),
			new Vector3(canvasWidth*0.005f ,-canvasHeight*0.005f ),
			new Vector3(-canvasWidth*0.005f ,-canvasHeight*0.005f),
			new Vector3(-canvasWidth*0.005f ,canvasHeight*0.005f )
		};
		m.uv=new Vector2[]{
			new Vector2(1,1),
			new Vector2(1,0),
			new Vector2(0,0),
			new Vector2(0,1)
		};
		m.triangles=new int[]{0,1,2,2,3,0};
		m.RecalculateBounds();
		m.RecalculateNormals();
		return m;
	}
}
