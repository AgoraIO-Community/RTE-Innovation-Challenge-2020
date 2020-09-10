Shader "Depth Mask" {
    SubShader {
        Tags {"Queue" = "Background+10" }       
        Lighting Off
        ZTest LEqual
        ZWrite On
        ColorMask 0
        Pass {}
    }
}
