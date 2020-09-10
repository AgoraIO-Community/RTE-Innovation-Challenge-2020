using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using LitJson;
using System;

public class PainterClientController : MonoBehaviour
{

    [Header("Paint Canvas")]
    public Painter painterCanvas;
    public Material penMat;
    public Material canvasMat;
    public bool shouldActive = true;
    void Awake()
    {
        Material pen = Instantiate(penMat);
        pen.name = UnityEngine.Random.Range(10, 1000) + "pen";
        Material canMat = Instantiate(canvasMat);
        canMat.name = UnityEngine.Random.Range(10, 1000) + "canMat";
        painterCanvas.penMat = pen;
        painterCanvas.canvasMat = canMat;
        painterCanvas.GetComponent<RawImage>().material = canMat;
    }
    // Use this for initialization
    void Start()
    {
    }
    public void UpdatePara(JsonData json)
    {
        //        Debug.Log(json.ToJson());
        //    return;
        try
        {
            _drag = false;
            if (((IDictionary)json).Contains("ctrl"))
            {
                JsonData jsonData = json["ctrl"];

                if (jsonData == null)
                    return;
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
                }
                if (((IDictionary)jsonData).Contains("penAlpha"))
                {
                    _penAlpha = float.Parse(jsonData["penAlpha"].ToString());
                }
                if (((IDictionary)jsonData).Contains("brushScale"))
                {
                    _brushScale = float.Parse(jsonData["brushScale"].ToString());
                }
                if (((IDictionary)jsonData).Contains("drawLerpDamp"))
                {
                    _drawLerp = float.Parse(jsonData["drawLerpDamp"].ToString()) * 0.5f;
                }
                if (((IDictionary)jsonData).Contains("isErase"))
                {
                    _isEraser = bool.Parse(jsonData["isErase"].ToString());
                }
                if (((IDictionary)jsonData).Contains("paintType"))
                {
                    _paintType = int.Parse(jsonData["paintType"].ToString());
                }
                if (((IDictionary)jsonData).Contains("clean"))
                    _isClean = bool.Parse(jsonData["clean"].ToString());
            }
            if (((IDictionary)json).Contains("nrl"))
            {
                JsonData jsonData = json["nrl"];

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

                //                Debug.LogError("_click" + _click);
            }

        }
        catch (Exception e)
        {
            Debug.LogError(e.Message);
        }
    }
    bool _isRawImg = false;
    bool _isClean = false;
    string _rawName = "pen";
    bool _colorful = false;
    Color _penColor = Color.white;
    float _penAlpha = 1f;
    float _drawLerp = 0.5f;
    float _brushScale = 0.1f;
    bool _isEraser = false;
    int _paintType = 2;
    bool _click = false;
    bool _drag = false;
    Vector2 _currentPos;
    public void CopyPara(Hashtable table)
    {
        _rawName = table["_rawName"] as string;
        if (_rawName != "")
        {
            Texture tt = Resources.Load(_rawName) as Texture;
            if (tt == null)
                tt = Resources.Load("pen") as Texture;
            painterCanvas.penMat.mainTexture = tt;
            _rawName = "";
        }
        _penColor = (Color)table["_penColor"];
        painterCanvas.penColor = _penColor;
        _penAlpha = (float)table["_penAlpha"];
        //Set the pen alpha value.
        painterCanvas.penMat.SetFloat("_Alpha", _penAlpha);
        _brushScale = (float)table["_brushScale"];
        //pen size.
        painterCanvas.brushScale = _brushScale;

        _drawLerp = (float)table["_drawLerp"];
        //draw damp value.
        painterCanvas.drawLerpDamp = _drawLerp;
        _isEraser = (bool)table["_isEraser"];
        //is erase.
        painterCanvas.isErase = _isEraser;
        _paintType = (int)table["_paintType"];
        //draw line or draw colorful line.
        painterCanvas.paintType = (Painter.PaintType)_paintType;
    }
    public Hashtable GeneralPara()
    {
        Hashtable table = new Hashtable();
        table.Add("_rawName", _rawName);
        table.Add("_penColor", _penColor);
        table.Add("_penAlpha", _penAlpha);
        table.Add("_brushScale", _brushScale);
        table.Add("_drawLerp", _drawLerp);
        table.Add("_isEraser", _isEraser);
        table.Add("_paintType", _paintType);
        return table;
    }
    void FixedUpdate()
    {
        if (_rawName != "")
        {
            Texture tt = Resources.Load(_rawName) as Texture;
            if (tt == null)
                tt = Resources.Load("pen") as Texture;
            painterCanvas.penMat.mainTexture = tt;
            _rawName = "";
        }
        painterCanvas.penColor = _penColor;
        //Set the pen alpha value.
        painterCanvas.penMat.SetFloat("_Alpha", _penAlpha);
        //pen size.
        painterCanvas.brushScale = _brushScale;

        //draw damp value.
        painterCanvas.drawLerpDamp = _drawLerp;

        //is erase.
        painterCanvas.isErase = _isEraser;

        //draw line or draw colorful line.
        painterCanvas.paintType = (Painter.PaintType)_paintType;
        return;
    }

    // Update is called once per frame
    void Update()
    {
        try
        {
            if (_click)
                OnPointerClick();
            if (_drag)
                OnDrag();
            else
                OnEndDrag();
            if (_isClean)
            {
                OnClickClearCanvas();
                _isClean = false;
            }
        }
        catch (Exception e)
        {
            Debug.LogError(e.Message);
        }
        if (shouldActive)
            GetComponent<Canvas>().enabled = true;
        else
            GetComponent<Canvas>().enabled = false;
    }
    public void OnClickClearCanvas()
    {
        //clear the paint canvas.
        painterCanvas.ClearCanvas();
    }
    public void OnPointerClick()
    {

        //Draw once when mouse down.
        if (painterCanvas.isInited)
            painterCanvas.ClickDraw(_currentPos, null, painterCanvas.penMat.mainTexture, painterCanvas.brushScale, painterCanvas.penMat, painterCanvas.renderTexture, true);

    }



    public void OnEndDrag()
    {
        if (painterCanvas.isInited)
            painterCanvas.EndDraw();

    }

    public void OnDrag()
    {
        //        Debug.LogError("OnDrag" + name);
        if (painterCanvas.isInited)
            painterCanvas.Drawing(_currentPos, null, painterCanvas.renderTexture, false, true);
        //   else
        //      Debug.LogError("OnDrag" + name);
    }
}
