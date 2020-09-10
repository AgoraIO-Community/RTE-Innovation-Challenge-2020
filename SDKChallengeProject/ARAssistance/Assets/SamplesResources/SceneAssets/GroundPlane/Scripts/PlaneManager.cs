/*==============================================================================
Copyright (c) 2019 PTC Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other
countries.
==============================================================================*/
using System.Timers;
using UnityEngine;
using Vuforia;

public class PlaneManager : MonoBehaviour
{
    public enum PlaneMode
    {
        GROUND,
        MIDAIR,
        PLACEMENT
    }

    #region PUBLIC_MEMBERS
    public static PlaneMode CurrentPlaneMode = PlaneMode.PLACEMENT;
    public static bool GroundPlaneHitReceived { get; private set; }
    #endregion // PUBLIC_MEMBERS


    #region PRIVATE_MEMBERS
    [SerializeField] PlaneFinderBehaviour planeFinder = null;
    [SerializeField] MidAirPositionerBehaviour midAirPositioner = null;

    [Header("Plane, Mid-Air, & Placement Augmentations")]
    [SerializeField] GameObject planeAugmentation = null;
    [SerializeField] GameObject midAirAugmentation = null;
    [SerializeField] GameObject placementAugmentation = null;

    const string UnsupportedDeviceTitle = "Unsupported Device";
    const string UnsupportedDeviceBody =
        "This device has failed to start the Positional Device Tracker. " +
        "Please check the list of supported Ground Plane devices on our site: " +
        "\n\nhttps://library.vuforia.com/articles/Solution/ground-plane-supported-devices.html";

    StateManager stateManager;
    SmartTerrain smartTerrain;
    PositionalDeviceTracker positionalDeviceTracker;
    ContentPositioningBehaviour contentPositioningBehaviour;
    TouchHandler touchHandler;
    ProductPlacement productPlacement;
    GroundPlaneUI groundPlaneUI;
    AnchorBehaviour planeAnchor, midAirAnchor, placementAnchor;
    int automaticHitTestFrameCount;
    static TrackableBehaviour.Status StatusCached = TrackableBehaviour.Status.NO_POSE;
    static TrackableBehaviour.StatusInfo StatusInfoCached = TrackableBehaviour.StatusInfo.UNKNOWN;

    // More Strict: Property returns true when Status is Tracked and StatusInfo is Normal.
    public static bool TrackingStatusIsTrackedAndNormal
    {
        get
        {
            return
                (StatusCached == TrackableBehaviour.Status.TRACKED ||
                 StatusCached == TrackableBehaviour.Status.EXTENDED_TRACKED) &&
                StatusInfoCached == TrackableBehaviour.StatusInfo.NORMAL;
        }
    }

    // Less Strict: Property returns true when Status is Tracked/Normal or Limited/Unknown.
    public static bool TrackingStatusIsTrackedOrLimited
    {
        get
        {
            return
                ((StatusCached == TrackableBehaviour.Status.TRACKED ||
                 StatusCached == TrackableBehaviour.Status.EXTENDED_TRACKED) &&
                 StatusInfoCached == TrackableBehaviour.StatusInfo.NORMAL) ||
                (StatusCached == TrackableBehaviour.Status.LIMITED &&
                 StatusInfoCached == TrackableBehaviour.StatusInfo.UNKNOWN);
        }
    }

    bool SurfaceIndicatorVisibilityConditionsMet
    {
        // The Surface Indicator should only be visible if the following conditions
        // are true:
        // 1. Tracking Status is Tracked or Limited (sufficient for Hit Test Anchors
        // 2. Ground Plane Hit was received for this frame
        // 3. The Plane Mode is equal to GROUND or PLACEMENT(see #4)
        // 4. If the Plane Mode is equal to PLACEMENT and *there's no active touches
        get
        {
            return
                (TrackingStatusIsTrackedOrLimited &&
                 GroundPlaneHitReceived &&
                 (CurrentPlaneMode == PlaneMode.GROUND ||
                    (CurrentPlaneMode == PlaneMode.PLACEMENT && Input.touchCount == 0)));
        }
    }

