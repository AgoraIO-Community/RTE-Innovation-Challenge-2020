/*===============================================================================
Copyright (c) 2019 PTC Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other 
countries.
===============================================================================*/
using UnityEngine;
using Vuforia;
using UnityEngine.UI;

[RequireComponent(typeof(Light))]
public class AmbientLightManager : MonoBehaviour
{
    #region PRIVATE_MEMBERS

    IlluminationManager illuminationManager;
    Light sceneLight;
    float maxIntensity;

    #endregion // PRIVATE_MEMBERS


    #region MONOBEHAVIOUR_METHODS
    void Start()
    {
        VuforiaARController.Instance.RegisterVuforiaStartedCallback(OnVuforiaStarted);

        this.sceneLight = GetComponent<Light>();
        this.maxIntensity = this.sceneLight.intensity;
    }

    void Update()
    {
        if (this.illuminationManager != null && this.illuminationManager.AmbientIntensity != null)
        {
            float intensity = (float)this.illuminationManager.AmbientIntensity / 1000;

            // Set light intensity to range between 0 and it's max intensity value
            this.sceneLight.intensity = Mathf.Clamp(intensity, 0, this.maxIntensity);

            // Set to scene's ambient light intensity and clamp between 0 and 1
            RenderSettings.ambientIntensity = Mathf.Clamp01(intensity);
        }
        else
        {
            this.sceneLight.intensity = this.maxIntensity;
        }
    }

    #endregion // MONOBEHAVIOUR_METHODS


    #region VUFORIA_CALLBACK_METHODS

    void OnVuforiaStarted()
    {
        this.illuminationManager = TrackerManager.Instance.GetStateManager().GetIlluminationManager();
    }

    #endregion // VUFORIA_CALLBACK_METHODS
}
