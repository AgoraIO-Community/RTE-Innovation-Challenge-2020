Shader "RegionCapture/SimpleColor" {
Properties {
	[NoScaleOffset] _MainTex("Texture", 2D) = "white" {}
	_Color ("Main Color", Color) = (1,1,1,1)
	[Space(20)][Toggle] _transoutofbounds("Set transparent if out of bounds", Float) = 0
}

SubShader {
	Tags {"Queue"="Transparent" "IgnoreProjector"="True" "RenderType"="Transparent"}
	LOD 100
	
	ZWrite On
	Blend SrcAlpha OneMinusSrcAlpha 
	
	Pass {
		CGPROGRAM
			#pragma vertex vert
			#pragma fragment frag
			
			#include "UnityCG.cginc"

			struct appdata_t {
				float4 vertex : POSITION;
				float2 texcoord : TEXCOORD1;
			};

			struct v2f {
				float4 vertex : SV_POSITION;
				half2 texcoord : TEXCOORD1;
			};

			sampler2D _MainTex;
			float4 _MainTex_ST;
			float _transoutofbounds;

			uniform half4 _Color;
			
			v2f vert (appdata_t v)
			{
				v2f o;
				o.vertex = UnityObjectToClipPos(v.vertex);
				o.texcoord = TRANSFORM_TEX(v.texcoord, _MainTex);
				return o;
			}
			
			fixed4 frag (v2f i) : SV_Target
			{
				half4 col = tex2D(_MainTex, i.texcoord);

				col *= _Color;
				if (_transoutofbounds > 0)
					if (i.texcoord.x < 0 || i.texcoord.x > 1 || i.texcoord.y < 0 || i.texcoord.y > 1) { col.a = 0; }
					else col.a = 1;

				return col;
			}
		ENDCG
		}
	}
}