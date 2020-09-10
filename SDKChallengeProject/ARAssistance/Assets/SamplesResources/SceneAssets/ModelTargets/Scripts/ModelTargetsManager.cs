/*==============================================================================
Copyright (c) 2019 PTC Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other
countries.
==============================================================================*/

using System.Collections.Generic;
using System.Linq;
using UnityEngine;
using Vuforia;

public class ModelTargetsManager : MonoBehaviour
{
    #region PUBLIC_MEMBERS

    public enum ModelTargetMode
    {
        MODE_STANDARD,
        MODE_ADVANCED
    }

    #endregion // PUBLIC_MEMBERS
    
    
    #region PRIVATE_MEMBERS

    // For private serialized fields, we assign references in the Inspector, so disable assignment warnings.
    // Disable: CS0649: Field '' is never assigned to, and will always have its default value false
#pragma warning disable 649
    [Header("Initial Model Target Mode")]
    [SerializeField] ModelTargetMode modelTargetMode = ModelTargetMode.MODE_STANDARD;

    [Header("Model Target Shared Augmentation")]
    [SerializeField] GameObject augmentation;

    [Header("Model Target Behaviours")]
    [SerializeField] ModelTargetBehaviour modelStandard;
    [SerializeField] ModelTargetBehaviour modelAdvanced;

    [Header("DataSet Names")]
    [SerializeField] string DataSetStandard = "";
    [SerializeField] string DataSetAdvanced = "";

    [Header("Mobile")]
    [Tooltip("Guide Views Button only for Mobile")]
    [SerializeField] UnityEngine.UI.Button guideViewsButton;
    [SerializeField] CanvasGroup datasetsMenu;
#pragma warning restore 649
    
    ObjectTracker objectTracker;
    StateManager stateManager;
    ModelTargetsUIManager modelTargetsUIManager;
    List<ModelTargetBehaviour> modelTargetBehaviours;
    string currentActiveDataSet = string.Empty;

    #endregion // PRIVATE_MEMBERS
    

    #region MONOBEHAVIOUR_METHODS

    void Awake()
    {
        this.modelTargetBehaviours = new List<ModelTargetBehaviour>();
        this.modelTargetsUIManager = FindObjectOfType<ModelTargetsUIManager>();
    }
    
    void Start()
    {
        VuforiaARController.Instance.RegisterVuforiaStartedCallback(OnVuforiaStarted);
    }


    void LateUpdate()
    {
        if (this.modelTargetMode == ModelTargetMode.MODE_ADVANCED)
        {
            // Loop through a List of ModelTargetBehaviours checking the CurrentStatusInfo
            // to verify that all of them are in Initializing state.

            if (this.modelTargetBehaviours != null && this.modelTargetBehaviours.Count > 0)
            {
                bool allAdvancedTrackablesInitializing = false;

                foreach (ModelTargetBehaviour mtb in this.modelTargetBehaviours)
                {
                    if (mtb && mtb.CurrentStatusInfo != TrackableBehaviour.StatusInfo.INITIALIZING)
                    {
                        allAdvancedTrackablesInitializing = false;
                        break;
                    }
                    
                    allAdvancedTrackablesInitializing = true;
                }

                // If all of the MTB's are initializing
                EnableSymbolicTargetsUI(allAdvancedTrackablesInitializing);
            }
        }
    }

    void OnDestroy()
    {
        VuforiaARController.Instance.UnregisterVuforiaStartedCallback(OnVuforiaStarted);

        DeactivateActiveDataSets(true);
    }

    #endregion // MONOBEHAVIOUR_METHODS


    #region VUFORIA_CALLBACKS

