using UnityEngine;
using System.Collections;

public class EarseController : MonoBehaviour {

	public RectTransform proBar;
	public UnityEngine.UI.Text proTex;

	private RenderTexturePainter _painter;
	private PaintCompleteChecker _completeChecker;

	// Use this for initialization
	void Start () {
		_painter = GetComponent<RenderTexturePainter>();
		_completeChecker = GetComponent<PaintCompleteChecker>();
	}
	
	// Update is called once per frame
	void Update () {
		if(Input.GetMouseButton(0))
		{
			_painter.Drawing(Input.mousePosition,Camera.main,true);
			_completeChecker.Drawing(Input.mousePosition);

			Vector2 size = proBar.sizeDelta;
			size.x = _completeChecker.Progress*300f;
			proBar.sizeDelta = size;
			proTex.text = "Progress:"+_completeChecker.Progress*100f+"%";
		}
		else if(Input.GetMouseButtonUp(0))
		{
			_painter.EndDraw();
			_completeChecker.EndDraw();
		}
	}

	public void OnClickReset(){
		_painter.isErase = false;
		_painter.ShowScribbleComplete();
		_completeChecker.Reset();
		_painter.isErase = true;

		Vector2 size = proBar.sizeDelta;
		size.x = 0f;
		proBar.sizeDelta = size;
		proTex.text = "Progress:0%";
	}
}
