<template>
  <vue-scroll :ops="ops">
    <div class="app-container" ref="teams">
      <!-- 没有组队展示 -->
      <div class="nodata" v-if="teamList!=null&&teamList.length==0">
        <img :src="NoTeam" alt="">
        <p class="gray small">没有相关房间，你可以</p>
        <p class="gray small">刷新页面试试~</p>
        <span class="reload-btn small" @click="getTeamList(activeGameId)">刷新页面</span>
      </div>
      <!-- ENDOF 没有组队展示 -->
      <!-- 组队信息 -->
      <div class="game-room" v-for="(team,index) in teamList" :key="team.objectId" @click="joinTeam(team)" :class="{lineFirst:index%lineMaxTeam==0}">
        <!-- 头部 -->
        <div class="header small">
          <div class="left">
            <img :src="team.game.gameicon.url|urlFilter" alt="" class="img">
            <span>{{team.game.gameName}}
              <span class="bloder"> · </span>{{team.type.name}}</span>
          </div>
          <div class="right " :class="[team,currentUserId]|classFilter">{{[team,currentUserId]|statusFilter}}</div>
        </div>
        <!-- 内容 -->
        <div class="body middle">
          <img :src="team.publisher.avatar|avatarFilter" alt="" class="img">
          <div class="info">
            <span>{{team.title}}</span>
            <!-- 参与者信息 -->
            <div class="participants">
              <span class="already" v-for="item in team.participants" :key="item"></span>
              <span class="vacancy" v-for="item in domainList.slice(0,team.maxnum-team.participants.length)" :key="item"></span>
              <span class="small">{{team.participants.length}}/{{team.maxnum}}</span>
            </div>
          </div>
        </div>
      </div>
      <!-- ENDOF 组队信息 -->

      <!-- 加入新队伍弹窗 -->
      <join-new-team v-if="joinNewTeamConfirmShow" @closeModal="joinNewTeamConfirmShow=false" @enSure="joinNewTeam"></join-new-team>

    </div>
  </vue-scroll>
</template>
<script>
// pic
import NoTeam from '@/assets/no_team.png'
import AvatarDefault from '@/assets/avatar-default.png'

import JoinNewTeam from './joinNewTeam'
// request
import { getTeamList, getUserHistory, teamLiveQuery } from '@/api/gameLobby'

