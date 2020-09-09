<!-- 已记笔记页面 -->
<template>
  <div class="noteDrawer">
    <p class="title">已记笔记</p>
    <el-divider />
    <div v-for="(note,index) in noteList" :key="index" class="noteBox">
      {{ note.time }}
      <br>
      <img style="width:90%;" :src="note.note" @click="openImgHandle(note.note)">
    </div>
    <bigPic v-if="bigPicVisi" :img-src="nowImg" @clickit="bigPicVisi = false" />
  </div>
</template>

<script>
import bigPic from '@/components/bigPic'

import { getUserNote } from '@/api/local.js'
import { formatDate } from '@/utils/time.js'
export default {
  components: {
    bigPic
  },
  data() {
    return {
      nowImg: '',
      bigPicVisi: false,
      noteList: []
    }
  },
  mounted() {
    this.getUserNoteHandle()
  },
  // 方法集合
  methods: {
    // 放大图片
    openImgHandle(src) {
      this.nowImg = src
      this.bigPicVisi = true
    },
    // 获取用户笔记
    getUserNoteHandle() {
      getUserNote(this.$route.query.room, this.$route.query.uid).then(
        res => {
          console.log('res', res)
          this.noteList = res.data
          this.noteList.forEach(note => {
            note.time = formatDate(+note.time)
          })
        }
      )
    }
  }
}
</script>
<style lang='scss' scoped>
.noteDrawer{
padding: 25px 0;
  height: 100%;
  width: 100%;
  overflow-y: auto;

  p{
      margin:0;
  }
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
