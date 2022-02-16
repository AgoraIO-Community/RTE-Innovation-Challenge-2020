using System.Collections;
using UnityEngine;
using UnityEngine.Rendering;

/// <summary>
/// PenMat and canvasMat must use the draw line shader If it's draw line or draw colorful line .
/// 
/// </summary>
public class Painter : MonoBehaviour {

	#region enums
	public enum PaintType
	{
		Scribble,
		ScribbleOverlay,
		DrawLine,
		DrawColorfulLine,
		None = 100
	}
	#endregion

	#if UNITY_EDITOR
	public Color gizmosColor = Color.red;
	#endif

	[Header("Painter Setting")]
	public PaintType paintType = PaintType.Scribble;

	//矢量GL绘图,性能更高，但是不支持画笔贴图的alpha通道
	public bool useVectorGraphic = true; 

	[SerializeField]
	private bool m_renderTexMipmap = false;

	[SerializeField]
	private Texture m_sourceTex; //scribble source texture
	public Texture sourceTex{
		get{ return m_sourceTex; }
		set{ 
			m_sourceTex = value; 
			if(canvasMat2){
				canvasMat2.SetTexture("_SourceTex",m_sourceTex);
			}else if(canvasMat){
				canvasMat.SetTexture("_SourceTex",m_sourceTex);
			}
		}
	}

	[SerializeField]
	private int m_renderTexWidth = 256;
	public int renderTexWidth {
		get{ return m_renderTexWidth; }
	}
	[SerializeField]
	private int m_renderTexHeight = 256;
	public int renderTexHeight {
		get{ return m_renderTexHeight; }
	}

	[SerializeField]
	private Color m_penColor=new Color(1, 1, 1, 1);

	[Range(0.1f,5f)]
	public float brushScale = 1f;//change pen size

	[Range(0.01f,2f)]
	public float drawLerpDamp = 0.02f; //line continous

	[SerializeField]
	private bool m_isEraser = false;

    [SerializeField]
    private bool m_isUGUI = false;

	[Header("Colorfull paint Setting")]
	public Color[] paintColorful ;
	[Range(0f,10f)]
	public float colorChangeRate = 1f;


	[Header("Auto Setting")]
	public bool isAutoInit = false;
	public bool isAutoDestroy = true;//Destroy renderTexture when gameobject is destroyed.
	public bool isShowSource = false;


	[Header("Material")]
	public Material penMat;
	public Material canvasMat;
	public Material canvasMat2; //用于makeup

	//[SerializeField]
	private RenderTexture m_rt,m_rt2;
	[HideInInspector]
//	[SerializeField]
	private bool m_inited = false;
	public bool isInited{
		get{
			return m_inited;
		}
	}

	public RenderTexture renderTexture{
		get{ return m_rt; }
		set{ 
			m_rt = value; 
			if(canvasMat){
				canvasMat.SetTexture("_RenderTex",m_rt);
			}
		}
	}
	public RenderTexture renderTexture2{
		get{ return m_rt2; }
		set{ 
			m_rt2 = value; 
			if(canvasMat2){
				canvasMat2.SetTexture("_RenderTex",m_rt2);
			}
		}
	}

	public Color penColor{
		get{ return m_penColor; }
		set { m_penColor = value; }
	}

	private int m_colorfulIndex = 1;
	private Vector3 m_prevMousePosition;
	private float m_colorfulTime = 0f;
	private bool m_isDrawing = false;
	private Rect m_uv = new Rect(0f,0f,1f,1f);
	private Color m_canvasColor = new Color(1,1,1,0);

	[Header("Mesh")]
	[SerializeField]
	private MeshCollider[] m_colliders;//for 3d meshcollider


	public bool isErase{
		get{ return m_isEraser; }
		set{
			if(m_isEraser!=value){
				m_isEraser = value;
				if(m_inited){
					penMat.SetFloat("_Cutoff",0f);
					if(m_isEraser){
						penMat.SetFloat("_BlendSrc",(int)BlendMode.Zero);
						penMat.SetFloat("_BlendDst",(int)BlendMode.OneMinusSrcAlpha);
						if(paintType== PaintType.DrawLine || paintType== PaintType.DrawColorfulLine){
							penMat.SetFloat("_FactorA",(int)BlendMode.Zero);
						}
					}else{
						penMat.SetFloat("_BlendSrc",(int)BlendMode.SrcAlpha);
						if(paintType== PaintType.DrawLine || paintType== PaintType.DrawColorfulLine){
							penMat.SetFloat("_BlendDst",(int)BlendMode.OneMinusSrcAlpha);
							penMat.SetFloat("_FactorA",(int)BlendMode.One);
						}
						else if(paintType== PaintType.None){
							penMat.SetFloat("_BlendDst",(int)BlendMode.SrcAlpha);
						}
						else {
							penMat.SetFloat("_BlendDst",(int)BlendMode.One);
						}
					}
				}
			}
		}
	}


