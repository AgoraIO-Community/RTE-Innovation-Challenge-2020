/*
 * @Author: he.xiaoxue
 * @Date: 2018-08-20 15:58:09
 * @Last Modified by: he.xiaoxue
 * @Last Modified time: 2018-12-26 23:06:54
 */
<template>
  <transition name="mask-bg-fade">
    <div class="modal" v-if="show">
      <!--遮罩 -->
      <div class="mask_bg" @click="closeModal"></div>
      <transition name="slide-fade">
        <!-- 弹窗内容 -->
        <div class="modal-container sm">
          <!-- 游戏类型列表 -->
          <div class="header small">
            <vue-scroll>
              <div class="game-name" :class="{active:activeGame.objectId==game.objectId}" v-for="game in gameList" :key="game.objectId" @click="changeGame(game)">
                <img :src="game.gameicon && game.gameicon.url" alt="">
                <p>{{game.gameName}}</p>
              </div>
            </vue-scroll>
            <img :src="closeIcon" alt="" class="close" @click="closeModal">
          </div>
          <!-- ENDOF 游戏类型列表 -->
          <!-- 游戏模式选择 -->
          <div class="body">
            <div>
              <p class="title">房间类型：</p>
              <span v-for="item in activeGame.taskType" :key="item.objectId" @click="changeGameType(item)" class="btn" :class="{active:activeGameType.objectId==item.objectId}">{{item.name}}</span>
            </div>
            <div>
              <p class="title last">房间人数：{{activeGame.taskType.maxnum}}</p>
              <span v-for="item in numList" :key="item" @click="changeMaxnum(item)" class="btn" :class="{active:item==activeMaxNum}">{{item}}人</span>
            </div>
          </div>
          <!-- ENDOF 游戏模式选择 -->
          <div class="footer">
            <span class="submit-btn large" @click="roomSubmit">立即创建</span>
          </div>
        </div>
      </transition>
    </div>
  </transition>
</template>
<script>
// icon
import closeIcon from '@/assets/close.png'

import {
  DB_TEAM,
  DB_GAME,
  DB_GAME_TYPE,
  GGMATHINECUSTOMER,
  DB_CONVERSATION,
  DB_USER_HISTORY,
  DB_TEAM_AREA
} from '@/leanCloud/global'

import { insertUserHistory } from '@/api/gameLobby'

