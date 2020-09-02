<template>
  <transition name="mask-bg-fade">
    <div class="modal" v-if="show">
      <!--遮罩 -->
      <div class="mask_bg" @click="closeModal"></div>
      <transition name="slide-fade">
        <!-- 弹窗内容 修改用户信息 -->
        <div class="modal-container user-edit samll" v-if="edit">
          <div class="box_row">
            <div class="box_row_item">
              <div class="head_img_btn">
                <img :src="form.avatar" class="icon_img" />
                <div class="vertical">
                  <span class="btn_up" @click="changeAvatar">上传头像</span>
                </div>
                <my-upload field="img" @crop-success="cropSuccess" v-model="uploadAvatarShow" img-format="png" :width="50" :height="50">></my-upload>
              </div>
            </div>
            <div class="box_row_item">
              <div class="head_img_btn">
                <span class="text1 text3">昵&#8195;&#8195;称 :</span>
                <input class="input_item text border-bottom" type="text" v-model="form.username" maxlength="12">
                <span class="count-input">{{form.username.length||0}}/12</span>
              </div>
            </div>
            <!-- <div class="box_row_item">
              <div class="head_img_btn">
                <span class="text1 text3">SteamID: </span>
                <input class="input_item text  border-bottom" type="text" v-model="form.steamID"></div>
            </div> -->
            <div class="box_row_item">
              <div class="head_img_btn">
                <span class="text1 text3">年龄: </span>
                <input class="input_item text  border-bottom" v-model.number="form.age"></div>
            </div>
            <!-- <div class="box_row_item">
              <div class="head_img_btn">
                <span class="text1 text3">出生年份:</span>
                <select v-model="selected" class="input_item text select-item">
                  <option v-for="item in items" v-bind:value="item.value" :key="item.text">
                    {{item.text}}
                  </option>
                </select>
              </div>
            </div> -->
            <div class="box_row_item">
              <div class="head_img_btn">
                <span class="text1 text3">性&#8195;&#8195;别:
                </span>
                <select v-model="form.sex" class="input_item text select-item">
                  <option v-for="item in sexs" v-bind:value="item.value" :key="item.value">
                    {{item.text}}
                  </option>
                </select>
              </div>
            </div>
            <div class="box_row_item">
              <div class="head_img_btn">
                <button class="bottom_btn btn1" @click="edit= false">
                  <span>取消</span>
                </button>
                <button class="bottom_btn btn2" @click="saveSubmit">
                  <span>保存</span>
                </button>
              </div>
            </div>
          </div>
        </div>
        <!-- 弹窗内容 查看用户信息 -->
        <div class="modal-container user-info" v-else>
          <div class="header small">
            <span class="base gray">&nbsp;</span>
            <img :src="closeIcon" alt="" class="close" @click="closeModal">
          </div>
          <div class="body small white">
            <img :src="userInfo.avatarUrl" alt="" class="avatar">
            <p class="large">{{userInfo.username}}</p>
            <div class="stem-sex ">
              <!-- <p>SteamID：{{userInfo.steamID||'-'}}</p> -->
              <p class="gray">
                <span class="sex " :class="{man:userInfo.sex}"></span>{{userInfo.age+'岁 · '+userInfo.lastAddress}}
              </p>
            </div>
            <div class="achievement">
              <div class="tdzl">
                <p class="count base">{{userInfo.tuandui}}</p>
                <p class="x-samll">团队主力</p>
              </div>
              <div class="zjdy">
                <p class="count base"> {{userInfo.zuijia}}</p>
                <p class="x-samll">最佳队友</p>
              </div>
              <div class="tdyh">
                <p class="count base">{{userInfo.taidu}}</p>
                <p class="x-samll">态度友好</p>
              </div>
              <div class="lyzr">
                <p class="count base">{{userInfo.leyu}}</p>
                <p class="x-samll">乐于助人</p>
              </div>
              <p class="x-samll gray">下载APP即可为TA点赞</p>
            </div>
          </div>
          <div class="footer small white">
            <div>
              <span class="owner" @click="edit=true">修改个人资料</span>
              <span class="owner" @click="loginOut">切换账号</span>
            </div>
          </div>
        </div>
      </transition>
    </div>
  </transition>
</template>
<script>
// icon
import closeIcon from '@/assets/close.png'
import AvatarDefault from '@/assets/avatar-default.png'
import myUpload from 'vue-image-crop-upload'

