/*==============================================================================
Copyright (c) 2019 PTC Inc. All Rights Reserved.
 
Copyright (c) 2015 Qualcomm Connected Experiences, Inc.
All Rights Reserved.
Confidential and Proprietary - Protected under copyright and other laws.

Vuforia is a trademark of PTC Inc., registered in the United States and other 
countries.  
==============================================================================*/

using UnityEngine;
using UnityEditor;

namespace Vuforia.EditorClasses
{
    [InitializeOnLoad]
    public static class ConfigurePlayerSettings
    {
        static readonly string VUFORIA_ANDROID_SETTINGS = "VUFORIA_ANDROID_SETTINGS";
        static readonly string VUFORIA_IOS_SETTINGS = "VUFORIA_IOS_SETTINGS";
        static readonly string VUFORIA_WSA_SETTINGS = "VUFORIA_WSA_SETTINGS";

        static ConfigurePlayerSettings()
        {
            EditorApplication.update += UpdatePlayerSettings;
        }

        static void UpdatePlayerSettings()
        {
            BuildTargetGroup androidBuildTarget = BuildTargetGroup.Android;
            BuildTargetGroup iOSBuildTarget = BuildTargetGroup.iOS;
            BuildTargetGroup wsaBuildTarget = BuildTargetGroup.WSA;


            ////// Android Platform \\\\\\

            string androidSymbols = PlayerSettings.GetScriptingDefineSymbolsForGroup(androidBuildTarget);
            androidSymbols = androidSymbols ?? string.Empty;
            if (!androidSymbols.Contains(VUFORIA_ANDROID_SETTINGS))
            {
                if (PlayerSettings.Android.androidTVCompatibility)
                {
                    // Disable Android TV compatibility, as this is not compatible with
                    // portrait, portrait-upside-down and landscape-right orientations.
                    Debug.Log("Disabling Android TV compatibility");
                    PlayerSettings.Android.androidTVCompatibility = false;
                }

                // Here we set the scripting define symbols for Android
                // so we can remember that the settings were set once.
                PlayerSettings.SetScriptingDefineSymbolsForGroup(androidBuildTarget, androidSymbols + ";" + VUFORIA_ANDROID_SETTINGS);
            }


            ////// iOS Platform \\\\\\

            string iOSSymbols = PlayerSettings.GetScriptingDefineSymbolsForGroup(iOSBuildTarget);
            iOSSymbols = iOSSymbols ?? "";
            if (!iOSSymbols.Contains(VUFORIA_IOS_SETTINGS))
            {
                if (PlayerSettings.iOS.cameraUsageDescription.Length == 0)
                {
                    Debug.Log("Setting Camera Usage Description for iOS");
                    PlayerSettings.iOS.cameraUsageDescription = "Camera access required for target detection and tracking";
                }

                if (PlayerSettings.GetScriptingBackend(iOSBuildTarget) != ScriptingImplementation.IL2CPP)
                {
                    Debug.Log("Setting iOS Scripting Backend to IL2CPP to enable 64bit support");
                    PlayerSettings.SetScriptingBackend(iOSBuildTarget, ScriptingImplementation.IL2CPP);
                }

                if (PlayerSettings.iOS.targetOSVersionString != "11.0")
                {
                    Debug.Log("Setting Minimum iOS Version to 11.0");
                    PlayerSettings.iOS.targetOSVersionString = "11.0";
                }

                // Here we set the scripting define symbols for IOS
                // so we can remember that the settings were set once.
                PlayerSettings.SetScriptingDefineSymbolsForGroup(iOSBuildTarget, iOSSymbols + ";" + VUFORIA_IOS_SETTINGS);
            }


            ////// Universal Windows Platform (UWP) \\\\\\

            string wsaSymbols = PlayerSettings.GetScriptingDefineSymbolsForGroup(wsaBuildTarget);
            wsaSymbols = wsaSymbols ?? "";
            if (!wsaSymbols.Contains(VUFORIA_WSA_SETTINGS))
            {
                if (PlayerSettings.GetScriptingBackend(wsaBuildTarget) != ScriptingImplementation.IL2CPP)
                {
                    Debug.Log("Setting WSA Scripting Backend to IL2CPP");
                    PlayerSettings.SetScriptingBackend(wsaBuildTarget, ScriptingImplementation.IL2CPP);
                }

                // Vuforia needs WebCam permission; UWP requires Microphone permission if using WebCam permission.
                Debug.Log("Setting WSA Capability for WebCam");
                PlayerSettings.WSA.SetCapability(PlayerSettings.WSACapability.WebCam, true);
                Debug.Log("Setting WSA Capability for Microphone");
                PlayerSettings.WSA.SetCapability(PlayerSettings.WSACapability.Microphone, true);

                // Vuforia SDK for UWP also requires InternetClient Access
                Debug.Log("Setting WSA Capability for InternetClient");
                PlayerSettings.WSA.SetCapability(PlayerSettings.WSACapability.InternetClient, true);

                // Here we set the scripting define symbols for WSA
                // so we can remember that the settings were set once.
                PlayerSettings.SetScriptingDefineSymbolsForGroup(wsaBuildTarget, wsaSymbols + ";" + VUFORIA_WSA_SETTINGS);
            }
            
            // Unregister callback so that this script is only executed once
            EditorApplication.update -= UpdatePlayerSettings;
        }
    }
}
