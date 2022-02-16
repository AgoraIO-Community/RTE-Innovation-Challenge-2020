using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;
using com.unity.mgobe;
#if (UNITY_2018_3_OR_NEWER && UNITY_ANDROID)
using System.Collections;
using UnityEngine.Android;
#endif
public enum PaintJoinState
{
    Create,
    Joined
}
public enum ShareCameraType
{
    FrontCam,
    BackCam,
    Screen
}
public class TelentDrawController : MonoBehaviour
{
    public static TelentDrawController instance;
    private bool chatOk = false;
    public PaintJoinState paintJoinState;
    private PlayerInfoPara playerPara;
    private CreateRoomPara createPara;
    private JoinRoomPara joinPara;
  public  ShareCameraType shareCamType;
    private string currentRoomName;
    string joinRoomId;
    public bool ChatOk { get => chatOk; set => chatOk = value; }
    public PlayerInfoPara PlayerInfoPara { get => playerPara; set => playerPara = value; }
    public JoinRoomPara JoinPara { get => joinPara; set => joinPara = value; }
    public CreateRoomPara CreatePara { get => createPara; set => createPara = value; }
    public string JoinRoomId { get => joinRoomId; set => joinRoomId = value; }
public string CurrentRoomName
    {
        get
        {
            return currentRoomName;
        }

        set
        {
            currentRoomName = value;
        }
    }

    // Use this for initialization
#if (UNITY_2018_3_OR_NEWER && UNITY_ANDROID)
    private ArrayList permissionList = new ArrayList();
#endif
    // PLEASE KEEP THIS App ID IN SAFE PLACE
    // Get your own App ID at https://dashboard.agora.io/
    [SerializeField]
    public string AppID = "2768f353cebd4f64b00c88c28798e3f1";
       // PLEASE KEEP THIS App ID IN SAFE PLACE
    // 使用腾讯的小游戏平台
    //Mobile Game Online Battle Engin https://cloud.tencent.com/document/product/1038
    [SerializeField]
    public string gameID = "obg-myrpsb12";
    [SerializeField]
    public string gameSecret =  "981c73a2f4189d08debc1d2e285baea13be638fd";
    void Awake()
    {
        instance = this;
#if (UNITY_2018_3_OR_NEWER && UNITY_ANDROID)
		permissionList.Add(Permission.Microphone);         
		permissionList.Add(Permission.Camera);               
#endif
    }
    // Start is called before the first frame update
    void Start()
    {
        DontDestroyOnLoad(gameObject);
         CheckAppId();
        StartChatSDK();
        StartVideoSDK();
        //  LoadGame("Main");
    }
    public void LoadGame(string name)
    {
        SceneManager.LoadScene(name);
    }
    // Update is called once per frame
    void Update()
    {
  CheckPermissions();
    }
     private void CheckAppId()
    {
        Debug.Assert(AppID.Length > 10, "Please fill in your AppId first on Game Controller object.");
    }

    /// <summary>
    ///   Checks for platform dependent permissions.
    /// </summary>
    private void CheckPermissions()
    {
#if (UNITY_2018_3_OR_NEWER && UNITY_ANDROID)
        foreach(string permission in permissionList)
        {
            if (!Permission.HasUserAuthorizedPermission(permission))
            {                 
				Permission.RequestUserPermission(permission);
			}
        }
#endif
    }
    void StartVideoSDK()
    {
        VideoShareEngine.initEngine(AppID, OnLoasMediaSuccess);

    }
    public void OnLoasMediaSuccess()
    {
        ToastManager.instance.AddToast(ToastType.Info, "加载音视频模块成功");

    }
        void StartChatSDK()
    {
        //    AppendTxt("开始初始化\r\n");
        GameInfoPara gameInfo = new GameInfoPara
        {
            // 替换 为控制台上的“游戏ID”
            GameId =gameID,// 
            // 玩家 openId
            OpenId = UnityEngine.Random.Range(1, 10000).ToString(),
            //替换 为控制台上的“游戏Key”
            SecretKey =gameSecret
        };
        ConfigPara config = new ConfigPara
        {
            // 替换 为控制台上的“域名”
            Url = "myrpsb12.wxlagame.com",
            ReconnectMaxTimes = 5,
            ReconnectInterval = 1000,
            ResendInterval = 1000,
            ResendTimeout = 10000
        };
        // 初始化监听器 Listener
        Listener.Init(gameInfo, config, (ResponseEvent eve) =>
        {
            string res = "";
            if (eve.Code == 0)
            {
                Debug.Log("初始化成功 \r\n");
                      ChatOk = true;
                ToastManager.instance.AddToast(ToastType.Info, "初始化RTM 成功 ");
            }
            else
            {
                Debug.Log("初始化失败 \r\n");
                ChatOk = false;
                ToastManager.instance.AddToast(ToastType.Error, "初始化RTM 失败 ");
            }

        });
    }
void OnApplicationQuit()
{
    VideoShareEngine.leave();
      VideoShareEngine.unloadEngine();
}
}
