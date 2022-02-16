using System;
using System.Collections;
using System.Collections.Generic;
using com.unity.mgobe;
using UnityEngine;
using UnityEngine.UI;
using LitJson;
public class ExpertPaintRoom : ARPaintRoom
{
    public PainterExpertController paintCanvas2;
    public RoomSharedInfo sharedInfo;
    public ColorSignal[] paintContainer;
    Dictionary<string, PainterExpertController> paintCanvasDic;
    private List<string> actPanelName;
    public VideoShareController videoController;
    [Header("Aniamtor")]
    public Animator mainPanelAnimator;
    public Button paintCtrl;
    public Button cleanBtn;
    string id = "";
    bool startTrans = false;
    // Start is called before the first frame update
    public void Init(GameObject gg)
    {
        
        tmp = gg;

    }
    public override void Init()
    {
        actPanelName = new List<string>();
        paintCanvasDic = new Dictionary<string, PainterExpertController>();
        paintCanvasDic.Add(LocalPanel, paintCanvas2);
        //添加按钮回调
        cleanBtn.onClick.AddListener(paintCanvasDic[LocalPanel].OnClickClearCanvas);



    }
    void OnEnable()
    {
        if (mainPanelAnimator != null)
            mainPanelAnimator.Play("mainPanel_io_1");
        //其他角色，不打开绘画面板

        if (UserManager.instance && UserManager.instance.UType != UserType.Expert)
        {
            paintCtrl.interactable = false;
            //                    Debug.LogError(paintCtrl.interactable+"||"+UserManager.instance.UType);
        }
    }
    bool current = false;
    public void OpenPaintPanel()
    {
        if (!current)
            mainPanelAnimator.Play("mainPanel_io_0");
        else
            mainPanelAnimator.Play("mainPanel_io_1");
        current = !current;
        paintCanvas2.gameObject.SetActive(current);
        paintCanvas2.shouldActive = current;
    }
    public void OpenSettingPanel()
    {
        if (sharedInfo.gameObject.activeSelf)
            sharedInfo.gameObject.SetActive(false);
        else
            sharedInfo.gameObject.SetActive(true);
    }
    public override void JointSuccess()
    {
        base.JointSuccess();
        id = paintRoom.RoomInfo.Id;
    }
    protected override void onRoomUpdate(Room r)
    {
        base.onRoomUpdate(r);
        //更新内容
        sharedInfo.SetUpInfo(r);

    }
    // Update is called once per frame
    public override void Update()
    {
        base.Update();
        if (connect && !videoConnect)
        {
            videoController.JoinRoom(paintRoom.RoomInfo.Id);
            videoConnect = true;
        }
        for (int i = 0; i < paintContainer.Length; i++)
        {
            string tt = paintContainer[i].tarName;
            if (paintCanvasDic.ContainsKey(tt))
            {
                PainterExpertController tempCtrl = paintCanvasDic[tt];
                if (tempCtrl.shouldActive)
                {
                    paintContainer[i].GetComponent<Image>().color = new Color(1f, 0, 0);
                }
                else
                    paintContainer[i].GetComponent<Image>().color = new Color(0f, 1f, 0);
            }
            else if (tt != "")
            {
                bool act = paintCanvas2.shouldActive;
                paintCanvas2.shouldActive = false;
                GameObject tgo = Instantiate(paintCanvas2.gameObject);
                PainterExpertController tempCtrl = tgo.GetComponent<PainterExpertController>();
                //按照16:9比例进行缩放
                tempCtrl.painterCanvas.SetUpSize((int)(Screen.height*1f/9*16),Screen.height);
                paintCanvas2.shouldActive = act;
                paintContainer[i].GetComponent<Image>().color = new Color(1f, 0, 0);
                paintCanvasDic.Add(tt, tempCtrl);
            }
            else
            {
                paintContainer[i].tarName = "";
                paintContainer[i].GetComponent<Image>().color = new Color(1f, 1, 1);
            }
        }
        bool anyAct = false;
        List<string> ss = new List<string>();
        foreach (string np in paintCanvasDic.Keys)
        {
            bool find = false;
            for (int i = 0; i < paintContainer.Length; i++)
            {
                if (np == paintContainer[i].tarName)
                {
                    if (paintCanvasDic[np].shouldActive)
                        anyAct = true;
                    find = true;
                    break;
                }
            }
            if (!find)
            {
                ss.Add(np);

            }

        }
        for (int k = 0; k < ss.Count; k++)
        {
            PainterExpertController cc = paintCanvasDic[ss[k]];
            paintCanvasDic.Remove(ss[k]);
            Destroy(cc.gameObject);
        }
        if (!anyAct)
        {
            if (!paintCanvasDic[LocalPanel].shouldActive)
            {
                cleanBtn.onClick.RemoveAllListeners();
                cleanBtn.onClick.AddListener(paintCanvasDic[LocalPanel].OnClickClearCanvas);
            }

            paintCanvasDic[LocalPanel].shouldActive = true;

        }

    }
    public override void PanelItemClicked(string id)
    {
        if (!paintCanvasDic.ContainsKey(id))
            return;
        foreach (string na in paintCanvasDic.Keys)
        {
            if (na == id)
            {
                paintCanvasDic[na].shouldActive = true;
                //绑定清除按钮
                cleanBtn.onClick.RemoveAllListeners();
                cleanBtn.onClick.AddListener(paintCanvasDic[na].OnClickClearCanvas);
                if (ToastManager.instance)
                    ToastManager.instance.AddToast(ToastType.Info, "切换绘画板：" + na);
            }
            else
            {
                paintCanvasDic[na].shouldActive = false;
            }
        }

    }
    public void StartTrans()
    {
        startTrans = !startTrans;
    }

