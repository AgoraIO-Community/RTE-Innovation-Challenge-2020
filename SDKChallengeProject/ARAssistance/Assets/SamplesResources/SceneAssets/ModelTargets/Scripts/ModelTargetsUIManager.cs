/*==============================================================================
Copyright (c) 2019 PTC Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other
countries.
==============================================================================*/

using System.Collections;
using UnityEngine;
using UnityEngine.UI;

public class ModelTargetsUIManager : MonoBehaviour
{
    #region PRIVATE_MEMBERS

    // For private serialized fields, we assign references in the Inspector, so disable assignment warnings.
    // Disable: CS0649: Field '' is never assigned to, and will always have its default value false
#pragma warning disable 649
    [Header("Symbolic Icon Canvas Groups")]
    [SerializeField] private CanvasGroup canvasGroupAdvanced;
    [Tooltip("Cycle Multiple Targets")]
    [SerializeField] private bool cycleMultipleIcons;
    
    [Header("HoloLens")]
    [Tooltip("Spatial Mapping only for HoloLens")]
    [SerializeField] GameObject spatialMapping;
#pragma warning restore 649

    private readonly Color whiteTransparent = new Color(1f, 1f, 1f, 0f);
    private Image[] imageSequence;
    private Image[] imagesAdvanced;
    private bool uiEnabled;
    private bool imageSequencePaused;
    private int imageSequenceIndex;
    private float clock;
    private float fadeMeter;
    private const float ImageSwapFadeRangeMax = 0.001f;

    #endregion // PRIVATE_MEMBERS


    #region MONOBEHAVIOUR_METHODS

    // Start is called before the first frame update
    private void Start()
    {
        InitSymbolicTargetIcons();
    }

    // Update is called once per frame
    private void Update()
    {
        // Use the Symbolic Target Fade Cycle when running on Mobile, but not HoloLens
        if (this.cycleMultipleIcons)
        {
            UpdateSymbolicTargetIconFadeCycle();
        }
    }

    #endregion // MONOBEHAVIOUR_METHODS


    #region PUBLIC_METHODS

    public void SetUI(ModelTargetsManager.ModelTargetMode modelTargetMode, bool enable)
    {
        switch (modelTargetMode)
        {
            case ModelTargetsManager.ModelTargetMode.MODE_STANDARD:
                this.canvasGroupAdvanced.alpha = 0;
                break;
            case ModelTargetsManager.ModelTargetMode.MODE_ADVANCED:
                this.imageSequence = this.imagesAdvanced;
                this.canvasGroupAdvanced.alpha = enable ? 1 : 0;
                break;
        }

        // If the UI enabled status has changed, update spatial mapping (HoloLens) and sequence variables (Mobile)
        if (this.uiEnabled != enable)
        {
            if (this.spatialMapping)
            {
                this.spatialMapping.SetActive(enable);
            }
            else
            {
                if (this.cycleMultipleIcons)
                {
                    // For Mobile, we'll use image sequence (Advanced) and fade cycling (Advanced, 360)
                    ResetImageSequenceValues();
                }
            }

            this.uiEnabled = enable;
        }
    }

    #endregion // PUBLIC_METHODS


    #region PRIVATE_METHODS

    private void InitSymbolicTargetIcons()
    {
        if (this.canvasGroupAdvanced)
        {
            this.imagesAdvanced = this.canvasGroupAdvanced.GetComponentsInChildren<Image>();
        }

        if (this.cycleMultipleIcons)
        {
            // Set all the symbolic icons to be transparent at start.
            foreach (Image image in this.imagesAdvanced)
            {
                image.color = this.whiteTransparent;
            }
        }
    }

    private void UpdateSymbolicTargetIconFadeCycle()
    {
        if (this.uiEnabled)
        {
            this.fadeMeter = Mathf.InverseLerp(-1f, 1f, Mathf.Sin(this.clock += Time.deltaTime * 2));
            this.fadeMeter = Mathf.SmoothStep(0, 1, this.fadeMeter);

            if (this.imageSequence != null)
            {
                if (this.imageSequence.Length > 1)
                {
                    if (this.fadeMeter < ImageSwapFadeRangeMax && !this.imageSequencePaused)
                    {
                        this.imageSequence[this.imageSequenceIndex].color = Color.clear;
                        this.imageSequenceIndex = (this.imageSequenceIndex + 1) % this.imageSequence.Length;
                        this.imageSequence[this.imageSequenceIndex].color = Color.white;
                        this.imageSequencePaused = true;
                        StartCoroutine(ClearImageSequencePause());
                    }

                    this.imageSequence[this.imageSequenceIndex].color =
                        Color.Lerp(this.whiteTransparent, Color.white, this.fadeMeter);
                }
                else
                {
                    this.imageSequence[0].color =
                        Color.Lerp(this.whiteTransparent, Color.white, this.fadeMeter);
                }
            }
        }
    }

    private void ResetImageSequenceValues()
    {
        this.clock = 0f;
        this.imageSequenceIndex = 0;
        
        foreach (Image image in this.imageSequence)
        {
            image.color = this.whiteTransparent;
        }
    }

    private IEnumerator ClearImageSequencePause()
    {
        // Wait until the fade meter exits the valid image swapping range before clearing sequence flag.
        // This enforces a maximum of one symbolic icon change per fade cycle.
        yield return new WaitUntil(() => this.fadeMeter > ImageSwapFadeRangeMax);
        this.imageSequencePaused = false;
    }

    #endregion // PRIVATE_METHODS
}