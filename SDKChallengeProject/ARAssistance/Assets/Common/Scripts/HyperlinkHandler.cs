/*===============================================================================
Copyright (c) 2018 PTC Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other 
countries.
===============================================================================*/
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.EventSystems;
using TMPro;

public class HyperlinkHandler : MonoBehaviour, IPointerClickHandler, IPointerUpHandler
{
    TextMeshProUGUI textMeshPro;
    Camera cam;

    #region MONOBEHAVIOUR_METHODS

    void Start()
    {
        this.textMeshPro = GetComponentInChildren<TextMeshProUGUI>();

        // Get a reference to the camera if Canvas Render Mode is not ScreenSpace Overlay
        Canvas canvas = GetComponentInParent<Canvas>();
        this.cam = (canvas.renderMode == RenderMode.ScreenSpaceOverlay) ?
            null : canvas.worldCamera;
    }

    #endregion // MONOBEHAVIOUR_METHODS


    #region EVENT_CALLBACKS

    public void OnPointerClick(PointerEventData eventData)
    {
    }

    public void OnPointerUp(PointerEventData eventData)
    {
        CheckIfLinkAndOpenURL();
    }

    #endregion // EVENT_CALLBACKS


    #region PRIVATE_METHODS

    void CheckIfLinkAndOpenURL()
    {
        int linkIndex = TMP_TextUtilities.FindIntersectingLink(this.textMeshPro, Input.mousePosition, this.cam);

        if (linkIndex != -1)
        {
            TMP_LinkInfo linkInfo = this.textMeshPro.textInfo.linkInfo[linkIndex];
            Application.OpenURL(linkInfo.GetLinkID());
        }
    }

    #endregion // PRIVATE_METHODS
}