export default {
  name: 'CreateRoom',
  props: {
    show: Boolean,
    gameList: Array
  },
  data() {
    return {
      closeIcon: closeIcon, // 图标
      numList: [2, 4, 5, 6, 8], // 房间人数遍历数组，按照实际的使用slice截取
      activeGame: null, // 选择的游戏
      activeGameType: null, // 选择的房间类型
      activeMaxNum: 2 // 选择的房间最大人数,
    }
  },
  computed: {
    activeAreaId() {
      return this.$store.state.game.activeAreaId
    }
  },
  created() {
    // 初始化的时候，默认选择第一个游戏 房间类型为游戏的第一个类型
    this.activeGame = this.gameList[0]
    this.activeGameType = this.gameList[0].taskType[0]
    this.activeMaxNum = this.activeGameType.maxnum
    this.currentUserId = this.AV.User.current().toJSON().objectId
    console.log('>>>>> areaId:', this.activeAreaId)
  },
  mounted() {
    //
  },
  methods: {
    /**
     * 切换游戏
     * @param {object} game 游戏对象
     */
    changeGame(game) {
      this.activeGame = game
      this.activeGameType = game.taskType[0]
    },
    // 关闭弹窗
    closeModal() {
      this.$emit('closeModal')
    },
    /**
     * 切换房间类型 默认房间最大人数更改
     * @param {object} gameType 游戏房间类型对象
     */
    changeGameType(gameType) {
      this.activeGameType = gameType
      this.activeMaxNum = this.activeGameType.maxnum
    },
    // 切换人数
    changeMaxnum(num) {
      this.activeMaxNum = num
    },
    // 提交房间
    roomSubmit() {
      // 群聊数据
      const current = this.AV.User.current()
      const currentUser = current.toJSON()
      const userId = currentUser.objectId
      const title = this.roundTeamTitle() // 房间名 随机
      const chatUserArr = [userId, GGMATHINECUSTOMER] // 当前用户id
      const userArr = [userId]

      const createConv = () => {
        // 1.创建群聊
        this.LeanRT.imClient
          .createConversation({
            members: chatUserArr,
            name: title,
            attr: {
              type: 'room'
            }
          })
          .then(conversation => {
            conversation = conversation.toJSON()
            // 创建群聊成功后往team表中插入数据
            const Team = this.AV.Object.extend(DB_TEAM)
            const team = new Team()
            team.set('title', title)

            const gameType = this.AV.Object.createWithoutData(DB_GAME_TYPE, this.activeGameType.objectId) // 房间类型
            const game = this.AV.Object.createWithoutData(DB_GAME, this.activeGame.objectId) // 游戏类型
            const conv = this.AV.Object.createWithoutData(DB_CONVERSATION, conversation.id) // 对话
            if (this.activeAreaId) {
              const area = this.AV.Object.createWithoutData(DB_TEAM_AREA, this.activeAreaId) // 对话
              team.set('area', area)
            }

            //
            team.set('type', gameType)
            team.set('maxnum', this.activeMaxNum)
            team.set('paltform', 'web')
            team.set('publisher', current)

            // 游戏
            team.set('game', game)
            team.set('isLock', false)
            team.set('status', true)
            team.set('participants', userArr)
            team.set('conversation', conv)
            team.set('groupChatId', conversation.id)

            team
              .save()
              .then(team => {
                team.fetchWhenSave(true)
                return team.save()
              })
              .then(team => {
                // 创建队伍成功回调
                console.log('>>>>> 用户组队数据插入成功')
                this.$router.push({ name: 'chatRoom', params: { teamId: team.toJSON().objectId } })
                // 使用了 fetchWhenSave 选项，save 成功之后即可得到最新的 views 值
                // 1. 存储一条数据到用户组队历史表
                // insertUserHistory(this.activeGame.objectId, team.toJSON().objectId, conversation.id)
              })
              .catch(error => {
                // 异常处理
                console.log('>>>>> 组队失败 error:' + error)
              })
          })
      }

      if (!this.LeanRT.imClient) {
        // 未登录会话 尝试再次登录
        this.LeanRT.realtime
          .createIMClient(this.currentUserId)
          .then(imClient => {
            this.LeanRT.imClient = imClient // 将创建的client对象挂在在VUE下的LeanRT中
            createConv()
          })
          .catch(error => {
            console.error(error)
          })
      } else {
        // 已登录 直接获取
        createConv()
      }
    },
    // 随机房间名称
    roundTeamTitle() {
      const arr = [
        '队伍缺人，速度进来',
        '快来和我一起组队吧',
        '游戏不等人，速进',
        '带你游戏带你飞',
        '求大佬带我玩',
        '带萌新玩游戏'
      ]

      var current = this.AV.User.current().toJSON();
      var name = current.username + '的语音房间';
    //  return arr[Math.floor(Math.random() * arr.length)]
      return name;
    }
  }
}</script>
<style lang="scss" scoped >
.modal-container {
  width: 500px;
  min-height: 400px;
  margin-left: -250px;
  margin-top: -200px;
  .header {
    height: 130px;
    background-color: #242730;
    // padding-left: 30px;
    .game-name {
      float: left;
      text-align: center;
      margin-top: 20px;
      // margin-bottom: 20px;
      cursor: pointer;
      height: 70px;
      width: 20%;
      overflow: hidden;
      &.active,
      &:hover {
        color: #efca14;
        img {
          border: solid 2px #efca14;
          box-sizing: border-box;
          -moz-box-sizing: border-box; /* Firefox */
          -webkit-box-sizing: border-box; /* Safari */
        }
      }
      img {
        height: 50px;
        width: 50px;
        border-radius: 50%;
        font-size: 14px;
      }
      p {
        line-height: 2em;
      }
    }
  }
  .body {
    text-align: left;
    padding: 0 30px;
    .title {
      margin: 20px 0;
      &.last {
        margin-top: 10px;
      }
    }
    .btn {
      padding: 9px 12px;
      display: inline-block;
      background-color: #242730;
      border-radius: 4px;
      margin-bottom: 10px;
      cursor: pointer;
      min-width: 56px;
      text-align: center;
      + .btn {
        margin-left: 10px;
      }
      &:nth-child(6n + 1) {
        margin-left: 0;
      }
      &.active,
      &:hover {
        color: #1e2027;
        background-color: #efca14;
        border-radius: 4px;
      }
    }
  }
  .footer {
    margin-top: 40px;
    margin-bottom: 30px;
    .submit-btn {
      width: 440px;
      height: 50px;
      line-height: 50px;
      font-weight: bold;
      background-color: #efca14;
      border-radius: 25px;
      color: #1e2027;
      display: inline-block;
      cursor: pointer;
    }
  }
}
</style>
