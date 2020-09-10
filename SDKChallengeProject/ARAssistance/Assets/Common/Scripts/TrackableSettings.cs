/*===============================================================================
Copyright (c) 2015-2018 PTC Inc. All Rights Reserved.

Copyright (c) 2015 Qualcomm Connected Experiences, Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other
countries.
===============================================================================*/
using UnityEngine;
using UnityEngine.UI;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using Vuforia;
using System.Timers;

public class TrackableSettings : MonoBehaviour
{
    #region PRIVATE_MEMBERS
    bool deviceTrackerEnabled;
    PositionalDeviceTracker positionalDeviceTracker;
    Timer relocalizationStatusDelayTimer;
    Timer resetDeviceTrackerTimer;
    #endregion // PRIVATE_MEMBERS


    #region UNITY_MONOBEHAVIOUR_METHODS

    private void Awake()
    {
        deviceTrackerEnabled = VuforiaConfiguration.Instance.DeviceTracker.AutoInitAndStartTracker;
    }

    private void Start()
    {
        VuforiaARController.Instance.RegisterVuforiaStartedCallback(OnVuforiaStarted);
        DeviceTrackerARController.Instance.RegisterDevicePoseStatusChangedCallback(OnDevicePoseStatusChanged);

        // Setup a timer to have short delay before processing RELOCALIZING status
        this.relocalizationStatusDelayTimer = new Timer(1000);
        this.relocalizationStatusDelayTimer.Elapsed += RelocalizingStatusDelay;
        this.relocalizationStatusDelayTimer.AutoReset = false;

        // Setup a timer to restart the DeviceTracker if tracking does not receive
        // status change from StatusInfo.RELOCALIZATION after 10 seconds.
        this.resetDeviceTrackerTimer = new Timer(10000);
        this.resetDeviceTrackerTimer.Elapsed += ResetDeviceTracker;
        this.resetDeviceTrackerTimer.AutoReset = false;
    }

    private void OnDestroy()
    {
        VuforiaARController.Instance.UnregisterVuforiaStartedCallback(OnVuforiaStarted);
        DeviceTrackerARController.Instance.UnregisterDevicePoseStatusChangedCallback(OnDevicePoseStatusChanged);
    }

    #endregion // UNITY_MONOBEHAVIOUR_METHODS


    #region VUFORIA_CALLBACKS

    void OnVuforiaStarted()
    {
        this.positionalDeviceTracker = TrackerManager.Instance.GetTracker<PositionalDeviceTracker>();
    }

    void OnDevicePoseStatusChanged(Vuforia.TrackableBehaviour.Status status, Vuforia.TrackableBehaviour.StatusInfo statusInfo)
    {
        // Debug.Log("OnDevicePoseStatusChanged(" + status + ", " + statusInfo + ")");

        if (statusInfo == Vuforia.TrackableBehaviour.StatusInfo.RELOCALIZING)
        {
            // If the status is Relocalizing, then start the timer if it isn't active
            if (!this.relocalizationStatusDelayTimer.Enabled)
            {
                this.relocalizationStatusDelayTimer.Start();
            }
        }
        else
        {
            // If the status is not Relocalizing, then stop the timers if they are active
            if (this.relocalizationStatusDelayTimer.Enabled)
            {
                this.relocalizationStatusDelayTimer.Stop();
            }

            if (this.resetDeviceTrackerTimer.Enabled)
            {
                this.resetDeviceTrackerTimer.Stop();
            }

            // Clear the status message
            StatusMessage.Instance.Display(string.Empty);
        }
    }

    #endregion // VUFORIA_CALLBACKS


    #region PRIVATE_METHODS

    // This is a C# delegate method for the Timer:
    // ElapsedEventHandler(object sender, ElapsedEventArgs e)
    void RelocalizingStatusDelay(System.Object source, ElapsedEventArgs e)
    {
        StatusMessage.Instance.Display("Point back to previously seen area and rescan to relocalize.");

        if (!this.resetDeviceTrackerTimer.Enabled)
        {
            this.resetDeviceTrackerTimer.Start();
        }
    }

