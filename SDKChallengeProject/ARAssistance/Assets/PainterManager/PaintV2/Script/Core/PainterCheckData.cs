using System.Collections.Generic;
using UnityEngine;

[System.Serializable]
public class PainterCheckData :ScriptableObject{
	public bool twoDOrThreeD = true;
	public Vector2 gridSize;
	public List<Vector2> checkPoints;
}
