using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class LoadSceneNext : MonoBehaviour
{
    public string sceneName="";
    public bool startToLoad=false;
    // Start is called before the first frame update
    void Start()
    {
        if(sceneName!=""&&startToLoad)
        LoadNexd(sceneName);
    }
public void LoadNexd(string name){
SceneManager.LoadScene(name);
}
    // Update is called once per frame
    void Update()
    {
        
    }
}
