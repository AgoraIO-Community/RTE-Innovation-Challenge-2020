using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public enum ToastType
{
    Info,
    Error,
    Other
}
class ToastInfo
{
    public ToastInfo(ToastType type, string message)
    {
        this.type = type;
        data = message;
    }
    public string data;
    public ToastType type;
}
public class ToastManager : MonoBehaviour
{
    public static ToastManager instance;
    Queue<ToastInfo> toastQueue;
    float timeLimit = 1f;
    float count = 0;
    bool toasting = false;
    public Image toastBg;
    public Text title;
    public Text content;
    private void Awake()
    {
        instance = this;
         toastQueue = new Queue<ToastInfo>();
    }
    // Start is called before the first frame update
    void Start()
    {
       
    }
    public void AddToast(ToastType type,string message)
    {
        ToastInfo toast = new ToastInfo(type,message);
        toastQueue.Enqueue(toast);
    }
    // Update is called once per frame
    void Update()
    {
        if (!toasting )
        {
            if (toastQueue.Count > 0)
            {
                ToastInfo tInfo = toastQueue.Dequeue();
                switch (tInfo.type)
                {
                    case ToastType.Error:
                        toastBg.color = new Color(0.5f, 0.2f, 0f);
                        title.text = "错误";
                        break;
                    case ToastType.Info:
                        toastBg.color = new Color(0, 0, 0);
                        title.text = "通知";
                        break;
                    case ToastType.Other:
                        toastBg.color = new Color(0.8f, 0.6f, 0.2f);
                        title.text = "其他";
                        break;

                }
                content.text = tInfo.data;
                toastBg.gameObject.SetActive(true);
                toasting = true;
            }
        }else
        {
            count += Time.deltaTime;
            if (count > timeLimit)
            {
                toastBg.gameObject.SetActive(false);
                toasting = false;
                count = 0;
            }
        }
    }
  
}