import { getUserExtra } from '@/api/chatRoom'
export default {
  name: 'UserInfo',
  props: {
    show: Boolean
  },
  data() {
    return {
      closeIcon: closeIcon,
      selected: '',
      isHide: true,
      items: [{ text: '1994', value: '1' }, { text: '1995', value: '2' }, { text: '1996', value: '3' }],
      sexs: [{ text: '男', value: true }, { text: '女', value: false }],
      edit: false,
      userInfo: {
        avatar: ''
      },
      form: {},
      uploadAvatarShow: false, // 更换头像弹窗显示状态
      imgDataUrl: null
    }
  },
  created() {
    this.userInfoInit()
  },
  components: {
    'my-upload': myUpload
  },
  methods: {
    userInfoInit() {
      const userInfo = this.AV.User.current().toJSON()
      if (userInfo.avatar) {
        userInfo.avatarUrl = userInfo.avatar.url
        this.form.avatar = userInfo.avatar.url
      } else {
        userInfo.avatarUrl = AvatarDefault
      }
      this.form.username = userInfo.username
      // this.form.steamID = userInfo.steamID
      this.form.age = userInfo.age
      this.form.sex = userInfo.sex
      getUserExtra(userInfo.objectId)
        .then(res => {
          if (res) {
            userInfo.tuandui = res.tuandui
            userInfo.taidu = res.taidu
            userInfo.leyu = res.leyu
            userInfo.zuijia = res.zuijia
            this.userInfo = userInfo
          } else {
            userInfo.tuandui = 0
            userInfo.taidu = 0
            userInfo.leyu = 0
            userInfo.zuijia = 0
            this.userInfo = userInfo
          }
          this.userInfoModal = true
          console.log('>>>>> 用户其他信息', res)
        })
        .catch(error => {
          console.log(error)
        })
    },
    // 修改个人信息保存
    saveSubmit() {
      const current = this.AV.User.current()
      current.set('username', this.form.username)
      current.set('age', this.form.age)
      // current.set('steamID', this.form.steamID)
      current.set('sex', this.form.sex)
      if (this.form.avatar && this.form.avatar != this.userInfo.avatarUrl) {
        const fileData = { base64: this.form.avatar }
        const file = new this.AV.File('avatar', fileData)
        current.set('avatar', file)
      }
      this.$hx_toast({ message: '保存中' })
      current
        .save()
        .then(() => {
          this.$emit('refreshUserInfo')
          this.$emit('closeModal')
          this.bus.$emit('closeToastMsg')
        })
        .catch(error => {
          console.log(error)
        })
    },
    changeAvatar() {
      this.uploadAvatarShow = true
    },
    // 关闭弹窗
    closeModal() {
      this.$emit('closeModal')
    },
    loginOut() {
      this.AV.User.logOut()
      this.$router.go(0)
    },
    cropSuccess(imgDataUrl, field) {
      console.log('-------- crop success --------')
      this.form.avatar = imgDataUrl
    }
  }
}
</script>
<style lang="scss" scoped>
@import '@/styles/mixn.scss';
.modal-container {
  &.user-edit {
    width: px2rem(380);
    margin-left: px2rem(-210);
    margin-top: px2rem(-225);
    padding: px2rem(10) px2rem(30);
    background-color: #242730;
    border-radius: 4px;
  }
}

.text {
  color: #e8e8e8;
}

.text1 {
  color: #777777;
}
.text2 {
  color: #777777;
}
.text3 {
  line-height: px2rem(33);
  text-align: center;
}

.box_row {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
}

.box_row_item {
  flex: 1;
  margin: px2rem(15);
}

/*两端对齐*/
.head_img_btn {
  display: flex;
  justify-content: space-between;
  position: relative;
}

/*水平对齐*/
.align {
  display: flex;
  justify-content: center;
}

/*垂直对齐*/
.vertical {
  display: flex;
  align-items: center;
}

.bottom_btn {
  height: px2rem(40);
  width: px2rem(150);
  border-radius: px2rem(25);
  border: #00d95f 1px solid;
  outline: none;
  cursor: pointer;
}

.input_item {
  height: px2rem(33);
  width: px2rem(244);
  background-color: transparent;
  color: #e8e8e8;
  border: 0;
  outline: none;
  cursor: pointer;
  &.select-item {
    border: 1px solid #707070;
    option {
      background-color: #242730;
      text-align: center;
    }
  }
}
.count-input {
  position: absolute;
  right: px2rem(10);
  bottom: px2rem(10);
}
.border-bottom {
  border-bottom: #3a3d45 1px solid;
}
/*头像*/
.icon_img {
  height: px2rem(66);
  width: px2rem(66);
  border-radius: px2rem(33);
}

.btn_up {
  padding: px2rem(5) px2rem(12);
  background-color: #e8e8e8;
  border-radius: 4px;
  display: inline-block;
  border: none;
  box-shadow: none;
  color: #242730;
}

.btn1 {
  background-color: transparent;
}

.btn2 {
  background-color: #00d95f;
}

.btn1 span {
  color: #00d95f;
}

.btn2 span {
  color: #272430;
}

select {
  height: px2rem(33);
  width: px2rem(244);
  text-align: center;
  text-align-last: center;
  border: 1px solid #777;
}
</style>
