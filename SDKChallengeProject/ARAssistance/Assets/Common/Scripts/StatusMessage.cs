/*===============================================================================
Copyright (c) 2019 PTC Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other
countries.
===============================================================================*/

using System;
using System.Collections;
using UnityEngine;
using UnityEngine.UI;

public class StatusMessage : MonoBehaviour
{
    #region PRIVATE_MEMBERS

    CanvasGroup canvasGroup;
    Text message;
    bool initialized;
    static StatusMessage statusMessage;
    Coroutine coroutine;
    #endregion // PRIVATE_MEMBERS


    #region PUBLIC_PROPERTIES

    public static StatusMessage Instance
    {
        get
        {
            if (statusMessage == null)
            {
                GameObject prefab = (GameObject)Resources.Load("StatusMessage");
                if (prefab)
                {
                    statusMessage = Instantiate(prefab.GetComponent<StatusMessage>());
                    statusMessage.Init();
                    return statusMessage;
                }
                // If prefab not found, return null
                return null;
            }
            return statusMessage;
        }
    }

    #endregion // PUBLIC_PROPERTIES


    #region PRIVATE_METHODS

    void Init()
    {
        if (!this.initialized)
        {
            this.canvasGroup = GetComponentInChildren<CanvasGroup>();
            this.canvasGroup.alpha = 0;
            this.message = GetComponentInChildren<Text>();
            this.message.text = "";
            this.initialized = true;
            RectTransform root = transform.GetChild(0).GetComponent<RectTransform>();
            SafeAreaManager sam = FindObjectOfType<SafeAreaManager>();
            if (sam)
            {
                sam.AddSafeAreaRect(root, true, false);
            }
        }
    }

    #endregion // PRIVATE_METHODS


    #region PUBLIC_METHODS

    public void Display(string message, bool fadeOut = false)
    {
        // Check to see if there's an existing message first (i.e. Length > 0).
        // Return if the new message is a fadeOut message, otherwise overwrite.
        // Rule: Don't overwrite a non-fadeOut message with a fadeOut one.
        if (this.message.text.Length > 0 && fadeOut)
        {
            return;
        }
        
        if (this.coroutine != null)
        {
            StopCoroutine(this.coroutine);
            this.coroutine = null;
        }
        
        this.message.text = message;
        
        if (fadeOut)
        {
            this.canvasGroup.alpha = 1;    
            this.coroutine = StartCoroutine(FadeOut());
        }
        else
        {
            this.canvasGroup.alpha = (message.Length > 0) ? 1 : 0;
        }
    }

    IEnumerator FadeOut()
    {
        yield return new WaitForSeconds(1f);
        
        for (float f = 1f; f >= 0; f -= 0.1f)
        {
            f = (float)Math.Round(f, 1);
            this.canvasGroup.alpha = (float)Math.Round(f, 1);
            yield return null;
        }
        // clear message
        this.message.text = "";
    }

    #endregion PUBLIC_METHODS

}
