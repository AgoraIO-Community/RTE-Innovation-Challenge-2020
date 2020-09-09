// Upgrade NOTE: replaced 'mul(UNITY_MATRIX_MVP,*)' with 'UnityObjectToClipPos(*)'

Shader "Painter/Scribble Masked Shader"
{
	Properties
	{
		_SourceTex ("Source Tex", 2D) = "white" {}
		_RenderTex("Render Tex",2D) = "white" {} //mask
		_MaskTex("Mask Tex",2D) = "white" {} //mask
		_Color("Color",Color)=(1,1,1,1)
		_Alpha("Alpha",Range(0,1))=1
		_Cutoff("Alpha cutoff",Range(0,1))=0
		[Enum(UnityEngine.Rendering.BlendMode)] _BlendSrc("Src Factor",float)=5
		[Enum(UnityEngine.Rendering.BlendMode)] _BlendDst("Dst Factor",float)=10
		[Enum(UnityEngine.Rendering.CullMode)] _CullMode("Cull Mode",float)=0
	}
	SubShader
	{
		Tags { "RenderType"="Transparent" "Queue"="Transparent" "IgnoreProjector"="True"}
		LOD 100
		Cull [_CullMode]
		ZTest Always
		ZWrite off
		Blend [_BlendSrc] [_BlendDst]

		Pass
		{
			CGPROGRAM
			#pragma vertex vert
			#pragma fragment frag
			
			#include "UnityCG.cginc"

			struct appdata
			{
				float4 vertex : POSITION;
				float2 uv : TEXCOORD0;
			};

			struct v2f
			{
				float2 uv : TEXCOORD0;
				UNITY_FOG_COORDS(1)
				float4 vertex : SV_POSITION;
			};

			sampler2D _SourceTex;
			float4 _SourceTex_ST;
			sampler2D _RenderTex;
			fixed4 _Color;
			fixed _Alpha;
			fixed _Cutoff;
			sampler2D _MaskTex;

			v2f vert (appdata v)
			{
				v2f o;
				o.vertex = UnityObjectToClipPos(v.vertex);
				o.uv = TRANSFORM_TEX(v.uv, _SourceTex);
				return o;
			}
			
			fixed4 frag (v2f i) : SV_Target
			{
				// sample the texture
				fixed4 col = tex2D(_SourceTex, i.uv);
				col.rgb*=_Color.rgb;
				col.a*=_Alpha;

				fixed4 mask = tex2D (_RenderTex,i.uv );
//				col.a*=mask.a;
				col.a = min(col.a,mask.a);

//				col.a *= tex2D(_MaskTex,i.uv).a;
				col.a = min(col.a,tex2D(_MaskTex,i.uv).a);


				clip(col.a-_Cutoff);

				return col;
			}
			ENDCG
		}
	}
}
