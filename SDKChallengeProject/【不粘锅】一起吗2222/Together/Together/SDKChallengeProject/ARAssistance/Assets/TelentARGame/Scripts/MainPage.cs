using System;
using System.Collections;
using System.Collections.Generic;
using com.unity.mgobe;
using Google.Protobuf.Collections;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class MainPage : MonoBehaviour
{
    public InputField roomJoinId;
    public InputField usrJoinName;
    public InputField usrCreateName;
    public InputField roomCreateName;
    public InputField numCreatePlayer;
    bool findRoom = false;
    // Start is called before the first frame update
    void Start()
    {

    }
    public void Btn_Create()
    {
        if (TelentDrawController.instance && TelentDrawController.instance.ChatOk)
        {
            PlayerInfoPara playerInfoPara = new PlayerInfoPara
            {
                Name = usrCreateName.text.Trim(),
                CustomProfile = "customStr",
                CustomPlayerStatus = 0,
            };
            CreateRoomPara createRoomPara = new CreateRoomPara
            {
                RoomName = roomCreateName.text.Trim(),
                RoomType = "customStr",
                MaxPlayers = uint.Parse(numCreatePlayer.text.Trim()),
                IsPrivate = true,
                CustomProperties = "CustomProperties",
                PlayerInfo = playerInfoPara,
            };
            TelentDrawController.instance.PlayerInfoPara = playerInfoPara;
            TelentDrawController.instance.CreatePara = createRoomPara;
            TelentDrawController.instance.paintJoinState = PaintJoinState.Create;
            TelentDrawController.instance.CurrentRoomName = roomCreateName.text.Trim();
            SceneManager.LoadScene("PaintCanvas2");

        }
    }
    public void Btn_Join()
    {
        if (TelentDrawController.instance && TelentDrawController.instance.ChatOk)
        {
            PlayerInfoPara playerInfoPara = new PlayerInfoPara
            {
                Name = usrJoinName.text.Trim(),
                CustomProfile = "customStr",
                CustomPlayerStatus = 1,
            };
            // id="k8Ub1rle"
            JoinRoomPara joinRoomPara = new JoinRoomPara
            {
                PlayerInfo = playerInfoPara
            };

            GetRoomListPara getRoomListPara = new GetRoomListPara
            {
                PageNo = 1,
                PageSize = 10,
            };
            string roomId = roomJoinId.text.Trim();
            TelentDrawController.instance.JoinRoomId = roomId;
            TelentDrawController.instance.PlayerInfoPara = playerInfoPara;
            TelentDrawController.instance.JoinPara = joinRoomPara;
            TelentDrawController.instance.paintJoinState = PaintJoinState.Joined;
            TelentDrawController.instance.CurrentRoomName = roomCreateName.text.Trim();

            // 不要使用 room.getRoomList
            // 直接使用 Room 对象
            Room.GetRoomList(getRoomListPara, (ResponseEvent eve) =>
        {
            if (eve.Code == 0)
            {

                GetRoomListRsp rsp = (GetRoomListRsp)eve.Data;
                Debug.Log(rsp.Total);
                RepeatedField<RoomInfo> infos = rsp.RoomList;
                for (int i = 0; i < infos.Count; i++)
                {
                    if (roomId == infos[i].Id)
                    {

                        if ((int)infos[i].MaxPlayers == infos[i].PlayerList.Count)
                        {
                            if (ToastManager.instance)
                                ToastManager.instance.AddToast(ToastType.Error, "人员已满");
                            break;
                        }
                        findRoom = true;
                        //        Debug.Log(infos[i].Id);
                        break;
                    }

                }
                if (!findRoom)
                {
                    if (ToastManager.instance)
                        ToastManager.instance.AddToast(ToastType.Error, "未找到房间号");
                }


            }
            else
            {
                Debug.Log(eve);
            }
        });

        }
    }
    void Update()
    {
        if (findRoom)
        {
            if (TelentDrawController.instance)
            {
                if (UserManager.instance.UType == UserType.Staff)
                {
#if UNITY_EDITOR
                    Debug.Log("Unity Editor");
#endif

#if UNITY_STANDALONE_WIN
  if(ToastManager.instance)
  ToastManager.instance.AddToast(ToastType.Other, "Win不支持工程师加入");
#endif
#if UNITY_ANDROID
                    SceneManager.LoadScene("VuforiaDraw");
#endif

                }
                else
                {
                    SceneManager.LoadScene("PaintCanvas2");
                }
                findRoom = false;
            }


        }
    }

}
