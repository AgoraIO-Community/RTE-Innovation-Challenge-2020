<template>
  <div id="app">
    <div id="nav">
      <router-link to="/">绘画课堂 </router-link> [{{uid}}]
      <div v-if="$route.path==='/'">
        <input
          class="mg"
          @input="updateId"
          type="number"
          placeholder="用户id"
          value="88888888"
        />
      </div>
    </div>
    <router-view id="content" />
  </div>
</template>

<script>
import { mapActions,mapMutations,mapState } from "vuex";
export default {
  created() {
    this.init();
  },
  methods: {
    ...mapActions(["init"]),
    ...mapMutations(['SET_UID']),
    updateId(e) {
      this.SET_UID(parseInt(e.target.value))
    },
  },
  computed:{
    ...mapState(['uid'])
  }
};
</script>

<style>
@import url("style/basic.css");
body,
html {
  padding: 0;
  margin: 0;
}
#app {
  color: #2c3e50;
  background-color: bisque;
  min-height: 100vh;
}
#nav {
  padding: 16px;
}

#nav a {
  font-weight: bold;
  color: #2c3e50;
}

#nav a.router-link-exact-active {
  color: #42b983;
}
#content {
  padding: 16px;
}
div,
section {
  box-sizing: border-box;
  position: relative;
}
</style>