 void Awake()
{
	m_renderTexWidth=(int)(Screen.height*1f/9*16);
	m_renderTexHeight=Screen.height;
	if(UserManager.instance&&UserManager.instance.UType==UserType.Staff)
	GetComponent<RectTransform>().sizeDelta=new Vector2(m_renderTexHeight,m_renderTexHeight);
	else
	GetComponent<RectTransform>().sizeDelta=new Vector2(m_renderTexWidth,m_renderTexHeight);

}
public void SetUpSize(int w,int h){
		m_renderTexWidth=w;
	m_renderTexHeight=h;
}
	void Start () {
		if (isAutoInit) {
			Init();
		}
	}

	public void Init()
	{
		if(!m_inited){
//Debug.LogError("m_inited");
			if(penMat == null || canvasMat==null){
				Debug.LogError("Pen And CanvasMat is Null.");
				return;
			}

			m_inited = true;

			if(canvasMat.HasProperty("_Color")){
				m_canvasColor = canvasMat.GetColor("_Color");
			}
			if(paintType == PaintType.ScribbleOverlay){
				if(canvasMat2 == null){
					Debug.LogError("canvasMat2 is Null.");
					return;
				}

				if(m_rt==null){
					m_rt = new RenderTexture(m_renderTexWidth,m_renderTexHeight,0,RenderTextureFormat.ARGB32);
					m_rt.filterMode = FilterMode.Bilinear;
					m_rt.useMipMap = m_renderTexMipmap;
				}

				canvasMat.SetTexture("_MainTex",m_rt);

				if(m_rt2==null){
					m_rt2 = new RenderTexture(m_renderTexWidth,m_renderTexHeight,0,RenderTextureFormat.ARGB32);
					m_rt2.filterMode = FilterMode.Bilinear;
					m_rt2.useMipMap = m_renderTexMipmap;
					ClearCanvas(m_rt2);
				}

				canvasMat2.SetTexture("_SourceTex",m_sourceTex);
				canvasMat2.SetTexture("_RenderTex",m_rt2);

				penMat.SetFloat("_BlendSrc",(int)BlendMode.SrcAlpha);
				penMat.SetFloat("_BlendDst",(int)BlendMode.OneMinusSrcAlpha);

				canvasMat2.SetFloat("_BlendSrc",(int)UnityEngine.Rendering.BlendMode.One);
				canvasMat2.SetFloat("_BlendDst",(int)UnityEngine.Rendering.BlendMode.Zero);
			}
			else
			{
				if(m_rt==null){
					m_rt = new RenderTexture(m_renderTexWidth,m_renderTexHeight,0,RenderTextureFormat.ARGB32);
					m_rt.filterMode = FilterMode.Bilinear;
					m_rt.useMipMap = m_renderTexMipmap;
				}

				if(paintType== PaintType.Scribble){
					canvasMat.SetTexture("_SourceTex",m_sourceTex);
					canvasMat.SetTexture("_RenderTex",m_rt);
				}else{
					canvasMat.mainTexture = m_rt;
				}

				if(isErase){
					isErase = false;
					isErase = true;
				}else{
					isErase = true;
					isErase = false;
				}
			}

            if (m_isUGUI)
            {
                GetComponent<UnityEngine.UI.RawImage>().texture = m_rt;
            }

			if(isShowSource){
				ShowTexture(m_sourceTex);
			}else{
				ResetCanvas();
			}
		}
	}

	/// <summary>
	/// Set material
	/// </summary>
	/// <returns>The mat.</returns>
	/// <param name="shader">Shader.</param>
	/// <param name="c">C.</param>
	/// <param name="src">Source.</param>
	/// <param name="dst">Dst.</param>
	/// <param name="alpha">Alpha.</param>
	/// <param name="cutoff">Cutoff.</param>
	Material SetMat(Material m ,Shader shader ,Color c, BlendMode src , BlendMode dst , float alpha=1f,float cutoff=0f){
		m.SetFloat("_BlendSrc",(int)src);
		m.SetFloat("_BlendDst",(int)dst);
		m.SetColor("_Color",c);
		m.SetFloat("_Cutoff",cutoff);
		m.SetFloat("_Alpha",alpha);
		return m;
	}

