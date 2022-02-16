/*============================================================================== 
Copyright (c) 2018 PTC Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other 
countries.   
==============================================================================*/

using UnityEngine;
using Vuforia;

public class ProductPlacement : MonoBehaviour
{
    #region PUBLIC_MEMBERS
    public bool IsPlaced { get; private set; }
    #endregion // PUBLIC_MEMBERS


    #region PRIVATE_MEMBERS
    [Header("Augmentation Objects")]
    [SerializeField] GameObject chair = null;
    [SerializeField] GameObject chairShadow = null;

    [Header("Control Indicators")]
    [SerializeField] GameObject translationIndicator = null;
    [SerializeField] GameObject rotationIndicator = null;

    [Header("Augmentation Size")]
    [Range(0.1f, 2.0f)]
    [SerializeField] float productSize = 0.65f;

    MeshRenderer chairRenderer;
    MeshRenderer chairShadowRenderer;
    Material[] chairMaterials, chairMaterialsTransparent;
    Material chairShadowMaterial, chairShadowMaterialTransparent;

    GroundPlaneUI groundPlaneUI;
    Camera mainCamera;
    Ray cameraToPlaneRay;
    RaycastHit cameraToPlaneHit;

    float augmentationScale;
    Vector3 productScale;
    string floorName;

    // Property which returns whether chair visibility conditions are met
    bool ChairVisibilityConditionsMet
    {
        // The Chair should only be visible if the following conditions are met:
        // 1. Tracking Status is Tracked or Limited
        // 2. Ground Plane Hit was received for this frame
        // 3. The Plane Mode is equal to PLACEMENT
        get
        {
            return
                PlaneManager.TrackingStatusIsTrackedOrLimited &&
                PlaneManager.GroundPlaneHitReceived &&
                (PlaneManager.CurrentPlaneMode == PlaneManager.PlaneMode.PLACEMENT);
        }
    }
    #endregion // PRIVATE_MEMBERS


    #region MONOBEHAVIOUR_METHODS
    void Start()
    {
        this.mainCamera = Camera.main;
        this.groundPlaneUI = FindObjectOfType<GroundPlaneUI>();
        this.chairRenderer = this.chair.GetComponent<MeshRenderer>();
        this.chairShadowRenderer = this.chairShadow.GetComponent<MeshRenderer>();

        SetupMaterials();
        SetupFloor();


        this.augmentationScale = VuforiaRuntimeUtilities.IsPlayMode() ? 0.1f : this.productSize;

        this.productScale =
            new Vector3(this.augmentationScale,
                        this.augmentationScale,
                        this.augmentationScale);

        this.chair.transform.localScale = this.productScale;
    }


    void Update()
    {
        if (PlaneManager.CurrentPlaneMode == PlaneManager.PlaneMode.PLACEMENT)
        {
            EnablePreviewModeTransparency(!this.IsPlaced);
            if (!this.IsPlaced)
                UtilityHelper.RotateTowardCamera(this.chair);
        }

        if (PlaneManager.CurrentPlaneMode == PlaneManager.PlaneMode.PLACEMENT && this.IsPlaced)
        {
            this.rotationIndicator.SetActive(Input.touchCount == 2);

            this.translationIndicator.SetActive(
                (TouchHandler.IsSingleFingerDragging || TouchHandler.IsSingleFingerStationary) && !this.groundPlaneUI.IsCanvasButtonPressed());

            if (TouchHandler.IsSingleFingerDragging || (VuforiaRuntimeUtilities.IsPlayMode() && Input.GetMouseButton(0)))
            {
                if (!this.groundPlaneUI.IsCanvasButtonPressed())
                {
                    this.cameraToPlaneRay = this.mainCamera.ScreenPointToRay(Input.mousePosition);

                    if (Physics.Raycast(this.cameraToPlaneRay, out this.cameraToPlaneHit))
                    {
                        if (this.cameraToPlaneHit.collider.gameObject.name == floorName)
                        {
                            this.chair.PositionAt(this.cameraToPlaneHit.point);
                        }
                    }
                }
            }
        }
        else
        {
            this.rotationIndicator.SetActive(false);
            this.translationIndicator.SetActive(false);
        }
    }

    void LateUpdate()
    {
        if (!this.IsPlaced)
        {
            SetVisible(this.ChairVisibilityConditionsMet);
        }
    }
    #endregion // MONOBEHAVIOUR_METHODS


    #region PUBLIC_METHODS
    public void Reset()
    {
        this.chair.transform.position = Vector3.zero;
        this.chair.transform.localEulerAngles = Vector3.zero;
        this.chair.transform.localScale = this.productScale;
    }

    public void PlaceProductAtAnchor(Transform anchor)
    {
        this.chair.transform.SetParent(anchor, true);
        this.chair.transform.localPosition = Vector3.zero;
        this.IsPlaced = true;
    }

    public void PlaceProductAtAnchorFacingCamera(Transform anchor)
    {
        PlaceProductAtAnchor(anchor);

        UtilityHelper.RotateTowardCamera(this.chair);
    }

    public void DetachProductFromAnchor()
    {
        this.chair.transform.SetParent(null);
        this.IsPlaced = false;
    }
    #endregion // PUBLIC_METHODS


    #region PRIVATE_METHODS
    void SetupMaterials()
    {
        this.chairMaterials = new Material[]
        {
            Resources.Load<Material>("ChairBody"),
            Resources.Load<Material>("ChairFrame")
        };

        this.chairMaterialsTransparent = new Material[]
        {
            Resources.Load<Material>("ChairBodyTransparent"),
            Resources.Load<Material>("ChairFrameTransparent")
        };

        this.chairShadowMaterial = Resources.Load<Material>("ChairShadow");
        this.chairShadowMaterialTransparent = Resources.Load<Material>("ChairShadowTransparent");
    }

    void SetupFloor()
    {
        if (VuforiaRuntimeUtilities.IsPlayMode())
        {
            this.floorName = "Emulator Ground Plane";
        }
        else
        {
            this.floorName = "Floor";
            GameObject floor = new GameObject(this.floorName, typeof(BoxCollider));
            floor.transform.SetParent(this.chair.transform.parent);
            floor.transform.SetPositionAndRotation(Vector3.zero, Quaternion.identity);
            floor.transform.localScale = Vector3.one;
            floor.GetComponent<BoxCollider>().size = new Vector3(100f, 0, 100f);
        }
    }

    /// <summary>
    /// This method is used prior to chair being placed. Once placed, chair visibility is controlled
    /// by the DefaultTrackableEventHandler.
    /// </summary>
    /// <param name="visible">bool</param>
    void SetVisible(bool visible)
    {
        // Set the visibility of the chair and it's shadow
        this.chairRenderer.enabled = this.chairShadowRenderer.enabled = visible;
    }

    void EnablePreviewModeTransparency(bool previewEnabled)
    {
        this.chairRenderer.materials = previewEnabled ? this.chairMaterialsTransparent : this.chairMaterials;
        this.chairShadowRenderer.material = previewEnabled ? this.chairShadowMaterialTransparent : this.chairShadowMaterial;
    }
    #endregion // PRIVATE_METHODS

}
