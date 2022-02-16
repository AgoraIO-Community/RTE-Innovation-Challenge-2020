using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEditor;

[CanEditMultipleObjects]
[CustomEditor(typeof(Painter))]
public class PainterEditor : Editor {

	private Painter m_painter;

	void OnEnable(){
		m_painter = target as Painter;
	}


	public override void OnInspectorGUI(){
		if(m_painter.paintType== Painter.PaintType.None){

			serializedObject.Update();
			EditorGUILayout.PropertyField(serializedObject.FindProperty("gizmosColor"), true);
			EditorGUILayout.PropertyField(serializedObject.FindProperty("m_renderTexWidth"), true);
			EditorGUILayout.PropertyField(serializedObject.FindProperty("m_renderTexHeight"), true);
			EditorGUILayout.PropertyField(serializedObject.FindProperty("paintType"), true);
			EditorGUILayout.PropertyField(serializedObject.FindProperty("useVectorGraphic"), true);
			EditorGUILayout.PropertyField(serializedObject.FindProperty("m_sourceTex"), true);
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

			EditorGUILayout.PropertyField(serializedObject.FindProperty("penMat"), true);
			EditorGUILayout.PropertyField(serializedObject.FindProperty("canvasMat"), true);
			EditorGUILayout.PropertyField(serializedObject.FindProperty("canvasMat2"), true);
			EditorGUILayout.PropertyField(serializedObject.FindProperty("m_rt"), true);
			EditorGUILayout.PropertyField(serializedObject.FindProperty("m_rt2"), true);
			serializedObject.ApplyModifiedProperties();

			return;
		}

		serializedObject.Update();
        EditorGUILayout.PropertyField(serializedObject.FindProperty("gizmosColor"), true);
        EditorGUILayout.PropertyField(serializedObject.FindProperty("m_isUGUI"), true);

		EditorGUILayout.PropertyField(serializedObject.FindProperty("m_renderTexWidth"), true);
		EditorGUILayout.PropertyField(serializedObject.FindProperty("m_renderTexHeight"), true);

		EditorGUILayout.PropertyField(serializedObject.FindProperty("paintType"), true);
		EditorGUILayout.PropertyField(serializedObject.FindProperty("useVectorGraphic"), true);
		if(m_painter.paintType==Painter.PaintType.Scribble || m_painter.paintType== Painter.PaintType.ScribbleOverlay){
			EditorGUILayout.PropertyField(serializedObject.FindProperty("m_sourceTex"), true);
		}
		if(m_painter.paintType==Painter.PaintType.DrawLine){
			EditorGUILayout.PropertyField(serializedObject.FindProperty("m_penColor"), true);
		}
		EditorGUILayout.PropertyField(serializedObject.FindProperty("brushScale"), true);
		if(!m_painter.useVectorGraphic){
			EditorGUILayout.PropertyField(serializedObject.FindProperty("drawLerpDamp"), true);
		}
        EditorGUILayout.PropertyField(serializedObject.FindProperty("m_isEraser"), true);
		if(m_painter.paintType==Painter.PaintType.DrawColorfulLine){
			EditorGUILayout.PropertyField(serializedObject.FindProperty("paintColorful"), true);
			EditorGUILayout.PropertyField(serializedObject.FindProperty("colorChangeRate"), true);
		}
		EditorGUILayout.PropertyField(serializedObject.FindProperty("isAutoInit"), true);
		EditorGUILayout.PropertyField(serializedObject.FindProperty("isAutoDestroy"), true);
		EditorGUILayout.PropertyField(serializedObject.FindProperty("isShowSource"), true);


		EditorGUILayout.PropertyField(serializedObject.FindProperty("penMat"), true);
		EditorGUILayout.PropertyField(serializedObject.FindProperty("canvasMat"), true);
		if(m_painter.paintType== Painter.PaintType.ScribbleOverlay){
			EditorGUILayout.PropertyField(serializedObject.FindProperty("canvasMat2"), true);
		}

		EditorGUILayout.PropertyField(serializedObject.FindProperty("m_colliders"), true);

	/*	EditorGUILayout.PropertyField(serializedObject.FindProperty("m_rt"), true);
		if(m_painter.paintType==Painter.PaintType.Scribble || m_painter.paintType== Painter.PaintType.ScribbleOverlay){
			EditorGUILayout.PropertyField(serializedObject.FindProperty("m_rt2"), true);
		}
*/
		serializedObject.ApplyModifiedProperties();
	}
}
