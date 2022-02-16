using System;
using System.Collections;
using System.Collections.Generic;
using com.unity.mgobe;
using UnityEngine;
using UnityEngine.UI;
public abstract class ARPaintRoom : MonoBehaviour
{

    public static string LocalPanel = "100";
    protected Room paintRoom;
    public GameObject tmp;
    protected bool connect = false;
    protected bool videoConnect = false;
    public virtual void Init() { }
    void Awake()
    {
        Init();
    }
    void Start()
    {

        paintRoom = new Room(null);
        if (!TelentDrawController.instance)
            return;
        if (TelentDrawController.instance.paintJoinState == PaintJoinState.Create)
            Btn_CreateRoom();
        else
        {
            Btn_JoinRoom();
            Debug.Log("Btn_JoinRoom");
        }



    }
    public virtual void PanelItemClicked(string id)
    {

    }
    public void Btn_JoinRoom()
    {
        RoomInfo roomInfo = new RoomInfo();
        if (TelentDrawController.instance.JoinRoomId == "")
        {
            AppendTxt("加入的房间ID为空\r\n");
            return;
        }

        roomInfo.Id = TelentDrawController.instance.JoinRoomId;
        paintRoom.InitRoom(roomInfo);
        paintRoom.JoinRoom(TelentDrawController.instance.JoinPara, (ResponseEvent eve) =>
         {
             if (eve.Code == 0)
             {
                 AppendTxt("加入房间成功 \r\n");
                 if (ToastManager.instance)
                     ToastManager.instance.AddToast(ToastType.Info, "加入房间成功\r\n");
                 JointSuccess();
             }
             else
             {
                 AppendTxt("加入房间失败\r\n");
                 if (ToastManager.instance)
                     ToastManager.instance.AddToast(ToastType.Error, "加入房间失败\r\n");
             }
         });

    }
    public virtual void JointSuccess()
    {
        //初始化监听
        RegisterCallback();
        Btn_StartStep();

        connect = true;
    }
    public void Btn_CreateRoom()
    {
        paintRoom.CreateRoom(TelentDrawController.instance.CreatePara, eve =>
{
    if (eve.Code == 0)
    {

        // 创建成功
        // AppendTxt("创建成功"+((ResponseEvent)eve.Data).Msg);
        Debug.Log("创建成功" + paintRoom.RoomInfo.Id);
        //  paintRoom.RoomInfo.Id;
        Debug.Log("创建成功" + ((CreateRoomRsp)eve.Data).RoomInfo.Id);
        //初始化监听
        JointSuccess();
        // 使用 room 调用其他API，如 room.StartFrameSync
        //   return;
    }
    if (eve.Code == 20010)
    {
        Debug.Log("玩家已在房间内");
    }
});//

    }
    private void RegisterCallback()
    {
        Listener.Add(paintRoom);
        paintRoom.OnJoinRoom = onJoinIn;
        // 广播：房间有玩家退出
        paintRoom.OnLeaveRoom = onLeaveRoom;
        // 广播：房间被解散
        paintRoom.OnDismissRoom = onDismis;
        // update
        paintRoom.onUpdate = onRoomUpdate;
        // recv msg
        paintRoom.OnRecvFromClient = eve => AppendTxt("同步随机数种子:" + ((RecvFromClientBst)eve.Data).Msg + "\r\n");
        // start step
           paintRoom.OnStartFrameSync = eve => AppendTxt("收到开始帧同步！\r\n");
        //   paintRoom.OnStopFrameSync = eve => AppendTxt("收到停止帧同步！\r\n");

        paintRoom.OnRecvFrame = recvFrameStep;
        // match
        Room.OnMatch = onMatch;
    }
    protected virtual void onJoinIn(BroadcastEvent eve)
    {
        var data = (JoinRoomBst)eve.Data;
        AppendTxt("新玩家加入" + data.JoinPlayerId + "\r\n");
        foreach (PlayerInfo player in paintRoom.RoomInfo.PlayerList)
        {
            AppendTxt("当前玩家：" + player.Name + "\r\n");
        }
    }
    protected virtual void onLeaveRoom(BroadcastEvent eve)
    {

        var data = (LeaveRoomBst)eve.Data;
        AppendTxt("玩家退出" + data.LeavePlayerId + "\r\n");
    }
    protected virtual void onDismis(BroadcastEvent eve)
    {

        var data = (JoinRoomBst)eve.Data;
        AppendTxt("房间被解散" + "\r\n");
    }
    protected virtual void onRoomUpdate(Room r)
    {
        AppendTxt("房间更新:" + r.RoomInfo.Id + "|" + r.RoomInfo.PlayerList.Count + "\r\n");
    }
    protected virtual void onMatch(BroadcastEvent eve)
    {
        var data = (MatchBst)eve.Data;
        if (data.ErrCode == 0)
        {
            AppendTxt("onMatch匹配成功\r\n");
        }
        else
        {
            AppendTxt("onMatch匹配失败\r\n");
        }
    }
    public virtual void Btn_LeaveRoom()//离开房间
    {

        paintRoom.LeaveRoom(eve =>
        {
            if (eve.Code == 0)
            {
                AppendTxt("离开房间" + "\r\n");
            }
            else
            {
                AppendTxt("离开失败" + "\r\n");
            }
        });
    }

