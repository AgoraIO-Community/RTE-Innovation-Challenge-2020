using UnityEngine;
using System.Collections;
using UnityEngine.Rendering;

public class RenderTexturePainter : MonoBehaviour {

	#region enums
	public enum PaintType
	{
		Scribble,
		DrawLine,
		DrawColorfulLine,
		None = 100
	} 
	#endregion

	#if UNITY_EDITOR
	public Color gizmosColor = Color.red;
	#endif


	//sorting
	[SerializeField]
	protected string m_SortingLayerName = "Default";
	/// <summary>
	/// Name of the Renderer's sorting layer.
	/// </summary>
	public string sortingLayerName
	{
		get {
			return m_SortingLayerName;
		}
		set {
			m_SortingLayerName = value;
			MeshRenderer render = gameObject.GetComponent<MeshRenderer>();
			if(render)
				render.sortingLayerName=value;
		}
	}

	[SerializeField]
	protected int m_SortingOrder = 0;
	/// <summary>
	/// Renderer's order within a sorting layer.
	/// </summary>
	public int sortingOrder
	{
		get {
			return m_SortingOrder;
		}
		set {
			m_SortingOrder = value;
			MeshRenderer render = gameObject.GetComponent<MeshRenderer>();
			if(render)
				render.sortingOrder=value;
		}
	}


	[Header("Paint Canvas Setting")]
	//canvas size
	public bool useSourceTexSize=true;
	[SerializeField]
	private int m_canvasWidth=512; //pixels
	[SerializeField]
	private int m_canvasHeight=512; //pixels

	//default background color
	[SerializeField]
	private Color m_canvasColor = new Color(1,1,1,0);



	[Header("Painter Setting")]
	public PaintType paintType = PaintType.Scribble;
	public bool useVectorGraphic = true; //矢量GL绘图,性能更高，但是不支持画笔贴图的alpha通道
	public Texture penTex; //pen texture
	[SerializeField]
	private Texture m_sourceTex; //scribble source texture
	[SerializeField]
	private Texture m_maskTex; //drawline maskTex

	[SerializeField]
	private Shader m_paintShader;
	[SerializeField]
	private Shader m_scribbleShader;

	[SerializeField]
	private UnityEngine.Rendering.CullMode m_cullMode = UnityEngine.Rendering.CullMode.Back;

	[SerializeField]
	private Color m_penColor=new Color(1, 0, 0, 1);

	[Range(0.1f,5f)]
	public float brushScale = 1f;//change pen size

	[Range(0.01f,2f)]
	public float drawLerpDamp = 0.02f; //line continous

	[SerializeField]
	private bool m_isEraser = false;




	[Header("Colorfull paint Setting")]
	public Color[] paintColorful ;
	[Range(0f,10f)]
	public float colorChangeRate = 1f;




	[Header("Auto Setting")]
	public bool isAutoInit = false;
	public bool isAutoDestroy = true;//Destroy renderTexture when gameobject is destroyed.
	public bool isShowSource = false;



	[SerializeField]
	private RenderTexture m_rt;
	[SerializeField]
	private bool m_inited = false;
	[SerializeField]
	private Material m_penMat,m_canvasMat;
	[SerializeField]
	private Vector2 m_sourceTexScale;

	private int m_colorfulIndex = 1;
	private float m_colorfulTime = 0f;
	private bool m_isDrawing = false;
	private Vector3 m_prevMousePosition;
	private Rect m_uv = new Rect(0f,0f,1f,1f);



	#region getter / setter
	public RenderTexture renderTexture{ get{ return m_rt; } }
	public bool isInited{ get { return m_inited; } }
	public Material penMat{ get{ return m_penMat; } }
	public Material canvasMat{ get{ return m_canvasMat; } }

	public bool isErase{
		get{ return m_isEraser; }
		set{
			if(m_isEraser!=value){
				m_isEraser = value;
				if(m_inited){
					m_penMat.SetFloat("_Cutoff",0f);
					if(m_isEraser){
						m_penMat.SetFloat("_BlendSrc",(int)BlendMode.Zero);
						m_penMat.SetFloat("_BlendDst",(int)BlendMode.OneMinusSrcAlpha);

						if(paintType== PaintType.DrawLine || paintType== PaintType.DrawColorfulLine){
							m_penMat.SetFloat("_FactorA",(int)BlendMode.Zero);
						}

					}else{
						m_penMat.SetFloat("_BlendSrc",(int)BlendMode.SrcAlpha);
						if(paintType== PaintType.DrawLine || paintType== PaintType.DrawColorfulLine){
							m_penMat.SetFloat("_BlendDst",(int)BlendMode.OneMinusSrcAlpha);
							m_penMat.SetFloat("_FactorA",(int)BlendMode.One);
						}
						else if(paintType== PaintType.None){
							m_penMat.SetFloat("_BlendDst",(int)BlendMode.SrcAlpha);
						}
						else {
							m_penMat.SetFloat("_BlendDst",(int)BlendMode.One);
						}
					}
				}
			}
		}
	}


