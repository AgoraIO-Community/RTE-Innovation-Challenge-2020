using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class WaitingForDevelop : MonoBehaviour
{
    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        
    }
    public void SetUp(){
        if(ToastManager.instance)
        ToastManager.instance.AddToast(ToastType.Info,"功能待开发");
        
    }
}
