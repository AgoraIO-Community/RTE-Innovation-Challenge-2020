using UnityEngine;
using UnityEditor;
using UnityEditorInternal;
using System.Reflection;

[CanEditMultipleObjects]
[CustomEditor(typeof(RenderTexturePainter))]
public class RenderTexturePainterEditor : Editor {

	private RenderTexturePainter m_painter;

	string[] sortingLayerNames;
	int selectedOption;


	void OnEnable(){
		m_painter = target as RenderTexturePainter;
		sortingLayerNames = GetSortingLayerNames();
		selectedOption = GetSortingLayerIndex(m_painter.sortingLayerName);
	}

	public override void OnInspectorGUI(){
		if(m_painter.paintType== RenderTexturePainter.PaintType.None){

			serializedObject.Update();
			EditorGUILayout.PropertyField(serializedObject.FindProperty("gizmosColor"), true);

			selectedOption = EditorGUILayout.Popup("Sorting Layer", selectedOption, sortingLayerNames);
			if (sortingLayerNames[selectedOption] != m_painter.sortingLayerName)
			{
				Undo.RecordObject(m_painter, "Sorting Layer");
				m_painter.sortingLayerName = sortingLayerNames[selectedOption];
				EditorUtility.SetDirty(m_painter);
			}
			int newSortingLayerOrder = EditorGUILayout.IntField("Order in Layer", m_painter.sortingOrder);
			if (newSortingLayerOrder != m_painter.sortingOrder)
			{
				Undo.RecordObject(m_painter, "Edit Sorting Order");
				m_painter.sortingOrder = newSortingLayerOrder;
				EditorUtility.SetDirty(m_painter);
			}
			if(!m_painter.useSourceTexSize){
				EditorGUILayout.PropertyField(serializedObject.FindProperty("m_canvasWidth"), true);
				EditorGUILayout.PropertyField(serializedObject.FindProperty("m_canvasHeight"), true);
			}
			EditorGUILayout.PropertyField(serializedObject.FindProperty("m_canvasColor"), true);
			EditorGUILayout.PropertyField(serializedObject.FindProperty("paintType"), true);
			EditorGUILayout.PropertyField(serializedObject.FindProperty("useVectorGraphic"), true);
			EditorGUILayout.PropertyField(serializedObject.FindProperty("penTex"), true);
			EditorGUILayout.PropertyField(serializedObject.FindProperty("m_sourceTex"), true);
			EditorGUILayout.PropertyField(serializedObject.FindProperty("m_maskTex"), true);
			EditorGUILayout.PropertyField(serializedObject.FindProperty("m_paintShader"), true);
			EditorGUILayout.PropertyField(serializedObject.FindProperty("m_scribbleShader"), true);
			if(m_painter.paintShader==null) m_painter.paintShader = Shader.Find("Painter/Paint Shader");
			if(m_painter.scribbleShader==null) m_painter.scribbleShader = Shader.Find("Painter/Scribble Shader");
			EditorGUILayout.PropertyField(serializedObject.FindProperty("m_cullMode"), true);
			EditorGUILayout.PropertyField(serializedObject.FindProperty("m_penColor"), true);
			EditorGUILayout.PropertyField(serializedObject.FindProperty("brushScale"), true);
			if(!m_painter.useVectorGraphic){
				EditorGUILayout.PropertyField(serializedObject.FindProperty("drawLerpDamp"), true);
			}
			EditorGUILayout.PropertyField(serializedObject.FindProperty("m_isEraser"), true);
			EditorGUILayout.PropertyField(serializedObject.FindProperty("paintColorful"), true);
			EditorGUILayout.PropertyField(serializedObject.FindProperty("colorChangeRate"), true);
			EditorGUILayout.PropertyField(serializedObject.FindProperty("isAutoInit"), true);
			EditorGUILayout.PropertyField(serializedObject.FindProperty("isAutoDestroy"), true);
			EditorGUILayout.PropertyField(serializedObject.FindProperty("isShowSource"), true);
			serializedObject.ApplyModifiedProperties();

			return;
		}

		serializedObject.Update();
		EditorGUILayout.PropertyField(serializedObject.FindProperty("gizmosColor"), true);

		selectedOption = EditorGUILayout.Popup("Sorting Layer", selectedOption, sortingLayerNames);
		if (sortingLayerNames[selectedOption] != m_painter.sortingLayerName)
		{
			Undo.RecordObject(m_painter, "Sorting Layer");
			m_painter.sortingLayerName = sortingLayerNames[selectedOption];
			EditorUtility.SetDirty(m_painter);
		}
		int sortingLayerOrder = EditorGUILayout.IntField("Order in Layer", m_painter.sortingOrder);
		if (sortingLayerOrder != m_painter.sortingOrder)
		{
			Undo.RecordObject(m_painter, "Edit Sorting Order");
			m_painter.sortingOrder = sortingLayerOrder;
			EditorUtility.SetDirty(m_painter);
		}
		if(m_painter.paintType== RenderTexturePainter.PaintType.Scribble || m_painter.paintType== RenderTexturePainter.PaintType.None){
			EditorGUILayout.PropertyField(serializedObject.FindProperty("useSourceTexSize"), true);
			if(!m_painter.useSourceTexSize){
				EditorGUILayout.PropertyField(serializedObject.FindProperty("m_canvasWidth"), true);
				EditorGUILayout.PropertyField(serializedObject.FindProperty("m_canvasHeight"), true);
			}
		}else{
			EditorGUILayout.PropertyField(serializedObject.FindProperty("m_canvasWidth"), true);
			EditorGUILayout.PropertyField(serializedObject.FindProperty("m_canvasHeight"), true);
		}

		if(m_painter.paintType== RenderTexturePainter.PaintType.Scribble){
			EditorGUILayout.PropertyField(serializedObject.FindProperty("m_canvasColor"), true);
		}
		EditorGUILayout.PropertyField(serializedObject.FindProperty("paintType"), true);
		EditorGUILayout.PropertyField(serializedObject.FindProperty("useVectorGraphic"), true);
		EditorGUILayout.PropertyField(serializedObject.FindProperty("penTex"), true);
		if(m_painter.paintType== RenderTexturePainter.PaintType.Scribble){
			EditorGUILayout.PropertyField(serializedObject.FindProperty("m_sourceTex"), true);
		}
		EditorGUILayout.PropertyField(serializedObject.FindProperty("m_maskTex"), true);
		EditorGUILayout.PropertyField(serializedObject.FindProperty("m_paintShader"), true);
		EditorGUILayout.PropertyField(serializedObject.FindProperty("m_scribbleShader"), true);
		if(m_painter.paintShader==null) m_painter.paintShader = Shader.Find("Painter/Paint Shader");
		if(m_painter.scribbleShader==null) m_painter.scribbleShader = Shader.Find("Painter/Scribble Shader");
		EditorGUILayout.PropertyField(serializedObject.FindProperty("m_cullMode"), true);
		if(m_painter.paintType== RenderTexturePainter.PaintType.DrawLine){
			EditorGUILayout.PropertyField(serializedObject.FindProperty("m_penColor"), true);
		}
		EditorGUILayout.PropertyField(serializedObject.FindProperty("brushScale"), true);
		if(!m_painter.useVectorGraphic){
			EditorGUILayout.PropertyField(serializedObject.FindProperty("drawLerpDamp"), true);
		}
		EditorGUILayout.PropertyField(serializedObject.FindProperty("m_isEraser"), true);
		if(m_painter.paintType== RenderTexturePainter.PaintType.DrawColorfulLine){
			EditorGUILayout.PropertyField(serializedObject.FindProperty("paintColorful"), true);
			EditorGUILayout.PropertyField(serializedObject.FindProperty("colorChangeRate"), true);
		}
		EditorGUILayout.PropertyField(serializedObject.FindProperty("isAutoInit"), true);
		EditorGUILayout.PropertyField(serializedObject.FindProperty("isAutoDestroy"), true);
		EditorGUILayout.PropertyField(serializedObject.FindProperty("isShowSource"), true);
		serializedObject.ApplyModifiedProperties();
	}


	public string[] GetSortingLayerNames() {
		System.Type internalEditorUtilityType = typeof(InternalEditorUtility);
		PropertyInfo sortingLayersProperty = internalEditorUtilityType.GetProperty("sortingLayerNames", BindingFlags.Static | BindingFlags.NonPublic);
		return (string[])sortingLayersProperty.GetValue(null, new object[0]);
	}
	public int[] GetSortingLayerUniqueIDs()
	{
		System.Type internalEditorUtilityType = typeof(InternalEditorUtility);
		PropertyInfo sortingLayerUniqueIDsProperty = internalEditorUtilityType.GetProperty("sortingLayerUniqueIDs", BindingFlags.Static | BindingFlags.NonPublic);
		return (int[])sortingLayerUniqueIDsProperty.GetValue(null, new object[0]);
	}
	int GetSortingLayerIndex(string layerName){
		for(int i = 0; i < sortingLayerNames.Length; ++i){  
			if(sortingLayerNames[i] == layerName) return i;  
		}  
		return 0;  
	}
}
