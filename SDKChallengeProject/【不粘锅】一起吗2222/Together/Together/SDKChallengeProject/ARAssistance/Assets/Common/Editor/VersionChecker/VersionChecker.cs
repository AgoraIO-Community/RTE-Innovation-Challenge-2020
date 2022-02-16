/*===============================================================================
Copyright (c) 2018 PTC Inc. All Rights Reserved.

Confidential and Proprietary - Protected under copyright and other laws.
Vuforia is a trademark of PTC Inc., registered in the United States and other 
countries.
===============================================================================*/

// This is no longer required in Unity 2019.2, the package manager dependecy will automatically include the right version of Vuforia Engine.
#if !UNITY_2019_2_OR_NEWER
using System;
using System.IO;
using System.Reflection;
using UnityEditor;
using UnityEngine;

/// <summary>
/// Compares the Sample's version to the Vuforia SDK's version on project load and triggers a dialog to inform the developer 
/// to upgrade either the sample or the SDK to ensure compatibility if there is a version mismatch.
/// </summary>
[InitializeOnLoad]
public static class VersionChecker
{
    static Version sSdkVersion;
    static Version sSampleVersion;

    static string IntegratedSampleVersionFilePath
    {
        get
        {
            return Path.Combine(Application.dataPath, "Common/sample_version");
        }
    }

    static string IntegratedExtensionVersionFilePath
    {
        get
        {
            var basepath = Application.platform == RuntimePlatform.OSXEditor
                ? Path.GetFullPath(Path.Combine(EditorApplication.applicationPath, ".."))
                : EditorApplication.applicationContentsPath;

            return Path.Combine(basepath, "PlaybackEngines/VuforiaSupport/VuforiaResources/version");
        }
    }

    static VersionChecker()
    {
        EditorApplication.update += RunOnceOnInitialize;
    }

    static void RunOnceOnInitialize()
    {
        EditorApplication.update -= RunOnceOnInitialize;
        CheckVersions();
    }

    static Version GetVersionFromFile(string filePath)
    {
        if (!File.Exists(filePath))
            return null;

        var split = File.ReadAllText(filePath).Split(':');
        var versionString = split[0];
        try
        {
            return ParseVersion(versionString);

        }
        catch (Exception)
        {
            Debug.LogErrorFormat("Unable to parse version {0}", versionString);
            return null;
        }
    }

    static Version ParseVersion(string versionString)
    {
        var version = new Version(versionString);
        version = new Version(version.Major, version.Minor, 0);
        return version;
    }

    static void ShowGenericSdkUpdateDialog()
    {
        NewVersionPopup.ShowGenericUpdateExtensionPopup(sSdkVersion);
    }

    static void ShowSampleUpdateDialog()
    {
        NewVersionPopup.ShowUpdateSamplePopup(sSdkVersion);
    }

    static void ShowSdkUpdateDialog(string newSdkVersion, string url, string upgradeMessage)
    {
        NewVersionPopup.ShowGenericExtensionPopup(sSdkVersion, newSdkVersion, url, upgradeMessage);
    }

    static void CheckVersions()
    {
        sSdkVersion = GetVersionFromFile(IntegratedExtensionVersionFilePath);
        sSampleVersion = GetVersionFromFile(IntegratedSampleVersionFilePath);

        if (sSdkVersion == sSampleVersion)
            return;

        if (sSampleVersion == null) // sample version file not found
            return;

        if (sSdkVersion == null) //vuforia extension has not been installed
            ShowVuforiaNotInstalledPopup();
        else if (sSdkVersion > sSampleVersion) // sample_version is outdated
            ShowSampleUpdateDialog();
        else //sample version is higher than sdk version
            CheckForUpdateAndShowSDKUpdateDialog();

    }

    static void ShowVuforiaNotInstalledPopup()
    {
        NewVersionPopup.ShowExtensionMissingPopup();
    }

    static void CheckForUpdateAndShowSDKUpdateDialog()
    {
        try
        {
            var editorAssembly = Assembly.Load("Vuforia.UnityExtensions.Editor");
            var versionLookupService = editorAssembly.GetType("Vuforia.EditorClasses.VersionLookupService");
            var versionLookupData = editorAssembly.GetType("Vuforia.EditorClasses.VersionLookupData");

            var instance = versionLookupService.GetProperty("Instance", BindingFlags.GetProperty | BindingFlags.Static | BindingFlags.Public);
            var instanceValue = instance.GetValue(instance, null);
            var lookupVersion = versionLookupService.GetMethod("LookupVersion", BindingFlags.Public | BindingFlags.Instance);

            var callbackMethod = typeof(VersionChecker).GetMethod("OnLookupVersionComplete", BindingFlags.NonPublic | BindingFlags.Static).MakeGenericMethod(versionLookupData);
            var actionType = typeof(Action<>).MakeGenericType(versionLookupData);
            var param = Delegate.CreateDelegate(actionType, callbackMethod);

            lookupVersion.Invoke(instanceValue, new object[] {param});
        }
        catch (Exception)
        {
            OnLookupVersionComplete<object>(null);
        }
    }

