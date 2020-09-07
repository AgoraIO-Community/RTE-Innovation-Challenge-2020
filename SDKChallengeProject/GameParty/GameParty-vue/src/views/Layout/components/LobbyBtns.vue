<template>
  <div>
    <div class="current-room" @click="intoRoom" v-if="currentRoom">
      <img :src="currentRoom|publisherFilter" alt="" class="avatar">
      <div class="title white">
        <p class="large">{{currentRoom.team.title | titleFilter}}</p>
        <p class="small">当前所在房间</p>
      </div>
      <span class="arrow"></span>
    </div>
    <div class="lobby-btns">
      <!-- <div class="btn-content circle">
        <img :src="reload" alt="">
      </div> -->
        <!-- <div class="btn-content circle">
        <img :src="msgSrc" alt="">
      </div> -->
        <div class="btn-content">
          <span class="room" @click="createRoom">创建房间</span>
          <!-- <span class="team" @click="matchTeam">一键组队</span> -->
        </div>
      </div>
      <create-room-modal v-if="createModalShow" :show="createModalShow" :gameList="gameTypeList" @closeModal="closeCreateModal"></create-room-modal>
      <team-quick-match v-if="teamModalShow" :show="teamModalShow" @closeModal="closeTeamModal"></team-quick-match>
      <!-- 未登录提示 -->
      <login-modal :way="loginConfirmModalShowWay" v-if="loginConfirmModalShow" @loginBy="loginByFun" @closeModal="loginConfirmModalShow=false"></login-modal>
      <!-- 用户登录 -->
      <user-login v-if="loginModalShow" :loginBy="loginBy" @closeModal="loginModalShow=false" @loginSucess="userInit"></user-login>
      <div>
     
    </div>
    </div>
    

</template>
<script>
// icon
import msgSrc from '@/assets/msg.png'
import reloadPre from '@/assets/reload_pre.png'
import reload from '@/assets/reload.png'
import AvatarDefault from '@/assets/avatar-default.png'
import UserLogin from '@/views/room/userLogin'

// componets
import CreateRoomModal from '@/views/modal/createRoom'
import TeamQuickMatch from '@/views/modal/teamQuickMatch'
import UserInfo from '@/views/modal/userInfo'
import LoginModal from '@/views/modal/loginModal'

