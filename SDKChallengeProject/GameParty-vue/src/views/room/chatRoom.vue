/*
 * @Author: he.xiaoxue
 * @Date: 2018-08-30 15:58:03
 * @Last Modified by: xiaoxuehe
 * @Last Modified time: 2020-09-03 20:12:17
 */
<template>
  <div class="room">
    <!-- 房间背景 -->
    <div class="room-footer small">
      <span>下载G游客户端</span>
        <span class="ios"><a class="ios-href" href="http://file.guyuyin.com/b1005120761fbd26bb6d.zip">windows端</a></span>
      <span class="ios">ios下载
        <div class="platform">
          <div class="qrcode">
          <img :src="iosIcon" alt="">
          </div>
        </div>
      </span>
        <span class="android">安卓即将上线</span>
    </div>
    <div class="room-bg" v-if="!isWeixin && !isMobile" :style="'background-image:url('+ roomBg+')'">
      <div class="room-header small">
        <p>复制房间链接给好友加入,无需登陆即可直接加入房间语音</p>
        <span class="copy-url">{{codeEncrypt}}</span>
        <span class="copy-btn" @click="inviteFriend($event)">复制房间链接</span>
      </div>
      <div class="qq-group">
        <img :src="giftImg" alt="gift">
        <div class="right small white">
          <p>官方QQ群：902212747</p>
          <p>加入群组反馈</p>
        </div>
      </div>
    </div>

    <transition name="slide-fade" v-if="!isWeixin">
      <!-- 房间信息 -->
      <div class="room-container sm" v-if="isMobile==false">
        <!-- 房间顶部信息 操作栏 -->
        <div class="header small">
          <div class="left">
            <img class="title-img" :src="teamInfo.game.gameicon.url" alt="">{{teamInfo.game.gameName}}
            <span class="bloder">&nbsp;·&nbsp;</span>{{teamInfo.type.name}}
          </div>
          <div class="right gray">
            <span class="option" v-if="isWoner" @click="lockSetting" :class="{green:teamInfo.isLock}">{{teamInfo.isLock?'设为公开':'设为隐私'}}</span>
            <span class="option" v-if="isWoner" @click="userSettingModalShow=true">房间设置</span>
            <span class="option" v-if="!isAnonymous" @click="closeRoomModalShow= true" :style="'background-image:url('+ powerIcon+')'">{{isWoner?'解散房间':'离开队伍'}}</span>
          </div>
        </div>
        <!-- END OF 房间顶部信息 操作栏 -->
        <div class="body">
          <!-- 聊天室左侧 房间人员信息 麦克风调节等 -->
          <div class="left">
            <div class="title white">{{teamInfo.title}}</div>
            <!-- 参与者信息 -->
            <div class="participants">
              <span class="already" v-for="(item,index) in teamInfo.participants" :key="index+'1'"></span>
              <span class="vacancy" v-for="item in domainList.slice(0,teamInfo.maxnum-teamInfo.participants.length)" :key="item"></span>
              <span class="small" v-if="teamInfo.maxnum">{{teamInfo.participants.length}}/{{teamInfo.maxnum}}</span>
            </div>
            <!-- 群成员信息 -->
            <div class="members">
              <div class="member-item" v-for="user in participantsInfo" :key="user.objectId" @click="showUserInfo(user)">
                <img class="avatar" :src="user.avatar|avatarFilter" alt="">
                <p class="nickname">{{user.username}}</p>
                <img :src="offlineIcon" class="muted" v-if="!user.isOnline">
                <img :src="mutedIcon" class="muted" v-else-if="user.isMute">
                <img :src="speakIcon" class="speak" v-if="user.isSpeak">
              </div>
              <div class="member-item empty" @click="inviteFriend" v-for="item in domainList.slice(0,teamInfo.maxnum-teamInfo.participants.length)" :key="item"></div>
            </div>
            <!-- ENDOF 群成员信息 -->
            <!-- 操作栏 -->
            <div class="voicing">
              <div class="speak-btn middle" @click="changeMicro">{{isPublish?'正在说话...':'开启语音'}}</div>
              <div class="tuning">
                <!-- 声音 -->
                <div class="music">
                  <span class="icon"><img :src="musicIcon" alt=""></span>
                  <div class="bar" @click="turningEnter($event,1)">
                    <span class="bar-inner" :style="{width:musicPercent+4+'px'}"></span>
                    <span class="switch" :style="{left:musicPercent+'px'}"></span>
                  </div>
                </div>
                <!-- 麦克风 -->
                <div class="microphone">
                  <span class="icon"><img :src="microphoneIcon" alt=""></span>
                  <div class="bar" @click="turningEnter($event,2)">
                    <span class="bar-inner" :style="{width:microPercent+4+'px'}"></span>
                    <span class="switch" :style="{left:microPercent+'px'}"></span>
                  </div>
                </div>
              </div>
            </div>
            <!--ENDOF 操作栏 -->
          </div>
          <!-- END OF 聊天室左侧  -->
          <!-- 聊天室主体 -->
          <div class="right">
            <div class="notice white">
              <img :src="tipsIcon" style="margin-right:5px;">
              <!-- <span>公告：{{teamInfo.desc||'房主没有设置公告内容'}}</span> -->
              <span>公告：{{teamInfo.desc||'房主没有设置公告内容'}}</span>
            </div>
            <!-- 聊天框 -->
            <div class="chat-content">
              <vue-scroll ref="chat" :ops="scrollOptions" @handle-scroll="handleScroll" @handle-scroll-complete="handleComplete">
                <div v-for="(item, index) in messageList" :key="index">
                  <message :type="item.type" :msgInfo="item.content"></message>
                </div>
              </vue-scroll>
            </div>

            <!-- END OF 聊天框 -->
            <!-- 输入框组合 -->
            <div class="input-content">
              <textarea v-if="!isAnonymous" class="input-area" @keydown.enter="sendMessage" v-model="userInput"></textarea>
              <div v-else class="anonymous">
                <p class="gray">登录之后即可以发送消息及其他全部功能</p>
                <span class="btn-login login-wechat" @click="loginBy='wechat';loginModalShow=true"><img :src="wechatIcon" alt="">微信登录</span>
                <span class="btn-login login-phone" @click="loginBy='code';loginModalShow=true"><img :src="phoneIcon" alt="">验证码登录</span>
              </div>
              <!-- 按钮组合 -->
              <div class="btn-group">
                <div class="left-btn green">
                  <span class="btn" @click='inviteFriend($event)'>
                    <span class="icon-plus">+</span>
                    <span>邀请好友</span>
                  </span>
                  <span class="btn" @click="setTeamTop">在大厅中置顶</span>
                </div>
                <div class="right-btn">
                  <span class="btn btn-bg" @click="sendMessage" :class="{disabled:isAnonymous}">发送</span>
                </div>
              </div>
            </div>
            <!--  END OF 输入框组合 -->
          </div>
          <!-- END OF 聊天室主体 -->
        </div>
      </div>
      <!-- 移动端房间 -->
      <div class="room-container mobile" v-else>
        <div class="room-bg mobile-bg" v-if="isMobile">
          <!-- 移动端底部按钮 -->
          <div class="chat-footer">
            <span class="msg" @click="downloadAppModalShow=true"></span>
            <span class="music" @click="changeSubscribe" :class="{off:musicPercent<10}"></span>
            <span class="speak-btn" @click="changeMicro" :class="{publish:isPublish}">{{isPublish?'关闭说话':'开启语音'}}</span>
          </div>
          <!-- ENDOF移动端底部按钮 -->
          <!-- 邀请好友 -->
          <!-- <div class="share-content">
            <span class="plus"></span>
            呼唤队友
          </div> -->
          <!-- ENDOF邀请好友 -->

        </div>
        <download-app-modal v-if="isIOS" v-show="downloadAppModalShow" @closeModal="downloadAppModalShow=false"></download-app-modal>
        <div class="download-app" v-if="isIOS">
           <a href="https://itunes.apple.com/cn/app/id1433790115?mt=8" target="_blank">
           <img src="../../assets/download-app.png" alt="" srcset="">
           </a>
        </div>
        <div class="content">
          <!-- 队伍信息 -->
          <div class="team-info">
            <p class="title">{{teamInfo.title}}</p>
            <div class="">
              <span class="gamename">{{teamInfo.game.gameName}}</span>
              <span class="gametype">{{teamInfo.type.name}}</span>
              <span class="bloder">&nbsp;·&nbsp;</span>
              <span class="" v-if="teamInfo.maxnum">{{teamInfo.participants.length}}/{{teamInfo.maxnum}}人</span>
              <span class="look" v-if="teamInfo.isLock"></span>
            </div>
            <div class="notice white">
              <img :src="tipsIcon" style="margin-right:5px;">
              <span>公告：{{teamInfo.desc||'房主没有设置公告内容'}}</span>
            </div>
          </div>
          <!-- 成员信息 -->
          <div class="members">
            <div class="member-item" v-for="user in participantsInfo" :key="user.objectId" @click="showUserInfo(user)">
              <img class="avatar" :src="user.avatar|avatarFilter" alt="">
              <p class="nickname">{{user.username}}</p>
              <img :src="offlineIcon" class="muted" v-if="!user.isOnline">
              <img :src="mutedIcon" class="muted" v-else-if="user.isMute">
              <img :src="speakIcon" class="speak" v-if="user.isSpeak">
            </div>
            <div class="member-item" @click="inviteFriend" v-for="item in domainList.slice(0,teamInfo.maxnum-teamInfo.participants.length)" :key="item">
              <img class="avatar" src="../../assets/empty.png" alt="">
            </div>
          </div>
          <!-- ENDOF成员信息 -->
          <!-- 聊天框 -->
          <div class="chat-content">
            <vue-scroll ref="chat" :ops="scrollOptions" @handle-scroll="handleScroll" @handle-scroll-complete="handleComplete">
              <div v-for="(item, index) in messageList" :key="index">
                <message :type="item.type" :msgInfo="item.content"></message>
              </div>
            </vue-scroll>
          </div>
          <!-- ENDOF 聊天框 -->

        </div>

      </div>
    </transition>

    <div class="wechat-tips" v-if="isWeixin">
      <p> &#9312;点击右上角的按钮</p>
      <p> &#9313;选择或打开</p>
    </div>
    <!-- 用户信息 -->
    <user-info v-if="userInfoModal" :isWoner="isWoner" :show="userInfoModal" :userInfo="userInfoModalUser" @closeModal="userInfoModal=false" @kickedOneMember="kickedOneMember" @reportUser="reportUser"></user-info>
    <!-- 退出房间 -->
    <close-room-confirm v-if="closeRoomModalShow" :show="userInfoModal" @closeModal="closeRoomModalShow = false" @enSure="closeRoom"></close-room-confirm>
    <!-- 房间设置 -->
    <room-setting v-if="userSettingModalShow" :teamInfo="teamInfo" @update="updateTitleDesc" @closeModal="userSettingModalShow = false"></room-setting>
    <!-- 分享房间 -->
    <share-room v-if="shareRoomModalShow" :codeEncrypt="codeEncrypt" @closeModal="shareRoomModalShow=false"></share-room>
    <!-- 匿名用户登录 -->
    <user-login v-if="loginModalShow" :loginBy="loginBy" @closeModal="loginModalShow=false" @loginSucess="userInit"></user-login>
    <!-- 麦克风权限提示 -->
    <micro-auth v-if="microAuthModalShow" :openWay="openMicroModalWay" @closeModal="microAuthModalShow=false"></micro-auth>
    <!-- 举报用户 -->
    <report-user v-if="reportUserModalShow" :teamInfo="teamInfo" @update="updateTitleDesc" @closeModal="reportUserModalShow = false"></report-user>

    <div id="agora_local" style="display: none;"></div>
    <div id="agora_remote" style="display: none;"></div>
    <audio id="joinAudio" style="display: none;" :src="joinAudioSrc"></audio>
  </div>