    public override void LateUpdate()
    {
        base.LateUpdate();

        //    if (!startTrans)
        //         return;
        //            print("每2帧执行一次");
        if (connect)// && /*Time.frameCount % 5 == 0 &&*/ paintCanvas2.PosUpdate)
        {
            foreach (string pName in paintCanvasDic.Keys)
            {
                JsonData jsonData = new JsonData();
                jsonData["mark"] = pName;
                JsonData ctrlData = new JsonData();
                JsonData nrl = paintCanvasDic[pName].GetSendData();
                if (nrl != null)
                    ctrlData["nrl"] = nrl;
                JsonData ctrl = paintCanvasDic[pName].GetCtrlData();
                if (ctrl != null)
                    ctrlData["ctrl"] = ctrl;
                if (ctrlData.IsObject)
                    jsonData["ctrl-data"] = ctrlData;
                string sendData = jsonData.ToJson();
                //                Debug.Log(sendData);
                try
                {
                    if (sendData != null)
                    {
                        XXFrameData fd = new XXFrameData();
                        fd.strData = sendData;
                        fd.intData = (int)UserManager.instance.UType;
                        SendFramePara para = new SendFramePara()
                        {
                            Data = fd.ToString(),
                        };
                        paintRoom.SendFrame(para, eve =>
                        {
                            if (eve.Code == 0)
                            {
                                //                                Debug.Log("发送帧同步成功\r\n");
                                //  AppendTxt("发送帧同步成功\r\n");
                            }

                            else
                            {
                                //  if (ToastManager.instance)
                                //      ToastManager.instance.AddToast(ToastType.Error, "发送帧同步失败\r\n");
                                //   Debug.Log("发送帧同步失败\r\n");
                                //    AppendTxt("发送帧同步失败\r\n");
                            }
                        });
                    }

                }
                catch (Exception e) { }
            }
        }
    }
    public override void recvFrameStep(BroadcastEvent eve)
    {
        base.recvFrameStep(eve);
        RecvFrameBst bst = ((RecvFrameBst)eve.Data);
        //    if (bst.Frame.Items.Count != 0)
        //       Debug.Log("----收到帧消息：" + bst.Frame.Id + "\r\n");
        try
        {
            foreach (FrameItem fi in bst.Frame.Items)
            {
                XXFrameData xfd = XXFrameData.init(fi.Data);
                //    Debug.LogError("strData:" +xfd.strData);
                if ((UserType)xfd.intData != UserManager.instance.UType)
                {
                    JsonData jsonData = JsonMapper.ToObject(xfd.strData);
                    if (((IDictionary)jsonData).Contains("expert"))
                    {
                        bool temp = false;
                        temp = bool.Parse(jsonData["expert"].ToString());
                        videoController.SetStaffVideoType(temp);
                    }


                    if (((IDictionary)jsonData).Contains("mark"))
                    {

                        actPanelName = new List<string>();
                        for (int j = 0; j < jsonData["mark"].Count; j++)
                        {

                            string temp = jsonData["mark"][j].ToString();
                            actPanelName.Add(temp);
                            //            Debug.LogError("mark:" + temp + " paintCanvasDic:" + paintCanvasDic.Count);
                            bool find = false;
                            for (int i = 0; i < paintContainer.Length; i++)
                            {

                                if (paintContainer[i].tarName == temp)
                                {
                                    find = true;
                                    break;
                                }
                            }
                            if (!find)
                            {
                                for (int i = 0; i < paintContainer.Length; i++)
                                {

                                    if (paintContainer[i].tarName == "")
                                    {
                                        paintContainer[i].tarName = temp;
                                        break;
                                    }
                                }
                            }


                        }
                        for (int i = 0; i < paintContainer.Length; i++)
                        {
                            bool find = false;
                            for (int k = 0; k < actPanelName.Count; k++)
                            {
                                if (paintContainer[i].tarName == actPanelName[k])
                                {
                                    find = true;
                                    break;
                                }
                            }
                            if (!find)
                            {
                                paintContainer[i].tarName = "";
                            }

                        }



                    }

                }
                //   Debug.Log(fi.PlayerId + "|" + fi.Data + "\r\n");
            }
        }
        catch (Exception e)
        {
            if(ToastManager.instance)
            ToastManager.instance.AddToast(ToastType.Error,e.Message);
            Debug.LogError(e.Message);
        }
    }
}