    public virtual void Btn_DismisRoom() //解散房间
    {
        paintRoom.DismissRoom(eve =>
        {
            Debug.Log(eve.Code);
            if (eve.Code == 0)
            {
                AppendTxt("解散成功" + "\r\n");
            }
        });
    }

    public virtual void Btn_MatchRoom()
    {
        AppendTxt("开始匹配房间\r\n");
        MatchRoomPara para = new MatchRoomPara()
        {
            MaxPlayers = 4,
            PlayerInfo = new PlayerInfoPara() { Name = "p" + UnityEngine.Random.Range(1, 100), CustomPlayerStatus = 1, CustomProfile = "" },
            RoomType = "1",
        };
        paintRoom.MatchRoom(para, eve =>
        {
            if (eve.Code != 0)
            {
                AppendTxt("room匹配失败\r\n");
            }
            else
            {
                AppendTxt("room匹配成功...\r\n");
                //匹配队友
                Btn_MatchPlayer();
                Debug.Log(eve.Data);
            }

        });
    }
    public virtual void Btn_MatchPlayer()
    {
        AppendTxt("开始匹配玩家\r\n");
        MatchPlayerInfoPara playerInfo = new MatchPlayerInfoPara()
        {
            CustomPlayerStatus = 1,
            Name = "p" + UnityEngine.Random.Range(1, 100),
            MatchAttributes = new List<MatchAttribute>() {
                new MatchAttribute(){ Name = "1", Value = 2}
            },
        };
        MatchPlayersPara matchPlayerPara = new MatchPlayersPara()
        {
            //MatchCode = "match-0bjyargr", //2v2
            MatchCode = "match-82hzbk07", // 1v1
            PlayerInfoPara = playerInfo,
        };


        paintRoom.MatchPlayers(matchPlayerPara, eve =>
        {
            if (eve.Code != 0)
            {
                AppendTxt("player匹配失败" + "\r\n");
            }
            else
            {
                AppendTxt("player匹配中..." + "\r\n");
            }
        });

    }
    public void Btn_SendMsg()
    {
        SendToClientPara stcp = new SendToClientPara()
        {
            RecvType = RecvType.RoomAll,
            Msg = "123",
            RecvPlayerList = new List<string>(),
        };
        paintRoom.SendToClient(stcp, eve =>
        {

        });
    }

    public virtual void Btn_StartStep()
    {
        paintRoom.StartFrameSync(eve =>
        {
            if (eve.Code == 0)
            {
                AppendTxt("开启帧同步成功!\r\n");
            }
        });
    }
    public void Btn_StopStep()
    {
        paintRoom.StopFrameSync(eve =>
        {
            if (eve.Code == 0)
            {
                AppendTxt("结束帧同步成功!\r\n");
            }
        });
    }
    [Serializable]
    internal class XXFrameData
    {
        [SerializeField]
        public string strData;
        [SerializeField]
        public int intData;

        override public string ToString()
        {
            return strData + "&" + intData;
        }
        public static XXFrameData init(string data)
        {
            XXFrameData xfd = new XXFrameData();
            xfd.strData = data.Split('&')[0];
            xfd.intData = int.Parse(data.Split('&')[1]);
            return xfd;
        }
    }
    public virtual void Update()
    {
        /*  if (connect && !videoConnect)
          {
              videoController.JoinRoom(paintRoom.RoomInfo.Id);
              videoConnect = true;
          }*/
    }
    public void Btn_SendStep()
    {
        string strTime = DateTime.Now.ToLongTimeString();
        int intTime = (int)DateTime.Now.Second;
        XXFrameData fd = new XXFrameData();
        fd.strData = strTime;
        fd.intData = intTime;

        SendFramePara para = new SendFramePara()
        {
            Data = fd.ToString(),
        };
        paintRoom.SendFrame(para, eve =>
        {
            if (eve.Code == 0)
                AppendTxt("发送帧同步成功\r\n");
            else
                AppendTxt("发送帧同步失败\r\n");
        });
    }
    public virtual void recvFrameStep(BroadcastEvent eve)
    {
        /*  RecvFrameBst bst = ((RecvFrameBst)eve.Data);
          if (bst.Frame.Items.Count != 0)
              AppendTxt("----收到帧消息：" + bst.Frame.Id + "\r\n");
          foreach (FrameItem fi in bst.Frame.Items)
          {
              XXFrameData xfd = XXFrameData.init(fi.Data);
              AppendTxt(fi.PlayerId + "|" + xfd.ToString() + "\r\n");
          }*/
    }
    public List<string> strlist = new List<string>();


    public void AppendTxt(string txt)
    {
        strlist.Add(txt);
    }

    public void CleanTxt()
    {
        tmp.GetComponent<Text>().text = "";
    }

    public virtual void LateUpdate()
    {
        if (strlist.Count > 0)
        {
            tmp.GetComponent<Text>().text += strlist[0];
            strlist.RemoveAt(0);
        }
    }
    public virtual void OnDisable()
    {

        Btn_LeaveRoom();
     //   Btn_DismisRoom();

    }
}
