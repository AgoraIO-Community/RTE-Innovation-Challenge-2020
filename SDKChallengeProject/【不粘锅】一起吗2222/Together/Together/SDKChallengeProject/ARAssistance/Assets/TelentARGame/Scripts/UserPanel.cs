using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
public class UserPanel : MonoBehaviour
{
    public InputField nameInput;
    public Dropdown genderInput;
    public InputField phoneInput;
    public Dropdown jobInput;
    public InputField infoInput;
    public GameObject userPanel;
    // Start is called before the first frame update
    void Start()
    {
       Init();
    }
    public void UserInfoOpen()
    {

        userPanel.SetActive(true);

       Init();
    }
    void Init(){
 if (nameInput.text == "")
        {
            nameInput.text = UserManager.instance.UName;
        }
        genderInput.value = (int)UserManager.instance.UGender;
        jobInput.value = (int)UserManager.instance.UType;
        if (phoneInput.text == "")
        {
            phoneInput.text = UserManager.instance.UPhone;
        }
        if (infoInput.text == "")
        {
            infoInput.text = UserManager.instance.UInfo;
        }

    }
    public void UserInfoClose()
    {

        userPanel.SetActive(false);

        UserManager.instance.UName = nameInput.text;

        UserManager.instance.UGender = genderInput.value;


        if (jobInput.value == 0 || jobInput.value == 2)
        {
            TelentDrawController.instance.shareCamType = ShareCameraType.FrontCam;
        }
        else
        {
            TelentDrawController.instance.shareCamType = ShareCameraType.Screen;
        }
        UserManager.instance.UType = (UserType)jobInput.value;

        UserManager.instance.UPhone = phoneInput.text;
        UserManager.instance.UJob = jobInput.options[jobInput.value].ToString();

        UserManager.instance.UInfo = infoInput.text;
    }
    // Update is called once per frame
    void Update()
    {

    }
}
