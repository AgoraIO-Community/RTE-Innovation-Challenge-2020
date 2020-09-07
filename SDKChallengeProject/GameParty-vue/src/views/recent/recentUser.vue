/* 最近游玩
 * @Author: he.xiaoxue
 * @Date: 2018-09-11 17:40:17
 * @Last Modified by: he.xiaoxue
 * @Last Modified time: 2018-09-11 22:01:26
 */

<template>
  <transition name="fade">
    <vue-scroll :ops="ops">
      <div class="app-container">
        <!-- 没有记录展示 -->
        <div class="nodata" v-if="userList!=null&&userList.length==0">
          <img :src="NoUser" alt="">
          <p class="gray small">您最近没有和伙伴一起组队</p>
          <p class="gray small">快去组队开始认识新伙伴</p>
        </div>
        <!-- ENDOF 没有记录展示 -->
      </div>
    </vue-scroll>
  </transition>
</template>
<script>
import NoUser from '@/assets/nouser.png'
export default {
  name: 'RecentUser',
  data() {
    return {
      NoUser: NoUser,
      ops: {
        // 滚动条参数
        rail: {
          background: '#00d95f',
          opacity: 0
        }
      },
      userList: null
    }
  },
  methods: {
    getUserList() {}
  }
}
</script>
