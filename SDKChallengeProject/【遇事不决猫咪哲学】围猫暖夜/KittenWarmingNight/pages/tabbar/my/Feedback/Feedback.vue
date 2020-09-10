<template>
	<view>
		<view class="feedback-title">意见反馈：</view>
		<view class="feedback-content">
			<textarea class="feedback-textarea" v-model="content" placeholder="请输入您要反馈的问题" />
			</view>
		<view class="feedback-title">反馈图片：</view>
		<view class="feedback-image-box">
			<view class="feedback-image-item" v-for="(item,index) in imageLists" :key="index">
				<view class="close-icon" @click="del(index)">
					<uni-icons type="closeempty" size="18" color="#fff"></uni-icons>
				</view>
				<view class="image-box">
					<image :src="item.url" mode="aspectFill"></image>
				</view>
			</view>
			<view v-if="imageLists.length < 6" class="feedback-image-item" @click="addImage">
				<view class="image-box">
					<uni-icons type="plusempty" size="50" color="#eee"></uni-icons>
				</view>
			</view>
		</view>
		<button class="button-normal" style="margin-top:70rpx" type="primary" @click="submit" >提交反馈意见 </button>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				content:'',
				id:this.$store.state.vuex_wx_openid.toString(),
				imageLists:[],
				
			}
		},
		methods: {
			del(index){
				this.imageLists.splice(index,1)
			},
			addImage(){
				const count = 6 - this.imageLists.length
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
						title:"反馈提交成功",
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
						title:"反馈提交失败 ",
						icon:"none"
					})
				})
			},
			async uploadFiles(filePath){
				const result = await uniCloud.uploadFile({
					filePath:filePath,
					cloudPath:this.id+'-Feedback.jpg'
				})
				console.log(result);
				return result.fileID
			}
		}
	}
</script>

<style lang="scss">
.feedback-title {
	margin-top: 40rpx;
	margin-left: 60rpx;
	font-weight:bolder;
	font-family: 'Open Sans', sans-serif;
	font-size: 35rpx;
	color: #666;
	
}
.feedback-content {
	margin: 15px;
	padding: 10px;
	box-sizing: border-box;
	border: 1px #eee solid;
	.feedback-textarea {
		font-size: 12px;
		width: 100%;
		height: 100px;
	}
}
.feedback-image-box {
	display: flex;
	flex-wrap: wrap;
	padding: 10px;
	.feedback-image-item {
		position: relative;
		width: 33.33%;
		height: 0;
		padding-top: 33.33%;
		box-sizing: border-box;
		.close-icon {
			display: flex;
			justify-content: center;
			align-items: center;
			position: absolute;
			right: 0;
			top: 0;
			width: 22px;
			height: 22px;
			border-radius: 50%;
			background-color: #ff5a5f;
			z-index: 2;
		}
		.image-box  {
			display: flex;
			justify-content: center;
			align-items: center;
			position: absolute;
			top: 5px;
			right: 5px;
			bottom: 5px;
			left: 5px;
			border: 1px #eee solid;
			border-radius: 5px;
			overflow: hidden;
			image {
				width: 100%;
				height: 100%;
			}
		}
	}
}
</style>
