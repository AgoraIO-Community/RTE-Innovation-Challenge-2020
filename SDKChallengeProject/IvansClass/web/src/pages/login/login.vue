<template>
  <div>
    <el-form ref="loginForm" :model="form" :rules="rules" label-width="80px" class="login-box">
      <h3 class="login-title">欢迎登录</h3>
      <el-form-item label="账号" prop="username">
        <el-input type="text" placeholder="请输入账号" v-model="form.username"/>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input type="password" placeholder="请输入密码" v-model="form.password"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" v-on:click="onSubmit('loginForm')">登录</el-button>
      </el-form-item>
      <p>
        <el-link type="warning" href="/register">请先注册</el-link>
      </p>
    </el-form>
  </div>
</template>

<script>
import { login } from '@/utils/api'

export default {
  name: 'login',
  data () {
    return {
      form: {
        username: '',
        password: ''
      },

      // 表单验证，需要在 el-form-item 元素中增加 prop 属性
      rules: {
        username: [
          {
            required: true,
            message: '账号不可为空',
            trigger: 'blur'
          }
        ],
        password: [
          {
            required: true,
            message: '密码不可为空',
            trigger: 'blur'
          }
        ]
      },

      // 对话框显示和隐藏
      dialogVisible: false
    }
  },
  methods: {
    onSubmit (loginForm) {
      this.$refs[loginForm].validate((valid) => {
        if (valid) {
          const param = {
            account: this.form.username, // 将this.form.username和this.form.password传给父组件的on-success-valid参数
            password: this.form.password // 并使用 username 和 password 接受
          }
          // const that = this
          login(param).then((res) => {
            console.log(res.data)
            if (res.data.code === 200) {
              this.$message.info('登录成功')
              localStorage.setItem('token', res.data.data.token)
              this.$router.push({ path: '/courses' })
            }
          })
          // 使用 vue-router 路由到指定页面，该方式称之为编程式导航
        } else {
          this.$message.info('登录失败，请输入正确的用户名和密码')
          return false
        }
      })
    }
  }
}
</script>

<style>
  .login-box {
    border: 1px solid #DCDFE6;
    width: 350px;
    margin: 180px auto;
    padding: 35px 35px 15px 35px;
    border-radius: 5px;
    -webkit-border-radius: 5px;
    -moz-border-radius: 5px;
    box-shadow: 0 0 25px #909399;
  }

  .login-title {
    text-align: center;
    margin: 0 auto 40px auto;
    color: #303133;
  }
</style>
