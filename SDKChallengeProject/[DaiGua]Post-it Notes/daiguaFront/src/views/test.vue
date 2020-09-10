<!-- 已记笔记页面 -->
<template>
  <div class="noteDrawer">
    <p class="title">已记笔记</p>
    <el-divider />
    <div class="noteBox">
      2020/7/18日笔记 图片
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {

    }
  },
  mounted() {
    console.log(process.env)
  },
  // 方法集合
  methods: {

  }
}
</script>
<style lang='scss' scoped>
.noteDrawer{

  height: 100%;
  width: 100%;
  background-color: #eee;
  .title{
    font-size: 24px;
  }
  .noteBox{
    background-color: #000;
    border-radius: 20px;
    width: calc(100% - 40px);
    margin:20px;
    padding: 20px;
    color: #fff;
  }
}

</style>