// request
import { getUserHistory, getPrizeRecord } from '@/api/gameLobby'
export default {
  data() {
    return {
      msgSrc: msgSrc,
      reload: reload,
      reloadPre: reloadPre,
      createModalShow: false, // 创建房间弹窗显示条件
      teamModalShow: false, // 一键组队弹窗显示条件
      currentUser: null,
      currentRoom: null,
      loginConfirmModalShow: false,
      loginModalShow: false,
      loginBy: null,
      loginConfirmModalShowWay: 'gift'
    }
  },
  components: {
    CreateRoomModal,
    TeamQuickMatch,
    UserInfo,
    LoginModal,
    UserLogin
  },
  computed: {
    // 从Vuex中获取存储的游戏列表
    gameTypeList() {
      return this.$store.state.game.gameTypeList
    },
    prizeHasShow() {
      return this.$store.state.game.prizeHasShow
    }
  },
  mounted() {
    this.bus.$on('updateCurrentTeam', () => {
      this.getCurrentRoom()
    })
    this.bus.$on('showLoginConfirmModal', () => {
      this.loginConfirmModalShow = true
    })
  },
  filters: {
    publisherFilter(currentRoom) {
      if (currentRoom.team && currentRoom.team.publisher.avatar) {
        return currentRoom.team.publisher.avatar.url
      } else {
        return AvatarDefault
      }
    },

    titleFilter(title) {
      if (title.length > 6) {
        return title.slice(0, 6) + '...'
      } else {
        return title
      }
    }
  },
  created() {
    this.currentUser = this.AV.User.current() && this.AV.User.current().toJSON()

    this.getCurrentRoom()

    const wxCode = this.$route.query.code
    if (wxCode) {
      this.loginByWxcode(wxCode)
    } else {
      this.isLogin()
    }
  },
  methods: {
    userInit() {
      this.bus.$emit('updateCurrentTeam')
      this.bus.$emit('updateUserInfo')
      this.currentUser = this.AV.User.current().toJSON()
    },
    isLogin() {
      if (this.currentUser && !this.AV.User.current().isAnonymous()) {
        this.loginConfirmModalShow = false
        // getPrizeRecord()// 关闭见面礼提示
        //   .then(res => {
        //     if (!res && !this.prizeHasShow) {
        //       this.loginConfirmModalShow = true
        //       this.loginConfirmModalShowWay = '1'
        //       this.$store.dispatch('PrizeHasShow', true)
        //     }
        //   })
        //   .catch(error => {})
      } else {
        this.loginConfirmModalShow = true
      }
    },
    getCurrentRoom() {
      if (this.currentUser && !this.AV.User.current().isAnonymous()) {
        getUserHistory(this.currentUser.objectId)
          .then(res => {
            this.currentRoom = res[0]
          })
          .catch(res => {
            //
          })
      }
    },
    // 创建房间
    createRoom() {
      if (this.currentUser && !this.AV.User.current().isAnonymous()) {
        getUserHistory(this.currentUser.objectId)
          .then(res => {
            if (res.length >= 1) {
              this.$hx_toast({ message: '您当前已有组队，请退出当前组队', time: 2000 })
            } else {
              this.createModalShow = true
            }
          })
          .catch(res => {
            //
          })
        // this.createModalShow = true
      } else {
        this.loginConfirmModalShow = true
      }
    },
    // 一键组队
    matchTeam() {
      if (this.currentUser && !this.AV.User.current().isAnonymous()) {
        getUserHistory(this.currentUser.objectId)
          .then(res => {
            if (res.length >= 1) {
              this.$hx_toast({ message: '您当前已有组队，请退出当前组队', time: 2000 })
            } else {
              this.teamModalShow = true
            }
          })
          .catch(res => {
            //
          })
        // this.teamModalShow = true
      } else {
        this.loginConfirmModalShow = true
      }
    },
    // 进入房间
    intoRoom() {
      this.$router.push({ name: 'chatRoom', params: { teamId: this.currentRoom.team.objectId } })
    },
    closeCreateModal() {
      this.createModalShow = false
    },
    closeTeamModal() {
      this.teamModalShow = false
    },
    loginByFun(way) {
      // 打开对应的方式
      this.loginBy = way
      this.loginConfirmModalShow = false
      this.loginModalShow = true
    },
    loginByWxcode(wxCode) {
      let access_token = ''
      let openid = ''
      let current = null
      const wxUserInfo = {}

      const newQuery = {
        code: wxCode
      }
      const routeParam = {
        query: newQuery
      }
      this.$router.push(routeParam)
      // 获取微信code
      this.AV.Cloud.run('getWXUserInfo', { code: wxCode })
        .then(res => {
          res = JSON.parse(res)
          console.log(res)
          return res
        })
        .then(wxInfo => {
          access_token = wxInfo.access_token
          openid = wxInfo.openid
          return this.AV.User.loginWithAuthDataAndUnionId(
            {
              openid: wxInfo.unionid,
              access_token: wxInfo.access_token,
              expires_in: wxInfo.expires_in
            },
            'weixin',
            wxInfo.unionid,
            {
              unionIdPlatform: 'weixin',
              asMainAccount: true
            }
          )
        })
        .then(user => {
          current = user
          this.currentUser = user.toJSON()
          this.currentUserId = this.currentUser.objectId
          if (this.currentUser.username.length > 20) {
            // 判断用户是否登录过
            return this.AV.Cloud.run('getWXUserDetail', { access_token: access_token, openid: openid })
          } else {
            this.$router.push('/')
            return ''
          }
        })
        .then(res => {
          console.log(res)
          if (res) {
            // 设置用户名称头像
            res = JSON.parse(res)
            wxUserInfo.nickname = res.nickname
            wxUserInfo.headimgurl = res.headimgurl

            current.set('username', res.nickname)
            const file = this.AV.File.withURL('avatar', res.headimgurl)
            current.set('avatar', file)
            return current.save()
          }
        })
        .then(res => {
          this.$router.push('/')
          this.userInit()
          this.loginConfirmModalShow = false
        })
        .catch(error => {
          if (error.code == 202) {
            const random = Math.floor(Math.random() * 100)
            current.set('username', wxUserInfo.nickname + random)
            const file = this.AV.File.withURL('avatar', wxUserInfo.headimgurl)
            current.set('avatar', file)
            current.save()
            this.$router.push('/')
          }
          console.log(error)
          this.$hx_toast({ message: '微信登录失败', time: 1500 })
        })
    }
  }
}</script>
