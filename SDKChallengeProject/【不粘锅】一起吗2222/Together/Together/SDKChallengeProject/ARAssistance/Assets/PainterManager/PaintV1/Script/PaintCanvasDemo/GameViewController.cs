﻿using UnityEngine;
using System.Collections;
using UnityEngine.UI;
using UnityEngine.EventSystems;

public class GameViewController : MonoBehaviour {

	[Header("Setting")]
	public ToggleGroup leftBar;
	public ToggleGroup rightBar;
	public Slider penScaleBar;
	public Slider penAlphaBar;
	public Slider penLerp;
	public Toggle isEarseBar;
	public Toggle isColorfulBar;

	[Header("Paint Canvas")]
	public RenderTexturePainter painterCanvas;

	private bool _isMouseDown =false;

	
	// Update is called once per frame
	void FixedUpdate () {
		bool isRawImg = false;
		foreach(Toggle t in leftBar.ActiveToggles()){

			//Set pen texture.
			painterCanvas.penTex = t.GetComponent<Image>().sprite.texture;


			PenSetting ps = t.GetComponent<PenSetting>();

			//save raw img flag.
			isRawImg = ps.isRawImg;
			break;
		}

		if(!isColorfulBar.isOn){
			//Set the pen color if is not colorfull stuatus.
			if(isRawImg || isColorfulBar.isOn){
				painterCanvas.penColor = Color.white;
			}else{
				foreach(Toggle t in rightBar.ActiveToggles()){
					painterCanvas.penColor = t.GetComponent<Image>().color;
					break;
				}
			}
		}

		//Set the pen alpha value.
		painterCanvas.penMat.SetFloat("_Alpha",penAlphaBar.value);

		//pen size.
		painterCanvas.brushScale = penScaleBar.value;

		//draw damp value.
		painterCanvas.drawLerpDamp = penLerp.value;

		//is erase.
		painterCanvas.isErase = isEarseBar.isOn;

		//draw line or draw colorful line.
		painterCanvas.paintType = isColorfulBar.isOn? RenderTexturePainter.PaintType.DrawColorfulLine: RenderTexturePainter.PaintType.DrawLine;
	}

	void Update(){
		if(Input.GetMouseButtonDown(0)){
			_isMouseDown = true;
			//Draw once when mouse down.
			painterCanvas.ClickDraw(Input.mousePosition,Camera.main,painterCanvas.penTex,painterCanvas.brushScale,painterCanvas.penMat,painterCanvas.renderTexture);
		}
		else if(Input.GetMouseButton(0)){
			if(_isMouseDown)
			{
				//draw on mouse drag.
				painterCanvas.Drawing(Input.mousePosition,Camera.main);
			}
		}
		else if(Input.GetMouseButtonUp(0) && _isMouseDown)
		{
			painterCanvas.EndDraw();
			_isMouseDown = false;
		}
	}

	public void OnClickClearCanvas(){
		//clear the paint canvas.
		painterCanvas.ClearCanvas();
	}
}