    // This is a C# delegate method for the Timer:
    // ElapsedEventHandler(object sender, ElapsedEventArgs e)
    void ResetDeviceTracker(System.Object source, ElapsedEventArgs e)
    {
        ToggleDeviceTracking(false);
        ToggleDeviceTracking(true);
    }

    #endregion // PRIVATE_METHODS


    #region PUBLIC_METHODS

    public bool IsDeviceTrackingEnabled()
    {
        return this.deviceTrackerEnabled;
    }

    public virtual void ToggleDeviceTracking(bool enableDeviceTracking)
    {
        if (this.positionalDeviceTracker == null)
        {
            // If the VuforiaBehaviour is already active from a previous scene,
            // we need to deactivate it briefly to be able to initialize the device tracker
            bool enableVuforiaBehaviourAgain = false;
            var vuforiaBehaviour = GameObject.FindObjectOfType<VuforiaBehaviour>();

            if (VuforiaARController.Instance.HasStarted && vuforiaBehaviour != null)
            {
                vuforiaBehaviour.enabled = false;
                enableVuforiaBehaviourAgain = true;
            }
            
            this.positionalDeviceTracker = TrackerManager.Instance.InitTracker<PositionalDeviceTracker>();
            
            // restart the VuforiaBehaviour if it was stopped again
            if (enableVuforiaBehaviourAgain)
            {
                vuforiaBehaviour.enabled = true;
            }
        }
        
        if (this.positionalDeviceTracker != null)
        {
            if (enableDeviceTracking)
            {
                // if the positional device tracker is not yet started, start it
                if (!this.positionalDeviceTracker.IsActive)
                {
                    if (this.positionalDeviceTracker.Start())
                    {
                        Debug.Log("Successfully started device tracker");
                    }
                    else
                    {
                        Debug.LogError("Failed to start device tracker");
                    }
                }
            }
            else if (this.positionalDeviceTracker.IsActive)
            {
                this.positionalDeviceTracker.Stop();

                Debug.Log("Successfully stopped device tracker");
            }
        }
        else
        {
            Debug.LogError("Failed to toggle device tracker state, make sure device tracker is initialized");
            return;
        }

        this.deviceTrackerEnabled = this.positionalDeviceTracker.IsActive;
    }

    public string GetActiveDatasetName()
    {
        ObjectTracker tracker = TrackerManager.Instance.GetTracker<ObjectTracker>();
        List<DataSet> activeDataSets = tracker.GetActiveDataSets().ToList();
        if (activeDataSets.Count > 0)
        {
            string datasetPath = activeDataSets.ElementAt(0).Path;
            string datasetName = datasetPath.Substring(datasetPath.LastIndexOf("/") + 1);
            return datasetName.TrimEnd(".xml".ToCharArray());
        }
        else
        {
            return string.Empty;
        }
    }

    public void ActivateDataSet(string datasetName)
    {
        // ObjectTracker tracks ImageTargets contained in a DataSet and provides methods for creating and (de)activating datasets.
        ObjectTracker objectTracker = TrackerManager.Instance.GetTracker<ObjectTracker>();
        IEnumerable<DataSet> datasets = objectTracker.GetDataSets();

        IEnumerable<DataSet> activeDataSets = objectTracker.GetActiveDataSets();
        List<DataSet> activeDataSetsToBeRemoved = activeDataSets.ToList();

        // 1. Loop through all the active datasets and deactivate them.
        foreach (DataSet ads in activeDataSetsToBeRemoved)
        {
            objectTracker.DeactivateDataSet(ads);
        }

        // Swapping of the datasets should NOT be done while the ObjectTracker is running.
        // 2. So, Stop the tracker first.
        objectTracker.Stop();

        // 3. Then, look up the new dataset and if one exists, activate it.
        foreach (DataSet ds in datasets)
        {
            if (ds.Path.Contains(datasetName))
            {
                objectTracker.ActivateDataSet(ds);
            }
        }

        // 4. Finally, restart the object tracker and reset the device tracker.
        objectTracker.Start();

        if (this.positionalDeviceTracker != null)
        {
            this.positionalDeviceTracker.Reset();
        }
        else
        {
            Debug.LogError("Failed to reset device tracker");
        }
    }

    #endregion //PUBLIC_METHODS
}
