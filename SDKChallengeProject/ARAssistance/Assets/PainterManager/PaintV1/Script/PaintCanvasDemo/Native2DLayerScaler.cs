using UnityEngine;
using System.Collections;
/// <summary>
/// Native2D,游戏Sprite的根目录, 借助UGUI的Canvas的大小来适配
/// </summary>
[ExecuteInEditMode]
public class Native2DLayerScaler: MonoBehaviour {

	public UnityEngine.UI.CanvasScaler referenceCanvas;

	void Start(){
		if(referenceCanvas){
			transform.localScale = referenceCanvas.transform.localScale*100f;
		}
	}

	#if UNITY_EDITOR
	void LateUpdate(){
		if(referenceCanvas){
			transform.localScale = referenceCanvas.transform.localScale*100f;
		}
	}
	#endif
}
