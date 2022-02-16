/*===============================================================================
Copyright (c) 2018 PTC Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other
countries.
===============================================================================*/
using UnityEngine;

public class LookAtObject : MonoBehaviour
{
    public Transform lookAtObject;

    void Update()
    {
        if (lookAtObject)
        {
            Vector3 directionToTarget = this.lookAtObject.position - this.transform.position;
            this.transform.rotation = Quaternion.LookRotation(-directionToTarget, this.lookAtObject.up);
        }
    }
}
