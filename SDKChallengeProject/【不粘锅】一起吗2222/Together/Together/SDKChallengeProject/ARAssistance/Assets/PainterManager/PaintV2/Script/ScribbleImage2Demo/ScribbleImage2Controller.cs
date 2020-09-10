using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ScribbleImage2Controller : MonoBehaviour {

	public Painter painter;
	private Camera _targetCamera;
	private bool _mouseIsDown;

	// Use this for initialization
	void Start () {
		_targetCamera = Camera.main;
	}


	// Update is called once per frame
	void Update () {
		/** Draw always */
		if(Input.GetMouseButtonDown(0))
		{
			_mouseIsDown = true;
		}
		if(_mouseIsDown && Input.GetMouseButton(0))
		{
			Vector3 screenPos = Input.mousePosition;
			painter.Drawing(screenPos,_targetCamera,painter.renderTexture2);
			painter.DrawRT2OtherRT(painter.renderTexture2,painter.renderTexture,painter.canvasMat2);
			painter.ClearCanvas(painter.renderTexture2);
		}
		if(Input.GetMouseButtonUp(0))
		{
			_mouseIsDown = false;
			painter.EndDraw();
		}
	}


	public void ChangeScribbleTexture(Texture2D texture)
	{
		painter.sourceTex = texture;
	}
}
