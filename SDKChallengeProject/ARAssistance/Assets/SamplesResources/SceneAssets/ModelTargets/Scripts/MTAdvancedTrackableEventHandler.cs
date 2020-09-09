/*==============================================================================
Copyright (c) 2019 PTC Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other
countries.
==============================================================================*/

using UnityEngine;
using Vuforia;

public class MTAdvancedTrackableEventHandler : DefaultTrackableEventHandler
{
    #region PRIVATE_MEMBERS

    private ModelTargetsManager modelTargetsManager;
    private bool isModelTargetReferenceStored;

    #endregion // PRIVATE_MEMBERS


    #region MONOBEHAVIOUR_METHODS

    protected override void Start()
    {
        base.Start();

        this.modelTargetsManager = FindObjectOfType<ModelTargetsManager>();
    }

    void Update()
    {
        if (!this.isModelTargetReferenceStored && this.mTrackableBehaviour.Trackable != null)
        {
            VLog.Log("yellow", "Storing reference to " +
                this.mTrackableBehaviour.TrackableName + ": " +
                this.mTrackableBehaviour.CurrentStatus + " -- " +
                this.mTrackableBehaviour.CurrentStatusInfo);

            ModelTargetBehaviour mtb = this.mTrackableBehaviour as ModelTargetBehaviour;

            if (mtb != null && this.modelTargetsManager)
            {
                this.modelTargetsManager.AddAdvancedModelTargetBehaviour(mtb);
                this.isModelTargetReferenceStored = true;
            }
        }
    }

    #endregion // MONOBEHAVIOUR_METHODS

    
    #region PROTECTED_METHODS
    
    protected override void OnTrackingFound()
    {
        base.OnTrackingFound();

        this.modelTargetsManager.EnableSymbolicTargetsUI(false);
    }
    
    #endregion // PROTECTED_METHODS
}