	/// <summary>
	/// 显示贴图
	/// </summary>
	/// <param name="texture">Texture.</param>
	public void ShowTexture(Texture texture){
		if(m_rt && texture){
			Graphics.SetRenderTarget (m_rt);
			Graphics.Blit(texture,m_rt);
			RenderTexture.active = null;
		}
	}

	/// <summary>
	/// draw when moving
	/// </summary>
	/// <param name="screenPos">Screen position.</param>
	/// <param name="camera">Camera.</param>
	/// <param name="RenderTexture">RenderTexture.</param>
	/// <param name="drawOutside">Draw Outside.</param>
    public void Drawing(Vector3 screenPosOrUV , Camera camera, RenderTexture rt,bool drawOutside=false,bool posIsInRectangle = false){
		if(!m_inited) return;

		Vector3 uvPos = Vector2.zero;
		if(m_colliders!=null && m_colliders.Length>0){
			RaycastHit hit;
			if (!Physics.Raycast(camera.ScreenPointToRay(screenPosOrUV), out hit)){
				m_isDrawing = false;
				return;
			}
			if(m_colliders!=null){
				bool flag = false;
				foreach( Collider col  in m_colliders){
					if(col && hit.collider==col) {
						flag = true;
						break;
					}
				}
				if(flag==false) return;
			}
			uvPos = hit.textureCoord;
		}
		else
		{
            if (posIsInRectangle)
                uvPos = SpriteHitPoint2UV(screenPosOrUV,posIsInRectangle);
            else 
                uvPos= SpriteHitPoint2UV(camera.ScreenToWorldPoint(screenPosOrUV),posIsInRectangle);
		}
		screenPosOrUV = new Vector3(uvPos.x * m_renderTexWidth, m_renderTexHeight - uvPos.y * m_renderTexHeight,0f);
		if(!m_isDrawing){
			m_isDrawing = true;
			m_prevMousePosition = screenPosOrUV;
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
				penMat.color=m_penColor;
			}
			else if(paintType== PaintType.DrawLine){
				penMat.color=m_penColor;
			}
			GL.PushMatrix();
			GL.LoadPixelMatrix(0, rt.width, rt.height, 0);
			RenderTexture.active = rt;
			if(useVectorGraphic){
				VectorGraphicDraw(ref screenPosOrUV,ref m_prevMousePosition,drawOutside);
			}else{
				LerpDraw(ref screenPosOrUV,ref m_prevMousePosition,drawOutside);
			}
			RenderTexture.active = null;
			GL.PopMatrix();
			m_prevMousePosition = screenPosOrUV;

		}
	}

	void LerpDraw(ref Vector3 current ,ref Vector3 prev,bool drawOutside){
		float distance = Vector2.Distance(current, prev);
		if(distance>0f){
			Vector2 pos;
			float w = penMat.mainTexture.width*brushScale;
			float h = penMat.mainTexture.height*brushScale;
			float lerpDamp = Mathf.Min(w,h)*drawLerpDamp;
			m_uv.width = m_renderTexWidth;
			m_uv.height = m_renderTexHeight;
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
					Graphics.DrawTexture(rect,penMat.mainTexture,penMat);
				}
			}
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
			float radius = penMat.mainTexture!=null ? penMat.mainTexture.width*brushScale*0.5f : brushScale;
			m_uv.width = m_renderTexWidth;
			m_uv.height = m_renderTexHeight;
			Rect rect = new Rect(current.x-radius,current.y-radius,radius,radius);
			if(drawOutside || Intersect(ref m_uv,ref rect))
			{
				penMat.SetPass(0);

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
	/// click draw texture
	/// </summary>
	/// <param name="screenPos">Screen position.</param>
	/// <param name="camera">Camera is "Camera.main" if value is null</param>
	/// <param name="pen"> User default pen texture if value is null</param>
    public void ClickDraw(Vector3 screenPosOrUV , Camera camera=null , Texture pen=null, float penScale=1f , 
        Material drawMat = null , RenderTexture rt=null,bool posIsInRectangle = false){
		if (camera == null) camera = Camera.main;
		if(pen==null) pen = penMat.mainTexture;
		if(drawMat==null) drawMat = penMat;
		if(rt==null) rt = m_rt;

		Vector3 uvPos = Vector2.zero;
		if(m_colliders!=null && m_colliders.Length>0){
			RaycastHit hit;
			if (!Physics.Raycast(camera.ScreenPointToRay(screenPosOrUV), out hit)){
				m_isDrawing = false;
				return;
			}
			if(m_colliders!=null){
				bool flag = false;
				foreach( Collider col  in m_colliders){
					if(col && hit.collider==col) {
						flag = true;
						break;
					}
				}
				if(flag==false) return;
			}
			uvPos = hit.textureCoord;
		}
		else
		{
            if (posIsInRectangle)
                uvPos= SpriteHitPoint2UV(screenPosOrUV,posIsInRectangle);
            else 
                uvPos= SpriteHitPoint2UV(camera.ScreenToWorldPoint(screenPosOrUV),posIsInRectangle);
		}
 
		if(m_uv.Contains(uvPos))
		{
			penMat.color=m_penColor;

			Vector3 scPos = new Vector3(uvPos.x * rt.width, rt.height - uvPos.y * rt.height,0f);
			float w = pen.width*penScale;
			float h = pen.height*penScale;
			Rect rect = new Rect((scPos.x-w*0.5f),(scPos.y-h*0.5f),w,h);
			GL.PushMatrix();
			GL.LoadPixelMatrix(0, rt.width, rt.height, 0);
			RenderTexture.active = rt;
			Graphics.DrawTexture(rect,pen,drawMat);
			RenderTexture.active = null;
			GL.PopMatrix();
		}
	}

	public void DrawRT2OtherRT(RenderTexture rt, RenderTexture otherRt,Material drawMat = null){
		if(drawMat==null) drawMat = penMat;
		if(rt && otherRt){
			GL.PushMatrix();
			GL.LoadPixelMatrix(0, rt.width, rt.height, 0);
			RenderTexture.active = otherRt;
			Graphics.DrawTexture(new Rect(0,0,rt.width,rt.height),rt,drawMat);
			RenderTexture.active = null;
			GL.PopMatrix();
		}
	}

	/// <summary>
	/// draw end
	/// </summary>
	public void EndDraw(){
		m_isDrawing = false;
	}

	public void ResetCanvas()
	{
		ResetCanvas(null);
	}

	/// <summary>
	/// reset canvas
	/// </summary>
	public void ResetCanvas(RenderTexture rt)
	{
		if(rt==null) rt = m_rt;
		if(rt){
			Graphics.SetRenderTarget (rt);
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
	public void ClearCanvas(RenderTexture rt=null){
		if(rt==null) rt = m_rt;
		if(rt){
			Graphics.SetRenderTarget (rt);
			Color c = new Color(0,0,0,0) ;
			GL.Clear(true,true,c);
			RenderTexture.active = null;
		}
	}

	/// <summary>
	/// show scribble result
	/// </summary>
	public void ShowScribbleComplete(){
		if(paintType== PaintType.Scribble || paintType== PaintType.ScribbleOverlay || paintType== PaintType.None)
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
	/// Convert hit point to uv position.
	/// </summary>
	/// <returns>uv position.</returns>
	/// <param name="hitPoint">Hit point is world position</param>
    Vector2 SpriteHitPoint2UV( Vector3 hitPointOrUV,bool posIsInRectangle){
        Vector3 localPos = hitPointOrUV;
        if(!posIsInRectangle) localPos = transform.InverseTransformPoint(hitPointOrUV);
        if (m_isUGUI)
        {
            RectTransform tran = transform as RectTransform;
            localPos.x += m_renderTexWidth * tran.pivot.x;
            localPos.y += m_renderTexHeight * tran.pivot.y;
        }
        else
        {
            localPos *= 100f;
            localPos.x += m_renderTexWidth * 0.5f;
            localPos.y += m_renderTexHeight * 0.5f;
        }
		return new Vector2(localPos.x/m_renderTexWidth,localPos.y/m_renderTexHeight);
	}

	bool Intersect(ref Rect a,ref Rect b ) {
		bool c1 = a.xMin < b.xMax;
		bool c2 = a.xMax > b.xMin;
		bool c3 = a.yMin < b.yMax;
		bool c4 = a.yMax > b.yMin;
		return c1 && c2 && c3 && c4;
	}


	#if UNITY_EDITOR
	void OnDrawGizmos(){
		float w = m_renderTexWidth;
		float h = m_renderTexHeight;

		Gizmos.color = gizmosColor;
		Matrix4x4 oldGizmosMatrix = Gizmos.matrix;
		Gizmos.matrix = transform.localToWorldMatrix;
        if (m_isUGUI)
        {
            Gizmos.DrawWireCube(Vector3.zero, new Vector3(w , h , 0.1f));
        }
        else
        {
            Gizmos.DrawWireCube(Vector3.zero, new Vector3(w * 0.01f, h * 0.01f, 0.1f));
        }
		Gizmos.matrix = oldGizmosMatrix;
	}
	#endif
}
