using UnityEngine;
using System.Collections;

public class Earse2Controller : MonoBehaviour {

	public RectTransform proBar;
	public UnityEngine.UI.Text proTex;

	public Painter painter;
	public PainterProgressChecker proChecker;

	// Use this for initialization
	void Start () {
	}
	
	// Update is called once per frame
	void Update () {
		if(Input.GetMouseButton(0))
		{
			painter.Drawing(Input.mousePosition,Camera.main,painter.renderTexture,true);
			proChecker.Drawing(Input.mousePosition,Camera.main);
			Vector2 size = proBar.sizeDelta;
			size.x = proChecker.Progress*300f;
			proBar.sizeDelta = size;
			proTex.text = "Progress:"+proChecker.Progress*100f+"%";
		}
		else if(Input.GetMouseButtonUp(0))
		{
			painter.EndDraw();
			proChecker.EndDraw();
		}
	}

	public void OnClickReset(){
		painter.isErase = false;
		painter.ShowScribbleComplete();
		proChecker.Reset();
		painter.isErase = true;

		Vector2 size = proBar.sizeDelta;
		size.x = 0f;
		proBar.sizeDelta = size;
		proTex.text = "Progress:0%";
	}
}