export default {
  name: 'GameTeam',
  data() {
    return {
      NoTeam: NoTeam, // 没有组队图片
      lineMaxTeam: 4,
      teamList: null, // 组队列表
      activeGameId: null, // 选择的游戏类型
      ops: {
        // 滚动条参数
        vuescroll: {},
        scrollPanel: {},
        rail: {
          background: '#00d95f',
          opacity: 0,
          /** Rail's size(Height/Width) , default -> 6px */
          size: '6px'
        },
        bar: {}
      },
      domainList: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
      isSubscribe: false,
      currentUserId: null,
      joinNewTeamConfirmShow: false,
      waitJoinTeamObj: null // 预备加入的队伍信息 newTeam oldTeam
    }
  },
  components: {
    JoinNewTeam
  },
  filters: {
    urlFilter(url) {
      if (url) {
        return url
      } else {
        return ''
      }
    },
    avatarFilter(avatar) {
      if (avatar) {
        return avatar.url
      } else {
        return AvatarDefault
      }
    },
    classFilter([team, currentUserId]) {
      if (team.isLock && team.participants.indexOf(currentUserId) === -1) {
        return 'gray'
      }
      if (team.participants.length >= team.maxnum) {
        return 'gray'
      }
      return 'green'
    },
    statusFilter([team, currentUserId]) {
      if (team.isLock && team.participants.indexOf(currentUserId) === -1) {
        return '已上锁'
      }
      if (team.participants.length >= team.maxnum) {
        return '满员'
      }
      return '可加入'
    }
  },
  computed: {
    activeAreaId() {
      return this.$store.state.game.activeAreaId
    }
  },
  created() {
    // console.log('............')
    this.getTeamList(-1, -1, this.activeAreaId)
    this.subscribeTeam()
    this.currentUserId = this.AV.User.current() && this.AV.User.current().toJSON().objectId
  },
  mounted() {
    this.bus.$on('toChangeGame', gameId => {
      this.activeGameId = gameId
      this.getTeamList(gameId, -1, this.activeAreaId)
    })
    // FIXME:搜索
    this.bus.$on('searchTeam', title => {
      this.getTeamList(-1, title)
    })
    this.bus.$on('searchTeamByArea', areaId => {
      this.getTeamList(-1, -1, areaId)
    })
  },
  methods: {
    /**
     * 加载组队列表
     * @param {string} gameID 游戏类型id
     */
    getTeamList(gameId, title, areaId) {
      const that = this
      // this.$hx_toast({ message: '正在加载...' })
      getTeamList(gameId, title, areaId).then(res => {
        const { teamArr, query } = res
        this.teamList = teamArr
        // query.subscribe().then(liveQuery => {
        //   that.isSubscribe = true
        //   liveQuery.on('create', newDoingItem => {
        //     that.getTeamList(that.activeGameId, title)
        //   })
        //   liveQuery.on('update', newDoingItem => {
        //     that.getTeamList(that.activeGameId, title)
        //   })
        // })
      })
    },
    subscribeTeam() {
      const gameId = this.activeGameId || -1
      const that = this
      if (this.teamLiveQuery) {
        this.teamLiveQuery.unsubscribe() // 取消订阅
      }
      this.teamLiveQuery = teamLiveQuery()
      this.teamLiveQuery.subscribe().then(liveQuery => {
        liveQuery.on('create', newDoingItem => {
          that.getTeamList(that.activeGameId, -1, that.activeAreaId)
        })
        liveQuery.on('update', newDoingItem => {
          that.getTeamList(that.activeGameId, -1, that.activeAreaId)
        })
      })
    },
    /**
     * 改变toast的值
     * @param {string} msg 更改的值
     */
    changeToastMsg(msg) {
      this.bus.$emit('changeToastMsg', msg)
    },
    /**
     * 关闭toast
     */
    closeToastMsg() {
      this.bus.$emit('closeToastMsg')
    },
    // 加入新队伍 退出旧队伍
    joinNewTeam() {
      const { newTeam, oldTeam } = this.waitJoinTeamObj
      const oldTeamData = {
        oldConvId: oldTeam.conversation.objectId,
        teamId: oldTeam.team.objectId,
        userHisId: oldTeam.objectId
      }
      this.AV.Cloud.run('quitOldTeam', oldTeamData)
        .then(() => {
          console.log('>>>>>:quit oldTeam')
          this.bus.$emit('updateCurrentTeam')
          this.$router.push({ name: 'chatRoom', params: { teamId: newTeam.objectId } })
        })
        .catch(error => {
          console.log(error)
        })
    },
    /**
     * 加入房间
     */
    joinTeam(team) {
      const currentUserId = this.currentUserId
      const teamId = team.objectId
      getUserHistory(currentUserId)
        .then(res => {
          const oldTeam = res[0]
          const isInteam = !!(oldTeam && oldTeam.team.objectId === teamId)
          const isPulisher = !!(oldTeam && oldTeam.team.publisher.objectId === currentUserId)
          if (oldTeam && !isInteam && !isPulisher) {
            this.joinNewTeamConfirmShow = true
            this.waitJoinTeamObj = { newTeam: team, oldTeam: oldTeam }
            // this.$hx_toast({ message: '您当前已有组队，请退出当前组队', time: 2000 })
          } else if (!isInteam && team.isLock) {
            this.$hx_toast({ message: '队伍已上锁，不可加入', time: 2000 })
          } else if (!isInteam && team.participants.length >= team.maxnum) {
            this.$hx_toast({ message: '队伍已满员，不可加入', time: 2000 })
          } else {
            if (isPulisher && !isInteam) {
              this.$hx_toast({ message: '正在回到已有队伍...', time: 1000 })
              setTimeout(() => {
                this.$router.push({ name: 'chatRoom', params: { teamId: oldTeam.team.objectId } })
              }, 1000)
            } else {
              this.$router.push({ name: 'chatRoom', params: { teamId: teamId } })
            }
          }
        })
        .catch(res => {
          //
        })
    }
  }
}
</script>
