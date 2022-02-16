using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GetRealScale : MonoBehaviour
{
    public Vector3 targetScale = Vector3.one;
    // Start is called before the first frame update
    void Start()
    {
        Renderer r = GetComponent<Renderer>();
        if (r != null)
        {
            targetScale = r.bounds.size / 10;//new Vector3(k,1,k*tt.z);//r.bounds.size/10;
            Debug.Log(targetScale);
        }
    }

    // Update is called once per frame
    void Update()
    {
        //Renderer r = GetComponent<Renderer>();
        //  Debug.Log(r.bounds.size.x);
        //  Debug.Log(r.bounds.size.y);
        //  Debug.Log(r.bounds.size.z);
    }
}
