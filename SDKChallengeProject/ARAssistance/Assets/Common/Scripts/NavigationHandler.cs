/*===============================================================================
Copyright (c) 2018 PTC Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other 
countries.
===============================================================================*/

using UnityEngine;
using UnityEngine.SceneManagement;

public class NavigationHandler : MonoBehaviour
{
    #region PUBLIC_MEMBERS
    public string m_BackButtonNavigation = "[Name of Scene To Load]";
    #endregion // PUBLIC_MEMBERS


    #region MONOBEHAVIOUR_METHODS
    void Update()
    {
        // On Android, the Back button is mapped to the Esc key
        if (Input.GetKeyUp(KeyCode.Escape))
        {
            HandleBackButtonPressed();
        }
    }
    #endregion // MONOBEHAVIOUR_METHODS


    #region PUBLIC_METHODS
    public void HandleBackButtonPressed()
    {
        Debug.Log("HandleBackButtonPressed() called.");
        if (SceneManager.GetActiveScene().name != m_BackButtonNavigation)
            LoadScene(m_BackButtonNavigation);
    }
    #endregion // PUBLIC_METHODS


    #region PRIVATE_METHODS
    void LoadScene(string sceneName)
    {
        Debug.Log("LoadScene(" + sceneName + ") called.");
        if (!string.IsNullOrEmpty(sceneName))
        {
            SceneManager.LoadScene(sceneName);
        }
    }
    #endregion // PRIVATE_METHODS
}
