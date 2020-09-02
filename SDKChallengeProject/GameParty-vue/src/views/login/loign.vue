<template>
  <div class="login">
      <video autoplay loop="loop" class="video-bg">
      <source src="../../assets/n_5.mp4" type="video/mp4">
    </video>
    <login-modal></login-modal>
    <login-footer></login-footer>
  </div>
</template>
<script>
import LoginFooter from './footer'
import LoginModal from './modal'

export default {
  name: 'Login',
  components: {
    LoginFooter,
    LoginModal
  },
  created() {}
}
</script>
