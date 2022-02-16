/*===============================================================================
Copyright (c) 2016-2019 PTC Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other 
countries.
===============================================================================*/
using UnityEngine;
using UnityEngine.UI;
using Vuforia;
using TMPro;

public class SamplesMainMenu : MonoBehaviour
{
    #region PRIVATE_MEMBERS

    bool isAboutScreenVisible
    {
        get { return this.aboutCanvas.sortingOrder > this.menuCanvas.sortingOrder; }
    }

    [SerializeField] Canvas menuCanvas = null;
    [SerializeField] Canvas aboutCanvas = null;
    [SerializeField] Text aboutTitle = null;
    [SerializeField] TextMeshProUGUI aboutDescription = null;

    AboutScreenInfo aboutScreenInfo;
    SafeAreaManager safeAreaManager;
    readonly Color lightGrey = new Color(220f / 255f, 220f / 255f, 220f / 255f);

    #endregion // PRIVATE_MEMBERS


    #region MONOBEHAVIOUR_METHODS

    void Start()
    {
        if (this.aboutScreenInfo == null)
        {
            // initialize if null
            this.aboutScreenInfo = new AboutScreenInfo();
        }

        this.safeAreaManager = FindObjectOfType<SafeAreaManager>();

        if (this.safeAreaManager)
        {
            this.safeAreaManager.SetAreaColors(lightGrey, Color.white);
            this.safeAreaManager.SetAreasEnabled(true, true);
        }
    }

    void Update()
    {
        if (Input.GetKeyUp(KeyCode.Escape))
        {
            if (this.isAboutScreenVisible)
            {
                OnBackButton();
            }
            else
            {
                QuitApp();
            }
        }
        else if (Input.GetKeyUp(KeyCode.Return) || Input.GetKeyUp(KeyCode.JoystickButton0))
        {

            if (this.isAboutScreenVisible)
            {
                // In Unity 'Return' key same as clicking next button on About Screen
                // On ODG R7, JoystickButton0 is the Trackpad select button
                OnStartAR();
            }
        }
    }

    #endregion // MONOBEHAVIOUR_METHODS


    #region BUTTON_METHODS

    public void OnStartAR()
    {
        UnityEngine.SceneManagement.SceneManager.LoadScene("2-Loading");
    }

    public void OnBackButton()
    {
        ShowAboutScreen(false);
    }

    public void OnMenuItemSelected(string selectedMenuItem)
    {
        if (selectedMenuItem != string.Empty)
        {
            // Set the scene to be loaded.
            LoadingScreen.SceneToLoad = "3-" + selectedMenuItem;

            // Populate the about screen info.
            this.aboutTitle.text = this.aboutScreenInfo.GetTitle(selectedMenuItem);
            this.aboutDescription.text = this.aboutScreenInfo.GetDescription(selectedMenuItem);

            // Display the about screen.
            ShowAboutScreen(true);
        }
    }

    #endregion // BUTTON_METHODS


    #region PRIVATE_METHODS

    void ShowAboutScreen(bool showAboutScreen)
    {
        if (showAboutScreen)
        {
            // Place About canvas in front of Menu canvas
            this.aboutCanvas.sortingOrder = this.menuCanvas.sortingOrder + 1;

            if (this.safeAreaManager)
            {
                this.safeAreaManager.SetAreaColors(this.lightGrey, Color.clear);
                this.safeAreaManager.SetAreasEnabled(true, false);
            }
        }
        else
        {
            // Place About canvas behind Menu canvas
            this.aboutCanvas.sortingOrder = this.menuCanvas.sortingOrder - 1;

            if (this.safeAreaManager)
            {
                this.safeAreaManager.SetAreaColors(lightGrey, Color.white);
                this.safeAreaManager.SetAreasEnabled(true, true);
            }
        }
    }

    void QuitApp()
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

    #endregion // PRIVATE_METHODS
}
