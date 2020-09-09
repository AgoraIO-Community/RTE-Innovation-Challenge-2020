// Upgrade NOTE: replaced 'mul(UNITY_MATRIX_MVP,*)' with 'UnityObjectToClipPos(*)'

// Upgrade NOTE: replaced '_Object2World' with 'unity_ObjectToWorld'

// Upgrade NOTE: replaced '_Object2World' with 'unity_ObjectToWorld'

Shader "Unlit/SpecularFrag"
{
	Properties
	{
		_Diffuse("_Diffuse", Color) = (1.0,1.0,1.0,1.0)
		_Specular("Specular", Color) = (1.0,1.0,1.0,1.0)
		_Gloss("Gloss", Range(8,256)) = 20
		_Alpha("Alpha", Range(0,1.0)) = 1.0
	}
	SubShader
	{
		Tags {"Queue"="Transparent"  "RenderType"="Opaque"}
		LOD 300
		ZTest Always
		Pass
		{
			Tags { "LightMode"="ForwardBase" }

			//Blend SrcAlpha OneMinusSrcAlpha
			//Cull Front
			CGPROGRAM
			#pragma vertex vert
			#pragma fragment frag
			#include "UnityCG.cginc"
			#include "Lighting.cginc"
			struct a2v
			{
				float4 vertex : POSITION;
				float3 normal : NORMAL;
			};

			struct v2f
			{
				float4 vertex : SV_POSITION;
				float3 worldNormal : TEXCOORD0;
				float3 worldPos : TEXCOORD1;
			};

			fixed4 _Diffuse;
			fixed4 _Specular;
			float _Gloss;
			float _Alpha;
			v2f vert (a2v v)
			{
				v2f o;
				o.vertex = UnityObjectToClipPos(v.vertex);
				o.worldNormal = UnityObjectToWorldNormal(v.normal);
				o.worldPos = mul(unity_ObjectToWorld,v.vertex).xyz;
				return o;
			}
			
			fixed4 frag (v2f i) : SV_Target
			{
				

				//environment light color
				fixed3 ambient = UNITY_LIGHTMODEL_AMBIENT.xyz;

				//environment light direction
				fixed3 worldLightDir = normalize(_WorldSpaceLightPos0.xyz);

				fixed3 worldNormal = normalize(i.worldNormal);
				//calculate the lambert for diffuse
				fixed halfLambert = dot(worldNormal,worldLightDir)*0.5 + 0.5;
				//calculate diffuse
				fixed3 diffuse = _LightColor0.rgb * _Diffuse.rgb * halfLambert;
				//fixed3 diffuse = _LightColor0.rgb * _Diffuse.rgb * saturate( dot(worldNormal,worldLightDir));
				//
				fixed3 reflectDir = normalize(reflect(-worldLightDir,worldNormal));
				//
				fixed3 viewDir = normalize(_WorldSpaceCameraPos.xyz -i.worldPos.xyz);

				fixed3 halfDir = normalize(worldLightDir + viewDir);

				//
				//fixed3 specular = _LightColor0.rgb * _Specular.rgb * pow(saturate(dot(reflectDir,viewDir)), _Gloss);
				fixed3 specular = _LightColor0.rgb * _Specular.rgb * pow(max(0,dot(worldNormal,halfDir)), _Gloss);

				return fixed4(ambient + diffuse + specular, _Alpha);

			}
			ENDCG
		}

		Pass
		{
			Tags { "LightMode"="ForwardBase" }
			//ZWrite Off
			//Blend SrcAlpha OneMinusSrcAlpha
			//Cull Back
			CGPROGRAM
			#pragma vertex vert
			#pragma fragment frag
			#include "UnityCG.cginc"
			#include "Lighting.cginc"
			struct a2v
			{
				float4 vertex : POSITION;
				float3 normal : NORMAL;
			};

			struct v2f
			{
				float4 vertex : SV_POSITION;
				float3 worldNormal : TEXCOORD0;
				float3 worldPos : TEXCOORD1;
			};

			fixed4 _Diffuse;
			fixed4 _Specular;
			float _Gloss;
			float _Alpha;
			v2f vert (a2v v)
			{
				v2f o;
				o.vertex = UnityObjectToClipPos(v.vertex);
				o.worldNormal = UnityObjectToWorldNormal(v.normal);
				o.worldPos = mul(unity_ObjectToWorld,v.vertex).xyz;
				return o;
			}
			
			fixed4 frag (v2f i) : SV_Target
			{
				

				//environment light color
				fixed3 ambient = UNITY_LIGHTMODEL_AMBIENT.xyz;

				//environment light direction
				fixed3 worldLightDir = normalize(_WorldSpaceLightPos0.xyz);

				fixed3 worldNormal = normalize(i.worldNormal);
				//calculate the lambert for diffuse
				fixed halfLambert = dot(worldNormal,worldLightDir)*0.5 + 0.5;
				//calculate diffuse
				fixed3 diffuse = _LightColor0.rgb * _Diffuse.rgb * halfLambert;
				//fixed3 diffuse = _LightColor0.rgb * _Diffuse.rgb * saturate( dot(worldNormal,worldLightDir));
				//
				fixed3 reflectDir = normalize(reflect(-worldLightDir,worldNormal));
				//
				fixed3 viewDir = normalize(_WorldSpaceCameraPos.xyz -i.worldPos.xyz);

				fixed3 halfDir = normalize(worldLightDir + viewDir);

				//
				//fixed3 specular = _LightColor0.rgb * _Specular.rgb * pow(saturate(dot(reflectDir,viewDir)), _Gloss);
				fixed3 specular = _LightColor0.rgb * _Specular.rgb * pow(max(0,dot(worldNormal,halfDir)), _Gloss);

				return fixed4(ambient + diffuse + specular, _Alpha);

			}
			ENDCG
		}
	}
}
