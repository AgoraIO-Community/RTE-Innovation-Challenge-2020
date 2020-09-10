/*===============================================================================
Copyright (c) 2019 PTC Inc. All Rights Reserved.

Copyright (c) 2015 Qualcomm Connected Experiences, Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other
countries.
===============================================================================*/

using UnityEngine;
using UnityEngine.UI;
using Vuforia;
using System.Collections.Generic;
using System.Linq;

public class MenuOptions : MonoBehaviour
{
    #region PRIVATE_MEMBERS
    CameraSettings cameraSettings;
    TrackableSettings trackableSettings;
    Toggle deviceTrackerToggle, autofocusToggle, flashToggle;
    Canvas optionsMenuCanvas;
    OptionsConfig optionsConfig;
    #endregion // PRIVATE_MEMBERS

    public bool IsDisplayed { get; private set; }

    #region MONOBEHAVIOUR_METHODS
    protected virtual void Start()
    {
        this.cameraSettings = FindObjectOfType<CameraSettings>();
        this.trackableSettings = FindObjectOfType<TrackableSettings>();
        this.optionsConfig = FindObjectOfType<OptionsConfig>();
        this.optionsMenuCanvas = GetComponentInChildren<Canvas>(true);
        this.deviceTrackerToggle = FindUISelectableWithText<Toggle>("Tracker");
        this.autofocusToggle = FindUISelectableWithText<Toggle>("Autofocus");
        this.flashToggle = FindUISelectableWithText<Toggle>("Flash");

        if (this.flashToggle)
        {
            // Flash is not supported on ARCore Devices
            this.flashToggle.interactable = !(
            Application.platform == RuntimePlatform.Android &&
            VuforiaRuntimeUtilities.GetActiveFusionProvider() == FusionProviderType.PLATFORM_SENSOR_FUSION);
        }

        VuforiaARController.Instance.RegisterOnPauseCallback(OnPaused);
    }
    #endregion // MONOBEHAVIOUR_METHODS


    #region PUBLIC_BUTTON_METHODS

    public void ToggleAutofocus(bool enable)
    {
        if (this.cameraSettings)
        {
            this.cameraSettings.SwitchAutofocus(enable);
        }
    }

    public void ToggleTorch(bool enable)
    {
        if (this.flashToggle && this.cameraSettings)
        {
            this.cameraSettings.SwitchFlashTorch(enable);

            // Update UI toggle status (ON/OFF) in case the flash switch failed
            this.flashToggle.isOn = this.cameraSettings.IsFlashTorchEnabled();
        }
    }

    public void ToggleExtendedTracking(bool enable)
    {
        if (this.trackableSettings)
        {
            this.trackableSettings.ToggleDeviceTracking(enable);
        }
    }

    #endregion // PUBLIC_BUTTON_METHODS


    #region PUBLIC_METHODS

    public void ActivateDataset(string datasetName)
    {
        if (this.trackableSettings)
        {
            this.trackableSettings.ActivateDataSet(datasetName);
        }
    }

    public void UpdateUI()
    {
        if (this.deviceTrackerToggle && this.trackableSettings)
        {
            this.deviceTrackerToggle.isOn = this.trackableSettings.IsDeviceTrackingEnabled();
        }

        if (this.flashToggle && this.cameraSettings)
        {
            this.flashToggle.isOn = this.cameraSettings.IsFlashTorchEnabled();
        }

        if (this.autofocusToggle && this.cameraSettings)
        {
            this.autofocusToggle.isOn = this.cameraSettings.IsAutofocusEnabled();
        }
    }

    public void ResetDeviceTracker()
    {
        var objTracker = TrackerManager.Instance.GetTracker<ObjectTracker>();

        if (objTracker != null && objTracker.IsActive)
        {
            Debug.Log("Stopping the ObjectTracker...");
            objTracker.Stop();

            // Create a temporary list of active datasets to prevent
            // InvalidOperationException caused by modifying the active
            // dataset list while iterating through it
            List<DataSet> activeDataSets = objTracker.GetActiveDataSets().ToList();

            // Reset active datasets
            foreach (DataSet dataset in activeDataSets)
            {
                objTracker.DeactivateDataSet(dataset);
                objTracker.ActivateDataSet(dataset);
            }

            Debug.Log("Restarting the ObjectTracker...");
            objTracker.Start();
        }

        var deviceTracker = TrackerManager.Instance.GetTracker<PositionalDeviceTracker>();

        if (deviceTracker != null && deviceTracker.Reset())
        {
            Debug.Log("Successfully reset device tracker");
        }
        else
        {
            Debug.LogError("Failed to reset device tracker");
        }
    }

    public void ShowOptionsMenu(bool show)
    {
        if (this.optionsConfig && this.optionsConfig.AnyOptionsEnabled())
        {
            CanvasGroup canvasGroup = null;
            
            if (this.optionsMenuCanvas)
            {
                canvasGroup = this.optionsMenuCanvas.GetComponentInChildren<CanvasGroup>();
            }
            else
            {
                canvasGroup = GetComponent<CanvasGroup>();
            }
            
            if (show)
            {
                UpdateUI();
            }

            canvasGroup.interactable = show;
            canvasGroup.blocksRaycasts = show;
            canvasGroup.alpha = show ? 1.0f : 0.0f;
            this.IsDisplayed = show;
        }
    }

    #endregion //PUBLIC_METHODS


    #region PROTECTED_METHODS

    protected T FindUISelectableWithText<T>(string text) where T : UnityEngine.UI.Selectable
    {
        T[] uiElements = GetComponentsInChildren<T>(true);
        foreach (var element in uiElements)
        {
            string childText = element.GetComponentInChildren<Text>().text;
            if (childText.Contains(text))
            {
                return element;
            }
        }
        return null;
    }

    #endregion // PROTECTED_METHODS


    #region PRIVATE_METHODS

    private void OnPaused(bool paused)
    {
        if (paused)
        {
            // Handle any tasks when app is paused here:
        }
        else
        {
            // Handle any tasks when app is resume here:

            // The flash torch is switched off by the OS automatically when app is paused.
            // On resume, update torch UI toggle to match torch status.
            UpdateUI();
        }
    }

    #endregion // PRIVATE_METHODS

}
