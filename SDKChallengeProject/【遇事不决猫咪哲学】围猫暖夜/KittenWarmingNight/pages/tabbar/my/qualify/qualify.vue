<template>
	<view>
		<view class="current-status">
			目前状态: {{currentStatus}}
		</view>
		<view class="feedback-title">证书照片：</view>
		<view class="feedback-image-box">
			<view class="feedback-image-item" v-for="(item,index) in imageLists" :key="index">
				<view class="close-icon" @click="del(index)">
					<uni-icons type="closeempty" size="18" color="#fff"></uni-icons>
				</view>
				<view class="image-box">
					<image :src="item.url" mode="aspectFill"></image>
				</view>
			</view>
			<view v-if="imageLists.length < 1" class="feedback-image-item" @click="addImage">
				<view class="image-box">
					<uni-icons type="plusempty" size="50" color="#eee"></uni-icons>
				</view>
			</view>
		</view>
		<button class="button-normal" style="margin-top:70rpx" type="primary" @click="submit" >上传证书照片 </button>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				currentStatus:"未认证",
				content:'',
				id:this.$store.state.vuex_wx_openid.toString(),
				imageLists:[]
			}
		},
		onLoad(){
			uniCloud.callFunction({
				name:"CheckConsultant",
				data:{
					id:this.id
				}
			}).then(res => {
				if (res.result.code==200) {
					this.currentStatus="已认证"
				}
			})
		},
		methods: {
				del(index){
					this.imageLists.splice(index,1)
				},
				addImage(){
					const count = 1 - this.imageLists.length
					uni.chooseImage({
						count:count,
						success:(res)=> {
							const tempfilepaths = res.tempFilePaths
							tempfilepaths.forEach((item,index)=>{
								// 处理 h5 多选的状况
								if(index<count){
									this.imageLists.push({
										url:item
									})
								}
							})
							console.log(res);
						}
					})
				},
				async submit(){
					let imagesPath = []
					uni.showLoading()
					// 
					for(let i = 0 ; i < this.imageLists.length ;i++){
									const filePath = this.imageLists[i].url
									filePath =  await this.uploadFiles(filePath)
									imagesPath.push(filePath)
								}
								uniCloud.callFunction({
									name:"UploadFeedbacks",
									data:{
										"id":this.id,
										"content":this.content,
										"feedbackImages":imagesPath
									}
								}).then(res=>{
									uni.hideLoading()
									uni.showToast({
										title:"证书提交成功",
										icon:'none'
									})
									setTimeout(()=>{
										uni.switchTab({
											url:'/pages/tabbar/my/my'
										})
									},2000)
								}).catch(()=>{
									uni.hideLoading()
									uni.showToast({
										title:"证书提交失败 ",
										icon:"none"
									})
								})
							},
							async uploadFiles(filePath){
								const result = await uniCloud.uploadFile({
									filePath:filePath,
									cloudPath:this.id+'-certification.jpg'
								})
								console.log(result);
								return result.fileID
							}
						}
					}
	
</script>

<style lang="scss">
.current-status {
	margin-top: 50rpx;
	font-weight:bolder;
	font-family: 'Open Sans', sans-serif;
	font-size: 50rpx;
	text-align: center;
}
.feedback-title {
	margin-top: 40rpx;
	margin-left: 60rpx;
	font-weight:bolder;
	font-family: 'Open Sans', sans-serif;
	font-size: 35rpx;
	color: #666;
	
}
.feedback-image-box {
	display: flex;
	flex-wrap: wrap;
	padding: 30rpx;
	.feedback-image-item {
		text-align: center;
		margin:auto;
		position: relative;
		width: 70%;
		height: 0;
		padding-top: 70%;
		box-sizing: border-box;
		.close-icon {
			display: flex;
			justify-content: center;
			align-items: center;
			position: absolute;
			right: 0;
			top: 0;
			width: 48rpx;
			height: 48rpx;
			border-radius: 50%;
			background-color: #ff5a5f;
			z-index: 2;
		}
		.image-box  {
			display: flex;
			justify-content: center;
			align-items: center;
			position: absolute;
			top: 15rpx;
			right: 15rpx;
			bottom: 15rpx;
			left: 15rpx;
			border: 5rpx #eee solid;
			border-radius: 15rpx;
			overflow: hidden;
			image {
				width: 100%;
				height: 100%;
			}
		}
	}
}

</style>
