Shader "Depth Mask2" {
	Properties {
	        _MainColor ("Main Color", Color) = (1, 1, 1, 1)
	        _MainTex ("Base (RGB)", 2D) = "white" {}
	        _Alpha ("Alpha", Range(0, 1)) = 0.5
	    }
    SubShader {
        Tags {"Queue" = "Geometry-10" }       
        Lighting Off
        ZTest LEqual
        ZWrite On
        ColorMask 0
        Pass {}
    }



    SubShader {
        Tags { "RenderType"="Opaque" }
        LOD 200

        CGPROGRAM
        #pragma surface surf Lambert alpha

        fixed4 _MainColor;
        sampler2D _MainTex;
        fixed _Alpha;

        struct Input {
            float2 uv_MainTex;
        };

        void surf (Input IN, inout SurfaceOutput o) {
            half4 c = tex2D (_MainTex, IN.uv_MainTex);
            o.Albedo = _MainColor.rgb * c.rgb;
            o.Alpha = _Alpha;
        }
        ENDCG
    }
}