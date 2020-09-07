<template>
  <transition name="mask-bg-fade">
    <div class="modal" v-show="isShow">
      <!--遮罩 -->
      <div class="mask_bg" @click="closeModal"></div>
      <transition name="slide-fade">
        <!-- 弹窗内容 -->
        <div class="modal-container match-team">
          <div class="header small">
            <h3>{{activeGame.gameName}}</h3>
            <vue-scroll>
              <div class="game-name" :class="{active:activeGame.objectId==game.objectId}" v-for="game in gameList" :key="game.objectId" @click="changeGame(game)">
                <img :src="game.gameicon && game.gameicon.url" alt="">
                <p>{{game.gameName}}</p>
              </div>
            </vue-scroll>
            <img :src="closeIcon" alt="" class="close" @click="closeModal">
          </div>
          <div class="body">
            <div>
              <p class="title">组队偏好：</p>
              <span class="btn" :class="{active:gameTypeAll}">全部偏好</span>
              <span v-for="(item,index) in activeGameType" :key="item.objectId" @click="chooseType(item,index)" class="btn" :class="{active:item.isSelected}">{{item.name}}</span>
            </div>
          </div>
          <div class="footer">
            <span class="submit-btn large" @click="matchSubmit">立即寻找组队</span>
          </div>
        </div>
      </transition>
    </div>
  </transition>
</template>
<script>
// icon
import closeIcon from '@/assets/close.png'

import { DB_TEAM, DB_GAME, DB_GAME_TYPE, GGMATHINECUSTOMER, DB_CONVERSATION } from '@/leanCloud/global'

import { getQuickMatchTeam } from '@/api/gameLobby'
import { insertUserHistory } from '@/api/gameLobby'

