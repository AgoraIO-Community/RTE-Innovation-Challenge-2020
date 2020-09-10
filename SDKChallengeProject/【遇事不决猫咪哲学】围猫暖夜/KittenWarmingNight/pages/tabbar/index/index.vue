<template>
	<view class="content">
		<view>
			<swiper class="swiper" :indicator-dots="indicatorDots" :indicator-active-color="color" :autoplay="autoplay"
			 :circular="circular">
				<swiper-item v-for="item in images">
					<image :src="item.url" mode="aspectFill" />
				</swiper-item>
			</swiper>
		</view>
		<view class="option">
			<view class="option-box">
				<button type="default" plain="true" @click="navigate1">
					<image :src="image1" mode="aspectFill"></image>
				</button>
			</view>
			<view class="option-title-contain">
				<text class="option-title" style="margin:120rpx 100rpx 100rpx 10rpx;">语海寻真</text>
			</view>

			<view class="option-title-contain">
				<text class="option-title" style="margin:120rpx 10rpx 100rpx 100rpx;">私语空间</text>
			</view>

			<view class="option-box">
				<button type="default" plain="true" @click="navigate2">
					<image :src="image2" mode="aspectFill"></image>
				</button>
			</view>
		</view>
	</view>
	</view>
</template>

<script>
	import yesapi from '@/lib/yes3';
	export default {
		data() {
			return {
				circular: true,
				color: "#666",
				indicatorDots: true,
				autoplay: false,
				images: [{
						url: 'https://vkceyugu.cdn.bspapp.com/VKCEYUGU-wangshanyw/2fe30510-e87b-11ea-81ea-f115fe74321c.jpg'
					},
					{
						url: 'https://vkceyugu.cdn.bspapp.com/VKCEYUGU-wangshanyw/2ff1f930-e87b-11ea-8bd0-2998ac5bbf7e.jpg'
					},
					{
						url: 'https://vkceyugu.cdn.bspapp.com/VKCEYUGU-wangshanyw/2ff3cdf0-e87b-11ea-81ea-f115fe74321c.jpg'
					}
				],
				image1: "https://vkceyugu.cdn.bspapp.com/VKCEYUGU-wangshanyw/63b5a330-e938-11ea-8a36-ebb87efcf8c0.jpg",
				image2: "https://vkceyugu.cdn.bspapp.com/VKCEYUGU-wangshanyw/4ff18490-e938-11ea-8ff1-d5dcf8779628.jpg"
			}
		},
		onLoad() {
			console.log(this.$store.state.vuex_wx_openid)
		},
		methods: {
			navigate1() {
				uni.navigateTo({
					url: "/pages/tabbar/index/AnonymousChatting/AnonymousChatting",
				})
			},
			navigate2() {
				uni.navigateTo({
					url: '/pages/tabbar/index/MeetingRoom/MeetingRoom'
				})
			}
		}
	}
</script>
<style lang="scss">
	.swiper {
		display: flex;
		height: 450rpx;
		margin-top: 100rpx;
		margin-bottom: 100rpx;
		margin-left: 30rpx;
		margin-right: 30rpx;
		justify-content: center;

		image {
			height: 100%;
			width: 100%;
			justify-content: center;
		}
	}

	.option {
		width: 100%;
		justify-content: center;

		.option-box {
			display: flex;
			width: 50%;
			height: 230rpx;
			border-radius: 50%;
			margin-bottom: 80rpx;
			box-sizing: border-box;
			float: left;
			overflow: hidden;

			image {
				width: 100%;
				height: 100%;
			}

			button {
				width: 230rpx;
				height: 230rpx;
				border: None;
				padding: 0;
			}

		}
		
		.option-title-contain {
			display: flex;
			width: 50%;
			height: 270rpx;
			border-radius: 50%;
			margin-bottom: 60rpx;
			box-sizing: border-box;
			float: left;	
		}

		.option-title {
			position: relative;
			display: flex;
			font-size: 40rpx;
			font-weight: bold;
			font: Tahoma, Helvetica, Arial, '宋体', sans-serif;
			text-shadow: 2px 2px #8f673c;
			color: #f3a530;
		}
	}
</style>