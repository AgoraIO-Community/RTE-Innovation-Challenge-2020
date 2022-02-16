using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class SwitchIcon : MonoBehaviour
{
   public bool isActive=true;
    // Start is called before the first frame update
    void Start()
    {
   //     isActive=GetComponent<GameObject>().activeSelf;
    }
public void SwicthShow(){
    isActive=!isActive;
    gameObject.SetActive(isActive);
}
    // Update is called once per frame
    void Update()
    {
        
    }
}
