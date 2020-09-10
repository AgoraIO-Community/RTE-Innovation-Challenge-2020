<!-- 登录 -->
<template>
  <div class="login col-direction ">
    <div class="backgd">
      <div class="bgCircle" />
      <p class="logo blod">呆瓜团队</p>
      <div class="content col-direction center">
        <h1 class="blod">便 利 贴</h1>
        <span style="font-size:22px;" class="blod">Post-it Notes</span>
        <el-form ref="ruleForm" :model="ruleForm" :rules="rules" label-width="100px" class="center col-direction demo-ruleForm">
          <el-form-item label="" prop="account">
            <el-input
              v-model="ruleForm.account"
              placeholder="输入姓名"
              @input="changeBlue"
            >
              <i slot="prefix" class="el-input__icon el-icon-user-solid" />
            </el-input>
          </el-form-item>
          <el-form-item label="" prop="pass">
            <el-input
              v-model="ruleForm.pass"
              placeholder="输入密码"
              @input="changeBlue"
            >
              <i slot="prefix" class="el-input__icon el-icon-s-grid" />
            </el-input>
          </el-form-item>
          <el-form-item label="" prop="">
            <el-radio-group v-model="ruleForm.role">
              <el-radio :label="1">教师</el-radio>
              <el-radio :label="2">学生</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-button class="login-button" :class="{'blue':blue}" circle @click="loginHandle('ruleForm')"><i class="el-icon-right" /></el-button>
        </el-form>

      </div>
    </div>
  </div>
</template>

<script>
import { login } from '@/api/local.js'
export default {
  components: {

  },
  data() {
    return {
      ruleForm: {
        pass: '123456',
        account: 'dandan',
        role: 2
      },
      rules: {
        pass: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        account: [
          { required: true, message: '请输入姓名', trigger: 'change' }
        ]
      },
      blue: false
    }
  },
  mounted() {
    this.changeBlue()
  },
  // 方法集合
  methods: {

    // 改变按钮颜色
    changeBlue() {
      if (this.ruleForm.account !== '' && this.ruleForm.pass !== '') {
        this.blue = true
      } else {
        this.blue = false
      }
    },
    loginHandle(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          login(this.ruleForm.account, this.ruleForm.pass).then(
            res => {
              this.$router.replace({ path: '/home', query: { uid: res.userid, role: this.ruleForm.role }})
            }
          )
        } else {
          return false
        }
      })
    }
  }
}
</script>
<style lang='scss' scoped>
.login{
  width: 100%;
  height:100%;
  background-image: url('~@/assets/background.png');
    background-repeat: no-repeat;
     background-size:100% 100%;
     color: #fff;

  .logo{
    text-align: start;
    font-size: 20px;
    margin:40px 80px;

  }
  /deep/ .el-input__icon{
    margin-left: 12px;
    font-size: 30px;
    color: RGB(47,124,247);
    display: flex;
    align-items: center;
  }
  .el-radio-group{
    margin:7px;
    width: 500px;
    .el-radio{
      margin-right: 150px;
    }
    /deep/ .el-radio__inner{
      width:24px;
      height: 24px;
    }
    /deep/ .el-radio__label{
      font-size: 22px;
      color: #030303;
    }
  }
  .content{
    z-index: 10;
  }

}
.blue{
      background: linear-gradient(to top,RGB(101,169,249) ,RGB(156,216,252));

}
.el-form{
    // width:300px;
    margin-top:50px;
    margin-bottom: 90px;
    /deep/ .el-form-item__error{
      padding-right: 100px;
      width: 100%;
      text-align: center;
    }

    .el-input{
        margin:7px;
        height:64px;
        width: 500px;
        border-radius: 12px;
        background-color: #F3F4F8;
        margin-right: 100px;
    }

     /deep/ .el-input__inner{
      height:64px;
        width: 500px;
        border-radius: 12px;
        background-color: #F3F4F8;
        border:none !important;
        font-size: 22px;
        color: #030303;
        text-align: center;
    }
}
.login-img{
  height: 88px;
  width: 88px;
  margin-top: 68px;
}
.login-button{
  margin-top: 50px;
  height:80px;
  width:80px;
  font-size: 22px;
  background-color: #F3F4F8;
  border:none;

  .el-icon-right{
  color: #fff;
  font-weight: 900;
  font-size: 32px;
  }
}
.other{
  margin: 60px 32px;
}
</style>
