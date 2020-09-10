/*==============================================================================
Copyright (c) 2019 PTC Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other
countries.
==============================================================================*/
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.EventSystems;
using Vuforia;

public class GroundPlaneUI : MonoBehaviour
{
    #region PRIVATE_MEMBERS
    [Header("UI Elements")]
    [SerializeField] Text title = null;
    [SerializeField] Text instructions = null;
    [SerializeField] CanvasGroup screenReticle = null;

    [Header("UI Buttons")]
    [SerializeField] Toggle placementToggle = null;
    [SerializeField] Toggle groundToggle = null;
    [SerializeField] Toggle midAirToggle = null;

    bool resetDefaultToggle = true;
    const string TitleProductPlacement = "Product Placement";
    const string TitleGroundPlane = "Ground Plane";
    const string TitleMidAir = "Mid-Air";

    GraphicRaycaster graphicRayCaster;
    PointerEventData pointerEventData;
    EventSystem eventSystem;

    ProductPlacement productPlacement;
    TouchHandler touchHandler;
    #endregion // PRIVATE_MEMBERS


    #region MONOBEHAVIOUR_METHODS
    void Start()
    {
        this.title.text = TitleProductPlacement;

        this.productPlacement = FindObjectOfType<ProductPlacement>();
        this.touchHandler = FindObjectOfType<TouchHandler>();
        this.graphicRayCaster = FindObjectOfType<GraphicRaycaster>();
        this.eventSystem = FindObjectOfType<EventSystem>();

        DeviceTrackerARController.Instance.RegisterDevicePoseStatusChangedCallback(OnDevicePoseStatusChanged);
    }

    void Update()
    {
        this.midAirToggle.interactable =
            this.groundToggle.interactable =
                this.placementToggle.interactable =
                    PlaneManager.TrackingStatusIsTrackedAndNormal;

        if (this.resetDefaultToggle)
        {
            if (this.placementToggle.interactable)
            {
                this.placementToggle.isOn = true;
                this.resetDefaultToggle = false;
            }
        }
    }

    void LateUpdate()
    {
        if (PlaneManager.GroundPlaneHitReceived && PlaneManager.TrackingStatusIsTrackedAndNormal)
        {
            // We got an automatic hit test this frame

            // Hide the onscreen reticle when we get a hit test
            this.screenReticle.alpha = 0;

            this.instructions.transform.parent.gameObject.SetActive(true);
            this.instructions.enabled = true;

            if (PlaneManager.CurrentPlaneMode == PlaneManager.PlaneMode.GROUND)
            {
                this.instructions.text = "Tap to place Astronaut";
            }
            else if (PlaneManager.CurrentPlaneMode == PlaneManager.PlaneMode.PLACEMENT)
            {
                this.instructions.text = (this.productPlacement.IsPlaced) ?
                    "• Touch and drag to move Chair" +
                    "\n• Two fingers to rotate" +
                    ((this.touchHandler.enablePinchScaling) ? " or pinch to scale" : "")
                    :
                    "Tap to place Chair";
            }
        }
        else
        {
            // No automatic hit test, so set alpha based on which plane mode is active
            if (!PlaneManager.GroundPlaneHitReceived)
            {
                this.screenReticle.alpha =
                    (PlaneManager.CurrentPlaneMode == PlaneManager.PlaneMode.GROUND ||
                    PlaneManager.CurrentPlaneMode == PlaneManager.PlaneMode.PLACEMENT) ? 1 : 0;
            }

            this.instructions.transform.parent.gameObject.SetActive(true);
            this.instructions.enabled = true;

            if (PlaneManager.CurrentPlaneMode == PlaneManager.PlaneMode.GROUND ||
                PlaneManager.CurrentPlaneMode == PlaneManager.PlaneMode.PLACEMENT)
            {
                this.instructions.text = PlaneManager.GroundPlaneHitReceived ?
                    "Move to get better tracking for placing an anchor" :
                    "Point device towards ground";
            }
            else if (PlaneManager.CurrentPlaneMode == PlaneManager.PlaneMode.MIDAIR)
            {
                this.instructions.text = PlaneManager.TrackingStatusIsTrackedAndNormal ?
                    "Tap to place Drone" :
                    "Need better tracking to place a Mid-Air Anchor";
            }
        }
    }