    Timer timer;
    bool timerFinished;
    #endregion // PRIVATE_MEMBERS


    #region MONOBEHAVIOUR_METHODS

    void Start()
    {
        VuforiaARController.Instance.RegisterVuforiaStartedCallback(OnVuforiaStarted);
        VuforiaARController.Instance.RegisterOnPauseCallback(OnVuforiaPaused);
        DeviceTrackerARController.Instance.RegisterTrackerStartedCallback(OnTrackerStarted);
        DeviceTrackerARController.Instance.RegisterDevicePoseStatusChangedCallback(OnDevicePoseStatusChanged);

        this.planeFinder.HitTestMode = HitTestMode.AUTOMATIC;

        this.productPlacement = FindObjectOfType<ProductPlacement>();
        this.touchHandler = FindObjectOfType<TouchHandler>();
        this.groundPlaneUI = FindObjectOfType<GroundPlaneUI>();

        this.planeAnchor = this.planeAugmentation.GetComponentInParent<AnchorBehaviour>();
        this.midAirAnchor = this.midAirAugmentation.GetComponentInParent<AnchorBehaviour>();
        this.placementAnchor = this.placementAugmentation.GetComponentInParent<AnchorBehaviour>();

        UtilityHelper.EnableRendererColliderCanvas(this.planeAugmentation, false);
        UtilityHelper.EnableRendererColliderCanvas(this.midAirAugmentation, false);
        UtilityHelper.EnableRendererColliderCanvas(this.placementAugmentation, false);

        // Setup a timer to restart the DeviceTracker if tracking does not receive
        // status change from StatusInfo.RELOCALIZATION after 10 seconds.
        this.timer = new Timer(10000);
        this.timer.Elapsed += TimerFinished;
        this.timer.AutoReset = false;
    }

    void Update()
    {
        // The timer runs on a separate thread and we need to ResetTrackers on the main thread.
        if (this.timerFinished)
        {
            ResetTrackers();
            this.timerFinished = false;
        }
    }

    void LateUpdate()
    {
        // The AutomaticHitTestFrameCount is assigned the Time.frameCount in the
        // HandleAutomaticHitTest() callback method. When the LateUpdate() method
        // is then called later in the same frame, it sets GroundPlaneHitReceived
        // to true if the frame number matches. For any code that needs to check
        // the current frame value of GroundPlaneHitReceived, it should do so
        // in a LateUpdate() method.
        GroundPlaneHitReceived = (this.automaticHitTestFrameCount == Time.frameCount);

        // Surface Indicator visibility conditions rely upon GroundPlaneHitReceived,
        // so we will move this method into LateUpdate() to ensure that it is called
        // after GroundPlaneHitReceived has been updated in Update().
        SetSurfaceIndicatorVisible(SurfaceIndicatorVisibilityConditionsMet);
    }

    void OnDestroy()
    {
        Debug.Log("OnDestroy() called.");

        VuforiaARController.Instance.UnregisterVuforiaStartedCallback(OnVuforiaStarted);
        VuforiaARController.Instance.UnregisterOnPauseCallback(OnVuforiaPaused);
        DeviceTrackerARController.Instance.UnregisterTrackerStartedCallback(OnTrackerStarted);
        DeviceTrackerARController.Instance.UnregisterDevicePoseStatusChangedCallback(OnDevicePoseStatusChanged);
    }

    #endregion // MONOBEHAVIOUR_METHODS


    #region GROUNDPLANE_CALLBACKS

