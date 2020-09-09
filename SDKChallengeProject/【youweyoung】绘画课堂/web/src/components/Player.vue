<template>
  <div class="video-player" ref="player" :id="uid"></div>
</template>

<script>
export default {
  name: 'stream-player',
  props: [
    'stream',
    'uid',
  ],
  mounted () {
    console.log(this)
    this.$nextTick(function () {
      if (this.stream && !this.stream.isPlaying()) {
        this.stream.play(`${this.uid}`, {fit: 'cover'}, (err) => {
          if (err && err.status !== 'aborted') {
            console.warn('trigger autoplay policy')
          }
        })
      }
    })
  },
  beforeDestroy () {
    if (this.stream) {
      if (this.stream.isPlaying()) {
        this.stream.stop()
      }
      this.stream.close()
    }
  }
}
</script>

<style>
.video-player {
  height: 100%;
  width: 100%;
}
.video-player>div{
  display: none
}
</style>