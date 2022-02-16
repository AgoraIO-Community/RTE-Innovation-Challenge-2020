// Upgrade NOTE: replaced 'mul(UNITY_MATRIX_MVP,*)' with 'UnityObjectToClipPos(*)'

Shader "Painter/Draw Line Shader - UGUI"
{
	Properties
	{
		_MainTex ("Sprite Texture", 2D) = "white" {}
		_MaskTex ("Mask Texture", 2D) = "white" {}
		_Color("Color",Color)=(1,1,1,1)
		_Alpha("Alpha",Range(0,1))=1
		[Enum(UnityEngine.Rendering.BlendMode)] _BlendSrc("Src Factor",float)=5
		[Enum(UnityEngine.Rendering.BlendMode)] _BlendDst("Dst Factor",float)=10
		[Enum(UnityEngine.Rendering.BlendMode)] _FactorA("Factor A",float)=1
		[Enum(UnityEngine.Rendering.BlendMode)] _FactorB("Factor B",float)=10
		[Enum(UnityEngine.Rendering.CullMode)] _CullMode("Cull Mode",float)=0

		_StencilComp ("Stencil Comparison", Float) = 8
		_Stencil ("Stencil ID", Float) = 0
		_StencilOp ("Stencil Operation", Float) = 0
		_StencilWriteMask ("Stencil Write Mask", Float) = 255
		_StencilReadMask ("Stencil Read Mask", Float) = 255
	}
	SubShader
	{
		Tags
		{ 
			"Queue"="Transparent" 
			"IgnoreProjector"="True" 
			"RenderType"="Transparent" 
			"PreviewType"="Plane"
			"CanUseSpriteAtlas"="True"
		}
		Stencil
		{
			Ref [_Stencil]
			Comp [_StencilComp]
			Pass [_StencilOp] 
			ReadMask [_StencilReadMask]
			WriteMask [_StencilWriteMask]
		}

		LOD 100
		Cull [_CullMode]
		ZTest Always
		Lighting Off
		ZWrite off
		ZTest [unity_GUIZTestMode]
		Blend [_BlendSrc] [_BlendDst] , [_FactorA] [_FactorB]
	
		Pass
		{
			CGPROGRAM
			#pragma vertex vert
			#pragma fragment frag
			
			#include "UnityCG.cginc"
			#include "UnityUI.cginc"

			struct appdata
			{
				float4 vertex : POSITION;
				float2 uv : TEXCOORD0;
				float4 color    : COLOR;
			};

			struct v2f
			{
				fixed4 color    : COLOR;
				float2 uv : TEXCOORD0;
				float2 uv1 : TEXCOORD1;
				float4 worldPosition : TEXCOORD2;
				float4 vertex : SV_POSITION;
			};

			sampler2D _MainTex;
			float4 _MainTex_ST;
			sampler2D _MaskTex;
			float4 _MaskTex_ST;
			fixed4 _Color;
			fixed _Alpha;
			fixed4 _TextureSampleAdd;
			float4 _ClipRect;

			v2f vert (appdata v)
			{
				v2f o;
				o.vertex = UnityObjectToClipPos(v.vertex);
				o.uv = TRANSFORM_TEX(v.uv, _MainTex);
				o.uv1 = TRANSFORM_TEX(v.uv, _MaskTex);
				o.worldPosition = v.vertex;
				o.color = v.color * _Color;
				return o;
			}
			
			fixed4 frag (v2f i) : SV_Target
			{
				// sample the texture
				half4 col = (tex2D(_MainTex, i.uv) + _TextureSampleAdd) * i.color;
				col.a *= UnityGet2DClipping(i.worldPosition.xy, _ClipRect);
				
				fixed maskA = tex2D(_MaskTex, i.uv1).a*_Alpha;
				col *= maskA;

				return col;
			}
			ENDCG
		}
	}
}