    public void HandleAutomaticHitTest(HitTestResult result)
    {
        this.automaticHitTestFrameCount = Time.frameCount;

        if (CurrentPlaneMode == PlaneMode.PLACEMENT && !productPlacement.IsPlaced)
        {
            this.productPlacement.DetachProductFromAnchor();
            this.placementAugmentation.PositionAt(result.Position);
        }
    }
    public void HandleInteractiveHitTest(HitTestResult result)
    {
        if (result == null)
        {
            Debug.LogError("Invalid hit test result!");
            return;
        }

        if (!groundPlaneUI.IsCanvasButtonPressed())
        {
            Debug.Log("HandleInteractiveHitTest() called.");

            // If the PlaneFinderBehaviour's Mode is Automatic, then the Interactive HitTestResult will be centered.

            // PlaneMode.Ground and PlaneMode.Placement both use PlaneFinder's ContentPositioningBehaviour
            this.contentPositioningBehaviour = this.planeFinder.GetComponent<ContentPositioningBehaviour>();
            this.contentPositioningBehaviour.DuplicateStage = false;

            // Place object based on Ground Plane mode
            switch (CurrentPlaneMode)
            {
                case PlaneMode.GROUND:

                    // With each tap, the Astronaut is moved to the position of the
                    // newly created anchor. Before we set any anchor, we first want
                    // to verify that the Status=TRACKED/EXTENDED_TRACKED and StatusInfo=NORMAL.
                    if (TrackingStatusIsTrackedAndNormal)
                    {
                        this.contentPositioningBehaviour.AnchorStage = this.planeAnchor;
                        this.contentPositioningBehaviour.PositionContentAtPlaneAnchor(result);
                        UtilityHelper.EnableRendererColliderCanvas(this.planeAugmentation, true);

                        // Astronaut should rotate toward camera with each placement
                        this.planeAugmentation.transform.localPosition = Vector3.zero;
                        UtilityHelper.RotateTowardCamera(this.planeAugmentation);
                    }

                    break;

                case PlaneMode.PLACEMENT:

                    // With a tap a new anchor is created, so we first check that
                    // Status=TRACKED/EXTENDED_TRACKED and StatusInfo=NORMAL before proceeding.
                    if (TrackingStatusIsTrackedAndNormal)
                    {
                        // We assign our stage content, set an anchor and enable the
                        // content.
                        this.contentPositioningBehaviour.AnchorStage = this.placementAnchor;
                        this.contentPositioningBehaviour.PositionContentAtPlaneAnchor(result);
                        UtilityHelper.EnableRendererColliderCanvas(placementAugmentation, true);
                        
                        // If the product has not been placed in the scene yet, we attach it to the anchor
                        // while rotating it to face the camera. Then we activate the content, also
                        // enabling rotation input detection.
                        // Otherwise, we simply attach the content to the new anchor, preserving its rotation.
                        // The placement methods will set the IsPlaced flag to true if the 
                        // transform argument is valid and to false if it is null.
                        if (!this.productPlacement.IsPlaced)
                        {
                            this.productPlacement.PlaceProductAtAnchorFacingCamera(this.placementAnchor.transform);
                            this.touchHandler.enableRotation = true;
                        }
                        else
                        {
                            this.productPlacement.PlaceProductAtAnchor(this.placementAnchor.transform);
                        }
                    }

                    break;
            }
        }
    }

    public void PlaceObjectInMidAir(Transform midAirTransform)
    {
        if (CurrentPlaneMode == PlaneMode.MIDAIR)
        {
            Debug.Log("PlaceObjectInMidAir() called.");

            // With each tap, the Drone is moved to the position of the
            // newly created anchor. Before we set any anchor, we first want
            // to verify that the Status=TRACKED/EXTENDED_TRACKED and StatusInfo=NORMAL.
            if (TrackingStatusIsTrackedAndNormal)
            {
                this.contentPositioningBehaviour.AnchorStage = this.midAirAnchor;
                this.contentPositioningBehaviour.PositionContentAtMidAirAnchor(midAirTransform);
                UtilityHelper.EnableRendererColliderCanvas(this.midAirAugmentation, true);

                this.midAirAugmentation.transform.localPosition = Vector3.zero;
                UtilityHelper.RotateTowardCamera(this.midAirAugmentation);
            }
        }
    }

    #endregion // GROUNDPLANE_CALLBACKS


    #region PUBLIC_BUTTON_METHODS

    public void SetGroundMode(bool active)
    {
        if (active)
        {
            SetMode(PlaneMode.GROUND);
        }
    }

