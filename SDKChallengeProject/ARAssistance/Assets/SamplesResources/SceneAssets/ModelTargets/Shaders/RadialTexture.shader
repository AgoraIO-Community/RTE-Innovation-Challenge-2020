// Upgrade NOTE: replaced 'mul(UNITY_MATRIX_MVP,*)' with 'UnityObjectToClipPos(*)'

Shader "Custom/RadialTexture" {
    Properties{
        _Color("Base Color", Color) = (1.0,1.0,1.0,1.0)
        _MainTex("Base (RGBA)", 2D) = "white" {}
    }

    SubShader {
      Tags { "Queue" = "Transparent" "RenderType" = "Transparent" }

        Pass {
          ZWrite Off
          Cull Off
          Blend SrcAlpha OneMinusSrcAlpha

          CGPROGRAM

          #pragma vertex vert
          #pragma fragment frag

          #include "UnityCG.cginc"

          sampler2D _MainTex;
          float4 _Color;
          
          struct v2f {
              float4  pos : SV_POSITION;
              float2  uv : TEXCOORD0;
          };

          float4 _MainTex_ST;

          v2f vert(appdata_base v)
          {
               v2f o;
               o.pos = UnityObjectToClipPos(v.vertex);
               o.uv = TRANSFORM_TEX(v.texcoord, _MainTex);
               return o;
          }


          half4 frag(v2f i) : COLOR
          {
              float r = 2.0 * length(i.uv - float2(0.5, 0.5));
              float alpha = 1.0 - r;
              half4 c = _Color * tex2D(_MainTex, i.uv);
              c.a *= alpha;
              return c;
          }

          ENDCG
        }
    }

    FallBack "Diffuse"
}

