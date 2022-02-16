using System;
using System.Collections;
using System.Collections.Generic;
using com.unity.mgobe;
using UnityEngine;
using UnityEngine.UI;
using LitJson;
public class StaffPaintRoom : ARPaintRoom
{
    Dictionary<string, UserDefEventHandler> paintClients;
    public VuforiaCameraShareStaff shareStaff;
    public UDTEventHandlerMy uDTEventHandler;
    public ColorSignal[] paintContainer;
    public UserDefEventHandler activeTargetHandler;
    string id = "";
    public void Init(GameObject gg)
    {
        tmp = gg;

    }
    public override void Init()
    {
        paintClients = new Dictionary<string, UserDefEventHandler>();
        //        Debug.LogError("paintClients init ed");
        base.Init();

    }
    public override void JointSuccess()
    {
        base.JointSuccess();
        id = paintRoom.RoomInfo.Id;
    }
    // Update is called once per frame
    public override void Update()
    {
        base.Update();
        if (connect && !videoConnect)
        {
            shareStaff.JoinRoom(paintRoom.RoomInfo.Id);
            videoConnect = true;
        }
        //  Debug.Log(paintClients.Count);
        //遍历修改标志颜色
        bool anyActive = false;
        for (int i = 0; i < paintContainer.Length; i++)
        {
            string pname = paintContainer[i].tarName;
            if (paintClients.ContainsKey(pname))
            {
                UserDefEventHandler udef = paintClients[pname];
                if (udef == null)
                {
                    paintContainer[i].tarName = "";
                    paintClients.Remove(pname);
                    paintContainer[i].GetComponent<Image>().color = new Color(1f, 1f, 1f);
                    break;
                }
                else
                {
                    if (udef.isActive)
                    {
                        if (pname != LocalPanel)
                            anyActive = true;
                        paintContainer[i].GetComponent<Image>().color = new Color(1f, 0, 0);
                        activeTargetHandler = udef;
                        shareStaff.cameraImageAccess = activeTargetHandler.cameraImageAccess;
                        activeTargetHandler.painterClientController.shouldActive = true;
                        //                        Debug.Log("Target Active:" + activeTargetHandler.name);
                    }
                    else
                        paintContainer[i].GetComponent<Image>().color = new Color(0f, 1f, 0);
                }
            }
            else
            {
                paintContainer[i].tarName = "";
                paintContainer[i].GetComponent<Image>().color = new Color(1f, 1f, 1f);
            }
        }
        foreach (string pn in paintClients.Keys)
        {
            bool find = false;
            for (int i = 0; i < paintContainer.Length; i++)
            {
                if (pn == paintContainer[i].tarName)
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
                        paintContainer[i].tarName = pn;
                        break;
                    }
                }
            }
        }
        if (!anyActive)
        {
            for (int i = 1; i < paintContainer.Length; i++)
            {
                if (paintContainer[i].tarName == LocalPanel)
                {
                    paintContainer[i].GetComponent<Image>().color = new Color(1f, 0f, 0);
                    break;
                }
            }
            paintClients[LocalPanel].isActive = true;
            activeTargetHandler = paintClients[LocalPanel];
            activeTargetHandler.painterClientController.shouldActive = true;
        }
        else
        {
            for (int i = 1; i < paintContainer.Length; i++)
            {
                if (paintContainer[i].tarName == LocalPanel)
                {
                    paintContainer[i].GetComponent<Image>().color = new Color(0f, 1f, 0);
                    break;
                }
            }
            paintClients[LocalPanel].isActive = false;
            paintClients[LocalPanel].painterClientController.shouldActive = false;
        }


    }
    public void BuildNewTarget()
    {
        uDTEventHandler.BuildNewTarget();
        shareStaff.SetOnlyOne(false);
    }
    //点击之后，删除target
    public override void PanelItemClicked(string id)
    {
        //        Debug.Log("Remove Target:" + id);
        if (paintClients.ContainsKey(id))
        {
            uDTEventHandler.RemoveTarget(id);
            UserDefEventHandler defEventHandler = paintClients[id];
            paintClients.Remove(id);
            Destroy(defEventHandler.gameObject);
            for (int i = 0; i < paintContainer.Length; i++)
            {
                if (paintContainer[i].tarName == id)
                {
                    paintContainer[i].tarName = "";
                    break;
                }
            }

        }
        if (paintClients.Count == 1)
        {
            //设置能否切换
            shareStaff.SetOnlyOne(true);
            paintClients[LocalPanel].isActive = true;
        }

    }
    public void AddPainter(string name, UserDefEventHandler paintClient)
    {
        if (!paintClients.ContainsKey(name))
        {
            paintClients.Add(name, paintClient);
            if (ToastManager.instance)
                ToastManager.instance.AddToast(ToastType.Info, "添加识别图:" + name);
            if (name != LocalPanel)
                paintClient.painterClientController.CopyPara(activeTargetHandler.painterClientController.GeneralPara());
            //Debug.LogError("添加识别图:" + name);
            for (int i = 0; i < paintContainer.Length; i++)
            {
                if (paintContainer[i].tarName == "")
                {
                    paintContainer[i].tarName = name;
                    break;
                }
            }
        }
        else
        {
            //         Debug.LogError("该识别图ID已存在:" + name);
        }


    }

    string lastMessage = "";
    string GeneralData()
    {
        List<string> paPanel = new List<string>();
        foreach (string user in paintClients.Keys)
        {
            paPanel.Add(user);

        }
        JsonData jsonData = new JsonData();
        jsonData["mark"] = new JsonData();
        for (int i = 0; i < paPanel.Count; i++)
        {
            jsonData["mark"].Add(i);
            jsonData["mark"][i] = paPanel[i];

        }
        jsonData["expert"] = !shareStaff.useCam;
        return jsonData.ToJson();
    }
    public override void LateUpdate()
    {
        base.LateUpdate();
        if (connect)
        {
            XXFrameData fd = new XXFrameData();
            fd.strData = GeneralData();
            fd.intData = (int)UserManager.instance.UType;
            SendFramePara para = new SendFramePara()
            {
                Data = fd.ToString(),
            };
            try
            {
                paintRoom.SendFrame(para, eve =>
                {
                    /* if (eve.Code == 0)
                         AppendTxt("发送帧同步成功\r\n");
                     else
                         AppendTxt("发送帧同步失败\r\n");*/
                });
            }
            catch (Exception e) { }
        }

    }
    public override void recvFrameStep(BroadcastEvent eve)
    {
        base.recvFrameStep(eve);

        RecvFrameBst bst = ((RecvFrameBst)eve.Data);
        //    if (bst.Frame.Items.Count != 0)
        //       Debug.Log("----收到帧消息：" + bst.Frame.Id + "\r\n");
        foreach (FrameItem fi in bst.Frame.Items)
        {
            XXFrameData xfd = XXFrameData.init(fi.Data);
            int kk = xfd.intData;
            //            Debug.LogError("ssss:"+kk+":"+UserManager.instance.UType);
            if ((UserType)xfd.intData != UserManager.instance.UType)
            {
                //                                                          Debug.Log(xfd.strData);
                JsonData jsonData = JsonMapper.ToObject(xfd.strData);
                if (((IDictionary)jsonData).Contains("mark"))
                {
                    string canvasId = jsonData["mark"].ToString();

                    //选择不同的画布
                    if (paintClients.ContainsKey(canvasId))
                    {

                        if (((IDictionary)jsonData).Contains("ctrl-data"))
                        {
                            //                             Debug.LogError("Contain ID：" + canvasId + "DDD:" + jsonData["ctrl-data"].ToJson());
                            UserDefEventHandler handler = paintClients[canvasId];
                            if (handler == null || handler.painterClientController == null)
                            {
                                Debug.LogError("Some NUll");
                            }
                            if (handler.painterClientController.painterCanvas.isInited)
                                paintClients[canvasId].painterClientController.UpdatePara(jsonData["ctrl-data"]);

                        }
                    }

                }

            }
            //            Debug.Log(fi.PlayerId + "|" + fi.Data + "\r\n");
        }
    }

}