	public Color penColor{
		get{ return m_penColor; }
		set { m_penColor = value; }
	}

	public Color canvasColor{
		get { return m_canvasColor; }
		set {
			m_canvasColor = value;
			if(m_canvasMat!=null){
				m_canvasMat.color = m_canvasColor;
			}
		}
	}


	public float canvasAlpha{
		get{ return m_canvasColor.a; }
		set{ 
			m_canvasColor.a = value;
			if(m_canvasMat!=null) 
				m_canvasMat.SetFloat("_Alpha",value);
		}
	}

	public Texture sourceTexture{
		get { return m_sourceTex; }
		set {
			this.m_sourceTex = value;
			if(m_canvasMat!=null){
				m_canvasMat.SetTexture("_SourceTex",value);
			}
		}

	}

	public Texture maskTexture{
		get { return m_maskTex; }
		set {
			this.m_maskTex = value;
			if(m_canvasMat!=null){
				m_canvasMat.SetTexture("_MaskTex",value);
			}
		}
	}

	public int canvasWidth{
		get{ return m_canvasWidth; }
		set{
			if(value>1){
				m_canvasWidth = value;
				if(m_rt) m_rt.width = m_canvasWidth;
			}
		}
	}
	public int canvasHeight{
		get{ return m_canvasHeight; }
		set{
			if(value>1){
				m_canvasHeight = value;
				if(m_rt) m_rt.height = m_canvasHeight;
			}
		}
	}

	public Shader paintShader{
		get { return m_paintShader; }
		set { m_paintShader=value; }
	}
	public Shader scribbleShader{
		get { return m_scribbleShader; }
		set { m_scribbleShader=value; }
	}
	#endregion




	// Use this for initialization
	void Start () {
		if (isAutoInit) {
			Init();
		}
	}

	public void Init()
	{
		if(!m_inited){
			m_inited = true;

			if(useSourceTexSize&&m_sourceTex){
				m_canvasWidth = m_sourceTex.width;
				m_canvasHeight = m_sourceTex.height;
			}

			m_rt = new RenderTexture(m_canvasWidth,m_canvasHeight,0,RenderTextureFormat.ARGB32);
			m_rt.filterMode = FilterMode.Trilinear;
			m_rt.useMipMap = false;
			//canvas
			if(paintType== PaintType.Scribble){
				m_canvasMat = CreateMat(m_scribbleShader,m_canvasColor,BlendMode.SrcAlpha,BlendMode.OneMinusSrcAlpha,1,0.02f);
				CreateQuad(m_canvasMat);
				m_canvasMat.SetTexture("_SourceTex",m_sourceTex);
				m_canvasMat.SetTexture("_RenderTex",m_rt);
			}
			else if(paintType== PaintType.DrawLine || paintType== PaintType.DrawColorfulLine){
				m_canvasColor = Color.white;
				m_canvasMat = CreateMat(m_paintShader,m_canvasColor,BlendMode.One,BlendMode.OneMinusSrcAlpha,1,0.0f);
				CreateQuad(m_canvasMat);
				m_canvasMat.SetTexture("_MainTex",m_rt);
			}
			else
			{
				m_canvasMat = CreateMat(m_paintShader,m_canvasColor,BlendMode.SrcAlpha,BlendMode.OneMinusSrcAlpha,1,0.02f);
				CreateQuad(m_canvasMat);
				m_canvasMat.SetTexture("_MainTex",m_rt);
			}
			if(m_maskTex){
				m_canvasMat.SetTexture("_MaskTex",m_maskTex);
			}

			if(m_isEraser)
			{
				m_canvasColor.a = 1f;
				m_penMat = CreateMat(m_paintShader,m_penColor,BlendMode.Zero,BlendMode.OneMinusSrcAlpha,1,0.01f);
			}
			else
			{
				if(paintType== PaintType.Scribble){
					m_canvasColor.a = 0f;
					m_penMat = CreateMat(m_paintShader,m_penColor,BlendMode.SrcAlpha,BlendMode.One,1,0.01f);

				}else if(paintType== PaintType.DrawLine || paintType== PaintType.DrawColorfulLine){
					m_penMat = CreateMat(m_paintShader,m_penColor,BlendMode.SrcAlpha,BlendMode.OneMinusSrcAlpha,m_penColor.a,0.0f);
				}else if(paintType== PaintType.None){
					m_penMat = CreateMat(m_paintShader,m_penColor,BlendMode.SrcAlpha,BlendMode.OneMinusSrcAlpha,1,0.01f);
				}
			}

			if(isShowSource){
				if(m_rt && m_sourceTex){
					Graphics.SetRenderTarget (m_rt);
					Graphics.Blit(m_sourceTex,m_rt);
					RenderTexture.active = null;
				}
			}else{
				ResetCanvas();
			}

		}
	}