    void OnDestroy()
    {
        Debug.Log("OnDestroy() called.");

        DeviceTrackerARController.Instance.UnregisterDevicePoseStatusChangedCallback(OnDevicePoseStatusChanged);
    }
    #endregion // MONOBEHAVIOUR_METHODS


    #region PUBLIC_METHODS
    /// <summary>
    /// Resets the UI Buttons and the Initialized property.
    /// It is called by PlaneManager.ResetScene().
    /// </summary>
    public void Reset()
    {
        this.placementToggle.isOn = true;
        this.resetDefaultToggle = true;
    }

    public void UpdateTitle()
    {
        switch (PlaneManager.CurrentPlaneMode)
        {
            case PlaneManager.PlaneMode.GROUND:
                this.title.text = TitleGroundPlane;
                break;
            case PlaneManager.PlaneMode.MIDAIR:
                this.title.text = TitleMidAir;
                break;
            case PlaneManager.PlaneMode.PLACEMENT:
                this.title.text = TitleProductPlacement;
                break;
        }
    }

    public bool IsCanvasButtonPressed()
    {
        pointerEventData = new PointerEventData(this.eventSystem)
        {
            position = Input.mousePosition
        };
        List<RaycastResult> results = new List<RaycastResult>();
        this.graphicRayCaster.Raycast(pointerEventData, results);

        bool resultIsButton = false;
        foreach (RaycastResult result in results)
        {
            if (result.gameObject.GetComponentInParent<Toggle>() ||
                result.gameObject.GetComponent<Button>())
            {
                resultIsButton = true;
                break;
            }
        }
        return resultIsButton;
    }
    #endregion // PUBLIC_METHODS

    #region VUFORIA_CALLBACKS

    void OnDevicePoseStatusChanged(TrackableBehaviour.Status status, TrackableBehaviour.StatusInfo statusInfo)
    {
        Debug.Log("GroundPlaneUI.OnDevicePoseStatusChanged(" + status + ", " + statusInfo + ")");

        string statusMessage = "";

        switch (statusInfo)
        {
            case TrackableBehaviour.StatusInfo.NORMAL:
                statusMessage = "";
                break;
            case TrackableBehaviour.StatusInfo.UNKNOWN:
                statusMessage = "Limited Status";
                break;
            case TrackableBehaviour.StatusInfo.INITIALIZING:
                statusMessage = "Point your device to the floor and move to scan";
                break;
            case TrackableBehaviour.StatusInfo.EXCESSIVE_MOTION:
                statusMessage = "Move slower";
                break;
            case TrackableBehaviour.StatusInfo.INSUFFICIENT_FEATURES:
                statusMessage = "Not enough visual features in the scene";
                break;
            case TrackableBehaviour.StatusInfo.INSUFFICIENT_LIGHT:
                statusMessage = "Not enough light in the scene";
                break;
            case TrackableBehaviour.StatusInfo.RELOCALIZING:
                // Display a relocalization message in the UI if:
                // * No AnchorBehaviours are being tracked
                // * None of the active/tracked AnchorBehaviours are in TRACKED status

                // Set the status message now and clear it none of conditions are met.
                statusMessage = "Point back to previously seen area and rescan to relocalize.";

                StateManager stateManager = TrackerManager.Instance.GetStateManager();
                if (stateManager != null)
                {
                    // Cycle through all of the active AnchorBehaviours first.
                    foreach (TrackableBehaviour behaviour in stateManager.GetActiveTrackableBehaviours())
                    {
                        if (behaviour is AnchorBehaviour)
                        {
                            if (behaviour.CurrentStatus == TrackableBehaviour.Status.TRACKED)
                            {
                                // If at least one of the AnchorBehaviours has Tracked status,
                                // then don't display the relocalization message.
                                statusMessage = "";
                            }
                        }
                    }
                }
                break;
            default:
                statusMessage = "";
                break;
        }

        StatusMessage.Instance.Display(statusMessage);
        // Uncomment the following line to show Status and StatusInfo values
        //StatusMessage.Instance.Display(status.ToString() + " -- " + statusInfo.ToString());
    }

    #endregion // VUFORIA_CALLBACKS
}
