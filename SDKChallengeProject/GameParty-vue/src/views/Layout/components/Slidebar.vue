<template>
  <div>
    <div class="slidebar">
      <!-- LOGO -->
      <div class="logo green c-p" @click="reloadPage">
        <img src="../../../assets/logo.png" alt="G游语音">
        <h2 class="small">G游语音</h2>
        <p>极速·轻便·简洁</p>
      </div>
      <!-- MENU -->
      <!-- <div class="menu base">
        <div class="menu-item" v-for="(menu,index) in menuList" :key="menu.text" :class="{active:menu.active}" @click="menuClick(index)">
          <span></span>
          <img :src="menu.active?menu.iconPre:menu.icon">
          <p>{{menu.text}}</p>
        </div>
      </div> -->
      <div class="games">
        <div class="game-group">
          <vue-scroll>
            <div class="game-item isall" :class="{active:isAll}" @click="changeGame()">
              <img :src="isAll?AllIconPre:AllIcon" alt="" />
              <p>全部</p>
              <span></span>
            </div>
            <!-- 游戏列表 -->
            <div class="game-item" :class="{active:game.isActive}" v-for="(game,index) in gameList" :key="game.id" @click="changeGame(game.objectId,index)">
              <img :src="game.gameicon.url" alt="" />
              <p>{{game.gameName}}</p>
              <span></span>
            </div>
          </vue-scroll>
        </div>
        <!-- <div class="game-item">
        <img src="../../../assets/more.png" alt="" />
        <p>其他</p>
        <span></span>
      </div> -->
      </div>
      <!-- USER -->
      <div class="user c-p" @click="showUserInfo">
        <img :src="userAvatar" alt="">
        <p teclass="small">{{userName}}</p>
      </div>
    </div>
    <!-- 用户信息 -->
    <personal-info v-if="userInfoShow" :show="userInfoShow" @closeModal="closeUserInfoModal" @refreshUserInfo="refreshUserInfo"></personal-info>
  </div>
</template>
<script>
// icons
import AllIconPre from '@/assets/all_pre.png'
import AllIcon from '@/assets/all.png'
import MoreIcon from '@/assets/all.png'

import AvatarDefault from '@/assets/avatar-default.png'
import PersonalInfo from '@/views/modal/personal'

// request
import { getGameList } from '@/api/gameLobby'

export default {
  name: 'Sildebar',
  components: {
    PersonalInfo
  },
  data() {
    return {
      AllIcon: AllIcon, // 图标图片文件
      AllIconPre: AllIconPre,
      MoreIcon: MoreIcon,
      gameList: [],
      isAll: true,
      AvatarDefault: AvatarDefault,
      activeIndex: 1,
      userName: '',
      userAvatar: '',
      userInfoShow: false,
      isLogin: Boolean
    }
  },
  mounted() {
    this.bus.$on('updateUserInfo', () => {
      this.refreshUserInfo()
    })
  },
  created() {
    this.refreshUserInfo()
    this.getGames()
  },
  methods: {
    getGames() {
      getGameList()
        .then(res => {
          this.gameList = res
          this.$store.dispatch('GetAllGames', res)
        })
        .catch(res => {
          //
        })
    },
    /**
     * 游戏大厅，切换不同的游戏加载组队列表
     * @param {string} gameId 点击的游戏类型id
     * @param {number} index 点击的索引值
     */
    changeGame(gameId, index) {
      for (let i = 0; i < this.gameList.length; i++) {
        // 遍历
        this.$set(this.gameList[i], 'isActive', false)
      }
      if (!gameId) {
        // 点击全部
        this.bus.$emit('toChangeGame')
        this.isAll = true
        return
      }
      if (this.isAll) this.isAll = false
      // 设置当前的为active状态
      this.$set(this.gameList[index], 'isActive', true)
      this.bus.$emit('toChangeGame', gameId)
    },
    // 菜单切换
    menuClick(index) {
      this.activeIndex = index
      for (let i = 0; i < this.menuList.length; i++) {
        this.menuList[i].active = false
      }
      this.menuList[index].active = true
      if (index == 1) {
        // this.$router.push({ name: 'RecentUser' })
      }
    },
    closeUserInfoModal() {
      this.userInfoShow = false
    },
    refreshUserInfo() {
      let currentUser = this.AV.User.current()
      if (currentUser) {
        if (!currentUser.isAnonymous()) {
          this.userName = currentUser.username
          this.isLogin = true
        } else {
          this.userName = '匿名用户'
          this.isLogin = false
        }
        currentUser = currentUser.toJSON()
      } else {
        this.userName = '未登录'
        this.isLogin = false
      }
      // console.log(currentUser)
      if (currentUser && currentUser.avatar) {
        this.userAvatar = currentUser.avatar.url
      } else {
        this.userAvatar = AvatarDefault
      }
    },
    reloadPage() {
      this.$router.go(0)
    },
    showUserInfo() {
      if (this.isLogin) {
        this.userInfoShow = true
      } else {
        this.bus.$emit('showLoginConfirmModal')
      }
    }
  }
}
</script>