    public void SetMidAirMode(bool active)
    {
        if (active)
        {
            SetMode(PlaneMode.MIDAIR);
        }
    }

    public void SetPlacementMode(bool active)
    {
        if (active)
        {
            SetMode(PlaneMode.PLACEMENT);
        }
    }

    /// <summary>
    /// This method resets the augmentations and scene elements.
    /// It is called by the UI Reset Button and also by OnVuforiaPaused() callback.
    /// </summary>
    public void ResetScene()
    {
        Debug.Log("ResetScene() called.");

        // reset augmentations
        this.planeAugmentation.transform.position = Vector3.zero;
        this.planeAugmentation.transform.localEulerAngles = Vector3.zero;
        UtilityHelper.EnableRendererColliderCanvas(this.planeAugmentation, false);

        this.midAirAugmentation.transform.position = Vector3.zero;
        this.midAirAugmentation.transform.localEulerAngles = Vector3.zero;
        UtilityHelper.EnableRendererColliderCanvas(this.midAirAugmentation, false);

        this.productPlacement.Reset();
        UtilityHelper.EnableRendererColliderCanvas(this.placementAugmentation, false);

        this.productPlacement.DetachProductFromAnchor();
        this.groundPlaneUI.Reset();
        this.touchHandler.enableRotation = false;
    }

    /// <summary>
    /// This method stops and restarts the PositionalDeviceTracker.
    /// It is called by the UI Reset Button and when RELOCALIZATION status has
    /// not changed for 10 seconds.
    /// </summary>
    public void ResetTrackers()
    {
        Debug.Log("ResetTrackers() called.");

        this.smartTerrain = TrackerManager.Instance.GetTracker<SmartTerrain>();
        this.positionalDeviceTracker = TrackerManager.Instance.GetTracker<PositionalDeviceTracker>();

        // Stop and restart trackers
        this.smartTerrain.Stop(); // stop SmartTerrain tracker before PositionalDeviceTracker
        this.positionalDeviceTracker.Reset();
        this.smartTerrain.Start(); // start SmartTerrain tracker after PositionalDeviceTracker
    }

    #endregion // PUBLIC_BUTTON_METHODS


    #region PRIVATE_METHODS

    /// <summary>
    /// This private method is called by the UI Button handler methods.
    /// </summary>
    /// <param name="mode">PlaneMode</param>
    void SetMode(PlaneMode mode)
    {
        CurrentPlaneMode = mode;
        this.groundPlaneUI.UpdateTitle();
        this.planeFinder.enabled = (mode == PlaneMode.GROUND || mode == PlaneMode.PLACEMENT);
        this.midAirPositioner.enabled = (mode == PlaneMode.MIDAIR);
        this.touchHandler.enableRotation = (mode == PlaneMode.PLACEMENT) &&
            this.placementAugmentation.activeInHierarchy;
    }

    /// <summary>
    /// This method can be used to set the Ground Plane surface indicator visibility.
    /// This sample will display it when the Status=TRACKED and StatusInfo=Normal.
    /// </summary>
    /// <param name="isVisible">bool</param>
    void SetSurfaceIndicatorVisible(bool isVisible)
    {
        Renderer[] renderers = this.planeFinder.PlaneIndicator.GetComponentsInChildren<Renderer>(true);
        Canvas[] canvas = this.planeFinder.PlaneIndicator.GetComponentsInChildren<Canvas>(true);

        foreach (Canvas c in canvas)
            c.enabled = isVisible;

        foreach (Renderer r in renderers)
            r.enabled = isVisible;
    }

    /// <summary>
    /// This is a C# delegate method for the Timer:
    /// ElapsedEventHandler(object sender, ElapsedEventArgs e)
    /// </summary>
    /// <param name="source">System.Object</param>
    /// <param name="e">ElapsedEventArgs</param>
    void TimerFinished(System.Object source, ElapsedEventArgs e)
    {
        this.timerFinished = true;
    }
    #endregion // PRIVATE_METHODS


    #region VUFORIA_CALLBACKS

