<template>
  <div>
    <el-menu
      class="el-menu-demo"
      mode="horizontal"
      background-color="#545c64"
      text-color="#fff"
      :router="true"
      active-text-color="#ffd04b">
      <el-menu-item index="/courses">首页</el-menu-item>
      <el-menu-item index="/user/courses">个人中心</el-menu-item>
    </el-menu>
  </div>
</template>

<script>
export default {
  name: 'UserTopMenu'
}
</script>
