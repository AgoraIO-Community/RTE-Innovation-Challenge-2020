/*===============================================================================
Copyright (c) 2019 PTC Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other
countries.
===============================================================================*/

using System;
using UnityEngine;
using UnityEngine.UI;

public class SafeAreaManager : MonoBehaviour
{
    #region PRIVATE_MEMBERS

    [System.Serializable]
    class SafeAreaRect
    {
        public RectTransform rectTransform = null;
        [Header("Apply Safe Area Constraints")]
        public bool top = false;
        public bool bottom = false;
    }

    [Header("Global Unsafe Area Settings (Per-Scene)")]
    [Tooltip("Unsafe Area Colors can be changed programmatically at runtime.")]
    [SerializeField] RectTransform topArea = null;
    [SerializeField] RectTransform bottomArea = null;
    [SerializeField] Color topAreaColor;
    [SerializeField] Color bottomAreaColor;
    [Tooltip("Safe Area Margin reduces the Safe Area by the specified amount at the Top/Bottom boundaries. " +
             "It is useful for testing Safe Area Behaviour in PlayMode.")]
    [Range(0,100)] // Max range value is arbitrary for example purposes
    [SerializeField] private int SafeAreaMargin = 0;

    [Header("Apply Safe Area Constraints to RectTransforms")]
    [SerializeField] SafeAreaRect[] safeAreaRects = null;
    
    ScreenOrientation lastOrientation;
    Rect lastSafeArea = new Rect(0, 0, 0, 0);
    Rect safeArea;
    Image topAreaImage = null;
    Image bottomAreaImage = null;
    bool colorsChanged => (topAreaColor != topAreaImage.color) || (bottomAreaColor != bottomAreaImage.color);

    #endregion // PRIVATE_MEMBERS


    #region MONOBEHAVIOUR_METHODS

    void Awake()
    {
        if (!topArea || !bottomArea)
        {
            Debug.LogWarning("Either topArea or bottomArea is null. Programmatically getting the required references.");
            SetAreaRectTransforms();
        }
        
        // cache our unsafe area image components
        this.topAreaImage = this.topArea.GetComponent<Image>();
        this.bottomAreaImage = this.bottomArea.GetComponent<Image>();

        // Set the unsafe area colors using Inspector values
        SetAreaColors(this.topAreaColor, this.bottomAreaColor);
        
        this.safeArea = GetSafeArea();
    }

    void SetAreaRectTransforms()
    {
        var images = GetComponentsInChildren<Image>();
        if (images.Length != 2)
        {
            Debug.LogError($"SafeAreaManager must have exactly two children with Image components attached.");
            return;
        }

        topArea = images[0].rectTransform;
        bottomArea = images[1].rectTransform;
    }
    
    Rect GetSafeArea()
    {
        return new Rect(
            Screen.safeArea.x, 
            Screen.safeArea.y + this.SafeAreaMargin,
            Screen.safeArea.width, 
            Screen.safeArea.height - (this.SafeAreaMargin * 2));
    }

    void Start()
    {
        this.lastOrientation = Screen.orientation;

        Refresh();
    }

    void Update()
    {
        Refresh();
    }

    #endregion // MONOBEHAVIOUR_METHODS


    #region PRIVATE_METHODS

    void Refresh()
    {
        this.safeArea = GetSafeArea();

        if ((this.safeArea != this.lastSafeArea) || (Screen.orientation != this.lastOrientation))
        {
            ApplySafeArea();
            UpdateUnsafeArea();
        }

        if (this.colorsChanged)
        {
            SetAreaColors(this.topAreaColor, this.bottomAreaColor);
        }
    }

    void ApplySafeArea()
    {
        this.lastSafeArea = this.safeArea;
        this.lastOrientation = Screen.orientation;

        foreach (SafeAreaRect areaRect in this.safeAreaRects)
        {
            var anchorMin = this.safeArea.position;
            var anchorMax = this.safeArea.position + this.safeArea.size;

            anchorMin.x /= Screen.width;
            anchorMin.y = areaRect.bottom ? anchorMin.y / Screen.height : 0;
            anchorMax.x /= Screen.width;
            anchorMax.y = areaRect.top ? anchorMax.y / Screen.height : 1;
            
            if (Screen.orientation == ScreenOrientation.LandscapeLeft ||
                Screen.orientation == ScreenOrientation.LandscapeRight)
            {
                anchorMin.x = 0;
                anchorMax.x = 1;
            }
            
            areaRect.rectTransform.anchorMin = anchorMin;
            areaRect.rectTransform.anchorMax = anchorMax;
        }
    }
    
    void UpdateUnsafeArea()
    {
        var anchorMin = this.safeArea.position;
        var anchorMax = this.safeArea.position + this.safeArea.size;

        anchorMin.x /= Screen.width;
        anchorMin.y = anchorMin.y / Screen.height;
        anchorMax.x /= Screen.width;
        anchorMax.y = anchorMax.y / Screen.height;

        SetUnsafeAreaSizes(anchorMin.y, anchorMax.y);
        
        SetAreaColors(this.topAreaColor, this.bottomAreaColor);
    }

    void SetUnsafeAreaSizes(float safeAreaAnchorMinY, float safeAreaAnchorMaxY)
    {
        this.topArea.anchorMin = new Vector2(0, safeAreaAnchorMaxY);
        this.topArea.anchorMax = Vector2.one;

        this.bottomArea.anchorMin = Vector2.zero;
        this.bottomArea.anchorMax = new Vector2(1, safeAreaAnchorMinY);
    }

    #endregion // PRIVATE_METHODS


    #region PUBLIC_METHODS

    public void AddSafeAreaRect(RectTransform rect, bool applyTopConstraint, bool applyBottomConstraint)
    {
        Array.Resize(ref this.safeAreaRects, this.safeAreaRects.Length + 1);
        this.safeAreaRects[this.safeAreaRects.Length - 1] = new SafeAreaRect
        {
            rectTransform = rect,
            top = applyTopConstraint,
            bottom = applyBottomConstraint
        };

        ApplySafeArea();
    }

    public void SetAreasEnabled(bool topAreaEnabled, bool bottomAreaEnabled)
    {
        this.topAreaImage.enabled = topAreaEnabled;
        this.bottomAreaImage.enabled = bottomAreaEnabled;
    }

    /// <summary>
    /// Sets the area colors programmatically and updates Inspector colors.
    /// </summary>
    /// <param name="topColor">Top color.</param>
    /// <param name="bottomColor">Bottom color.</param>
    public void SetAreaColors(Color topColor, Color bottomColor)
    {
        // update Inspector-level colors to match programmatic ones
        this.topAreaColor = topColor;
        this.bottomAreaColor = bottomColor;

        // assign the colors
        this.topAreaImage.color = this.topAreaColor;
        this.bottomAreaImage.color = this.bottomAreaColor;
    }

    #endregion // PUBLIC_METHODS
}