    void OnVuforiaStarted()
    {
        this.stateManager = TrackerManager.Instance.GetStateManager();
        this.objectTracker = TrackerManager.Instance.GetTracker<ObjectTracker>();

        LoadDataSet(DataSetStandard);
        LoadDataSet(DataSetAdvanced);

        // We can only have one ModelTarget active at a time, so disable all MTBs at start.
        ModelTargetBehaviour[] behaviours = FindObjectsOfType<ModelTargetBehaviour>();
        foreach (ModelTargetBehaviour mtb in behaviours)
        {
            mtb.enabled = false;
        }

        switch (modelTargetMode)
        {
            case ModelTargetMode.MODE_STANDARD:
                // Start with the Standard Model Target DataSet
                SelectDataSetStandard(true);
                break;
            case ModelTargetMode.MODE_ADVANCED:
                // Start with the Advanced Model Target DataSet
                SelectDataSetAdvanced(true);
                break;
        }
    }

    #endregion // VUFORIA_CALLBACKS


    #region PUBLIC_METHODS

    public void EnableSymbolicTargetsUI(bool enable)
    {
        this.modelTargetsUIManager.SetUI(this.modelTargetMode, enable);
    }

    public void AddAdvancedModelTargetBehaviour(ModelTargetBehaviour mtb)
    {
        Debug.Log("AddAdvancedModelTargetBehaviour() called.");
     
        if (mtb != null && this.modelTargetBehaviours != null)
        {
            this.modelTargetBehaviours.Add(mtb);
        }
        
        VLog.Log("cyan", "# of Advanced MTBs: " + this.modelTargetBehaviours.Count);

        EnableSymbolicTargetsUI(this.modelTargetBehaviours.Count == 0);
    }

    #endregion // PUBLIC_METHODS


    #region PUBLIC_BUTTON_METHODS

    public void SelectDataSetStandard(bool active)
    {
        if (active)
        {
            ActivateDataSet(DataSetStandard);
            this.modelTargetMode = ModelTargetMode.MODE_STANDARD;
        }

        if (this.guideViewsButton)
        {
            this.guideViewsButton.interactable = active;
        }

        EnableSymbolicTargetsUI(false);
    }

    public void SelectDataSetAdvanced(bool active)
    {
        if (active)
        {
            ActivateDataSet(DataSetAdvanced);
            this.modelTargetMode = ModelTargetMode.MODE_ADVANCED;
        }

        EnableSymbolicTargetsUI(active);
    }

    public void ShowDataSetMenu(bool active)
    {
        if (this.datasetsMenu)
        {
            this.datasetsMenu.alpha = active ? 1 : 0;
            this.datasetsMenu.interactable = active;
            this.datasetsMenu.blocksRaycasts = active;
        }
    }

    /// <summary>
    /// Cycles through guide views for Standard Model Targets with multiple views.
    /// </summary>
    public void CycleGuideView()
    {
        VLog.Log("cyan", "CycleGuideView() called.");

        if (this.modelStandard != null)
        {
            ModelTarget modelTarget = this.modelStandard.ModelTarget;

            int activeView = modelTarget.GetActiveGuideViewIndex();
            int totalViews = modelTarget.GetNumGuideViews();

            if (totalViews > 1 && activeView > -1)
            {
                int guideViewIndexToActivate = (activeView + 1) % totalViews;

                VLog.Log("yellow",
                    modelTarget.Name + ": Activating Guide View Index " +
                    guideViewIndexToActivate.ToString() + " of " +
                    totalViews.ToString() + " total Guide Views.");

                modelTarget.SetActiveGuideViewIndex(guideViewIndexToActivate);
            }
            else
            {
                VLog.Log("yellow",
                    "GuideView was not cycled." +
                    "\nActive Guide View Index = " + activeView +
                    "\nNumber of Guide Views = " + totalViews);
            }
        }
    }

    #endregion // PUBLIC_BUTTON_METHODS


    #region PRIVATE_METHODS

    void LoadDataSet(string datasetName)
    {
        if (DataSet.Exists(datasetName))
        {
            DataSet dataset = this.objectTracker.CreateDataSet();

            if (dataset.Load(datasetName))
            {
                VLog.Log("yellow", "Loaded DataSet: " + datasetName);
            }
            else
            {
                Debug.LogError("Failed to load DataSet: " + datasetName);
            }
        }
        else
        {
            VLog.Log("yellow", "The following DataSet not found in 'StreamingAssets/Vuforia': " + datasetName);
        }
    }

