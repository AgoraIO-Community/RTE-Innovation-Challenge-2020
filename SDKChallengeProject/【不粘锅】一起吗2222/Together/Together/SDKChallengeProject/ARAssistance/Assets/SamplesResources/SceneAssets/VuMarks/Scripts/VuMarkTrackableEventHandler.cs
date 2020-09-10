/*===============================================================================
Copyright (c) 2017-2018 PTC Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other
countries.
===============================================================================*/
using UnityEngine;
using Vuforia;

public class VuMarkTrackableEventHandler : DefaultTrackableEventHandler
{
    #region PRIVATE_MEMBERS
    const int nthFrame = 15;
    const int persistentNumberOfChildren = 2;  // Persistent Children: 1. Canvas, 2. LineRenderer
    VuMarkBehaviour vumarkBehaviour;
    LineRenderer lineRenderer;
    CanvasGroup canvasGroup;
    Vector2 fadeRange;
    Transform centralAnchorPoint;
    #endregion // PRIVATE_MEMBERS


    #region PROTECTED_METHODS
    protected override void Start()
    {
        base.Start();

        this.canvasGroup = GetComponentInChildren<CanvasGroup>();
        this.fadeRange = VuforiaRuntimeUtilities.IsPlayMode() ? new Vector2(0.5f, 0.6f) : new Vector2(0.9f, 1.0f);

        VuforiaARController.Instance.RegisterVuforiaStartedCallback(OnVuforiaStarted);

        if (this.mTrackableBehaviour is VuMarkBehaviour)
        {
            this.vumarkBehaviour = (VuMarkBehaviour)this.mTrackableBehaviour;
            this.vumarkBehaviour.RegisterVuMarkTargetAssignedCallback(OnVuMarkTargetAssigned);
            this.vumarkBehaviour.RegisterVuMarkTargetLostCallback(OnVuMarkTargetLost);
        }
    }

    // Override, but don't implement these base methods,
    // since VuMark gameobjects are automatically disabled
    protected override void OnTrackingFound() { }
    protected override void OnTrackingLost() { }

    #endregion // PROTECTED_METHODS


    #region VUFORIA_CALLBACKS
    void OnVuforiaStarted()
    {
        this.centralAnchorPoint = VuforiaManager.Instance.CentralAnchorPoint;
    }
    #endregion // VUFORIA_CALLBACKS


    #region MONOBEHAVIOUR_METHODS

    void OnDisable()
    {
        DestroyChildAugmentationsOfTransform(transform);
    }

    void Update()
    {
        // Every nth frame, update the VuMark border outline to catch any changes to target tracking status
        if (Time.frameCount % nthFrame == 0)
        {
            UpdateVuMarkBorderOutline();
        }

        // Every frame, check target distance from camera and set alpha value of VuMark info canvas
        UpdateCanvasFadeAmount();
    }
    #endregion // MONOBEHAVIOUR_METHODS


    #region PRIVATE_METHODS

    void UpdateVuMarkBorderOutline()
    {
        if (this.lineRenderer)
        {
            // Only enable line renderer when target becomes Extended Tracked or when running in Unity Editor.
            this.lineRenderer.enabled =
                (m_NewStatus == TrackableBehaviour.Status.EXTENDED_TRACKED) || VuforiaRuntimeUtilities.IsPlayMode();

            if (this.lineRenderer.enabled)
            {
                // If the Device Tracker is enabled and the target becomes Extended Tracked,
                // set the VuMark outline to green. If in Unity Editor PlayMode, set to cyan.
                // Note that on HoloLens, the Device Tracker is always enabled (as of Vuforia 7.2).
                this.lineRenderer.material.color =
                    (m_NewStatus == TrackableBehaviour.Status.EXTENDED_TRACKED) ? Color.green : Color.cyan;
            }
        }
        else
        {
            this.lineRenderer = GetComponentInChildren<LineRenderer>();
        }
    }

    void UpdateCanvasFadeAmount()
    {
        if (this.centralAnchorPoint != null)
        {
            Vector3 positionInCameraSpace = this.centralAnchorPoint.InverseTransformPoint(this.transform.position);

            float distance = Vector3.Distance(Vector2.zero, positionInCameraSpace);

            this.canvasGroup.alpha = 1 - Mathf.InverseLerp(this.fadeRange.x, this.fadeRange.y, distance);
        }
    }

    void DestroyChildAugmentationsOfTransform(Transform parent)
    {
        if (parent.childCount > persistentNumberOfChildren)
        {
            for (int x = persistentNumberOfChildren; x < parent.childCount; x++)
            {
                Destroy(parent.GetChild(x).gameObject);
            }
        }
    }
    #endregion // PRIVATE_METHODS


    #region VUMARK_CALLBACK_METHODS
    public void OnVuMarkTargetAssigned()
    {
        Debug.Log("<color=cyan>VuMarkBehaviour.OnVuMarkTargetAssigned() called.</color>");

        if (this.mTrackableBehaviour is VuMarkBehaviour)
        {
            this.vumarkBehaviour = this.mTrackableBehaviour as VuMarkBehaviour;
            if (this.vumarkBehaviour.VuMarkTarget != null)
                Debug.Log("<color=cyan>VuMark ID Tracked: </color>" + this.vumarkBehaviour.VuMarkTarget.InstanceId.NumericValue);
        }
    }

    public void OnVuMarkTargetLost()
    {
        Debug.Log("<color=cyan>VuMarkBehaviour.OnVuMarkLost() called.</color>");
    }

    #endregion // VUMARK_CALLBACK_METHODS

}
