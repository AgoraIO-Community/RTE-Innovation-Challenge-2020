using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using LitJson;
using System;

public class PainterExpertController : MonoBehaviour
{
    static int queueLENG = 300;
    // public RawImage rawImg;
    [Header("Setting")]
    public ToggleGroup leftBar;
    public ToggleGroup rightBar;
    public Slider penScaleBar;
    public Slider penAlphaBar;
    public Slider penLerp;
    public Toggle isEarseBar;
    public Toggle isColorfulBar;

    [Header("Paint Canvas")]
    public Painter painterCanvas;
    public bool shouldActive = true;

    private bool _mouseIsDown = false;
    public Camera cam;
    public Material penMat;
    public Material canvasMat;
    bool controlMsg = false;
    Queue<JsonData> readyCtrlMsg = new Queue<JsonData>(queueLENG);
    Queue<JsonData> readyNormalMsg = new Queue<JsonData>(queueLENG);
    Vector2 currentPos;
    string lastMessage = "";
    public bool ControlMsg
    {
        get
        {
            return controlMsg;
        }

        set
        {
            controlMsg = value;
        }
    }

    // private RenderTexture _rt;

    void Awake()
    {
        Material pen = Instantiate(penMat);
        pen.name = UnityEngine.Random.Range(10, 1000) + "pen";
        Material canMat = Instantiate(canvasMat);
        canMat.name = UnityEngine.Random.Range(10, 1000) + "canMat";
        painterCanvas.penMat = pen;
        painterCanvas.canvasMat = canMat;
        painterCanvas.GetComponent<RawImage>().material = canMat;
        //   painterCanvas.Init();
    }
    // Use this for initialization
    void Start()
    {
        /*  painterCanvas.gameObject.SetActive(true);
          _rt = new RenderTexture(painterCanvas.renderTexWidth, painterCanvas.renderTexHeight, 0, RenderTextureFormat.ARGB32);
          _rt.useMipMap = false;
          if (rawImg.texture != null)
          {
              Graphics.SetRenderTarget(_rt);
              Graphics.Blit(rawImg.texture, _rt);
              RenderTexture.active = null;
          }

          //set mask
          drawOtherRtMat.SetTexture("_MaskTex", rawImg.texture);
          painterCanvas.canvasMat.SetTexture("_MaskTex", rawImg.texture);

          //use rendertexture to replace.
          rawImg.texture = _rt;*/

    }
    public void UpdatePara(string data)
    {
        //        Debug.Log(data);
        try
        {
            JsonData jsonData = JsonMapper.ToObject(data);
            if (((IDictionary)jsonData).Contains("isRawImg"))
            {
                _isRawImg = bool.Parse(jsonData["isRawImg"].ToString());
            }
            if (((IDictionary)jsonData).Contains("rawName"))
            {
                _rawName = jsonData["rawName"].ToString();
            }
            if (((IDictionary)jsonData).Contains("isColorful"))
            {
                _colorful = bool.Parse(jsonData["isColorful"].ToString());
            }
            if (((IDictionary)jsonData).Contains("penColor"))
            {
                float r, g, b, a;
                float.TryParse(jsonData["penColor"][0].ToString(), out r);
                float.TryParse(jsonData["penColor"][1].ToString(), out g);
                float.TryParse(jsonData["penColor"][2].ToString(), out b);
                float.TryParse(jsonData["penColor"][3].ToString(), out a);

                _penColor = new Color(r, g, b, a);
                Debug.LogError(_penColor.ToString());
            }
            if (((IDictionary)jsonData).Contains("penAlpha"))
            {
                _penAlpha = float.Parse(jsonData["penAlpha"].ToString());
            }
            if (((IDictionary)jsonData).Contains("brushScale"))
            {
                jsonData["brushScale"] = penScaleBar.value;
                _brushScale = float.Parse(jsonData["brushScale"].ToString());
            }
            if (((IDictionary)jsonData).Contains("drawLerpDamp"))
            {
                _drawLerp = float.Parse(jsonData["drawLerpDamp"].ToString());
            }
            if (((IDictionary)jsonData).Contains("isErase"))
            {
                _isEraser = bool.Parse(jsonData["isErase"].ToString());
            }
            if (((IDictionary)jsonData).Contains("paintType"))
            {
                _paintType = int.Parse(jsonData["paintType"].ToString());
            }
            if (((IDictionary)jsonData).Contains("currentPos"))
            {
                float x, y;
                float.TryParse(jsonData["currentPos"][0].ToString(), out x);
                float.TryParse(jsonData["currentPos"][1].ToString(), out y);
                _currentPos = new Vector2(x, y);

            }
            if (((IDictionary)jsonData).Contains("click"))
                _click = bool.Parse(jsonData["click"].ToString());
            if (((IDictionary)jsonData).Contains("drag"))
                _drag = bool.Parse(jsonData["drag"].ToString());
        }
        catch (Exception e)
        {
            Debug.LogError(e.Message);
        }




    }
    bool _isRawImg = false;
    bool _isClean = false;
    string _rawName = "";
    bool _colorful = false;
    Color _penColor = Color.white;
    float _penAlpha = 0;
    float _drawLerp = 0;
    float _brushScale = 0;
    bool _isEraser = false;
    int _paintType = 0;
    bool _click = false;
    bool _drag = false;
    Vector2 _currentPos;
    void FixedUpdate()
    {

        bool isRawImg = false;
        string rawName = "";
        foreach (Toggle t in leftBar.ActiveToggles())
        {
            //Set pen texture.
            painterCanvas.penMat.mainTexture = t.GetComponent<Image>().sprite.texture;
            PenSetting ps = t.GetComponent<PenSetting>();
            rawName = painterCanvas.penMat.mainTexture.name;
            //                 Debug.Log(rawName);
            //save raw img flag.
            isRawImg = ps.isRawImg;
            break;
        }

        if (!isColorfulBar.isOn)
        {
            //Set the pen color if is not colorfull stuatus.
            if (isRawImg || isColorfulBar.isOn)
            {
                painterCanvas.penColor = Color.white;
            }
            else
            {
                foreach (Toggle t in rightBar.ActiveToggles())
                {
                    //                    Debug.Log("sss");
                    painterCanvas.penColor = t.GetComponent<Image>().color;
                    break;
                }
            }
        }

        //Set the pen alpha value.
        painterCanvas.penMat.SetFloat("_Alpha", penAlphaBar.value);

        //pen size.
        painterCanvas.brushScale = penScaleBar.value;

        //draw damp value.
        painterCanvas.drawLerpDamp = penLerp.value;

        //is erase.
        painterCanvas.isErase = isEarseBar.isOn;

        //draw line or draw colorful line.
        painterCanvas.paintType = isColorfulBar.isOn ? Painter.PaintType.DrawColorfulLine : Painter.PaintType.DrawLine;

        JsonData json = new JsonData();
        bool sendCtrl = false;
        bool sendNormal = false;
        if (_isRawImg != isRawImg)
        {
            //     json["isRawImg"] = isRawImg;
            _isRawImg = isRawImg;
            sendCtrl = true;
        }
        if (_rawName != rawName)
        {
            //     json["rawName"] = rawName;
            _rawName = rawName;
            sendCtrl = true;
        }
        if (_colorful != isColorfulBar.isOn)
        {
            //    json["isColorful"] = isColorfulBar.isOn;
            _colorful = isColorfulBar.isOn;
            sendCtrl = true;
        }
        if (_penColor.ToString() != painterCanvas.penColor.ToString())
        {
            _penColor = painterCanvas.penColor;
            sendCtrl = true;
        }
        if (_penAlpha != penAlphaBar.value)
        {
            //    json["penAlpha"] = penAlphaBar.value;
            _penAlpha = penAlphaBar.value;
            sendCtrl = true;
        }
        if (_brushScale != penScaleBar.value)
        {
            //        json["brushScale"] = penScaleBar.value;
            _brushScale = penScaleBar.value;
            sendCtrl = true;
        }
        if (_drawLerp != penLerp.value)
        {
            //    json["drawLerpDamp"] = penLerp.value;
            _drawLerp = penLerp.value;
            sendCtrl = true;
        }
        if (_isEraser != isEarseBar.isOn)
        {
            //       json["isErase"] = isEarseBar.isOn;
            _isEraser = isEarseBar.isOn;
            sendCtrl = true;
        }
        if (_paintType != (int)painterCanvas.paintType)
        {
            _paintType = (int)painterCanvas.paintType;
            //       json["paintType"] = (int)painterCanvas.paintType;
            sendCtrl = true;
        }
        //ctrl 当数据有变化或者没30帧同步一次
        if (sendCtrl || Time.frameCount % 10 == 0)
        {
            json["isRawImg"] = _isRawImg;
            json["rawName"] = _rawName;
            json["isColorful"] = isColorfulBar.isOn;
            json["penColor"] = new JsonData();
            json["penColor"].Add(0);
            json["penColor"][0] = _penColor.r;
            json["penColor"].Add(1);
            json["penColor"][1] = _penColor.g;
            json["penColor"].Add(2);
            json["penColor"][2] = _penColor.b;
            json["penColor"].Add(3);
            json["penColor"][3] = _penColor.a;
            json["penAlpha"] = _penAlpha;
            json["brushScale"] = _brushScale;
            json["drawLerpDamp"] = _drawLerp;
            json["isErase"] = _isEraser;
            json["paintType"] = _paintType;
            json["clean"] = _isClean;
            if (readyCtrlMsg.Count >= queueLENG)
                readyCtrlMsg.Dequeue();
            readyCtrlMsg.Enqueue(json);
        }
        //
        json = new JsonData();
        if (_currentPos != currentPos)
        {
            sendNormal = true;
            json["currentPos"] = new JsonData();
            json["currentPos"].Add(0);
            json["currentPos"][0] = currentPos.x;//*1024f/Screen.height;
            json["currentPos"].Add(1);
            json["currentPos"][1] = currentPos.y;//*1024f/Screen.height;
            _currentPos = currentPos;
        }
        json["click"] = _click;
        json["drag"] = _drag;
        if (sendNormal)
        {
            if (readyNormalMsg.Count >= queueLENG)
                readyNormalMsg.Dequeue();
            readyNormalMsg.Enqueue(json);
        }

    }
    public JsonData GetSendData()
    {
        if (readyNormalMsg.Count > 0)
            return readyNormalMsg.Dequeue();
        else
            return null;
    }
    public JsonData GetCtrlData()
    {
        if (readyCtrlMsg.Count > 0)
            return readyCtrlMsg.Dequeue();
        else
            return null;
    }
    // Update is called once per frame
    void Update()
    {
        //        Debug.Log(readyNormalMsg.Count);
        if (shouldActive)
            GetComponent<Canvas>().enabled = true;
        else
            GetComponent<Canvas>().enabled = false;
    }
    public void OnClickClearCanvas()
    {
        //clear the paint canvas.
        painterCanvas.ClearCanvas();
        _isClean = true;
        //        Debug.LogError("_isClean"+_isClean);
        //保持
        Invoke("StayClean", 0.5f);
    }
    void StayClean()
    {
        _isClean = false;
    }
    public void OnPointerDown()
    {
        _click = true;

    }
    public void OnPointerUp()
    {
        _click = false;
        _drag = false;

    }
    public void OnPointerClick()
    {

        Vector2 pos;
        if (RectTransformUtility.ScreenPointToLocalPointInRectangle(painterCanvas.transform as RectTransform, Input.mousePosition, cam, out pos))
        {
            //Draw once when mouse down.
            painterCanvas.ClickDraw(pos, null, painterCanvas.penMat.mainTexture, painterCanvas.brushScale, painterCanvas.penMat, painterCanvas.renderTexture, true);

            currentPos = pos;
            _click = true;
        }
    }


    public void OnEndDrag()
    {
        painterCanvas.EndDraw();

        _click = false;
        _drag = false;
    }

    public void OnDrag()
    {

        Vector2 pos;
        //draw on mouse drag.
        if (RectTransformUtility.ScreenPointToLocalPointInRectangle(painterCanvas.transform as RectTransform, Input.mousePosition, cam, out pos))
        {

            painterCanvas.Drawing(pos, null, painterCanvas.renderTexture, false, true);
            currentPos = pos;
            _click = false;
            _drag = true;
        }
    }
}
