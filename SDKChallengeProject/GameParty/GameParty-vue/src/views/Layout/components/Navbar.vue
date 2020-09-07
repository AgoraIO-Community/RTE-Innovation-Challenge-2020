<template>
  <div class="navbar">
    <!-- 游戏大厅 -->
    <div class="menus">

    <div class="menu base">
      <div class="menu-item" v-for="(menu,index) in menuList" :key="menu.text" :class="{active:menu.active}" @click="menuClick(index)">
        <span></span>
        <img :src="menu.active?menu.iconPre:menu.icon">
        <p>{{menu.text}}</p>
      </div>
      <div class="menu-item" v-for="(area,index) in areaList" :key="index" :class="{active:area.objectId==activeArea}" @click="areaClick(area)">
        <span></span>
        <img :src="area.icon.url" class="area-icon">
        <p>{{area.areaName}}</p>
      </div>
    </div>
      <!-- <div class="serach">
        <input type="text" v-model.trim="serachValue" placeholder="在此搜索游戏队伍" @keyup.enter="serachFun">
      </div> -->
    </div>
    <!-- 最近游玩 -->
    <div class="tabs" v-if="navbarName=='playHistory'">
      <span class="tab-item">和我组过队的玩家</span>
    </div>
    <!-- 我的关注 -->
    <div class="tabs" v-if="navbarName=='concern'">
      <span class="tab-item c-p" :class="activeIndex==1?'active':''" @click="changeTab(1)"> 组队动态</span>
      <span class="tab-item c-p" :class="activeIndex==2?'active':''" @click="changeTab(2)"> 关注的人(0)</span>
    </div>
  </div>
</template>
<script>
// icons
import AllIconPre from '@/assets/all_pre.png'
import AllIcon from '@/assets/all.png'
import MoreIcon from '@/assets/all.png'

import teamIcon from '@/assets/menu_team.png'
import teamIconPre from '@/assets/menu_team_pre.png'
import historyIcon from '@/assets/menu_history.png'
import historyIconPre from '@/assets/menu_history_pre.png'
import focusIcon from '@/assets/menu_focus.png'
import focusIconPre from '@/assets/menu_focus_pre.png'

// request
import { getGameList, getAreaList } from '@/api/gameLobby'
export default {
  name: 'Navbar',
  data() {
    return {
      ops: {
        // 滚动条参数
        vuescroll: {},
        scrollPanel: {
          scrollingX: true,
          scrollingY: false
        },
        rail: {
          background: '#00d95f',
          opacity: 0,
          /** Rail's size(Height/Width) , default -> 6px */
          size: '6px'
        },
        bar: {}
      },
      menuList: [
        {
          icon: teamIcon,
          iconPre: teamIconPre,
          text: '所有房间',
          active: true
        }
        // {
        //   icon: historyIcon,
        //   iconPre: historyIconPre,
        //   text: '最近游玩',
        //   active: false
        // },
        // {
        //   icon: focusIcon,
        //   iconPre: focusIconPre,
        //   text: '我的关注',
        //   active: false
        // }
      ],

      navbarName: 'gameLobby',
      activeIndex: 1,

      serachValue: '',

      areaList: [],
      activeArea: null
    }
  },
  created() {
    if (this.navbarName === 'gameLobby') {
      this.getGames()
    }
    this.getAaea()
    this.activeArea = this.$store.state.game.activeAreaId
    if (this.activeArea) {
      this.menuList[0].active = false
    }
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
    getAaea() {
      getAreaList()
        .then(res => {
          this.areaList = res
        })
        .catch(res => {
          //
        })
    },
    // 切换tab
    changeTab(index) {
      this.activeIndex = index
    },
    menuClick(index) {
      this.menuList[index].active = true
      this.activeArea = null
      this.bus.$emit('searchTeamByArea', '')
      this.$store.dispatch('SetActiveArea', '')
    },
    // 区域选择
    areaClick(area) {
      //
      this.activeArea = area.objectId
      this.menuList[0].active = false
      this.bus.$emit('searchTeamByArea', this.activeArea)
      this.$store.dispatch('SetActiveArea', this.activeArea)
    },
    /* 搜索函数 */
    serachFun() {
      const searchValue = this.serachValue
      if (searchValue !== '') {
        console.log(searchValue)
        this.bus.$emit('searchTeam', searchValue)
      }
    }
  }
}
</script>
