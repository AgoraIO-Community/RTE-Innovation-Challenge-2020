using UnityEngine;

[ExecuteInEditMode]
public class SetAutoOrthographicSize : MonoBehaviour
{
    void Update()
    {
        if (GetComponent<Camera>())
        {
            GetComponent<Camera>().orthographicSize = transform.lossyScale.y * 5.0f;
        }
    }
}