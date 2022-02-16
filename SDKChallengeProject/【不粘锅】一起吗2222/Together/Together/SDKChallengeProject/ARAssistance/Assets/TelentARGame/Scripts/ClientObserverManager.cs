using System.Collections;
using System.Collections.Generic;
using agora_gaming_rtc;
using UnityEngine;
using UnityEngine.UI;
public class ClientObserverManager : MonoBehaviour
{
    
        public Transform bkaVideoParent;
    public List<GameObject> allObserver;
    public GameObject prefab;
    public GameObject bigPic;
    GameObject bigPicTemp;
    bool isExpert = false;
    bool tempExpert = false;
    //    public Vector2 mas;
    public Vector2 mis;
    // Start is called before the first frame update
    void Start()
    {
        //   allObserver = new List<GameObject>();

    }

    public void AddObserver(uint uid)
    {

        GameObject go = Instantiate(prefab);
        go.name = uid.ToString();
        go.transform.parent = transform;
        go.transform.localScale = new Vector3(1, -1, 1);// Vector3.one;
        go.GetComponent<Button>().onClick.AddListener(delegate
        {
            VideoImageClicked(go.name);
        });
        // set up transform
        VideoSurface videoSurface = go.AddComponent<VideoSurface>();
        // configure videoSurface
        videoSurface.SetForUser(uid);
        videoSurface.SetEnable(true);
        videoSurface.SetVideoSurfaceType(AgoraVideoSurfaceType.RawImage);
        videoSurface.SetGameFps(25);
        allObserver.Add(go);
        //更新大小
        UpdateSelfSize(allObserver.Count);
    }
    public void SetStaffVideoType(bool expert)
    {
//           Debug.Log(expert+"||");
        tempExpert = expert;
    }
    void VideoImageClicked(string name)
    {
        if (bigPicTemp != null)
        {
            if (bigPicTemp.name == name)
            {
                removeObserver(uint.Parse(name));
                AddObserver(uint.Parse(name));
            }
            RemoveBig();
        }
        else
        {
            GameObject go = null;
            for (int i = 0; i < allObserver.Count; i++)
            {
                if (allObserver[i].name == name)
                {
                    go = allObserver[i];
                    break;
                }
                Debug.Log(name);
            }
            if (go != null)
            {
                VideoSurface video1 = go.GetComponent<VideoSurface>();
                DestroyImmediate(video1);
            }

            bigPicTemp = Instantiate(bigPic);
            bigPicTemp.name = name;
            bigPicTemp.transform.parent =bkaVideoParent;
            bigPicTemp.GetComponent<RectTransform>().SetAsFirstSibling();
            bigPicTemp.transform.localScale = new Vector3(1, -1, 1);
            bigPicTemp.SetActive(true);
            VideoSurface videoSurface = bigPicTemp.AddComponent<VideoSurface>();
            videoSurface.resize = true;
            videoSurface.expert = isExpert;
            // configure videoSurface
            videoSurface.SetForUser(uint.Parse(name));
            videoSurface.SetEnable(true);
            videoSurface.SetVideoSurfaceType(AgoraVideoSurfaceType.RawImage);
            videoSurface.SetGameFps(25);
        }
    }
    public void RemoveBig()
    {
        if (bigPicTemp != null)
        {
            Destroy(bigPicTemp);
        }


    }
    void UpdateSelfSize(int count)
    {
        RectTransform rectT = GetComponent<RectTransform>();
        //0,50
        //-100,-300
        rectT.offsetMax = Vector2.zero;
        rectT.offsetMin = new Vector2(-150 * count, -200);
    }
    public void removeObserver(uint uid)
    {
        GameObject go = null;
        for (int i = 0; i < allObserver.Count; i++)
        {
            if (allObserver[i].name == uid.ToString())
            {
                go = allObserver[i];
                break;
            }
            Debug.Log(uid.ToString());
        }
        if (go != null)
        {
            allObserver.Remove(go);
            DestroyImmediate(go);
        }
        if (bigPicTemp != null && uid + "" == bigPicTemp.name)
            RemoveBig();
        //更新大小
        UpdateSelfSize(allObserver.Count);
    }
    // Update is called once per frame
    void Update()
    {
     //   if (isExpert != tempExpert)
     //   {
            isExpert = tempExpert;
            if (bigPicTemp == null)
                return;
            bigPicTemp.GetComponent<VideoSurface>().expert = isExpert;

       // }

    }
}
