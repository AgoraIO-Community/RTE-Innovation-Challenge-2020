/*===============================================================================
Copyright (c) 2018 PTC Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other 
countries.
===============================================================================*/

using UnityEngine;

public class SceneOrientation : MonoBehaviour {

    #region PUBLIC_MEMBERS

    public enum Orientation {
        AUTOROTATION,
        PORTRAIT,
        LANDSCAPE
    }

    public Orientation sceneOrientation;

    #endregion // PUBLIC_MEMBERS


    #region MONOBEHAVIOUR_METHODS

    void Awake()
    {
        SetSceneOrientation();
    }

    #endregion // MONOBEHAVIOUR_METHODS


    #region PRIVATE_METHODS

    private void SetSceneOrientation()
    {
        switch (sceneOrientation)
        {
            case Orientation.AUTOROTATION:
                Screen.orientation = ScreenOrientation.AutoRotation;
                break;
            case Orientation.PORTRAIT:
                Screen.orientation = ScreenOrientation.Portrait;
                break;
            case Orientation.LANDSCAPE:
                Screen.orientation = ScreenOrientation.LandscapeLeft;
                break;
        }
    }

    #endregion // PRIVATE_METHODS

}
