using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using UnityEditor;
using System.IO;

/// <summary>
/// Complete checker editor.
/// author:zhouzhanglin
/// </summary>
[CustomEditor(typeof(PaintCompleteChecker))]
public class CompleteCheckerEditor : Editor {

	public override void OnInspectorGUI(){
		PaintCompleteChecker checker = target as PaintCompleteChecker;
		RenderTexturePainter painter = checker.gameObject.GetComponent<RenderTexturePainter>();

		EditorGUILayout.Space();
		serializedObject.Update();
		EditorGUILayout.PropertyField(serializedObject.FindProperty("gridDefaultStatus"), true);
		if(painter==null){
			EditorGUILayout.PropertyField(serializedObject.FindProperty("sourceTexture"), true);
			EditorGUILayout.PropertyField(serializedObject.FindProperty("penTexture"), true);
			EditorGUILayout.PropertyField(serializedObject.FindProperty("gridScale"), true);
		}
		EditorGUILayout.PropertyField(serializedObject.FindProperty("enableColor"), true);
		EditorGUILayout.PropertyField(serializedObject.FindProperty("disableColor"), true);
		EditorGUILayout.PropertyField(serializedObject.FindProperty("checkData"), true);
		EditorGUILayout.PropertyField(serializedObject.FindProperty("canResetData"), true);
		serializedObject.ApplyModifiedProperties();

		EditorGUILayout.Space();
		serializedObject.Update();
		EditorGUILayout.PropertyField(serializedObject.FindProperty("editorBrushSize"), true);
		serializedObject.ApplyModifiedProperties();
		EditorGUILayout.BeginHorizontal();
		if(GUILayout.Button("(Ctrl+)Read Data")){
			ReadGrid();
		}
		SpriteRenderer sr = checker.gameObject.GetComponent<SpriteRenderer>();
		if(sr ==null && GUILayout.Button("Show Source Tex")){
			Texture2D tex = checker.sourceTexture as Texture2D;
			if(painter&&painter.sourceTexture){
				tex = painter.sourceTexture as Texture2D;
			}
			if(tex){
				SpriteRenderer render = checker.GetComponent<SpriteRenderer>();
				if(!render) render = checker.gameObject.AddComponent<SpriteRenderer>();
				render.sprite = Sprite.Create(tex,new Rect(0,0,tex.width,tex.height),Vector2.one*0.5f);
			}
		}
		else if(sr && GUILayout.Button("Remove SpriteRenderer")){
			DestroyImmediate(sr);
			EditorGUIUtility.ExitGUI();
		}
		EditorGUILayout.EndHorizontal();


		EditorGUILayout.Space();
		EditorGUILayout.BeginHorizontal();
		if(GUILayout.Button("(Ctrl+)Create Data")){
			CreateGrid();
		}
		if(GUILayout.Button("Save Grid Data")){
			SaveDataToFile();
		}
		EditorGUILayout.EndHorizontal();

		EditorGUILayout.Space();
		EditorGUILayout.TextArea("Cmd/Ctrl + Mouse: Add Point\nAlt + Mouse : Remove Point");
	}

	void CreateGrid(){
		PaintCompleteChecker checker = target as PaintCompleteChecker;
		RenderTexturePainter painter = checker.gameObject.GetComponent<RenderTexturePainter>();

		Texture2D tex = checker.sourceTexture as Texture2D;
		if(painter&&painter.sourceTexture){
			tex = painter.sourceTexture as Texture2D;
		}
		Texture2D pen = checker.penTexture as Texture2D;
		if(painter && painter.penTex!=null){
			pen = painter.penTex as Texture2D;
		}

		if(tex && pen){
			checker.gridsDic = new Dictionary<string,Rect> ();
			checker.enablesDic = new Dictionary<string,bool> ();

			Vector2 gridSize = GetGridSize();
			int gridW = (int)(gridSize.x);
			int gridH = (int)(gridSize.y);

			int canvasW = tex.width;
			int canvasH = tex.height;

			for(int w=-canvasW/2;w<=canvasW/2;w+=gridW)
			{
				for(int h=-canvasH/2;h<=canvasH/2;h+=gridH){
					string key =  w*0.01f+"-"+h*0.01f;
					Rect value = new Rect(w*0.01f,h*0.01f,gridH*0.01f,gridW*0.01f);
					checker.gridsDic[key]=value;
					checker.enablesDic[key] = checker.gridDefaultStatus;
				}
			}
		}
	}

