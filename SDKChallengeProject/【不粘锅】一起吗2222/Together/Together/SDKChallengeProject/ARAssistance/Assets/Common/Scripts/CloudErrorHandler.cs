/*===============================================================================
Copyright (c) 2018 PTC Inc. All Rights Reserved.
 
Vuforia is a trademark of PTC Inc., registered in the United States and other
countries.
===============================================================================*/
using UnityEngine;
using Vuforia;

public class CloudErrorHandler : MonoBehaviour
{

    #region PRIVATE_MEMBERS
    bool mustRestartApp;
    string errorTitle;
    string errorMsg;
    CloudRecoBehaviour m_CloudRecoBehaviour;
    #endregion PRIVATE_MEMBERS


    #region MONOBEHAVIOUR_METHODS
    void Start()
    {
        // Register this event handler with CloudRecoBehaviour
        m_CloudRecoBehaviour = FindObjectOfType<CloudRecoBehaviour>();

        if (m_CloudRecoBehaviour)
        {
            m_CloudRecoBehaviour.RegisterOnInitErrorEventHandler(OnInitError);
            m_CloudRecoBehaviour.RegisterOnUpdateErrorEventHandler(OnUpdateError);
        }

        if (VuforiaConfiguration.Instance.Vuforia.LicenseKey == string.Empty && m_CloudRecoBehaviour)
        {
            errorTitle = "Cloud Reco Init Error";
            errorMsg = "Vuforia License Key not found. Cloud Reco requires a valid license.";

            MessageBox.DisplayMessageBox(errorTitle, errorMsg, false, null);
        }
    }

    void OnDestroy()
    {
        if (m_CloudRecoBehaviour)
        {
            m_CloudRecoBehaviour.UnregisterOnInitErrorEventHandler(OnInitError);
            m_CloudRecoBehaviour.UnregisterOnUpdateErrorEventHandler(OnUpdateError);
        }
    }

    #endregion MONOBEHAVIOUR_METHODS


    #region INTERFACE_IMPLEMENTATION_ICloudRecoEventHandler

    /// <summary>
    /// Called if Cloud Reco initialization fails
    /// </summary>
    public void OnInitError(TargetFinder.InitState initError)
    {
        switch (initError)
        {
            case TargetFinder.InitState.INIT_ERROR_NO_NETWORK_CONNECTION:
                mustRestartApp = true;
                errorTitle = "Network Unavailable";
                errorMsg = "Please check your Internet connection and try again.";
                break;
            case TargetFinder.InitState.INIT_ERROR_SERVICE_NOT_AVAILABLE:
                errorTitle = "Service Unavailable";
                errorMsg = "Failed to initialize app because the service is not available.";
                break;
        }

        // Prepend the error code in red
        errorMsg = "<color=red>" + initError.ToString().Replace("_", " ") + "</color>\n\n" + errorMsg;

        // Remove rich text tags for console logging
        var errorTextConsole = errorMsg.Replace("<color=red>", "").Replace("</color>", "");

        Debug.LogError("OnInitError() - Initialization Error: " + initError + "\n\n" + errorTextConsole);

        MessageBox.DisplayMessageBox(errorTitle, errorMsg, true, CloseDialog);
    }

    /// <summary>
    /// Called if a Cloud Reco update error occurs
    /// </summary>
    public void OnUpdateError(TargetFinder.UpdateState updateError)
    {
        switch (updateError)
        {
            case TargetFinder.UpdateState.UPDATE_ERROR_AUTHORIZATION_FAILED:
                errorTitle = "Authorization Error";
                errorMsg = "The cloud recognition service access keys are incorrect or have expired.";
                break;
            case TargetFinder.UpdateState.UPDATE_ERROR_NO_NETWORK_CONNECTION:
                errorTitle = "Network Unavailable";
                errorMsg = "Please check your Internet connection and try again.";
                break;
            case TargetFinder.UpdateState.UPDATE_ERROR_PROJECT_SUSPENDED:
                errorTitle = "Authorization Error";
                errorMsg = "The cloud recognition service has been suspended.";
                break;
            case TargetFinder.UpdateState.UPDATE_ERROR_REQUEST_TIMEOUT:
                errorTitle = "Request Timeout";
                errorMsg = "The network request has timed out, please check your Internet connection and try again.";
                break;
            case TargetFinder.UpdateState.UPDATE_ERROR_SERVICE_NOT_AVAILABLE:
                errorTitle = "Service Unavailable";
                errorMsg = "The service is unavailable, please try again later.";
                break;
            case TargetFinder.UpdateState.UPDATE_ERROR_TIMESTAMP_OUT_OF_RANGE:
                errorTitle = "Clock Sync Error";
                errorMsg = "Please update the date and time and try again.";
                break;
            case TargetFinder.UpdateState.UPDATE_ERROR_UPDATE_SDK:
                errorTitle = "Unsupported Version";
                errorMsg = "The application is using an unsupported version of Vuforia.";
                break;
            case TargetFinder.UpdateState.UPDATE_ERROR_BAD_FRAME_QUALITY:
                errorTitle = "Bad Frame Quality";
                errorMsg = "Low-frame quality has been continuously observed.\n\nError Event Received on Frame: " + Time.frameCount;
                break;
        }

        // Prepend the error code in red
        errorMsg = "<color=red>" + updateError.ToString().Replace("_", " ") + "</color>\n\n" + errorMsg;

        // Remove rich text tags for console logging
        var errorTextConsole = errorMsg.Replace("<color=red>", "").Replace("</color>", "");

        Debug.LogError("OnUpdateError() - Update Error: " + updateError + "\n\n" + errorTextConsole);

        MessageBox.DisplayMessageBox(errorTitle, errorMsg, true, CloseDialog);
    }

    #endregion INTERFACE_IMPLEMENTATION_ICloudRecoEventHandler


    #region PUBLIC_METHODS

    public void CloseDialog()
    {
        if (mustRestartApp) RestartApplication();
    }

    #endregion PUBLIC_METHODS


    #region PRIVATE_METHODS

    // Callback for network-not-available error message
    void RestartApplication()
    {
        int startLevel = UnityEngine.SceneManagement.SceneManager.GetActiveScene().buildIndex - 2;
        if (startLevel < 0) startLevel = 0;
        UnityEngine.SceneManagement.SceneManager.LoadScene(startLevel);
    }

    #endregion PRIVATE_METHODS
}