</template>
<script>
// icon
import closeIcon from '@/assets/close.png'
import powerIcon from '@/assets/power.png'
import musicIcon from '@/assets/music.png'
import microphoneIcon from '@/assets/microphone.png'
import wechatIcon from '@/assets/wechat.png'
import tipsIcon from '@/assets/tips.png'
import phoneIcon from '@/assets/phone.png'
import roomBg from '@/assets/room_bg.png'
import defaultAvatar from '@/assets/avatar-default.png'
import mutedIcon from '@/assets/muted.png'
import offlineIcon from '@/assets/offline.png'
import speakIcon from '@/assets/speak.png'
import iosIcon from '@/assets/qrcode-ios.png'
import joinAudioSrc from '@/assets/sms-received2.mp3'
import giftImg from '@/assets/gift.png'

import { DB_TEAM } from '@/leanCloud/global'

import {
  getTeam,
  getTeamByCode,
  getParticipants,
  messageFormat,
  getUserExtra,
  xor_enc,
  getUserByUserId
} from '@/api/chatRoom'
import { getUserHistory, insertUserHistory, deleteUserHistory } from '@/api/gameLobby'
import { getWxAccessToken } from '@/api/wechat'
import * as Util from '@/api/util'

const { TextMessage, Event } = require('leancloud-realtime')
import scrollOptions from './scrollConfig'