    static void OnLookupVersionComplete<T>(T result)
    {
        if (result == null)
        {
            ShowGenericSdkUpdateDialog();
        }
        else
        {
            try
            {
                var url = GetVersionLookupDataPropertyValue(result, "URL");
                var updatedVersion = GetVersionLookupDataPropertyValue(result, "Version");
                var upgradeMessage = GetVersionLookupDataPropertyValue(result, "UpgradeMessage");

                ShowSdkUpdateDialog(updatedVersion, url, upgradeMessage);
            }
            catch (Exception)
            {
                ShowGenericSdkUpdateDialog();
            }
        }
    }

    static string GetVersionLookupDataPropertyValue(object versionLookupData, string key)
    {
        return versionLookupData.GetType().GetProperty(key, BindingFlags.Public | BindingFlags.Instance).GetValue(versionLookupData, null).ToString();
    }
}

internal class NewVersionPopup
{
    const string VUFORIA_PTC_ASSET_STORE_URL = "https://assetstore.unity.com/publishers/24484";

    const string DIALOG_TITLE = "Vuforia Sample Version Check";
    const string NEW_SAMPLE_VERSION_BODY = "This sample is not compatible with Vuforia SDK version {0}. Please go to the asset store to download the newest version of the sample.";
    const string NEW_EXTENSION_VERSION_GENERIC_BODY = "This sample is not compatible with Vuforia SDK version {0}. Please update the SDK to the newest version. A link to the updated SDK version can be found in the Vuforia Configuration. An internet connection is required.";
    const string NEW_EXTENSION_VERSION_BODY = "This sample is not compatible with Vuforia SDK version {0}. A newer version of the SDK ({1}) is available.{2}";
    const string NEW_EXTENSION_VERSION_UPGRADE_MESSAGE = "\nChanges in the new version include:\n{0}";
    const string EXTENSION_MISSING_BODY = "The Vuforia SDK cannot be found. Please install Vuforia to use this sample. A link to the installer can be found in PlayerSettings under \"XR Settings\".";
    const string UPDATE_SAMPLE = "Go to Asset Store";
    const string UPDATE_SDK_GENERIC = "Show Vuforia Configuration";
    const string UPDATE_SDK = "Download new SDK Version";
    const string SHOW_PLAYER_SETTINGS = "Show PlayerSettings";
    const string CLOSE_BUTTON_MESSAGE = "Cancel";


    public static void ShowExtensionMissingPopup()
    {
        var userConfirmed = EditorUtility.DisplayDialog(DIALOG_TITLE, EXTENSION_MISSING_BODY, SHOW_PLAYER_SETTINGS, CLOSE_BUTTON_MESSAGE);
        if (userConfirmed)
            EditorApplication.ExecuteMenuItem("Edit/Project Settings/Player");
    }


    public static void ShowGenericUpdateExtensionPopup(Version sampleVersion)
    {
        var userConfirmed = EditorUtility.DisplayDialog(DIALOG_TITLE, string.Format(NEW_EXTENSION_VERSION_GENERIC_BODY, FormatVersion(sampleVersion)), UPDATE_SDK_GENERIC, CLOSE_BUTTON_MESSAGE);
        if (userConfirmed)
            EditorApplication.ExecuteMenuItem("Window/Vuforia Configuration");
    }

    public static void ShowUpdateSamplePopup(Version sampleVersion)
    {
        var userConfirmed = EditorUtility.DisplayDialog(DIALOG_TITLE, string.Format(NEW_SAMPLE_VERSION_BODY, FormatVersion(sampleVersion)), UPDATE_SAMPLE, CLOSE_BUTTON_MESSAGE);
        if (userConfirmed)
            Application.OpenURL(VUFORIA_PTC_ASSET_STORE_URL);
    }

    public static void ShowGenericExtensionPopup(Version sampleVersion, string newSdkVersion, string url, string upgradeMessage)
    {
        var upgradeMessageText = string.IsNullOrEmpty(upgradeMessage) ? string.Empty : string.Format(NEW_EXTENSION_VERSION_UPGRADE_MESSAGE, upgradeMessage);

        var userConfirmed = EditorUtility.DisplayDialog(DIALOG_TITLE, string.Format(NEW_EXTENSION_VERSION_BODY, FormatVersion(sampleVersion), newSdkVersion, upgradeMessageText), UPDATE_SDK, CLOSE_BUTTON_MESSAGE);
        if (userConfirmed)
            Application.OpenURL(url);
    }

    static string FormatVersion(Version version)
    {
        return version == null ? "<no version>" : string.Format("{0}.{1}", version.Major, version.Minor);
    }
}
#endif
