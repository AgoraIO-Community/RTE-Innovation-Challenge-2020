<template>
  <div>
    <nav-bar></nav-bar>
    <app-main></app-main>
    <slidebar></slidebar>
    <lobby-btns></lobby-btns>
  </div>
</template>
<script>
import NavBar from './components/Navbar'
import Slidebar from './components/Slidebar'
import AppMain from './components/AppMain'
import LobbyBtns from './components/LobbyBtns'
export default {
  name: 'Layout',
  components: {
    NavBar,
    Slidebar,
    AppMain,
    LobbyBtns
  }
}
</script>
