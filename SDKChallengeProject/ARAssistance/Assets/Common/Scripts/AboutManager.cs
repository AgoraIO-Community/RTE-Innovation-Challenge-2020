/*============================================================================== 
Copyright (c) 2017-2018 PTC Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other 
countries.   
==============================================================================*/

using UnityEngine;
using UnityEngine.UI;
using TMPro;

public class AboutManager : MonoBehaviour
{
    public enum AboutScreenSample
    {
        ImageTargets,
        VuMark,
        CylinderTargets,
        MultiTargets,
        UserDefinedTargets,
        ObjectReco,
        CloudReco,
        VirtualButtons,
        ModelTargets,
        GroundPlane,
        BackgroundTextureAccess,
        OcclusionManagement,
        Books,
        ModelTargetsTest
    }

    #region PUBLIC_MEMBERS
    public AboutScreenSample m_AboutScreenSample;
    #endregion //PUBLIC_MEMBERS


    #region PRIVATE_METHODS
    public void LoadNextScene()
    {
        UnityEngine.SceneManagement.SceneManager.LoadScene(UnityEngine.SceneManagement.SceneManager.GetActiveScene().buildIndex + 1);
    }
    #endregion //PRIVATE_METHODS


    #region MONOBEHAVIOUR_METHODS

    void Start()
    {
        UpdateAboutText();
    }

    void Update()
    {
        if (Input.GetKeyUp(KeyCode.Return) || Input.GetKeyUp(KeyCode.JoystickButton0))
        {
            LoadNextScene();
        }

        if (Input.GetKeyUp(KeyCode.Escape))
        {
            if (Application.isEditor)
            {
#if UNITY_EDITOR
                UnityEditor.EditorApplication.isPlaying = false;
#endif
            }
            else
            {
                Application.Quit();
            }
        }
    }
    #endregion // MONOBEHAVIOUR_METHODS

    void UpdateAboutText()
    {
        AboutScreenInfo m_AboutScreenInfo = new AboutScreenInfo();

        string title = m_AboutScreenInfo.GetTitle(m_AboutScreenSample.ToString());
        string description = m_AboutScreenInfo.GetDescription(m_AboutScreenSample.ToString());

        Text[] textElements = GetComponentsInChildren<Text>();
        textElements[0].text = title;
        TextMeshProUGUI textMeshProUGUI = GetComponentInChildren<TextMeshProUGUI>();
        textMeshProUGUI.text = description;
    }

}