	void SaveDataToFile(){
		//序列化存储
		PaintCompleteChecker checker = target as PaintCompleteChecker;
		if(checker.gridsDic!=null){
			if(checker.checkData==null){
				AssetDatabase.CreateAsset(GetGridData(),"Assets/"+checker.name+"_ScribbleCheckData.asset");
			}else{
				AssetDatabase.Refresh();
				ScribbleCheckData checkData = checker.checkData;
				checkData.checkPoints = new List<Vector2>();
				checkData.gridSize = GetGridSize();
				foreach(string key in checker.gridsDic.Keys){
					if(checker.enablesDic[key])
					{
						Rect r = checker.gridsDic[key];
						checkData.checkPoints.Add(r.center);
					}
				}
				EditorUtility.SetDirty(checker.checkData);
				AssetDatabase.SaveAssets();
			}
		}
	}

	ScribbleCheckData GetGridData(){
		PaintCompleteChecker checker = target as PaintCompleteChecker;
		ScribbleCheckData checkData = ScriptableObject.CreateInstance<ScribbleCheckData>();
		checkData.checkPoints = new List<Vector2>();
		checkData.gridSize = GetGridSize();
		foreach(string key in checker.gridsDic.Keys){
			if(checker.enablesDic[key])
			{
				Rect r = checker.gridsDic[key];
				checkData.checkPoints.Add(r.center);
			}
		}
		return checkData;
	}

	Vector2 GetGridSize(){
		PaintCompleteChecker checker = target as PaintCompleteChecker;
		RenderTexturePainter painter = checker.gameObject.GetComponent<RenderTexturePainter>();

		Texture2D pen = checker.penTexture as Texture2D;
		if(painter && painter.penTex!=null){
			pen = painter.penTex as Texture2D;
			int gridW = Mathf.FloorToInt(pen.width*painter.brushScale/4f);
			int gridH = Mathf.FloorToInt(pen.height*painter.brushScale/4f);
			return new Vector2(gridW,gridH);
		}
		if(pen){
			int gridW = Mathf.FloorToInt(pen.width*checker.gridScale/4f);
			int gridH = Mathf.FloorToInt(pen.height*checker.gridScale/4f);
			return new Vector2(gridW,gridH);
		}
		return Vector2.one*checker.gridScale;
	}

	void ReadGrid()
	{
		PaintCompleteChecker checker = target as PaintCompleteChecker;
		if(checker.checkData!=null){
			checker.gridsDic = new Dictionary<string, Rect>();
			checker.enablesDic = new Dictionary<string, bool>();
			Vector2 gridSize = GetGridSize();
			foreach(Vector2 v in checker.checkData.checkPoints){
				Rect rect = new Rect( v.x-gridSize.x*0.005f,v.y-gridSize.y*0.005f, gridSize.x*0.01f,gridSize.y*0.01f);
				string key = v.x+"-"+v.y;
				checker.gridsDic[key]=rect;
				checker.enablesDic[key] = true;
			}
		}
	}

	bool Intersect(ref Rect a,ref Rect b ) {
		bool c1 = a.xMin < b.xMax;
		bool c2 = a.xMax > b.xMin;
		bool c3 = a.yMin < b.yMax;
		bool c4 = a.yMax > b.yMin;
		return c1 && c2 && c3 && c4;
	}

	void OnSceneGUI() {
		PaintCompleteChecker checker = target as PaintCompleteChecker;
		Handles.color = Color.blue;
		int controlID = GUIUtility.GetControlID(FocusType.Passive);
		Event current = Event.current;
		if (checker.gridsDic!=null && (current.control||current.command||current.alt)){
			switch (current.GetTypeForControl(controlID)) {
			case EventType.MouseDown:
				current.Use();
				break;
			case EventType.MouseMove:
				Vector3 p = HandleUtility.GUIPointToWorldRay(current.mousePosition).origin;
				Vector3 localPos = checker.transform.InverseTransformPoint(p);

				Rect brushSize = new Rect(localPos.x-checker.editorBrushSize/2f,localPos.y-checker.editorBrushSize/2f,checker.editorBrushSize,checker.editorBrushSize);

				if(current.control||current.command){
					foreach(string key in checker.gridsDic.Keys)
					{
						Rect rect = checker.gridsDic[key];
						if(Intersect(ref rect,ref brushSize)){
							checker.enablesDic[key]=true;
						}
					}
				}else if(current.alt){
					foreach(string key in checker.gridsDic.Keys)
					{
						Rect rect = checker.gridsDic[key];
						if(Intersect(ref rect,ref brushSize)){
							checker.enablesDic[key]=false;
						}
					}
				}
				Event.current.Use();
				break;
			case EventType.Layout:
				HandleUtility.AddDefaultControl(controlID);
				break;
			}
		}
	}
}
