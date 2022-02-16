using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class RC_Change_Color : MonoBehaviour {


	void Start() 
	{
		ColorBlue ();
	}


	public void ColorRed ()
	{
		if (GetComponent<MeshRenderer>()) GetComponent<MeshRenderer> ().material.color = new Color32 (255, 150, 150, 255);
	}

	public void ColorGreen ()
	{
		if (GetComponent<MeshRenderer>()) GetComponent<MeshRenderer> ().material.color = new Color32 (150, 255, 150, 255);
	}

	public void ColorBlue ()
	{
		if (GetComponent<MeshRenderer>()) GetComponent<MeshRenderer> ().material.color = new Color32 (150, 150, 255, 255);
	}
}