    void OnVuforiaStarted()
    {
        Debug.Log("OnVuforiaStarted() called.");

        stateManager = TrackerManager.Instance.GetStateManager();

        // Check trackers to see if started and start if necessary
        this.positionalDeviceTracker = TrackerManager.Instance.GetTracker<PositionalDeviceTracker>();
        this.smartTerrain = TrackerManager.Instance.GetTracker<SmartTerrain>();

        if (this.positionalDeviceTracker != null && this.smartTerrain != null)
        {
            if (!this.positionalDeviceTracker.IsActive)
            {
                Debug.LogError("The Ground Plane feature requires the Device Tracker to be started. " +
                               "Please enable it in the Vuforia Configuration or start it at runtime through the scripting API.");
                return;
            }

            if (this.positionalDeviceTracker.IsActive && !this.smartTerrain.IsActive)
                this.smartTerrain.Start();
        }
        else
        {
            if (this.positionalDeviceTracker == null)
                Debug.Log("PositionalDeviceTracker returned null. GroundPlane not supported on this device.");
            if (this.smartTerrain == null)
                Debug.Log("SmartTerrain returned null. GroundPlane not supported on this device.");

            MessageBox.DisplayMessageBox(UnsupportedDeviceTitle, UnsupportedDeviceBody, false, null);
        }
    }

    void OnVuforiaPaused(bool paused)
    {
        Debug.Log("OnVuforiaPaused(" + paused.ToString() + ") called.");
    }

    #endregion // VUFORIA_CALLBACKS


    #region DEVICE_TRACKER_CALLBACKS

    void OnTrackerStarted()
    {
        Debug.Log("PlaneManager.OnTrackerStarted() called.");

        this.positionalDeviceTracker = TrackerManager.Instance.GetTracker<PositionalDeviceTracker>();
        this.smartTerrain = TrackerManager.Instance.GetTracker<SmartTerrain>();

        if (this.positionalDeviceTracker != null && this.smartTerrain != null)
        {
            if (!this.positionalDeviceTracker.IsActive)
            {
                Debug.LogError("The Ground Plane feature requires the Device Tracker to be started. " +
                               "Please enable it in the Vuforia Configuration or start it at runtime through the scripting API.");
                return;
            }

            if (!this.smartTerrain.IsActive)
                this.smartTerrain.Start();

            Debug.Log("PositionalDeviceTracker is Active?: " + this.positionalDeviceTracker.IsActive +
                      "\nSmartTerrain Tracker is Active?: " + this.smartTerrain.IsActive);
        }
    }

    void OnDevicePoseStatusChanged(TrackableBehaviour.Status status, TrackableBehaviour.StatusInfo statusInfo)
    {
        Debug.Log("PlaneManager.OnDevicePoseStatusChanged(" + status + ", " + statusInfo + ")");

        StatusCached = status;
        StatusInfoCached = statusInfo;

        // If the timer is running and the status is no longer Relocalizing, then stop the timer
        if (statusInfo != TrackableBehaviour.StatusInfo.RELOCALIZING && this.timer.Enabled)
        {
            this.timer.Stop();
        }

        switch (statusInfo)
        {
            case TrackableBehaviour.StatusInfo.NORMAL:
                break;
            case TrackableBehaviour.StatusInfo.UNKNOWN:
                break;
            case TrackableBehaviour.StatusInfo.INITIALIZING:
                break;
            case TrackableBehaviour.StatusInfo.EXCESSIVE_MOTION:
                break;
            case TrackableBehaviour.StatusInfo.INSUFFICIENT_FEATURES:
                break;
            case TrackableBehaviour.StatusInfo.INSUFFICIENT_LIGHT:
                break;
            case TrackableBehaviour.StatusInfo.RELOCALIZING:
                // Start a 10 second timer to Reset Device Tracker
                if (!this.timer.Enabled)
                {
                    this.timer.Start();
                }
                break;
            default:
                break;
        }
    }

    #endregion // DEVICE_TRACKER_CALLBACK_METHODS
}