export default {
  name: 'TeamMatch',
  props: {
    show: Boolean
  },
  data() {
    return {
      closeIcon: closeIcon,
      activeGame: null,
      activeGameType: null,
      gameTypeAll: true,
      isShow: true,
      hasMatchTeam: false, // 是否匹配到队伍
      countTime: null,
      userCreated: false // 未匹配到用户创建了队伍
    }
  },
  computed: {
    // 从Vuex中获取存储的游戏列表
    gameList() {
      return this.$store.state.game.gameTypeList
    }
  },
  created() {
    this.activeGame = Object.assign({}, this.gameList[0])
    this.activeGameType = this.activeGame.taskType.map(item => {
      item.isSelected = false
      return item
    })
  },
  methods: {
    /**
     * 切换游戏
     * @param {object} game 游戏对象
     */
    changeGame(game) {
      this.activeGame = Object.assign({}, game)
      this.activeGameType = this.activeGame.taskType.map(item => {
        item.isSelected = false
        return item
      })
    },
    chooseType(gameType, index) {
      gameType.isSelected = !gameType.isSelected
      this.$set(this.activeGameType, index, gameType)
      const canChooseNum = this.activeGameType.length
      this.gameTypeAll = (() => {
        for (let i = 0; i < canChooseNum; i++) {
          if (this.activeGameType[i].isSelected == false) {
            return false
          }
        }
        return true
      })()
    },
    // 关闭弹窗
    closeModal() {
      this.$emit('closeModal')
    },
    // 切换类型
    changeKind() {
      // TODO:
    },
    // 切换人数
    changeNum() {
      // TODO:
    },
    // 提交房间
    matchSubmit() {
      // 1. 检查用户能否组队
      // 2. 获得game对象
      // 3. gameType数组（偏好设置）or查询
      // 4. 根据2 3 去team表查询 拿到query对象
      // 5. 没有删除，没有上锁 查询结果，如果4 返回有值 findm
      // 6. 5 返回的数组遍历 拿到第一个未满员 return
      // 7. 6有返回 进入房间，无返回 ->8
      // 8. 拿4的query对象 去订阅livequery
      // 9. query要监听create事件 监听到回调，取消订阅，执行加入
      // 10. 15秒内未收到回调，取消订阅，另自动创建房间，取第一种偏好，maxnum取默认
      const matchArry = arr => {
        for (let i = 0; i < arr.length; i++) {
          if (arr[i].maxnum > arr[i].participants.length) {
            return arr[i]
            // break;
          }
        }
        return false
      }
      this.$hx_toast({ message: '正在寻找队伍，已等待...1s' })
      this.changeToastMsg('正在寻找队伍，已等待...', 2)
      this.isShow = false
      const gameTypeArr = (() => {
        let arr = []
        if (this.gameTypeAll) {
          arr = this.activeGameType
        } else {
          for (let i = 1; i < this.activeGameType.length; i++) {
            if (this.activeGameType[i].isSelected) {
              arr.push(this.activeGameType[i])
            }
          }
        }
        if (arr.length == 0) {
          arr = this.activeGameType
        }
        return arr
      })()

      getQuickMatchTeam(this.activeGame.objectId, gameTypeArr)
        .then(res => {
          const { teamArr, query } = res
          if (!teamArr) {
            teamArr = []
          }
          const matchTeam = matchArry(teamArr)
          if (matchTeam) {
            this.closeToastMsg()
            this.hasMatchTeam = true
            this.$router.push({ name: 'chatRoom', params: { teamId: matchTeam.objectId } })
          } else {
            // livequery 订阅
            query
              .subscribe()
              .then(liveQuery => {
                liveQuery.on('create', newDoingItem => {
                  if (this.userCreated) {
                    liveQuery.unsubscribe() // 取消订阅
                  } else {
                    newDoingItem = newDoingItem.toJSON()
                    const matchTeam = matchArry([newDoingItem])
                    if (matchTeam) {
                      this.closeToastMsg()
                      this.hasMatchTeam = true
                      liveQuery.unsubscribe() // 取消订阅
                      this.$router.push({ name: 'chatRoom', params: { teamId: matchTeam.objectId } })
                    }
                  }
                })
              })
              .catch(error => {
                console.log(error)
              })
          }
        })
        .catch(error => {
          console.log(error)
        })
      // this.$emit('closeModal')
      // TODO: 提交请求
    },
    /**
     * 改变toast的值
     * @param {string} msg 更改的值
     */
    changeToastMsg(msg, count) {
      this.countTime = count
      const showMsg = msg + count + 's'
      console.log(showMsg)
      this.bus.$emit('changeToastMsg', showMsg)
      if (!this.hasMatchTeam && count < 15) {
        count++
        setTimeout(() => {
          this.changeToastMsg(msg, count)
        }, 1000)
      } else if (this.hasMatchTeam && count < 15) {
        this.bus.$emit('changeToastMsg', '已找到，正在进入房间队伍...1s')
        this.closeToastMsg()
      } else if (!this.hasMatchTeam && count >= 15) {
        this.bus.$emit('changeToastMsg', '未找到，正在创建房间队伍...1s')
        this.userCreated = true
        this.createTeam()
      }
    },
    createTeam() {
      const roundTeamTitle = () => {
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
      //  return arr[Math.floor(Math.random() * arr.length)]
      }
      const currentUser = this.AV.User.current().toJSON()
      const userId = currentUser.objectId
      const chatUserArr = [userId, GGMATHINECUSTOMER] // 当前用户id
      const title = roundTeamTitle()
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

            const gameTypeObj = (() => {
              if (this.gameTypeAll) {
                return this.activeGameType[0]
              } else {
                for (let i = 1; i < this.activeGameType.length; i++) {
                  if (this.activeGameType[i].isSelected) {
                    return this.activeGameType[i]
                  }
                }
                return this.activeGameType[0]
              }
            })()

            const gameType = this.AV.Object.createWithoutData(DB_GAME_TYPE, gameTypeObj.objectId) // 房间类型
            const game = this.AV.Object.createWithoutData(DB_GAME, this.activeGame.objectId) // 游戏类型
            const conv = this.AV.Object.createWithoutData(DB_CONVERSATION, conversation.id) // 对话

            //
            team.set('type', gameType)
            team.set('maxnum', gameTypeObj.maxnum)
            team.set('paltform', 'web')
            team.set('publisher', currentUser)

            // 游戏
            team.set('game', game)
            team.set('isLock', false)
            team.set('desc', '')
            team.set('status', true)
            team.set('participants', userArr)
            team.set('conversation', conv)
            team.set('groupChatId', conversation.id)

            team
              .save()
              .then(team => {
                return team.save()
              })
              .then(team => {
                // 创建队伍成功回调
                console.log('>>>>> 用户组队数据插入成功')
                this.closeToastMsg()
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
          .createIMClient(userId)
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
    /**
     * 关闭toast
     */
    closeToastMsg() {
      this.bus.$emit('closeToastMsg')
    }
  }
}
</script>

<style lang="scss" scoped>
.__vuescroll {
  height: 130px !important;
}
.modal-container {
  width: 500px;
  min-height: 400px;
  margin-left: -250px;
  margin-top: -200px;
  .header {
    clear: both;
    overflow: hidden;
    h3 {
      font-size: 36px;
      color: #e8e8e8;
      margin: 40px 0;
    }
    .game-name {
      float: left;
      text-align: center;
      margin-top: 20px;
      // margin-bottom: 20px;
      cursor: pointer;
      width: 20%;
      height: 70px;
      &.active,
      &:hover {
        color: #00d95f;
        img {
          border: solid 2px #00d95f;
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
      + .btn {
        margin-left: 10px;
      }
      &.active,
      &:hover {
        color: #1e2027;
        background-color: #00d95f;
        border-radius: 4px;
      }
    }
  }
  .footer {
    margin-top: 40px;
    .submit-btn {
      width: 100%;
      height: 70px;
      line-height: 70px;
      font-weight: bold;
      background-color: #00d95f;
      color: #1e2027;
      display: inline-block;
      cursor: pointer;
    }
  }
}
</style>