	/// <summary>
	/// click draw texture
	/// </summary>
	/// <param name="screenPos">Screen position.</param>
	/// <param name="camera">Camera is "Camera.main" if value is null</param>
	/// <param name="pen"> User default pen texture if value is null</param>
	public void ClickDraw(Vector3 screenPos , Camera camera=null , Texture pen=null, float penScale=1f , Material drawMat = null , RenderTexture rt=null){
		if (camera == null) camera = Camera.main;
		if(pen==null) pen = penTex;
		if(drawMat==null) drawMat = m_penMat;
		if(rt==null) rt = m_rt;
		Vector3 uvPos= SpriteHitPoint2UV(camera.ScreenToWorldPoint(screenPos));

		if(m_uv.Contains(uvPos))
		{
			screenPos = new Vector3(uvPos.x * m_canvasWidth, m_canvasHeight - uvPos.y * m_canvasHeight,0f);
			float w = pen.width*penScale;
			float h = pen.height*penScale;
			Rect rect = new Rect((screenPos.x-w*0.5f),(screenPos.y-h*0.5f),w,h);
			m_uv.width=m_canvasWidth;
			m_uv.height=m_canvasHeight;
			if(Intersect(ref rect,ref m_uv))
			{
				m_penMat.color=m_penColor;
				GL.PushMatrix();
				GL.LoadPixelMatrix(0, m_canvasWidth, m_canvasHeight, 0);
				RenderTexture.active = rt;
				Graphics.DrawTexture(rect,pen,drawMat);
				RenderTexture.active = null;
				GL.PopMatrix();
			}
		}
	}

