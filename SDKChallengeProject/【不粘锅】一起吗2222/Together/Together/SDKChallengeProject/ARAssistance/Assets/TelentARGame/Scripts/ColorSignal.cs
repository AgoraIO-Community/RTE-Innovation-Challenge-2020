using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ColorSignal : MonoBehaviour
{
public ARPaintRoom staff;
    public string tarName="";
    // Start is called before the first frame update
    void Start()
    {
        
    }
    public void Close(){
        if(tarName!="")
        staff.PanelItemClicked(tarName);
    }
    // Update is called once per frame
    void Update()
    {
        
    }
}
