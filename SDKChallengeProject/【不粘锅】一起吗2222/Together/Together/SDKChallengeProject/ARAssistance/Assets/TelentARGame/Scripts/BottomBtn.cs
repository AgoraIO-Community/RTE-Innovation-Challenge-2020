using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class BottomBtn : MonoBehaviour
{
    public Animator bottomBars;
    bool down = false;
    // Start is called before the first frame update
    void Start()
    {

    }
    public void SetBottom()
    {
        if (down)
             bottomBars.Play("bottomBars_");
        else
        {
             bottomBars.Play("bottomBars");

        }
        down = !down;
       

    }
    // Update is called once per frame
    void Update()
    {

    }
}