// 用户信息弹窗
import UserInfo from '@/views/modal/userInfo'
import Message from './message'
import CloseRoomConfirm from './closeRoom'
import RoomSetting from './roomSetting'
import ShareRoom from './shareRoom'
import UserLogin from './userLogin'
import MicroAuth from './microAuth'
import ReportUser from '@/views/modal/reportUser'
import DownloadAppModal from './downloadApp'

import clip from '@/utils/clipboard' // use clipboard directly

export default {
  name: 'ChatRoom',
  props: {
    show: Boolean
  },
  components: {
    UserInfo,
    Message,
    CloseRoomConfirm,
    RoomSetting,
    ShareRoom,
    UserLogin,
    MicroAuth,
    ReportUser,
    DownloadAppModal
  },
  data() {
    return {
      joinAudioSrc: joinAudioSrc,
      closeIcon: closeIcon, // tupi
      roomBg: roomBg,
      powerIcon: powerIcon,
      musicIcon: musicIcon,
      microphoneIcon: microphoneIcon,
      wechatIcon: wechatIcon,
      phoneIcon: phoneIcon,
      tipsIcon: tipsIcon,
      defaultAvatar: defaultAvatar,
      mutedIcon: mutedIcon,
      offlineIcon: offlineIcon,
      speakIcon: speakIcon,
      iosIcon: iosIcon,
      giftImg: giftImg,
      domainList: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
      musicPercent: 25,
      microPercent: 25,
      isDrag: false,
      dragStartX: 0,
      scrollOptions: scrollOptions,
      teamInfo: {
        game: {
          gameicon: {}
        },
        type: {},
        participants: []
      }, // 组队信息
      participantsInfo: null,
      userInfoModal: false, // 用户信息弹窗展示条件
      userInfoModalUser: null, // 展示的用户信息
      isWoner: false, // 当前登录用户是否为队长
      currentUser: null,
      currentUserId: null, // 当前登录用户id
      isAnonymous: false,
      userInput: '', // 用户输入
      messageList: [
        {
          // 消息列表
          type: 'tips'
        }
      ],
      rtcClient: null,
      rtcLocalStream: null,
      closeRoomModalShow: false, // 关闭房间弹窗展示状态
      userSettingModalShow: false,
      shareRoomModalShow: false,
      loginModalShow: false,
      reportUserModalShow: false,
      downloadAppModalShow: false,
      codeEncrypt: null, // 加密后的code
      loginBy: null, // 登录方式
      isPublish: true, // 语音发布标志
      isSubscribe: true, // 订阅远程视频流状态
      mutedUsers: [], // 闭麦用户
      onlineUsers: [], // 闭麦用户
      microAuthModalShow: false,
      openMicroModalWay: null,
      rtcRemoteStream: [],
      isPlaying: false,
      isWeixin: false,
      isMobile: Boolean,
      isIOS: Boolean,
      isTeamCodeJoin:false
    }
  },
  filters: {
    avatarFilter(avatar) {
      if (avatar) {
        return avatar.url
      } else {
        return defaultAvatar
      }
    }
  },
  watch: {
    mutedUsers(newVal, oldVal) {
      const newValStr = ',' + newVal.join(',') + ','
      this.participantsInfo.map(item => {
        if (newValStr.indexOf(',' + item.userId + ',') > -1) {
          item.isMute = true
        } else {
          item.isMute = false
        }
        return item
      })
    },
    onlineUsers(newVal, oldVal) {
      const newValStr = ',' + newVal.join(',') + ','
      this.participantsInfo.map(item => {
        if (newValStr.indexOf(',' + item.objectId + ',') > -1) {
          item.isOnline = true
        } else {
          item.isOnline = false
        }
        return item
      })
    }
  },
  mounted() {},
  beforeCreate() {
    function is_weixin() {
      var ua = navigator.userAgent.toLowerCase()
      if (ua.match(/MicroMessenger/i) == 'micromessenger') {
        return true
      } else {
        return false
      }
    }
    this.isWeixin = is_weixin()
  },
  created() {
    this.isMobileFun()

    if (!this.isWeixin) {
      this.userInit()
    }
  },
  destroyed() {
    console.log('>>>>> room destroyed')
    const leaveRtc = () => {
      return new Promise((reslove, reject) => {
        this.rtcClient.leave(
          () => {
            reslove()
          },
          () => {
            const err = { code: 2 }
            reject(err)
          }
        )
      })
    }
    if (this.rtcClient) {
      leaveRtc().then(() => {
        console.log('>>>>> quit rtc success')
      })
      if (this.LeanRT.imClient) {
        this.LeanRT.imClient.off(Event.KICKED) //  当前用户被移出
      }
    }
  },
  methods: {
    userInit() {
      const current = this.AV.User.current()
      const wxCode = this.$route.query.code
      if (wxCode && current && current.isAnonymous()) {
        this.currentUser = this.AV.User.current().toJSON()
        this.currentUserId = this.currentUser.objectId
        // 当前匿名用户登录 且url中有wxCode
        this.getWxUserInfo(wxCode)
        return
      }

      if (current) {
        // 当前有登录用户
        if (current.isAnonymous()) {
          // 当前登录用户为匿名用户
          this.isAnonymous = true
        } else {
          this.isAnonymous = false
        }
        this.currentUser = this.AV.User.current().toJSON()
        this.currentUserId = this.currentUser.objectId
        this.getTeamInfo()
      } else {
        // 当前无登录用户，调用匿名登录
        this.AV.User.loginAnonymously()
          .then(user => {
            this.currentUser = this.AV.User.current().toJSON()
            this.currentUserId = this.currentUser.objectId
            this.isAnonymous = true
            this.getTeamInfo()
          })
          .catch(function(error) {
            // 异常处理
            console.error(error)
          })
      }
    },
    getWxUserInfo(wxCode) {
      let access_token = ''
      let openid = ''
      let current = null
      const wxUserInfo = {}

      const newQuery = {
        code: wxCode
      }
      let routeParam = {
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
          // 调用微信登录
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
          console.log('>>>>登录成功', user)
          this.currentUser = user.toJSON()
          this.currentUserId = this.currentUser.objectId
          if (this.currentUser.username.length > 20) {
            // 判断用户是否登录过
            return this.AV.Cloud.run('getWXUserDetail', { access_token: access_token, openid: openid })
          } else {
            this.getTeamInfo()
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
          this.getTeamInfo()
        })
        .catch(error => {
          console.log(error)
          this.$hx_toast({ message: '微信登录失败', time: 1500 })
          routeParam = {
            query: ''
          }
          this.$router.push(routeParam)
          this.userInit()
        })
    },
    getTeamInfo() {
      const teamId = this.$route.params.teamId
      let teamCode = this.$route.params.teamCode
      if (teamId) {
        getTeam(teamId)
          .then(res => {
            // 获取队伍信息成功
            this.teamInfo = res
            this.inviteFriend()
            this.userJoinTeam()
            return getParticipants(res.participants)
          })
          .then(participants => {
            // 获取队伍成员信息成功
            this.participantsInfo = participants
            this.getOnlineUser(this.teamInfo.participants)
          })
          .catch(error => {
            // 异常处理
            console.log(error)
          })
      } else if (teamCode) {
        // this.AV.User.logOut()
        teamCode = parseInt(xor_enc(teamCode, 'AAAAAAAAAAAAAAAAAAAAAAAAAA'))
        getTeamByCode(teamCode)
          .then(res => {
            // 获取队伍信息成功
            this.teamInfo = res
            this.inviteFriend()
            console.log("获取队伍信息成功");
            this.isTeamCodeJoin = true;
            this.userJoinTeam()
            return getParticipants(res.participants)
          })
          .then(participants => {
            // 获取队伍成员信息成功
            this.participantsInfo = participants
          })
          .catch(error => {
            if (error.code === 404) {
              this.$hx_toast({ message: '队伍不存在', time: 1500 })
              setTimeout(() => {
                this.$router.replace({ path: '/' })
              }, 1500)
            }
            // 异常处理
            console.log(error)
          })
      }
    },
    upDateTeamInfo() {
      const teamId = this.teamInfo.objectId

      getTeam(teamId)
        .then(res => {
          // 获取队伍信息成功
          this.teamInfo = res
          return getParticipants(res.participants)
        })
        .then(participants => {
          // 获取队伍成员信息成功
          this.onlineUsers.push('test')
          this.onlineUsers.pop()
          this.mutedUsers.push('test')
          this.mutedUsers.pop()
          this.participantsInfo = participants
        })
        .catch(error => {
          // 异常处理
          console.log(error)
        })
    },
    /**
     * 用户加入房间
     */
    userJoinTeam() {
      const teamInfo = this.teamInfo
      const currentUserId = this.currentUserId
      const isInArray = (item, arr) => {
        let falg = false
        if (!arr) return false
        arr.forEach(arrItem => {
          // console.log(arrItem)
          if (item == arrItem) {
            falg = true
            return
          }
        })
        return falg
      }
      const goHome = msg => {
        // 发生错误跳转
        this.$hx_toast({ message: msg || '正在回到首页...', time: 2000 })
        setTimeout(() => {
          this.$router.replace({ path: '/' })
        }, 2000)
      }
      if (teamInfo.delete) {
        goHome('房间已不存在')
        return
      }

      if (isInArray(currentUserId, teamInfo.participants)) {
        // 当前用户为房间队员
        // 判断当前登录用户是否为队长
        if (currentUserId == teamInfo.publisher.objectId) {
          this.isWoner = true
          const teamObj = this.AV.Object.createWithoutData(DB_TEAM, teamInfo.objectId)
          teamObj.set('hidden', false)
          teamObj
            .save()
            .then(() => {
              console.log('>>>>> : room hidden false')
            })
            .catch(error => {
              console.log('>>>>> : set room hidden error', error)
            })
        }
        this.coversationInit()
        this.rtcInit(this.teamInfo.teamCode)
        return
      }
      // 浏览器不支持 不执行加入房间操作
      if (!AgoraRTC.checkSystemRequirements()) {
        if (!this.microAuthModalShow) {
          this.openMicroModalWay = 'system'
          this.microAuthModalShow = true
        }
        return
      }
      if (isInArray(currentUserId, teamInfo.outUserId)) {
        // 判断是否为被踢出房间用户
        goHome('您已被队长请出,请勿重复加入')
        return
      }
      if ((teamInfo.isLock || teamInfo.maxnum <= teamInfo.participants.length) && this.isTeamCodeJoin != true) {
        const msg = teamInfo.isLock ? '房间已上锁，不可加入' : '房间已满员，不可加入'
        goHome(msg)
      } else {
        getUserHistory(currentUserId).then(res => {
          if (res.length >= 1) {
            goHome('您当前已有组队')
          } else {
            // 更新team表 participants 字段
            const teamObj = this.AV.Object.createWithoutData(DB_TEAM, teamInfo.objectId)
            teamObj.addUnique('participants', currentUserId)
            teamObj
              .save()
              .then(res => {
                this.teamInfo.participants.push(currentUserId)
                if (!this.isAnonymous) {
                  // insertUserHistory(teamInfo.game.objectId, teamInfo.objectId, teamInfo.conversationId) // 用户历史表中插入数据
                }
                // 判断当前登录用户是否为队长
                if (currentUserId == this.teamInfo.publisher.objectId) {
                  this.isWoner = true
                }
                // 加入会话，加入语音
                this.coversationInit()
                this.rtcInit(this.teamInfo.teamCode)
              })
              .catch(error => {
                // 异常处理
                console.log(error)
              })
          }
        })
      }
    },
    /**
     * 用户信息注册
     */
    coversationInit() {
      this.getCurrentConversation(this.teamInfo.conversation.objectId)
    },
    rtcInit(teamCode) {
      // return
      AgoraRTC.Logger.setLogLevel(AgoraRTC.Logger.NONE)
      // AgoraRTC.Logger.setLogLevel(AgoraRTC.Logger.ERROR)
      // AgoraRTC.Logger.setLogLevel(AgoraRTC.Logger.WARNING)
      // AgoraRTC.Logger.setLogLevel(AgoraRTC.Logger.INFO)
      // AgoraRTC.Logger.setLogLevel(AgoraRTC.Logger.DEBUG)

      const that = this

      // 创建 初始化 Client 对象
      const rtcClientInit = () => {
        this.rtcClient = AgoraRTC.createClient({ mode: 'h264_interop', codec: 'h264' })
        // console.log(this.rtcClient)
        // this.rtcClient.enableWebSdkInteroperability()
        return new Promise((reslove, reject) => {
          this.rtcClient.init(
            '565813b5fefd4804a1303dc32d9e8a38',
            () => {
              reslove()
            },
            error => {
              reject(error)
            }
          )
        })
      }
      // 加入语音
      const rtcClientJoin = (channelKey, channel) => {
        return new Promise((resolve, reject) => {
          const userId = this.currentUser.userId
          console.log(channelKey, channel, userId)
          this.rtcClient.join(
            channelKey,
            channel,
            userId,
            uid => {
              resolve(uid)
            },
            error => {
              console.log('加入失败', error)
              reject(error)
            }
          )
        })
      }
      // 创建音频流 初始化音频流
      const localStreamInit = uid => {
        const localStream = AgoraRTC.createStream({
          streamID: uid,
          audio: true,
          video: false,
          screen: false
        })
        that.rtcLocalStream = localStream
        // 允许使用麦克风
        that.rtcLocalStream.on('accessAllowed', function() {
          console.log('accessAllowed')
        })
        // 用户禁止使用麦克风
        that.rtcLocalStream.on('accessDenied', function() {
          console.log('accessDenied')
          if (!that.microAuthModalShow) {
            that.microAuthModalShow = true
            that.openMicroModalWay = 'auth'
          }
        })
        localStream.init(() => {
          that.rtcLocalStream = localStream
          localStream.play('agora_local')
          // console.log()
          // 发布本地音频流
          that.rtcClient.publish(localStream, function(err) {
            console.log('发布本地音频流失败 ' + err)
          })
        })
      }
      if (AgoraRTC.checkSystemRequirements()) {
        // 检查浏览器兼容性
        rtcClientInit()
          .then(() => {
            return this.AV.Cloud.run('getDymicKey', { channelName: teamCode.toString() })
          })
          .then(channelKey => {
            return rtcClientJoin(channelKey, teamCode.toString())
          })
          .then(uid => {
            localStreamInit(uid)
          })
          .catch(error => {
            console.log('>>>> error', error)
          })
        // 注册事件
        this.rtcClientEvent()
      } else {
        if (!this.microAuthModalShow) {
          this.openMicroModalWay = 'system'
          this.microAuthModalShow = true
        }
        // this.$hx_toast({ message: '当前浏览器不支持语音通话，建议您切换浏览器哦~', time: 2000 })
      }
    },
    // rtc 回调事件
    rtcClientEvent() {
      const that = this
      const uidToObjectId = uid => {
        for (let i = 0; i < that.participantsInfo.length; i++) {
          if (that.participantsInfo[i].userId == uid) {
            return that.participantsInfo[i].objectId
          }
        }
        return -1
      }
      // 本地音视频已发布回调事件
      this.rtcClient.on('stream-published', function(evt) {
        console.log('本地视频流已发布')
      })
      // 远程音视频流已添加回调事件
      this.rtcClient.on('stream-added', evt => {
        var stream = evt.stream
        const uid = stream.streamId
        that.rtcRemoteStream.push(stream)

        const objectId = uidToObjectId(uid)
        const indexOnline = that.onlineUsers.indexOf(objectId)
        if (indexOnline === -1) {
          that.onlineUsers.push(objectId)
        }
        const indexMuted = that.mutedUsers.indexOf(uid)
        if (indexMuted !== -1) {
          that.$delete(that.mutedUsers, indexMuted)
        }

        if (that.isSubscribe) {
          // 订阅远程视频流
          this.rtcClient.subscribe(stream, function(err) {
            console.log('订阅远程视频流失败', err)
          })
        }
      })
      // 远程音视频流已订阅回调事件
      this.rtcClient.on('stream-subscribed', function(evt) {
        var remoteStream = evt.stream
        remoteStream.play('agora_remote')
      })
      // 对方用户离开回调
      this.rtcClient.on('peer-leave', function(evt) {
        var uid = evt.uid
        // ……
        console.log(evt)
        const index = () => {
          for (let i = 0; i < that.rtcRemoteStream.length; i++) {
            if (that.rtcRemoteStream[i].streamId == uid) {
              return i
            }
          }
        }
        /*   const indexOnline = that.onlineUsers.indexOf(uid)
          that.onlineUsers.splice(indexOnline, 1) */
        that.rtcRemoteStream.splice(index(), 1)

        const objectId = uidToObjectId(uid)
        const indexOnline = that.onlineUsers.indexOf(objectId)
        if (indexOnline !== -1) {
          that.onlineUsers.splice(indexOnline, 1)
        }
      })
      // 对方静音
      this.rtcClient.on('mute-audio', evt => {
        var uid = evt.uid
        const index = that.mutedUsers.indexOf(uid)
        if (index == -1) {
          that.mutedUsers.push(uid)
          console.log(that.mutedUsers)
        }

        // alert('mute audio:' + uid)
      })
      // 对方打开声音
      this.rtcClient.on('unmute-audio', function(evt) {
        var uid = evt.uid
        const index = that.mutedUsers.indexOf(uid)
        that.$delete(that.mutedUsers, index)
        // that.mutedUsers = Util.arrayReomve(this.mutedUsers, uid)
      })
      // 谁在说话
      this.rtcClient.on('active-speaker', function(evt) {
        var uid = evt.uid
        if (uid != undefined) {
          const index = (() => {
            for (let i = 0; i < that.participantsInfo.length; i++) {
              if (that.participantsInfo[i].userId == uid) {
                return i
              }
            }
          })()
          that.participantsInfo[index].isSpeak = true
          setTimeout(() => {
            that.participantsInfo[index].isSpeak = false
          }, 2000)
          console.log(uid + '在说话==========================================')
        }
      })
    },
    /**
     * 获取当前的coversation 取最新的20条message
     * @param{string} 会话的id
     */
    getCurrentConversation(conversationId) {
      const get = () => {
        console.log(this.teamInfo.participants)
        // this.getOnlineUser(this.teamInfo.participants)
        this.LeanRT.imClient
          .getConversation(conversationId)
          .then(conversation => {
            // 加入会话
            return conversation.join()
          })
          .then(conversation => {
            this.LeanRT.conversation = conversation
            console.log('>>>>> 获取conversation', conversation)

            this.imClientEvent() // 群聊事件注册
            // if (this.isAnonymous || this.isWoner) {
              // 匿名用户发送遗愿消息
              this.sendWillMessage()
            // }

            return this.LeanRT.conversation.queryMessages({
              limit: 20
            })
          })
          .then(message => {
            this.messageList = this.messageList.concat(messageFormat(message))
            this.scrollTo(0, '100%')
            console.log('>>>>> 获取message 成功')
          })
          .catch(error => {
            console.log('>>>>> 异常处理', error)
          })
      }
      if (!this.LeanRT.imClient) {
        // 未登录会话 尝试再次登录
        this.LeanRT.realtime
          .createIMClient(this.currentUserId)
          .then(imClient => {
            this.LeanRT.imClient = imClient // 将创建的client对象挂在在VUE下的LeanRT中
            get()
          })
          .catch(error => {
            console.error(error)
          })
      } else {
        // 已登录 直接获取
        get()
      }
    },
    /**
     * 发送消息
     */
    sendMessage() {
      if (this.userInput.length == 0) {
        return
      }
      if (this.isAnonymous) {
        // TODO:
        // alert('匿名用户')
        return
      }
      const message = this.currentUser.username + ':' + this.userInput
      console.log(new TextMessage(message))
      this.LeanRT.conversation.send(new TextMessage(message)).then(res => {
        console.log(res)
        this.messageList.push(messageFormat([res])[0])
        console.log('发送成功')
        this.userInput = ''
        this.scrollTo(0, '100%')
      })
    },
    // 发送意愿消息
    sendWillMessage() {
      var message = new TextMessage('用户已下线')
      const msgData = {
        isWill: true,
        teamId: this.teamInfo.objectId
      }
      if (this.isWoner) {
        msgData.isPublish = true
      }
      message.setAttributes(msgData)

      this.LeanRT.conversation
        .send(message, { will: true })
        .then(function() {
          // 发送成功，当前 client 掉线的时候，这条消息会被下发给对话里面的其他成员
        })
        .catch(function(error) {
          // 异常处理
          console.log(error)
        })
    },
    /**
     * 聊天室回调事件
     */
    imClientEvent() {
      // 新用户加入
      const membersJoinedHandler = payload => {
        // TODO: 新用户姓名可点击 新用户加入
        const members = payload.members
        getParticipants(members)
          .then(res => {
            res.forEach(newMember => {
              if (newMember.objectId != this.currentUserId) {
                this.autioPlay()
              }
              const item = {
                type: 'newMember',
                content: { userName: newMember.username, text: '加入了房间' }
              }
              // 新用户加入默认在线
              const indexOnline = this.onlineUsers.indexOf(newMember.objectId)
              if (indexOnline === -1) {
                this.onlineUsers.push(newMember.objectId)
              }

              this.upDateTeamInfo()
              this.messageList.push(item)
              this.scrollTo(0, '100%')
            })
          })
          .catch(error => {
            // 异常处理
            console.log(error)
          })
      }
      // 成员退出
      const membersLeftHandler = payload => {
        const members = payload.members
        getParticipants(members)
          .then(res => {
            res.forEach(leftMember => {
              const item = {
                type: 'newMember',
                content: { userName: leftMember.username, text: '退出了房间队伍' }
              }
              this.upDateTeamInfo()
              this.messageList.push(item)
              this.scrollTo(0, '100%')
            })
          })
          .catch(error => {
            // 异常处理
            console.log(error)
          })
      }
      // 被移出群聊
      const kickedHandler = payload => {
        if (payload.kickedBy != this.currentUserId) {
          const msg = `您已被群主移出对话`
          this.$hx_toast({ message: msg, time: 2000 })

          this.LeanRT.conversation.quit()
          this.rtcClient.leave(
            () => {
              this.$router.replace({ path: '/' })
            },
            () => {}
          )
        }
        // setTimeout(() => {
        //   this.$router.replace({ path: '/' })
        // }, 2000)
      }
      // 接收消息
      const messageUpdater = msg => {
        // 如果收到未知类型的暂态消息，直接丢弃
        if (msg.transient && msg.type === Message.TYPE) {
          return
        }
        // 数据添加 列表滚动
        if (messageFormat([msg]).length > 0) {
          this.messageList.push(messageFormat([msg])[0])
          this.scrollTo(0, '100%')
        }
      }
      this.LeanRT.imClient.on(Event.MEMBERS_JOINED, membersJoinedHandler) // 新用户加入
      this.LeanRT.imClient.on(Event.MEMBERS_LEFT, membersLeftHandler) // 成员退出
      this.LeanRT.imClient.on(Event.KICKED, kickedHandler) //  当前用户被移出
      this.LeanRT.imClient.on(Event.MESSAGE, messageUpdater) // 新消息

      // this.LeanRT.imClient.on(Event.INFO_UPDATED, infoUpdatedHandler); //群设置修改
      // this.LeanRT.imClient.on(Event.MEMBER_INFO_UPDATED, memberInfoUpdateHandler); //成员信息修改
      //  this.LeanRT.imClient.on(Event.LAST_READ_AT_UPDATE, receiptUpdateHandler); //已读回执
      //  this.LeanRT.imClient.on(Event.MESSAGE, readMarker);// 新消息
      //  this.LeanRT.imClient.on(Event.LAST_DELIVERED_AT_UPDATE, receiptUpdateHandler); // 送达回执
      //  this.LeanRT.imClient.on(Event.MESSAGE_RECALL, replaceRecalledMessage);// 撤回消息
    },
    /**
     * 展示用户信息
     * @param {object} user 用户信息
     */
    showUserInfo(user) {
      const currentUserId = this.currentUserId
      // if (currentUserId === user.objectId) {
      //   // 点击的自己头像返回
      //   // return false
      // }
      this.userInfoModalUser = Object.assign({}, user)
      // TODO: 获取用户的其他信息
      getUserExtra(user.objectId)
        .then(res => {
          if (res) {
            this.userInfoModalUser.tuandui = res.tuandui
            this.userInfoModalUser.taidu = res.taidu
            this.userInfoModalUser.leyu = res.leyu
            this.userInfoModalUser.zuijia = res.zuijia
          } else {
            this.userInfoModalUser.tuandui = 0
            this.userInfoModalUser.taidu = 0
            this.userInfoModalUser.leyu = 0
            this.userInfoModalUser.zuijia = 0
          }
          this.userInfoModal = true
          console.log('>>>>> 用户其他信息', res)
        })
        .catch(error => {
          console.log(error)
        })
    },
    turningMove(e) {
      // 1.判断是否为拖拽状态 非拖拽状态return
      // 2.元素left = 原来的位置 + 移动的位置（右移正 左移负）
      if (!this.isDrag) return
      const moveX = e.clientX - this.dragStartX
      let left = this[this.activeDrag] + moveX / 10
      if (left < 0) {
        left = 0
      } else if (left > 100) {
        left = 100
      }
      this[this.activeDrag] = left
    },
    /**
     * 鼠标在调节按钮上点击事件，拖拽开始
     * @param {object} e mouseEvent
     * @param {number} type 1 调节背景音乐 2 调节麦克风
     */
    turningEnter(e, type) {
      // 1.拖拽状态 为true
      // 2.记录当前控制的left
      if (type === 1) {
        this.changeSubscribe()
      } else if (type === 2) {
        this.changeMicro()
      }
      // this.rtcLocalStream.disableAudio()
    },
    handleComplete() {
      // console.log('>>>> scroll complete！')
    },
    /**
     * 关闭麦克风
     */
    changeMicro() {
      const uid = this.rtcLocalStream.streamId
      if (this.isPublish) {
        this.microPercent = 5
        this.rtcLocalStream.disableAudio() // 禁用音频轨道
        this.mutedUsers.push(uid)
      } else {
        this.microPercent = 25
        this.rtcLocalStream.enableAudio() // 启用音频轨道
        const index = this.mutedUsers.indexOf(uid)
        this.$delete(this.mutedUsers, index)
      }
      this.isPublish = !this.isPublish
    },
    changeSubscribe() {
      if (this.isSubscribe) {
        this.musicPercent = 5
        this.rtcRemoteStream.forEach(item => {
          // 取消订阅远程视频流
          this.rtcClient.unsubscribe(item, function(err) {
            console.log('取消订阅远程视频流失败', err)
          })
        })
        /*  const videoArr = document.querySelectorAll('video')
         for (let i = 0; i < videoArr.length; i++) {
           videoArr[i].muted = true
         } */
      } else {
        this.musicPercent = 25
        this.rtcRemoteStream.forEach(item => {
          // 订阅远程视频流
          this.rtcClient.subscribe(item, function(err) {
            console.log('订阅远程视频流失败', err)
          })
        })
        /*    const videoArr = document.querySelectorAll('video')
           for (let i = 0; i < videoArr.length; i++) {
             videoArr[i].muted = false
           } */
      }

      this.isSubscribe = !this.isSubscribe
    },
    /**
     * @param {Object} vertical    y方向滚动距离
     * @param {Object} horizontal  x方向滚动距离
     * @param {Object} nativeEvent 原生滚动事件
     */
    handleScroll(vertical, horizontal, nativeEvent) {
      // console.log('>>>>> scroll:')
      // console.log(vertical, horizontal, nativeEvent)
    },
    // 滚动到一个地方
    scrollTo(x, y) {
      // dom 更新后执行 否则页面没加载完毕，滚动百分比不对
      this.$nextTick().then(() => {
        if (this.$refs['chat']) {
          this.$refs['chat'].scrollTo(
            {
              x: x,
              y: y
            },
            true
          )
        }
      })
    },
    // 每次滚动一段距离
    scrollBy() {
      this.$nextTick().then(res => {
        // console.log(res)
        this.$refs['chat'].scrollBy({
          dx: 10,
          dy: '10%'
        })
      })
    },
    /**
     * 关闭当前房间
     */
    closeRoom() {
      if (this.isWoner) {
        // 队长关闭房间

        const participants = this.teamInfo.participants
        // deleteUserHistory(participants)
        const quitCoversation = () => {
          return new Promise((reslove, reject) => {
            this.LeanRT.conversation
              .remove(participants)
              .then(() => {
                return this.LeanRT.conversation.quit()
              })
              .then(() => {
                reslove()
              })
              .catch(() => {
                reject()
              })
          })
        }
        const leaveRtc = () => {
          return new Promise((reslove, reject) => {
            this.rtcClient.leave(
              () => {
                reslove()
              },
              () => {
                const err = { code: 2 }
                reject(err)
              }
            )
          })
        }
        const updateTeam = () => {
          // 更新team表 participants 字段
          return new Promise((reslove, reject) => {
            const teamObj = this.AV.Object.createWithoutData(DB_TEAM, this.teamInfo.objectId)
            teamObj.set('delete', true)
            teamObj
              .save()
              .then(() => {
                reslove()
              })
              .catch(error => {
                const err = { code: 3 }
                reject(err)
              })
          })
        }
        const closeRoomFun = async () => {
          if (AgoraRTC.checkSystemRequirements()) {
            await quitCoversation()
            await leaveRtc()
          }
          await updateTeam()
          this.$router.replace({ path: '/' })
        }
        closeRoomFun()
      } else {
        // 用户退出房间
        // deleteUserHistory([this.currentUserId])
        const quitCoversation = () => {
          return new Promise((reslove, reject) => {
            this.LeanRT.conversation
              .quit()
              .then(() => {
                reslove()
              })
              .catch(error => {
                const err = { code: 1 }
                reject(err)
              })
          })
        }
        const leaveRtc = () => {
          return new Promise((reslove, reject) => {
            this.rtcClient.leave(
              () => {
                reslove()
              },
              () => {
                const err = { code: 2 }
                reject(err)
              }
            )
          })
        }
        const updateParticipants = () => {
          // 更新team表 participants 字段
          return new Promise((reslove, reject) => {
            const teamObj = this.AV.Object.createWithoutData(DB_TEAM, this.teamInfo.objectId)
            teamObj.remove('participants', this.currentUserId)
            teamObj
              .save()
              .then(() => {
                reslove()
              })
              .catch(error => {
                const err = { code: 3 }
                reject(err)
              })
          })
        }
        const closeRoomFun = async () => {
          if (AgoraRTC.checkSystemRequirements()) {
            await quitCoversation()
            await leaveRtc()
          }
          await updateParticipants()
          this.$router.replace({ path: '/' })
        }
        closeRoomFun()
      }
    },
    // 房间上锁解锁
    lockSetting() {
      const isLock = !this.teamInfo.isLock
      const msg = isLock ? '当前房间为隐私状态,他人只有通过邀请链接才能加入' : '当前房间为公开状态,任何人均可加入'
      const teamObj = this.AV.Object.createWithoutData(DB_TEAM, this.teamInfo.objectId)
      teamObj.set('isLock', isLock)
      teamObj
        .save()
        .then(() => {
          const item = {
            type: 'newMember',
            content: { userName: '', text: msg }
          }
          this.messageList.push(item)
          this.scrollTo(0, '100%')
          this.teamInfo.isLock = isLock
        })
        .catch(error => {
          console.error()
        })
    },
    // 置顶
    setTeamTop() {
      if (this.isAnonymous) {
        // TODO:
        // alert('匿名用户')
        return
      }
      const msg = '房间已被置顶.将获得更多曝光'
      const teamObj = this.AV.Object.createWithoutData(DB_TEAM, this.teamInfo.objectId)
      teamObj.set('isTop', true)
      teamObj.set('sortDate', new Date())
      teamObj
        .save()
        .then(() => {
          const item = {
            type: 'newMember',
            content: { userName: '', text: msg }
          }
          this.messageList.push(item)
          this.scrollTo(0, '100%')
          this.teamInfo.isTop = true
        })
        .catch(error => {
          console.error()
        })
    },
    // 邀请好友
    inviteFriend(event) {
      // 利用teamcode 3des加密 获得链接
      // 用户打开后再解密
      const teamCode = this.teamInfo.teamCode
      const url = location.hostname + '/' + xor_enc(teamCode.toString(), 'AAAAAAAAAAAAAAAAAAAAAAAAAA')
      const game = this.teamInfo.game.gameName + ':'
      const teamTitle = this.teamInfo.title + '，点击链接加入我的房间'

      // 加密
      this.codeEncrypt = url
      const clipText = '我正在使用G游语音,打开链接加入我的语音房间\(若被屏蔽请手动补充3w和点c欧m\)，' + url + ''
      if (event) {
        clip(clipText, event)
        if (event.currentTarget.className !== 'copy-btn') {
          this.shareRoomModalShow = true
        } else {
          this.$hx_toast({ message: '复制成功，快去分享给好友吧~', time: 1500 })
        }
      }
    },
    decryptByDES(ciphertext) {
      const keyHex = CryptoJS.enc.Utf8.parse('guyuyincomdevelopeeeeeee')
      // direct decrypt ciphertext
      const options = {
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7,
        iv: CryptoJS.enc.Utf8.parse('01234567') // 向量
      }
      const decrypted = CryptoJS.TripleDES.decrypt(
        {
          ciphertext: CryptoJS.enc.Base64.parse(ciphertext)
        },
        keyHex,
        options
      )
      return decrypted.toString(CryptoJS.enc.Utf8)
    },
    updateTitleDesc(data) {
      console.log(data)
      this.teamInfo.title = data.title
      this.teamInfo.desc = data.desc
      this.$router.go(0)
    },
    // 踢出用户
    kickedOneMember(memberId) {
      // deleteUserHistory([memberId])
      this.LeanRT.conversation.remove([memberId]).then(res => {
        const teamObj = this.AV.Object.createWithoutData(DB_TEAM, this.teamInfo.objectId)
        teamObj.remove('participants', memberId)
        teamObj.addUnique('outUserId', memberId)
        teamObj.save()
        this.userInfoModal = false
      })
    },
    // 举报用户
    reportUser(memberId) {
      this.userInfoModal = false
      this.reportUserModalShow = true
      console.log('举报用户' + memberId)
    },
    autioPlay() {
      var audio = document.querySelector('#joinAudio')
      // if (!this.isPlaying) {
      audio.play()
      this.isPlaying = true
      // }
    },
    autioStop() {
      var audio = document.querySelector('#joinAudio')
      if (this.isPlaying) {
        audio.pause()
        audio.currentTime = 0
      }
    },
    // 初始化获取imclient在线用户
    getOnlineUser(objectIds) {
      objectIds = this.teamInfo.participants
      console.log(objectIds)
      this.LeanRT.imClient
        .ping(objectIds)
        .then(res => {
          this.onlineUsers = res
        })
        .catch(err => {
          console.log(err)
        })
    },
    isMobileFun() {
      const isMobile = navigator.userAgent.match(
        /(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i
      )
      const isIOS = navigator.userAgent.match(/(iPhone)/i)
      this.isMobile = !!isMobile
      this.isIOS = !!isIOS
    }
  }
}</script>

<style lang="scss">
// @import '@/styles/room.scss';
</style>
