using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
public enum UserType
{
    Expert,
    Staff,
    Ranger
}
public class UserManager : MonoBehaviour
{
    public static UserManager instance;
    [SerializeField]
    string uName = "Petit";
    [SerializeField]
    int uGender = 0;
    [SerializeField]
    string uPhone = "188148871060";
    [SerializeField]
    string uJob = "专家";
    [SerializeField]
    string uInfo = "学习中~";
    [SerializeField]
    UserType uType=UserType.Expert;

    public string UName
    {
        get
        {
            return uName;
        }

        set
        {
            uName = value;
        }
    }

    public int UGender
    {
        get
        {
            return uGender;
        }

        set
        {
            uGender = value;
        }
    }

    public string UPhone
    {
        get
        {
            return uPhone;
        }

        set
        {
            uPhone = value;
        }
    }

    public string UJob
    {
        get
        {
            return uJob;
        }

        set
        {
            uJob = value;
        }
    }

    public string UInfo
    {
        get
        {
            return uInfo;
        }

        set
        {
            uInfo = value;
        }
    }

    public UserType UType
    {
        get { return uType; }
        set { uType = value; }
    }

    void Awake()
    {
        instance = this;
    }
    void Start()
    {
/*#if UNITY_EDITOR
        uType = UserType.Ranger;
        Debug.Log("Unity Editor");
#endif
#if UNITY_STANDALONE_WIN
uType=UserType.Expert;
  if(ToastManager.instance)
  ToastManager.instance.AddToast(ToastType.Other, "注意Win不支持工程师加入");
#endif
#if UNITY_ANDROID
Debug.Log("UNITY_ANDROID");
        uType = UserType.Staff;
#endif*/
    }
    // Update is called once per frame
    void Update()
    {

    }
}
