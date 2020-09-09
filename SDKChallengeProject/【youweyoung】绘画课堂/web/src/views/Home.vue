<template>
  <div class="home">
    <div v-if="status === 1" class="frow fwrap">
      <div
        v-for="item in rooms"
        :key="item.id"
        class="shadow-light room-card fcol"
      >
        <div class="f1">{{ item.name }} [{{ item.uid }}]</div>
        <div class="frow">
          <button @click="goRoom(item)">进入</button>
        </div>
      </div>
    </div>
    <div v-else-if="status === 2">
      连接RTC服务器失败
    </div>
    <div v-else>
      尝试连接中...
    </div>
  </div>
</template>

<script lang="ts">
// @ is an alias to /src
import Vue from "vue";
import { mapState, mapActions } from "vuex";
import { rtc } from "../rtc";
import { createRoom, getRooms } from "../api";
export default Vue.extend({
  name: "Home",
  components: {},
  data() {
    return {
      list: [] as any,
    };
  },
  methods: {
    async goRoom(item: any) {
      this.$router.push(
        `/room/${item.id}${this.uid === item.uid ? "/teach" : ""}`
      );
      console.log( `/room/${item.id}${this.uid === item.uid ? "/teach" : ""}`)
    },
  },
  computed: {
    ...mapState(["status", "uid", "rooms"]),
  },
  mounted() {},
});
</script>

<style scoped>
.room-card {
  width: 200px;
  height: 200px;
  background-color: white;
  padding: 16px;
}
</style>