	/// <summary>
	/// draw when moving
	/// </summary>
	/// <param name="screenPos">Screen position.</param>
	/// <param name="camera">Camera.</param>
	/// <param name="drawOutside">Draw Outside.</param>
	public void Drawing(Vector3 screenPos , Camera camera=null,bool drawOutside=false){
		if (camera == null) camera = Camera.main;
		Vector3 uvPos= SpriteHitPoint2UV(camera.ScreenToWorldPoint(screenPos));
		screenPos = new Vector3(uvPos.x * m_canvasWidth, m_canvasHeight - uvPos.y * m_canvasHeight,0f);
		if(!m_isDrawing){
			m_isDrawing = true;
			m_prevMousePosition = screenPos;
		}

		if(m_isDrawing){
			if(paintType== PaintType.DrawColorfulLine){
				Color currC = paintColorful[m_colorfulIndex];
				m_penColor = Color.Lerp(m_penColor,currC,Time.deltaTime*colorChangeRate);
				m_colorfulTime+=Time.deltaTime*colorChangeRate;
				if(m_colorfulTime>1f){
					m_colorfulTime =0f;
					++m_colorfulIndex;
					if(m_colorfulIndex>=paintColorful.Length){
						m_colorfulIndex = 0;
					}
				}
				m_penMat.color=m_penColor;
			}
			else if(paintType== PaintType.DrawLine){
				m_penMat.color=m_penColor;
			}
			GL.PushMatrix();
			GL.LoadPixelMatrix(0, m_canvasWidth, m_canvasHeight, 0);
			RenderTexture.active = m_rt;
			if(useVectorGraphic){
				VectorGraphicDraw(ref screenPos,ref m_prevMousePosition,drawOutside);
			}else{
				LerpDraw(ref screenPos,ref m_prevMousePosition,drawOutside);
			}
			RenderTexture.active = null;
			GL.PopMatrix();
			m_prevMousePosition = screenPos;
		}
	}
	/// <summary>
	///  矢量绘制，不支持透明通道，PenTex和BurshScale大小只作为画线的宽度
	/// </summary>
	/// <param name="current">Current.</param>
	/// <param name="prev">Previous.</param>
	/// <param name="drawOutside">Draw Outside.</param>
	void VectorGraphicDraw(ref Vector3 current,ref Vector3 prev,bool drawOutside){
		if(Vector3.Distance(current,prev)>0)
		{
			float radius = penTex!=null ? penTex.width*brushScale*0.5f : brushScale;
			m_uv.width = m_canvasWidth;
			m_uv.height = m_canvasHeight;
			Rect rect = new Rect(current.x-radius,current.y-radius,radius,radius);
			if(drawOutside || Intersect(ref m_uv,ref rect))
			{
				m_penMat.SetPass(0);
		
				//draw circle
				float step = 0.2f;
				GL.Begin(GL.TRIANGLE_STRIP);
				GL.TexCoord2(0.5f, 0.5f);
				GL.Color(penColor);
				for (float i=-step;i<6.28318f;i+=step)
				{
					GL.Vertex3(prev.x,prev.y,0f);
					GL.Vertex3(prev.x+Mathf.Sin(i)*radius,prev.y+Mathf.Cos(i)*radius,0f);
					GL.Vertex3(prev.x+Mathf.Sin(i+step)*radius,prev.y+Mathf.Cos(i+step)*radius,0f);

					GL.Vertex3(current.x,current.y,0f);
					GL.Vertex3(current.x+Mathf.Sin(i)*radius,current.y+Mathf.Cos(i)*radius,0f);
					GL.Vertex3(current.x+Mathf.Sin(i+step)*radius,current.y+Mathf.Cos(i+step)*radius,0f);
				}
				GL.End();

				//draw rect
				GL.Begin(GL.QUADS);
				GL.TexCoord2(0.5f, 0.5f);
				GL.Color(penColor);
				Vector3 dir = (current - prev).normalized;
				Vector3 normal = new Vector2 (-dir.y, dir.x) * radius;
				GL.Vertex (prev + normal);
				GL.Vertex (prev - normal);
				GL.Vertex (current - normal);
				GL.Vertex (current + normal);
				GL.End();
			}
		}
	}

	/// <summary>
	/// draw end
	/// </summary>
	public void EndDraw(){
		m_isDrawing = false;
	}

	/// <summary>
	/// reset canvas
	/// </summary>
	public void ResetCanvas()
	{
		if(m_rt){
			Graphics.SetRenderTarget (m_rt);
			Color c = m_canvasColor ;
			if(m_isEraser){
				c.a = 1f;
				GL.Clear(true,true,c);
			}else{
				c.a = 0f;
				if(paintType== PaintType.DrawLine||paintType== PaintType.DrawColorfulLine){
					c = new Color(0,0,0,0);
				}
				GL.Clear(true,true,c);
			}
			RenderTexture.active = null;
		}
	}

	/// <summary>
	/// clear
	/// </summary>
	public void ClearCanvas(){
		if(m_rt){
			Graphics.SetRenderTarget (m_rt);
			Color c = new Color(0,0,0,0) ;
			GL.Clear(true,true,c);
			RenderTexture.active = null;
		}
	}

	/// <summary>
	/// show scribble result
	/// </summary>
	public void ShowScribbleComplete(){
		if(paintType== PaintType.Scribble || paintType== PaintType.None)
		{
			if(m_isEraser)
			{
				Graphics.SetRenderTarget (m_rt);
				Color c = m_canvasColor ;
				c.a = 0f;
				GL.Clear(true,true,c);
			}
			else
			{
				if(m_sourceTex){
					RenderTexture.active = m_rt;
					Graphics.Blit(m_sourceTex,m_rt);
					RenderTexture.active = null;
				}
			}
		}

	}


	/// <summary>
	/// create material
	/// </summary>
	/// <returns>The mat.</returns>
	/// <param name="shader">Shader.</param>
	/// <param name="c">C.</param>
	/// <param name="src">Source.</param>
	/// <param name="dst">Dst.</param>
	/// <param name="alpha">Alpha.</param>
	/// <param name="cutoff">Cutoff.</param>
	public Material CreateMat(Shader shader ,Color c, BlendMode src , BlendMode dst , float alpha=1f,float cutoff=0f){
		Material m = new Material(shader);
		m.SetFloat("_BlendSrc",(int)src);
		m.SetFloat("_BlendDst",(int)dst);
		m.SetColor("_Color",c);
		m.SetFloat("_Cutoff",cutoff);
		m.SetFloat("_Alpha",alpha);
		m.SetFloat("_CullMode",(int)m_cullMode);
		return m;
	}

