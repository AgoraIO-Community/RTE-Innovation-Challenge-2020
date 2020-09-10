<script>
	import yesapi from '@/lib/yes3';
	export default {
		onLaunch: function() {
			console.log('App Launch')
			this.checkLogin()
			console.log('登录成功')
		},
		onShow: function() {
			console.log('App Show')
			this.save()
			console.log("save success")
		},
		onHide: function() {
			console.log('App Hide')
		},
		methods:{
			async checkLogin() {
			    const _this = this;
			    if (!this.vuex_wx_openid) {
					const res = await uni.login({ provider: 'weixin' });
					console.log(res, res[1]);
			        const data = await yesapi.weixin.login(res[1].code);
			        const { openid } = JSON.parse(data.data).data.weixin_info;
			        const result = await yesapi.weixin.getInfo(openid);
			        console.log(data, openid, result);
			        // eslint-disable-next-line camelcase
			        const { weixin_info } = JSON.parse(result.data).data;
			        _this.$u.vuex('vuex_wx_openid', weixin_info.openid);
			        _this.$u.vuex('vuex_wx_info', weixin_info);
			    }
			},
			save(){
				uniCloud.callFunction({
					name:"CheckUserExistence",
					data: {
						openID : this.$store.state.vuex_wx_openid
					}
				}).then(res => {
					console.log(res)
					if (res.result.code==300){
						uniCloud.callFunction({
							name: "SaveUserInfo",
							data: {
								openID : this.$store.state.vuex_wx_openid,
							},
							success(res){
								console.log(res)
							},
							fail(err){
								console.log(err)
							}
						})
					}
				})
			}
		}
	}
</script>

<style lang="scss">
	/* 注意要写在第一行，同时给style标签加入lang="scss"属性 */
	@import "uview-ui/index.scss";
	
		.button-normal {
			width: 160px;
			height: 60px;
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
			margin: 0 auto;
			margin-bottom: 20px;
		
		}
		
		button:active {
			box-shadow: 10px 5px 0px 0px rgba(0, 0, 0, .2);
			top: 1px;
		}
</style>
