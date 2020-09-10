<template>
	<view>
		<view class="advise-content">
			<view>
				准备进入私语空间吧！
			</view>
		</view>
		<button class="btn" style="margin-top:100rpx;color: #fefefe" type="default" @tap="join">进入私语空间</button>
		<view class="words-box">
			<view class="words1">{{words1_add}}</view>
			<center>
				<text class="words">{{url_1}}</text>
			</center>
		</view>
		<view class="advise-content">
			<text>
				如果没有私语号，请先申请~
			</text>
		</view>
		<button class="btn" style="margin-top:100rpx;color:#fefefe" type="default" @tap="Apply">申请私语空间</button>
		<view class="words-box">
			<center>
				<view class="words1">{{tip}}{{channelID}}</view>
			</center>
			<view class="words1">{{words1}}</view>
			<center>
				<text>{{show_word_2}}</text>
				<text class="words">{{url_2}}</text>
			</center>
		</view>
		<image class="img" src="https://vkceyugu.cdn.bspapp.com/VKCEYUGU-wangshanyw/3d60e300-f132-11ea-b997-9918a5dda011.png"
		 mode="scaleToFill"></image>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				url_1: '',
				url_2: '',
				words1_add: '',
				words1: '',
				channelID: '',
				tip: '',
				show_word_1: '',
				show_word_2: '',
				id: this.$store.state.vuex_wx_openid.toString()
			}
		},
		props: {
			color: '#000000',
			underline: "true"

		},
		methods: {
			join() {
				this.words1_add = '复制链接'
				this.url_1 = 'https://wangshanyw.cn/KittenWarmingNight/SecretTalkingGarden/index.html'
				this.SetClip(this.url_1)
			},
			Apply() {
				uniCloud.callFunction({
					name: "CheckConsultant",
					data:{
						"id":this.id,
					}
				}).then(res => {
					if (res.result.code == 300) {
						uni.showModal({
							title: "抱歉",
							content: "您没有专业身份，不能申请",
						})
					} else {
						uniCloud.callFunction({
							name: "CreateSecretRoom",
							success: res => {
								uni.showModal({
									title: "申请成功",
									content: "快去邀请Ta加入私语空间吧！"
								})
								this.tip = 'Channel Name:'
								this.channelID = res.result.data
								this.words1 = '申请成功'
								this.url_2 = 'https://wangshanyw.cn/KittenWarmingNight/SecretTalkingGarden/index.html'
								this.SetClip(this.url_2)
							}
						})

					}
				})
			},
			SetClip(Data) {
				// #ifdef MP-WEIXIN
				wx.setClipboardData({
					data: Data,
					success: function(data) {console.log(data)},
					fail: function(err) {console.log(Data, "Error")},
				})
				// #endif
			}
		}
	}
</script>

<style lang="scss">
	.code-input-box {
		display: flex;
		flex-direction: column;
		width: 750rpx;
		height: 100rpx;
		margin-top: 80rpx;
	}

	.btn {
		justify-items: center;
		width: 500rpx;
		height: 100rpx;
		margin-top: 40rpx;
		border-radius: 100rpx;
		
		background: linear-gradient(to bottom, #fbaf51 0%, #F49255 100%);
		/* W3C */
		border: none;
		border-radius: 100px;
		position: relative;
		border-bottom: 8px solid #e76c3c;
			
		padding: 0px 0px 60px 0px;
		color: #fbfbfb;
		font-weight: 600;
		font-family: 'Open Sans', sans-serif;
		text-shadow: 5px 3px 2px rgba(0, 0, 0, .4);
		font-size: 25px;
		text-align: middle;
		text-indent: -5px;
		box-shadow: 3px 3px 3px 0px rgba(0, 0, 0, .2);
		cursor: pointer;
			
		/* Just for presentation */
		display: block;
		margin-bottom: 20px;
	}
	
	.btn-hide {
		justify-items: center;
		width: 300rpx;
		height: 90rpx;
		margin-top: 40rpx;
		border-radius: 100rpx;
		
		background: linear-gradient(to bottom, #fbaf51 0%, #F49255 100%);
		/* W3C */
		border: none;
		border-radius: 100px;
		position: relative;
		border-bottom: 8px solid #e76c3c;
			
		padding: 0px 0px 0px 0px;
		color: #fbfbfb;
		font-weight: 600;
		font-family: 'Open Sans', sans-serif;
		text-shadow: 5px 3px 2px rgba(0, 0, 0, .4);
		font-size: 15px;
		text-align: middle;
		text-indent: -5px;
		box-shadow: 1px 1px 1px 0px rgba(0, 0, 0, .2);
		cursor: pointer;
			
		/* Just for presentation */
		margin-bottom: 20px;
	}

	.advise-content {
		width: 750rpx;
		height: 80rpx;
		padding-top: 90rpx;
		text-align: center;
		margin: auto;
		font-size: 40rpx;
		color: "#666"
	}

	.words1 {
		padding-top: 40rpx;
		text-align: center;
		font-size: 45rpx;
		font-weight: bold
	}

	.words {
		width: 75%;
		text-align: center;
		font-size: 30rpx;
		font-weight: bold;
		word-wrap: break-word;
	}

	.img {
		margin-top: 40rpx;
		margin-left: 550rpx;
		width: 120rpx;
		height: 180rpx
	}
</style>
