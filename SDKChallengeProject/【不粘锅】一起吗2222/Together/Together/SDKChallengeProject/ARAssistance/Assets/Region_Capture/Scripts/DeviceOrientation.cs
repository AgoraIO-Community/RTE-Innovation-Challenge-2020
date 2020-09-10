using UnityEngine;

#if UNITY_EDITOR
#pragma warning disable 0414
#endif

public class DeviceOrientation : MonoBehaviour {

	[Space(15)]
	public bool FrontCamera;
	[Space(15)]
	private bool FlipX, FlipX_check;
	private bool FlipY, FlipY_check;
    private bool Rotate90, Rotate90_check;
    private bool Init;
    private ScreenOrientation Orient;

    private Material CustomBackgroundMaterial;

    void Start()
	{
		FlipX = GetComponent<RegionCapture>().FlipX;
		FlipY = GetComponent<RegionCapture>().FlipY;
        Rotate90 = GetComponent<RegionCapture>().Rotate90;

        Orient = Screen.orientation;
    }

	void Update()
	{
		FlipX_check = FlipX;
		FlipY_check = FlipY;
        Rotate90_check = Rotate90;

        #if !UNITY_EDITOR && !UNITY_STANDALONE

		if (!FrontCamera)           // Back-facing Camera
		{
            if (Orient != Screen.orientation || !Init)
            {
                if (Screen.orientation == ScreenOrientation.LandscapeRight)
                {
                    FlipX = true;
                    FlipY = false;
                    Rotate90 = false;
                }

                if (Screen.orientation == ScreenOrientation.LandscapeLeft)
                {
                    FlipX = false;
                    FlipY = true;
                    Rotate90 = false;
                }

                if (Screen.orientation == ScreenOrientation.Portrait)
                {
                    FlipX = true;
                    FlipY = true;
                    Rotate90 = true;
                }

                if (Screen.orientation == ScreenOrientation.PortraitUpsideDown)
                {
                    FlipX = false;
                    FlipY = false;
                    Rotate90 = true;
                }

                Orient = Screen.orientation;
                Init = true;
            }
		}

        if (FrontCamera)            // Front-facing Camera
		{
            if (Orient != Screen.orientation || !Init)
            {
                if (Screen.orientation == ScreenOrientation.LandscapeRight)
                {
                  //  FlipX = false;
                  //  FlipY = false;
                }

                if (Screen.orientation == ScreenOrientation.LandscapeLeft)
                {
                  //  FlipX = true;
                  //  FlipY = true;
                }

                if (Screen.orientation == ScreenOrientation.Portrait)
                {
                  //  FlipX = true;
                  //  FlipY = false;
                }

                if (Screen.orientation == ScreenOrientation.PortraitUpsideDown)
                {
                  //  FlipX = false;
                  //  FlipY = true;
                }

                Orient = Screen.orientation;
                Init = true;
            }
		}

		if (FlipX_check != FlipX || FlipY_check != FlipY || Rotate90_check != Rotate90)
		{
			GetComponent<RegionCapture>().FlipX = FlipX;
			GetComponent<RegionCapture>().FlipY = FlipY;
            GetComponent<RegionCapture>().Rotate90 = Rotate90;
            
            if (!CustomBackgroundMaterial) CustomBackgroundMaterial = GetComponent<RegionCapture>().CustomBackgroundMaterial;
            if (CustomBackgroundMaterial) StartCoroutine(GetComponent<RegionCapture>().ResetYUVTextures());
        }
        #endif
    }
}
