using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Vuforia;

public class UserDefEventHandler : MonoBehaviour
{
    public StaffPaintRoom staffPaintRoom;

    public PainterClientController painterClientController;
    public CameraImageAccess cameraImageAccess;
    public bool isActive = false;
    public bool screenTarget = false;
    string trackName = "";
    // Start is called before the first frame update
    void Start()
    {
        if (screenTarget)
            TargetLost("-100");
    }
    public void TargetFound(string nam)
    {
         if (!screenTarget){
        
        cameraImageAccess.mAccessCameraImage = true;
        painterClientController.shouldActive = true;

        isActive = true;
         }
    }
    public void TargetLost(string nam)
    {
        if (trackName == "" && nam != "")
        {
            //           Debug.LogError(nam);
            staffPaintRoom.AddPainter(nam.Replace("-", ""), this);
            trackName = nam;
        }

        //      Debug.Log("trackName:" + trackName);
        if (!screenTarget)
        {
            cameraImageAccess.mAccessCameraImage = false;
            painterClientController.shouldActive = false;
            isActive = false;
        }

    }
    // Update is called once per frame
    void Update()
    {

    }
}
