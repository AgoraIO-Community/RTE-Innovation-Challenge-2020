/*==============================================================================
Copyright (c) 2019 PTC Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other
countries.
==============================================================================*/

using UnityEngine;

// Custom Vuforia Logging Class
public static class VLog
{
    /// <summary>
    /// Will format log text in color when running in Unity Editor.
    /// See https://docs.unity3d.com/Manual/StyledText.html for available colors.
    /// </summary>
    /// <param name="color">Color.</param>
    /// <param name="log">Log.</param>
    public static void Log(string color, string log)
    {
        if (Application.isEditor)
        {
            Debug.Log("<color=" + color + ">" + log + "</color>");
        }
        else
        {
            Debug.Log(log);
        }
    }
}