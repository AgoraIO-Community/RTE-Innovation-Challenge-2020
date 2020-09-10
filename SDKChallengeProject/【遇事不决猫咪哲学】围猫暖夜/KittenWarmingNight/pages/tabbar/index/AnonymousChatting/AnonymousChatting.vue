<template>
	<view>
		<image src="https://vkceyugu.cdn.bspapp.com/VKCEYUGU-wangshanyw/64925c70-f12c-11ea-b997-9918a5dda011.jpg" mode="aspectFit"></image>
		<button class="button-normal" style="color: #fefefe" type="default" @tap="join">匹配陌生人</button>
		<view class="words-box">
			<view class="words1">{{channelName}}{{channelID}}</view>
			<view class="words1">{{tip}}</view>
			<view class="words">{{url}}</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				channelID: '',
				tip: '',
				channelName: '',
				url: '',
			}
		},
		methods: {
			SetClip(Data) {
				// #ifdef MP-WEIXIN
				wx.setClipboardData({
					data: Data,
					success: function(data) {
						console.log(data)
					},
					fail: function(err) {
						console.log(err)
					},
					complete: function(msg) {
						console.log(msg)
					}
				})
				// #endif
			},
			check() {
				uniCloud.callFunction({
					name: "CheckRoomEmpty"
				}).then(res => {
					if (res.result.code == 400) {
						uni.showModal({
							title: "抱歉",
							content: "暂时无人在线，未能帮你匹配"
						})
						// #ifdef MP-WEIXIN
						wx.hideLoading()
						// #endif
						this.channelID = ''
					} else {
						uni.showModal({
							title: "匹配成功",
							content: "快去加入私语空间吧！"
						})
						// #ifdef MP-WEIXIN
						wx.hideLoading()
						// #endif
					}
				})
			},
			join() {
				uniCloud.callFunction({
					name: 'ChangingWillingStatus',
					data: {
						"id": "5f55029ca0da5b0001a193ad"
					},
					success: res => {
						console.log(res.result)
					}
				})
				uniCloud.callFunction({
					name: 'CheckRoomEmpty',
				}).then(res => {
					console.log(res.result)
					if (res.result.code == 300) {
						uniCloud.callFunction({
							name: 'CreatingAnonymousRoom',
							success: res => {
								console.log(res.result)
								uniCloud.callFunction({
									name: 'initWillingStatus',
									data: {
										"id": "5f55029ca0da5b0001a193ad"
									}
								}).then(res => {
									console.log(res.result)
								})
								// #ifdef MP-WEIXIN
								wx.showLoading({
									title: '匹配中，请稍等，约持续10秒',
								})
								// #endif
								setTimeout(function() {
									uniCloud.callFunction({
										name: "CheckRoomEmpty"
									}).then(res => {
										if (res.result.code == 400) {
											uni.showModal({
												title: "抱歉",
												content: "暂时无人在线，未能匹配"
											})
											// #ifdef MP-WEIXIN
											wx.hideLoading()
											// #endif
											this.channelID = ''
											this.tip = ""
											this.channelName = ""
											this.url = ""
											uniCloud.callFunction({
												name: "TakingAnonymousRoom"
											}).then(res => {
												console.log(res.result)
											})
										} else {
											uni.showModal({
												title: "匹配成功",
												content: "快去加入私语空间吧！"
											})
											// #ifdef MP-WEIXIN
											wx.hideLoading()
											// #endif
										}
									})
								}, 10000)
							}
						})
						return
					} else {
						uniCloud.callFunction({
							name: "TakingAnonymousRoom",
							success: res => {
								this.channelID = res.result.data
								this.tip = "请将以下网址复制到手机浏览器中并访问~"
								this.channelName = "Channel Name: "
								this.url = "https://wangshanyw.cn/KittenWarmingNight/SecretTalkingGarden/index.html"
								this.SetClip(this.url)
								uniCloud.callFunction({
									name: 'initWillingStatus',
									data: {
										"id": "5f55029ca0da5b0001a193ad"
									}
								}).then(res => {
									console.log(res.result)
								})
							}
						})
					}
				})
			}
		}
	}
</script>

<style lang="scss">
	image {
		width: 650rpx;
		margin-left: 50rpx;
		border-radius: 100rpx;
	}

	.btn {
		margin-top: 40rpx;
		justify-items: center;
		width: 500rpx;
		height: 100rpx;
		border-radius: 100rpx;
		background: linear-gradient(to bottom right, #F8B33A, #F49255);
		color: #fff
	}


	.words1 {
		padding-top: 40rpx;
		text-align: center;
		font-size: 30rpx;
		font-weight: bold
	}

	.words {
		padding-top: 40rpx;
		text-align: center;
		font-size: 30rpx;
		font-weight: bold;
		width: 25em;
		word-wrap: break-word;
	}
</style>
