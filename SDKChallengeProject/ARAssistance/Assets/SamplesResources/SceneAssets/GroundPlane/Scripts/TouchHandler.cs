/*============================================================================== 
Copyright (c) 2018 PTC Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other 
countries.   
==============================================================================*/

using UnityEngine;

public class TouchHandler : MonoBehaviour
{
    #region PUBLIC_MEMBERS

    public Transform augmentationObject;

    [HideInInspector]
    public bool enableRotation;
    public bool enablePinchScaling;

    public static bool IsSingleFingerStationary => IsSingleFingerDown() && (Input.GetTouch(0).phase == TouchPhase.Stationary);

    public static bool IsSingleFingerDragging => IsSingleFingerDown() && (Input.GetTouch(0).phase == TouchPhase.Moved);

    #endregion // PUBLIC MEMBERS


    #region PRIVATE_MEMBERS
    const float ScaleRangeMin = 0.1f;
    const float ScaleRangeMax = 2.0f;

    Touch[] touches;
    static int lastTouchCount;
    bool isFirstFrameWithTwoTouches;
    float cachedTouchAngle;
    float cachedTouchDistance;
    float cachedAugmentationScale;
    Vector3 cachedAugmentationRotation;
    #endregion // PRIVATE_MEMBERS


    #region MONOBEHAVIOUR_METHODS

    void Start()
    {
        this.cachedAugmentationScale = this.augmentationObject.localScale.x;
        this.cachedAugmentationRotation = this.augmentationObject.localEulerAngles;
    }

    void Update()
    {
        if (Input.touchCount == 2)
        {
            var firstTouch = Input.GetTouch(0);
            var secondTouch = Input.GetTouch(1);
            
            float currentTouchDistance = Vector2.Distance(firstTouch.position, secondTouch.position);
            float diff_y = firstTouch.position.y - secondTouch.position.y;
            float diff_x = firstTouch.position.x - secondTouch.position.x;
            float currentTouchAngle = Mathf.Atan2(diff_y, diff_x) * Mathf.Rad2Deg;

            if (this.isFirstFrameWithTwoTouches)
            {
                this.cachedTouchDistance = currentTouchDistance;
                this.cachedTouchAngle = currentTouchAngle;
                this.isFirstFrameWithTwoTouches = false;
            }

            float angleDelta = currentTouchAngle - this.cachedTouchAngle;
            float scaleMultiplier = (currentTouchDistance / this.cachedTouchDistance);
            float scaleAmount = this.cachedAugmentationScale * scaleMultiplier;
            float scaleAmountClamped = Mathf.Clamp(scaleAmount, ScaleRangeMin, ScaleRangeMax);

            if (this.enableRotation)
            {
                this.augmentationObject.localEulerAngles = this.cachedAugmentationRotation - new Vector3(0, angleDelta * 3f, 0);
            }
            if (this.enableRotation && this.enablePinchScaling)
            {
                // Optional Pinch Scaling can be enabled via Inspector for this Script Component
                this.augmentationObject.localScale = new Vector3(scaleAmountClamped, scaleAmountClamped, scaleAmountClamped);
            }

        }
        else if (Input.touchCount < 2)
        {
            this.cachedAugmentationScale = this.augmentationObject.localScale.x;
            this.cachedAugmentationRotation = this.augmentationObject.localEulerAngles;
            this.isFirstFrameWithTwoTouches = true;
        }
        else if (Input.touchCount == 6)
        {
            // enable runtime testing of pinch scaling
            this.enablePinchScaling = true;
        }
        else if (Input.touchCount == 5)
        {
            // disable runtime testing of pinch scaling
            this.enablePinchScaling = false;
        }
    }

    #endregion // MONOBEHAVIOUR_METHODS


    #region PRIVATE_METHODS

    static bool IsSingleFingerDown()
    {
        if (Input.touchCount == 0 || Input.touchCount >= 2)
            lastTouchCount = Input.touchCount;

        return (
            Input.touchCount == 1 &&
            Input.GetTouch(0).fingerId == 0 &&
            lastTouchCount == 0);
    }

    #endregion // PRIVATE_METHODS

}