<script lang="ts">
  import { createEventDispatcher, afterUpdate } from "svelte";
  import AgoraRTC from "agora-rtc-sdk";
  import Modal from "./Modal.svelte";
  import type { Story } from "./utils";
  import { rtc, env } from "./store";

  export let visible: boolean = false;
  export let story: Story | null = null;

  const dispatch = createEventDispatcher();

  let targetCode = "";

  function init() {
    return new Promise((resolve, reject) => {
      rtc.set({
        ...$rtc,
        client: AgoraRTC.createClient({ mode: "rtc", codec: "h264" }),
      });
      $rtc.client.init(
        $env.AGORA_APP_ID,
        () => {
          $rtc.client.join(
            null,
            story.id,
            $rtc.uid,
            (uid) => {
              rtc.set({
                ...$rtc,
                uid,
                channel: story.id,
                localStream: AgoraRTC.createStream({
                  streamID: uid,
                  audio: true,
                  video: true,
                  screen: false,
                }),
              });
              resolve();
            },
            (err) => reject(err)
          );
        },
        (err) => reject(err)
      );
    });
  }

  let initP: Promise<unknown>;
  afterUpdate(() => {
    if (visible && !$rtc.uid) {
      initP = init();
    }
  });
</script>

{#if visible}
  <Modal on:close={() => dispatch('close')} showFooter={false}>
    <span slot="title">协作</span>
    <div slot="body">
      {#if story}
        <h5 class="font-weight-bold">邀请他人加入故事《{story.name}》的创作</h5>
        <div class="mb-4 pb-4 border-bottom">
          {#await initP}
            初始化中...
          {:then _}
            邀请码：<code>{story.id}</code>
          {:catch error}
            初始化失败：
            <pre>{error}</pre>
          {/await}
        </div>
      {/if}
      <div>
        <h5 class="font-weight-bold">参与故事创作</h5>
        <form
          class="form-inline"
          on:submit|preventDefault={() => console.log('yes')}>
          <div class="form-group mb-2">
            <label for="code">请输入邀请码</label>
            <input
              type="text"
              id="code"
              class="form-control mr-2 ml-2"
              bind:value={targetCode} />
          </div>
          <button type="submit" class="btn btn-primary mb-2">加入</button>
        </form>
      </div>
    </div>
  </Modal>
{/if}
