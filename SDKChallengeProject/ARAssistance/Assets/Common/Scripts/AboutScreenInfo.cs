/*===============================================================================
Copyright (c) 2019 PTC Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other
countries.
===============================================================================*/

using System.Collections.Generic;
using UnityEngine;
using Vuforia;

public class AboutScreenInfo
{
    #region PRIVATE_MEMBERS

    readonly Dictionary<string, string> titles;
    readonly Dictionary<string, string> descriptions;

    #endregion // PRIVATE_MEMBERS


    #region PUBLIC_METHODS

    public string GetTitle(string titleKey)
    {
        return GetValuefromDictionary(this.titles, titleKey);
    }

    public string GetDescription(string descriptionKey)
    {
        return GetValuefromDictionary(this.descriptions, descriptionKey);
    }

    #endregion // PUBLIC_METHODS


    #region PRIVATE_METHODS

    string GetValuefromDictionary(Dictionary<string, string> dictionary, string key)
    {
        if (dictionary.ContainsKey(key))
        {
            string value;
            dictionary.TryGetValue(key, out value);
            return value;
        }

        return "Key not found.";
    }

    #endregion // PRIVATE_METHODS


    #region CONSTRUCTOR

    public AboutScreenInfo()
    {

        // Init our Title Strings

        this.titles = new Dictionary<string, string>()
        {
            { "ImageTargets", "Image Targets (from database)" },
            { "InstantImageTargets", "Instant Image Targets" },
            { "VuMarks", "VuMarks" },
            { "CylinderTargets", "Cylinder Targets" },
            { "MultiTargets", "Multi Targets" },
            { "UserDefinedTargets", "User Defined Targets" },
            { "ObjectReco", "Object Reco" },
            { "CloudReco", "Cloud Reco" },
            { "VirtualButtons", "Virtual Buttons" },
            { "ModelTargets", "Model Targets" },
            { "GroundPlane", "Ground Plane" },
            { "BackgroundTextureAccess", "Background Texture Access" },
            { "OcclusionManagement", "Occlusion Management" },
            { "Books", "Books" },
            { "ModelTargetsTest", "Model Targets Test" }
        };

        // Init our Common Cache Strings

        string vuforiaVersion = Vuforia.VuforiaUnity.GetVuforiaLibraryVersion();
        string unityVersion = UnityEngine.Application.unityVersion;
        UnityEngine.Debug.Log("Vuforia Engine " + vuforiaVersion + "\nUnity " + unityVersion);

        string vuforia = Vuforia.VuforiaRuntime.Instance.InitializationState != Vuforia.VuforiaRuntime.InitState.NOT_INITIALIZED
                                ? "<#23B200>Yes</color>"
                                : "<color=red>No</color>";
        string fusionProvider = Vuforia.VuforiaRuntimeUtilities.GetActiveFusionProvider().ToString();

        string description = "\n<size=26>Description:</size>";
        string keyFunctionality = "<size=26>Key Functionality:</size>";
        string targets = "<size=26>Targets:</size>";
        string instructions = "<size=26>Instructions:</size>";
        string footer =
            "<size=26>Version Info:</size>" +
            "\n• Vuforia Engine " + vuforiaVersion +
            "\n• Unity " + unityVersion +
            "\n" +
            "\n<size=26>Vuforia Info:</size>" +
            "\n• Vuforia Engine Enabled: " + vuforia +
            "\n• Fusion Provider: " + fusionProvider.Replace("_", " ") +
            "\n" +
            "\n<size=26>System Info:</size>" +
            "\n• Device Name: " + SystemInfo.deviceName +
            "\n• Device Model: " + SystemInfo.deviceModel +
            "\n• Operating System: " + SystemInfo.operatingSystem +
            "\n• System Memory: " + SystemInfo.systemMemorySize +
            "\n• Processor Count: " + SystemInfo.processorCount +
            "\n• Processor Frequency: " + SystemInfo.processorFrequency +
            "\n" +
            "\n<size=26>Graphics Info:</size>" +
            "\n• Graphics Memory: " + SystemInfo.graphicsMemorySize +
            "\n• Device Name: " + SystemInfo.graphicsDeviceName +
            "\n• Device Vendor: " + SystemInfo.graphicsDeviceVendor +
            "\n• Device Type: " + SystemInfo.graphicsDeviceType +
            "\n• Device Version: " + SystemInfo.graphicsDeviceVersion +
            "\n• MultiThreaded: " + SystemInfo.graphicsMultiThreaded +
            "\n" +
            "\n<size=26>Links:</size>" +
            "\n• <link=https://developer.vuforia.com/legal/vuforia-developer-agreement><color=blue><u>Developer Agreement</u></color></link>" +
            "\n• <link=https://developer.vuforia.com/legal/privacy><color=blue><u>Privacy Policy</u></color></link>" +
            "\n• <link=https://developer.vuforia.com/legal/EULA><color=blue><u>Terms of Use</u></color></link>" +
            "\n• <link=https://developer.vuforia.com/legal/statistics><color=blue><u>Statistics</u></color></link>" +
            "\n\n" +
            "© 2019 PTC Inc. All Rights Reserved." +
            "\n";
        string targetPDFsURL = "<link=https://library.vuforia.com/content/vuforia-library/en/articles/Solution/sample-apps-target-pdfs.html>";

        // Init our Description Strings

        this.descriptions = new Dictionary<string, string>();

        // Image Targets

        this.descriptions.Add(
            "ImageTargets",
            description +
            "\nThe Image Targets sample shows how to detect an image " +
            "target and render a simple 3D object on top of it." +
            "\n\n" +
            keyFunctionality +
            "\n• Simultaneous detection and tracking of multiple targets" +
            "\n• Activate Extended Tracking" +
            "\n• Manage camera functions: flash and continuous autofocus" +
            "\n\n" +
            targets +
            "\n• " + targetPDFsURL +
            "<color=blue><u>Target PDFs</u></color></link>" +
            "\n\n" +
            instructions +
            "\n• Point camera at target to view" +
            "\n• Double tap to focus" +
            "\n\n" +
            footer + "\n");

        // Instant Image Targets
        
        // determine if a license key has been set
        string licenseKeyNote = string.Empty;

        if (VuforiaConfiguration.Instance.Vuforia.LicenseKey.Equals(string.Empty))
        {
            licenseKeyNote = "\n<color=red>Please configure a license key in the Vuforia Configuration!</color>";
        }
        else
        {
            licenseKeyNote = "\n<#23B200>A license key has been set.</color>";
        }

        this.descriptions.Add(
            "InstantImageTargets",
            description +
            "\nThe Instant Image Targets sample shows how to create an image " +
            "target from image assets loaded at runtime without creating a " +
            "target manager database." + 
            "\nThis sample requires a license key from:" +
            "\n<link=https://developer.vuforia.com/license-manager><color=blue><u>https://developer.vuforia.com/license-manager</u></color></link>" +
            licenseKeyNote + 
            "\n\n" +
            keyFunctionality +
            "\n• Creating an image target from image assets" +
            "\n• Simultaneous detection and tracking of multiple targets" +
            "\n• Activate Extended Tracking" +
            "\n• Manage camera functions: flash and continuous autofocus" +
            "\n\n" +
            targets +
            "\n• " + targetPDFsURL +
            "<color=blue><u>Target PDFs</u></color></link>" +
            "\n\n" +
            instructions +
            "\n• Point camera at target to view" +
            "\n• Double tap to focus" +
            "\n\n" +
            footer + "\n");


        // VuMark

        this.descriptions.Add(
            "VuMarks",
            description +
            "\nThe VuMarks sample shows how to detect and track VuMarks." +
            "\n\n" +
            keyFunctionality +
            "\n• Simultaneous detection and tracking of multiple VuMarks" +
            "\n• Load and activate a VuMark target" +
            "\n• Activate Extended Tracking" +
            "\n• Render an outline when a VuMark is detected" +
            "\n\n" +
            targets +
            "\n• " + targetPDFsURL +
            "<color=blue><u>Target PDFs</u></color></link>" +
            "\n\n" +
            instructions +
            "\n• Point device at VuMark" +
            "\n• Double tap to focus" +
            "\n• Tap window showing VuMark ID to dismiss" +
            "\n\n" +
            footer + "\n");


        // Cylinder Targets

        this.descriptions.Add(
            "CylinderTargets",
            description +
            "\nThe Cylinder Targets sample shows how to detect a cylindrical " +
            "target and animate a 3D object around the circumference of the cylinder." +
            "\n\n" +
            keyFunctionality +
            "\n• Detection and tracking of a cylinder target" +
            "\n• Occlusion handling" +
            "\n\n" +
            targets +
            "\n• " + targetPDFsURL +
            "<color=blue><u>Target PDFs</u></color></link>" +
            "\n\n" +
            "Print target and wrap around a standard soda can." +
            "\n\n" +
            instructions +
            "\n• Point camera at target to view" +
            "\n• Double tap to focus" +
            "\n\n" +
            footer + "\n");


        // Multi Targets

        this.descriptions.Add(
            "MultiTargets",
            description +
            "\nThe Multi Targets sample shows how to detect a simple cuboid 3D shape " +
            "and animate a 3D object around the shape." +
            "\n\n" +
            keyFunctionality +
            "\n• Detection and tracking of cuboid 3D shape" +
            "\n• Occlusion handling" +
            "\n\n" +
            targets +
            "\n• " + targetPDFsURL +
            "<color=blue><u>Target PDFs</u></color></link>" +
            "\n\n" +
            instructions +
            "\n• Point camera at target to view" +
            "\n• Double tap to focus" +
            "\n\n" +
            footer + "\n");


        // User Defined Targets

        this.descriptions.Add(
            "UserDefinedTargets",
            description +
            "\nThe User Defined Targets sample shows how to capture and create an " +
            "image target at runtime from user-selected camera video frames." +
            "\n\n" +
            keyFunctionality +
            "\n• Create and manage user defined image target" +
            "\n• Activate Extended Tracking" +
            "\n\n" +
            instructions +
            "\n• Hold device parallel to feature rich target and tap camera button" +
            "\n• Double tap to focus" +
            "\n\n" +
            footer + "\n");


        // Object Reco

        this.descriptions.Add(
            "ObjectReco",
            description +
            "\nThe Object Recognition sample shows how to recognize and track an object." +
            "\n\n" +
            keyFunctionality +
            "\n• Recognize and track up to 2 objects simultaneously" +
            "\n• Activate Extended Tracking" +
            "\n• Manage camera functions: flash" +
            "\n\n" +
            targets +
            "\n• " + targetPDFsURL +
            "<color=blue><u>Target PDFs</u></color></link>" +
            "\n\n" +
            instructions +
            "\n• Point camera at target to view" +
            "\n• Double tap to focus" +
            "\n\n" +
            footer + "\n");


        // Cloud Reco

        this.descriptions.Add(
            "CloudReco",
            description +
            "\nThe Cloud Reco sample shows how to use the cloud recognition service to " +
            "recognize targets located in a cloud database." +
            "\n\n" +
            keyFunctionality +
            "\n• Manage detection and tracking of cloud based image targets" +
            "\n• Activate Extended Tracking" +
            "\n\n" +
            targets +
            "\n• " + targetPDFsURL +
            "<color=blue><u>Target PDFs</u></color></link>" +
            "\n\n" +
            instructions +
            "\n• Point camera at target to view" +
            "\n• Double tap to focus" +
            "\n\n" +
            footer + "\n");


        // Virtual Buttons

        this.descriptions.Add(
            "VirtualButtons",
            description +
            "\nThe Virtual Buttons sample shows how the developer can define rectangular " +
            "regions on image targets that trigger an event when touched or occluded in " +
            "the camera view. The sample renders a 3D object that performs an animation when " +
            "one of the virtual buttons is triggered." +
            "\n\n" +
            keyFunctionality +
            "\n• Button occlusion event handling" +
            "\n\n" +
            targets +
            "\n• " + targetPDFsURL +
            "<color=blue><u>Target PDFs</u></color></link>" +
            "\n\n" +
            instructions +
            "\n• Point camera at target to view" +
            "\n• Double tap to focus" +
            "\n\n" +
            footer + "\n");


        // Model Targets

        this.descriptions.Add(
            "ModelTargets",
            description +
            "\nThe Model Targets Sample shows how to detect a 3D object and " +
            "render a simple 3D representation over it. The sample demonstrates " +
            "how Standard and Advanced DataSets work." +
            "\n\n" +
            keyFunctionality +
            "\n• Standard Model Target: Loads a Model Target with multiple Guide Views " +
            "that can be cycled with the click of a button and used to pick which view " +
            "to detect of the physical model" +
            "\n• Advanced Model Target: Loads two Model Targets that have automatic " +
            "detection from arbitrary views and snapping of Guide Views to the physical models" +
            "\n• Automatic 3D object tracking after successful detection" +
            "\n• Extended Tracking when target is not visible in the camera view" +
            "\n\n" +
            targets +
            "\n• Model Target: 3D Printed Model (Mars Lander)" +
            "\n• Model Target: Toy Model (Polaris RZR)" +
            "\n\n" +
            instructions +
            "\n• Point camera at target to view" +
            "\n• Double tap to focus" +
            "\n• Switch to access a different dataset" +
            "\n• Mars Lander is in both datasets" +
            "\n• Polaris RZR is in the Advanced dataset" +
            "\n• Change the Detection Position if needed" +
            "\n\n" +
            footer + "\n");


        // Ground Plane

        this.descriptions.Add(
            "GroundPlane",
            description +
            "\nThe Ground Plane sample demonstrates how to place " +
            "content on surfaces and in mid-air using anchor points." +
            "\n\n" +
            keyFunctionality +
            "\n• Hit testing places the Chair or the Astronaut on an intersecting plane in " +
            "the environment. Try this mode by pressing the Chair or Astronaut buttons." +
            "\n• Mid-Air anchoring places the drone on an anchor point created " +
            "at a fixed distance relative to the user. Select this mode by " +
            "pressing the Drone button." +
            "\n\n" +
            targets +
            "\n• None required" +
            "\n\n" +
            instructions +
            "\n• Launch the app and view your environment" +
            "\n• Look around until the indicator shows that you have found a surface" +
            "\n• Tap to place Chair on the ground" +
            "\n• Drag with one finger to move Chair along ground" +
            "\n• Touch and hold with two fingers to rotate Chair" +
            "\n• Select Ground Plane mode" +
            "\n• Tap to place Astronaut on the ground" +
            "\n• Tap again to move Astronaut to second point" +
            "\n• Select Mid-Air mode" +
            "\n• Tap to place Drone in the air" +
            "\n• Tap again to move Drone to the desired position" +
            "\n\n" +
            footer + "\n");


        // Background Texture Access

        this.descriptions.Add(
            "BackgroundTextureAccess",
            description +
            "\nThe Background Texture Access sample shows how to use two shaders to " +
            "manipulate the background video. One shader turns the video into inverted " +
            "black-and-white and another distorts the video where you touch on the screen." +
            "\n\n" +
            keyFunctionality +
            "\n• Apply shaders to video background" +
            "\n\n" +
            targets +
            "\n• " + targetPDFsURL +
            "<color=blue><u>ImageTarget: Fissure</u></color></link>" +
            "\n\n" +
            instructions +
            "\n• Point camera at target to view" +
            "\n• Tap and drag to distort video background" +
            "\n\n" +
            footer + "\n");


        // Occlusion Management

        this.descriptions.Add(
            "OcclusionManagement",
            description +
            "\nThe Occlusion Management sample shows the use of transparent shaders to " +
            "let users partially look inside a real object with an occlusion effect." +
            "\n\n" +
            keyFunctionality +
            "\n• Manage occlusion" +
            "\n\n" +
            targets +
            "\n• " + targetPDFsURL +
            "<color=blue><u>MultiTarget: MarsBox</u></color></link>" +
            "\n\n" +
            instructions +
            "\n• Point camera at target to view" +
            "\n• Double tap to focus" +
            "\n\n" +
            footer + "\n");


        // Books

        this.descriptions.Add(
            "Books",
            description +
            "\nThe Books sample shows how to use the Cloud Recognition service to build a " +
            "simple UI to scan a sample target book cover and display info on that book." +
            "\n\n" +
            keyFunctionality +
            "\n• Display reco query status" +
            "\n• Request book info meta data based on reco response" +
            "\n• Render book info on target" +
            "\n• Transition display of book info to screen when off target" +
            "\n\n" +
            targets +
            "\n• " + targetPDFsURL +
            "<color=blue><u>ImageTargets: 3 Book Covers</u></color></link>" +
            "\n\n" +
            instructions +
            "\n• Point camera at sample book cover to view info" +
            "\n• Press close button to scan another book" +
            "\n\n" +
            footer + "\n");
        
        // Model Targets Test
        
        this.descriptions.Add(
            "ModelTargetsTest",
            description +
            "\nThe Model Targets Test app allows you to detect " +
            "and track a Model Target out of a set of multiple targets." +
#if ENABLE_MODEL_TARGETS_TEST_APP_DIAGNOSTICS
            " You can also record tracking data and report feedback to Vuforia." +
#endif
            "\n\n" +
            keyFunctionality +
            "\n• Detection and tracking of Model Targets" +
#if ENABLE_MODEL_TARGETS_TEST_APP_DIAGNOSTICS
            "\n• Collection and sending of SDK-generated data for later analysis and support" +
            "\n• Screenshot Capture" +
#endif
            "\n• Tracker reset" +
            "\n\n" +
            targets +
            "\n• Model Target: 3D Printed Model (Mars Lander)" +
            "\n\n" +
            instructions +
            "\n• Point camera at 3D object to start tracking" +
            "\n• Double tap to focus" +
            "\n\n" +
            "<size=26>Compatible Devices:</size>" +
            "\n• <link=https://library.vuforia.com/articles/Solution/vuforia-fusion-supported-devices.html><color=blue><u>Vuforia Fusion Compatible Devices</u></color></link>" +
            "\n\n" +
            "<size=26>Additional Info:</size>" +
            "\n• <link=https://developer.vuforia.com/legal/CAD-3rd-Party-License><color=blue><u>3rd Party License: CAD</u></color></link>" +
#if ENABLE_MODEL_TARGETS_TEST_APP_DIAGNOSTICS
            "\n• <link=https://developer.vuforia.com/legal/statistics_CAD><color=blue><u>Statistics: CAD</u></color></link>" +
#endif
            "\n\n" +
            footer + "\n");
    }

    #endregion // CONSTRUCTOR

}
