<script lang="ts">
  import { createEventDispatcher } from "svelte";
  import { rtc } from "./store";
  import * as cr from "./cloud-recording";
  import Play from "./icons/play.svg";
  import Pause from "./icons/pause.svg";

  const dispatch = createEventDispatcher();

  export let visible: boolean = false;
  $: {
    if (visible) {
      init();
    }
  }

  let resourceId: string;
  let sid: string;
  let startAt: number;
  async function startRecord() {
    $rtc.client.publish($rtc.localStream, (err) => {
      if (err) {
        throw err;
      }
    });
    resourceId = await cr.acquire($rtc.channel);
    sid = await cr.start($rtc.channel, resourceId, {
      uids: [$rtc.uid],
    });
    startAt = new Date().getTime();
  }

  async function stopRecord() {
    const file = await cr.stop(resourceId, sid, $rtc.channel);
    const duration = new Date().getTime() - startAt;
    dispatch("finish", { file, duration });
    stop();
    playing = false;
  }

  function stop() {
    $rtc.localStream.stop();
    $rtc.localStream.close();
  }

  function init() {
    $rtc.localStream.init(
      () => {
        $rtc.localStream.play("rtc-video-placeholder");
      },
      (err) => {
        if (err.msg === "STREAM_ALREADY_INITIALIZED") {
          $rtc.localStream.play("rtc-video-placeholder");
        } else {
          throw err;
        }
      }
    );
  }

  let playing = false;

  function handleClose() {
    stop();
    dispatch("close");
    playing = false;
  }

  function toggle() {
    playing = !playing;
    if (playing) {
      startRecord();
    } else {
      stopRecord();
    }
  }
</script>

<style>
  #rtc-video-placeholder {
    width: 150px;
    height: 150px;
    border-radius: 75px;
    background: #858796;
    overflow: hidden;
    margin: 0 auto;
  }

  .close-video {
    left: 80px;
    top: -20px;
    position: absolute;
  }

  .toggle-video {
    left: 30px;
    top: -20px;
    position: absolute;
  }
</style>

{#if visible}
  <div id="rtc-video-placeholder" />
  <button
    type="button"
    class="btn-circle btn btn-primary toggle-video"
    on:click={toggle}>
    {#if playing}
      {@html Pause}
    {:else}
      {@html Play}
    {/if}
  </button>
  <button
    type="button"
    class="btn-circle btn btn-dark close-video"
    on:click={handleClose}
    disabled={playing}>
    <span aria-hidden="true">&times;</span>
  </button>
{/if}
