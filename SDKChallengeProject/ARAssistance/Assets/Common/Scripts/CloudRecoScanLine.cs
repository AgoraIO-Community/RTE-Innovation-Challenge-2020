/*===============================================================================
Copyright (c) 2016-2018 PTC Inc. All Rights Reserved.
 
Vuforia is a trademark of PTC Inc., registered in the United States and other 
countries.
===============================================================================*/
using UnityEngine;
using Vuforia;

public class CloudRecoScanLine : MonoBehaviour
{
    #region PRIVATE_MEMBERS

    const float SCAN_DURATION = 4; //seconds
    float mTime;
    bool mMovingDown = true;
    bool cachedEnabledState;
    Camera m_Camera;
    Renderer m_Renderer;
    CloudRecoBehaviour m_CloudRecoBehaviour;

    bool CloudEnabled
    {
        get { return m_CloudRecoBehaviour && m_CloudRecoBehaviour.CloudRecoEnabled; }
    }
    #endregion //PRIVATE_MEMBERS

    #region MONOBEHAVIOUR_METHODS

    void Start()
    {
        m_Camera = Camera.main;
        m_Renderer = GetComponent<Renderer>();
        m_CloudRecoBehaviour = FindObjectOfType<CloudRecoBehaviour>();

        // Cache the Cloud enable state so that we can reset the scanline
        // when the enabled state changes
        cachedEnabledState = CloudEnabled;
    }

    void Update()
    {
        if (cachedEnabledState != CloudEnabled)
        {
            cachedEnabledState = CloudEnabled;
            // Reset the ScanLine position when Cloud enabled state changes
            mTime = 0;
            mMovingDown = true;
        }

        m_Renderer.enabled = CloudEnabled; // show/hide scanline

        if (CloudEnabled)
        {
            float u = mTime / SCAN_DURATION;
            mTime += Time.deltaTime;
            if (u > 1)
            {
                // invert direction
                mMovingDown = !mMovingDown;
                u = 0;
                mTime = 0;
            }

            // Get the main camera
            float viewAspect = m_Camera.pixelWidth / (float)m_Camera.pixelHeight;
            float fovY = Mathf.Deg2Rad * m_Camera.fieldOfView;
            float depth = 1.02f * m_Camera.nearClipPlane;
            float viewHeight = 2 * depth * Mathf.Tan(0.5f * fovY);
            float viewWidth = viewHeight * viewAspect;

            // Position the mesh
            float y = -0.5f * viewHeight + u * viewHeight;
            if (mMovingDown)
            {
                y *= -1;
            }

            transform.localPosition = new Vector3(0, y, depth);

            // Scale the quad mesh to fill the camera view
            float scaleX = 1.02f * viewWidth;
            float scaleY = scaleX / 32;
            transform.localScale = new Vector3(scaleX, scaleY, 1.0f);
        }
    }

    #endregion // MONOBEHAVIOUR_METHODS
}