	public void Dispose(){
		m_inited = false;
		if(m_rt){
			ResetCanvas();
			RenderTexture.active = null;
			m_rt.Release();
			m_rt = null;
		}
		MeshFilter meshFilter= gameObject.GetComponent<MeshFilter>();
		if(meshFilter!=null) Destroy(meshFilter);

		MeshRenderer rend = gameObject.GetComponent<MeshRenderer>();
		if(rend!=null) Destroy(rend);

		if(m_canvasMat){
			Destroy(m_canvasMat);
		}
		if(m_penMat){
			Destroy(m_penMat);
		}
	}




	#region private function


	/// <summary>
	/// Convert hit point to uv position.
	/// </summary>
	/// <returns>uv position.</returns>
	/// <param name="hitPoint">Hit point is world position</param>
	Vector2 SpriteHitPoint2UV( Vector3 hitPoint){
		Vector3 localPos=transform.InverseTransformPoint(hitPoint);
		localPos*=100f;
		localPos.x += m_canvasWidth*0.5f;
		localPos.y += m_canvasHeight*0.5f;
		return new Vector2(localPos.x/m_canvasWidth,localPos.y/m_canvasHeight);
	}

	void LerpDraw(ref Vector3 current ,ref Vector3 prev,bool drawOutside){
		float distance = Vector2.Distance(current, prev);
		if(distance>0f){
			Vector2 pos;
			float w = penTex.width*brushScale;
			float h = penTex.height*brushScale;
			float lerpDamp = Mathf.Min(w,h)*drawLerpDamp;
			m_uv.width = m_canvasWidth;
			m_uv.height = m_canvasHeight;
			for (float i = 0; i < distance; i += lerpDamp)
			{
				float lDelta = i / distance;
				float lDifx = current.x - prev.x;
				float lDify = current.y - prev.y;
				pos.x = prev.x + (lDifx * lDelta);
				pos.y = prev.y + (lDify * lDelta);
				Rect rect = new Rect(pos.x-w*0.5f,pos.y-h*0.5f,w,h);
				if(drawOutside || Intersect(ref m_uv,ref rect))
				{
					Graphics.DrawTexture(rect,penTex,m_penMat);
				}
			}
		}
	}

	void OnDestroy(){
		if(isAutoDestroy){
			Dispose();
		}
	}

	bool Intersect(ref Rect a,ref Rect b ) {
		bool c1 = a.xMin < b.xMax;
		bool c2 = a.xMax > b.xMin;
		bool c3 = a.yMin < b.yMax;
		bool c4 = a.yMax > b.yMin;
		return c1 && c2 && c3 && c4;
	}

	void CreateQuad( Material mat){
		Mesh m = new Mesh();
		m.vertices = new Vector3[]{
			new Vector3(m_canvasWidth*0.005f ,m_canvasHeight*0.005f ),
			new Vector3(m_canvasWidth*0.005f ,-m_canvasHeight*0.005f ),
			new Vector3(-m_canvasWidth*0.005f ,-m_canvasHeight*0.005f),
			new Vector3(-m_canvasWidth*0.005f ,m_canvasHeight*0.005f )
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

		MeshFilter meshFilter= gameObject.GetComponent<MeshFilter>();
		if(meshFilter==null) meshFilter = gameObject.AddComponent<MeshFilter>();
		meshFilter.mesh = m;

		MeshRenderer rend = gameObject.GetComponent<MeshRenderer>();
		if(rend==null) rend = gameObject.AddComponent<MeshRenderer>();
		rend.material = mat;
		rend.sortingLayerName=sortingLayerName;
		rend.sortingOrder = sortingOrder;
	}



	#if UNITY_EDITOR
	void OnDrawGizmos(){
		float w = m_canvasWidth;
		float h = m_canvasHeight;
		if(useSourceTexSize && sourceTexture){
			w = sourceTexture.width;
			h = sourceTexture.height;
		}

		Gizmos.color = gizmosColor;
		Matrix4x4 oldGizmosMatrix = Gizmos.matrix;
		Gizmos.matrix = transform.localToWorldMatrix;
		Gizmos.DrawWireCube(Vector3.zero,new Vector3(w*0.01f,h*0.01f,0.1f));
		Gizmos.matrix = oldGizmosMatrix;
	}
	#endif

	#endregion

}
