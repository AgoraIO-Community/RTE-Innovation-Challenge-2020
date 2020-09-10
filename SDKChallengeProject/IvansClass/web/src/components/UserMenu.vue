<template>
  <div>
    <el-menu
      class="el-menu-demo"
      mode="horizontal"
      background-color="#545c64"
      text-color="#fff"
      :router='true'
      active-text-color="#ffd04b">
      <el-menu-item index="/courses">首页</el-menu-item>
      <el-menu-item index="/user/courses">我的课程</el-menu-item>
      <el-menu-item index="/user/classes">我的班级</el-menu-item>
      <el-menu-item index="/user/lives">我的直播</el-menu-item>
    </el-menu>
  </div>
</template>

<script>
export default {
  name: 'UserMenu'
}
</script>