    void DeactivateActiveDataSets(bool destroyDataSets = false)
    {
        List<DataSet> activeDataSets = this.objectTracker.GetActiveDataSets().ToList();

        foreach (DataSet ds in activeDataSets)
        {
            // The VuforiaEmulator.xml dataset (used by GroundPlane) is managed by Vuforia.
            if (!ds.Path.Contains("VuforiaEmulator.xml"))
            {
                VLog.Log("white", "Deactivating: " + ds.Path);
                this.objectTracker.DeactivateDataSet(ds);
                if (destroyDataSets)
                {
                    this.objectTracker.DestroyDataSet(ds, false);
                }
            }
        }
    }

    void ActivateDataSet(string datasetName)
    {
        VLog.Log("cyan", "ActivateDataSet() called: " + datasetName);

        if (this.currentActiveDataSet == datasetName)
        {
            VLog.Log("yellow", "The selected dataset is already active.");
            // If the current dataset is already active, return.
            return;
        }

        // Stop the Object Tracker before activating/deactivating datasets.
        this.objectTracker.Stop();

        // Deactivate the currently active datasets.
        DeactivateActiveDataSets();

        var dataSets = this.objectTracker.GetDataSets();

        bool dataSetFoundAndActivated = false;

        foreach (DataSet ds in dataSets)
        {
            if (ds.Path.Contains(datasetName + ".xml"))
            {
                // Activate the selected dataset.
                VLog.Log("orange", "Activating: " + ds.Path);
                if (this.objectTracker.ActivateDataSet(ds))
                {
                    this.currentActiveDataSet = datasetName;
                }

                dataSetFoundAndActivated = true;

                var trackables = ds.GetTrackables();

                foreach (Trackable t in trackables)
                {
                    ModelTarget modelTarget = t as ModelTarget;

                    VLog.Log("yellow",
                        modelTarget.Name + ": Active Guide View Index " +
                        modelTarget.GetActiveGuideViewIndex().ToString() + " of " +
                        modelTarget.GetNumGuideViews().ToString() + " total Guide Views.");
                }

                Transform modelTargetTransform = null;

                if (datasetName == DataSetStandard)
                {
                    modelTargetTransform = this.modelStandard.transform;
                }
                else if (datasetName == DataSetAdvanced)
                {
                    modelTargetTransform = this.modelAdvanced.transform;
                }

                // Set the parent transform for the augmentation.
                this.augmentation.transform.SetParent(modelTargetTransform);
                this.augmentation.transform.localPosition = Vector3.zero;
                this.augmentation.transform.localRotation = Quaternion.identity;
                this.augmentation.transform.localScale = Vector3.one;

                // Once we find and process selected dataset, exit foreach loop.
                break;
            }
        }

        if (!dataSetFoundAndActivated)
        {
            Debug.LogError("DataSet Not Found: " + datasetName);
        }

        // Start the Object Tracker.
        this.objectTracker.Start();
    }

    #endregion // PRIVATE_METHODS


    #region UTILITY_METHODS

    public bool ActiveTrackablesExist()
    {
        foreach (TrackableBehaviour trackable in stateManager.GetActiveTrackableBehaviours())
        {
            return true;
        }

        return false;
    }

    public void LogActiveDataSets()
    {
        List<DataSet> activeDataSets = this.objectTracker.GetActiveDataSets().ToList();

        foreach (DataSet ds in activeDataSets)
        {
            VLog.Log("cyan", "Active DS: " + ds.Path);
        }
    }

    public void LogAllDataSets()
    {
        List<DataSet> allDataSets = this.objectTracker.GetDataSets().ToList();

        foreach (DataSet ds in allDataSets)
        {
            VLog.Log("cyan", "DS: " + ds.Path);
        }
    }

    #endregion // UTILITY_METHODS
}