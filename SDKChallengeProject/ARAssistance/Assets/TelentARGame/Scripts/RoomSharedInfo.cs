using System.Collections;
using System.Collections.Generic;
using com.unity.mgobe;
using UnityEngine;
using UnityEngine.UI;
using ZXing;//引入库  
using ZXing.QrCode; 
public class RoomSharedInfo : MonoBehaviour
{
    Room room = null;
    public RawImage qrImg;
    public Text roomId;
    public Text roomName;
    public Text numPeople;
    public Text currentPle;
    bool changed = false;
    Texture2D qrCode;
    // Start is called before the first frame update
    void Start()
    {
qrCode=new Texture2D(256,256);
    }
    public void SetUpInfo(Room r)
    {
        if (room != r)
        {
            room = r;
            changed = true;
        }

    }
    // Update is called once per frame
    void Update()
    {
        if (changed)
        {
            roomId.text = room.RoomInfo.Id;
            currentPle.text = room.RoomInfo.PlayerList.Count + "";
            numPeople.text = room.RoomInfo.MaxPlayers + "";
            roomName.text = room.RoomInfo.Name;
            changed=false;
            string message=room.RoomInfo.Id+"#"+room.RoomInfo.MaxPlayers+"#"+room.RoomInfo.PlayerList.Count;
             //二维码写入图片  
            var color32 = Encode(message, qrCode.width, qrCode.height);  
            qrCode.SetPixels32(color32);  
            qrCode.Apply();  
            //生成的二维码图片附给RawImage  
            qrImg.texture = qrCode;  
        }

    }
     //定义方法生成二维码  
    private static Color32[] Encode(string textForEncoding, int width, int height)  
    {  
        var writer = new BarcodeWriter  
        {  
            Format = BarcodeFormat.QR_CODE,  
            Options = new QrCodeEncodingOptions  
            {  
                Height = height,  
                Width = width  ,
                Margin = 1
            }  
        };  
        return writer.Write(textForEncoding);  
    }  
  
  

}
