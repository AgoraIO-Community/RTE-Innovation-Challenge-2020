using UnityEngine;
using System.Collections;

public class ScribbleImageController : MonoBehaviour {

	public RenderTexturePainter tempPainter;

	private RenderTexturePainter _painterCanvas;

	// Use this for initialization
	void Start () {
		_painterCanvas = GetComponent<RenderTexturePainter>();
		_painterCanvas.paintType = RenderTexturePainter.PaintType.None;
		tempPainter.paintType = RenderTexturePainter.PaintType.Scribble;
		_painterCanvas.brushScale = tempPainter.brushScale*0.92f;//smaller
	}
	
	// Update is called once per frame
	void Update () {

		/** Draw always */
		if(Input.GetMouseButtonDown(0))
		{
			tempPainter.sourceTexture = _painterCanvas.sourceTexture;
		}
		else if(Input.GetMouseButton(0))
		{
			_painterCanvas.isErase = true;
			tempPainter.canvasMat.SetFloat("_BlendSrc",(int)UnityEngine.Rendering.BlendMode.SrcAlpha);
			tempPainter.canvasMat.SetFloat("_BlendDst",(int)UnityEngine.Rendering.BlendMode.OneMinusSrcAlpha);
			tempPainter.Drawing(Input.mousePosition);
			_painterCanvas.Drawing(Input.mousePosition);


			tempPainter.canvasMat.SetFloat("_BlendSrc",(int)UnityEngine.Rendering.BlendMode.One);
			tempPainter.canvasMat.SetFloat("_BlendDst",(int)UnityEngine.Rendering.BlendMode.Zero);
			//draw temp paint canvas to paint canvas
			_painterCanvas.isErase = false;
			_painterCanvas.ClickDraw(Camera.main.WorldToScreenPoint(tempPainter.transform.position),
				Camera.main,_painterCanvas.renderTexture,1f,tempPainter.canvasMat);
			//clear temp canvas
			tempPainter.ClearCanvas();

		}
		else if(Input.GetMouseButtonUp(0))
		{
			tempPainter.EndDraw();
			_painterCanvas.EndDraw();
		}


		/** Draw delay */
//		if(Input.GetMouseButtonDown(0))
//		{
//			_painterCanvas.isErase = true;
//
//			tempPainter.sourceTexture = _painterCanvas.sourceTexture;
//			tempPainter.canvasMat.SetFloat("_BlendSrc",(int)UnityEngine.Rendering.BlendMode.SrcAlpha);
//			tempPainter.canvasMat.SetFloat("_BlendDst",(int)UnityEngine.Rendering.BlendMode.OneMinusSrcAlpha);
//		}
//		else if(Input.GetMouseButton(0))
//		{
//			tempPainter.Drawing(Input.mousePosition);
//			_painterCanvas.Drawing(Input.mousePosition);
//		}
//		else if(Input.GetMouseButtonUp(0))
//		{
//			tempPainter.EndDraw();
//			_painterCanvas.EndDraw();
//
//			tempPainter.canvasMat.SetFloat("_BlendSrc",(int)UnityEngine.Rendering.BlendMode.One);
//			tempPainter.canvasMat.SetFloat("_BlendDst",(int)UnityEngine.Rendering.BlendMode.Zero);
//
//			//draw temp paint canvas to paint canvas
//			_painterCanvas.isErase = false;
//			_painterCanvas.ClickDraw(Camera.main.WorldToScreenPoint(tempPainter.transform.position),
//				Camera.main,_painterCanvas.renderTexture,1f,tempPainter.canvasMat);
//			//clear temp canvas
//			tempPainter.ClearCanvas();
//		}
	}

	public void ChangeScribbleTexture(Texture2D texture)
	{
		_painterCanvas.sourceTexture = texture;
	}
}
