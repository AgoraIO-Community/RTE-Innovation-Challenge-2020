<script lang="ts">
  import Replayer from "rrweb-player";
  import type { Replayer as _Replayer } from "rrweb";
  import type { playerMetaData } from "rrweb/typings/types";
  import { onMount, createEventDispatcher, tick } from "svelte";
  import { pannable } from "./pannable";
  import { formatTime, newScene } from "./utils";
  import type { Chapter, Scene, Tooltip, Skip } from "./utils";
  import Modal from "./Modal.svelte";
  import Play from "./icons/play.svg";
  import Pause from "./icons/pause.svg";
  import ChatLeftText from "./icons/chat-left-text.svg";
  import Scissors from "./icons/scissors.svg";
  import PlusCircle from "./icons/plus-circle.svg";

  export let chapter: Chapter;
  export let sceneIdx = 0;

  let scene: Scene;
  $: scene = chapter.sequence[sceneIdx];

  const dispatch = createEventDispatcher();
  $: {
    dispatch("update", { chapter, scene });
  }

  let wrapper: HTMLDivElement;

  let replayer: Replayer;
  let rrwebReplayer: _Replayer;
  $: rrwebReplayer = replayer?.getReplayer();
  let ifrmae: HTMLIFrameElement;
  $: iframe = rrwebReplayer && rrwebReplayer.iframe;

  let mask: HTMLDivElement;
  let selecting = false;
  let tooltip: HTMLDivElement;
  const defaultTooltipPayload = {
    target: null,
    originalBoxShadow: "",
    content: "",
    timeOffset: 0,
    id: -1,
    left: 0,
    top: 0,
  };
  let tooltipPayload = { ...defaultTooltipPayload };

  let scale = 1;
  const highlight = (target) => {
    const { x: wrapperX, y: wrapperY } = wrapper.getBoundingClientRect();
    const { x: baseX, y: baseY } = iframe.getBoundingClientRect();
    const { x, y, width, height } = target.getBoundingClientRect();
    Object.assign(mask.style, {
      left: `${x * scale + baseX - wrapperX}px`,
      top: `${y * scale + baseY - wrapperY}px`,
      width: `${width * scale}px`,
      height: `${height * scale}px`,
      display: "inherit",
    });
  };

  const removeHighlight = () => {
    Object.assign(mask.style, {
      left: 0,
      top: 0,
      width: 0,
      height: 0,
      display: "none",
    });
  };

  const over = (event) => {
    event.preventDefault();
    event.stopPropagation();
    if (event.target && event.target !== document.body) {
      highlight(event.target);
    }
  };

  const click = (event) => {
    event.preventDefault();
    event.stopPropagation();
    if (!selecting) {
      return;
    }
    cancelSelect();
    const { x, y, width, height } = event.target.getBoundingClientRect();
    const { x: wrapperX, y: wrapperY } = wrapper.getBoundingClientRect();
    const { x: baseX, y: baseY } = iframe.getBoundingClientRect();
    tooltipPayload.target = event.target;
    tooltipPayload.originalBoxShadow = event.target.style.boxShadow;
    tooltipPayload.timeOffset = rrwebReplayer.getCurrentTime();
    tooltipPayload.id = event.target.__sn.id;
    Object.assign(event.target.style, {
      outline: "2px solid #e74a3b",
      "outline-offset": "-1px",
    });
    Object.assign(tooltip.style, {
      left: `${(x + width) * scale + baseX - wrapperX}px`,
      top: `${(y + height) * scale + baseY - wrapperY}px`,
      display: "block",
    });
  };

  const finishTooltip = () => {
    Object.assign(tooltip.style, {
      left: 0,
      top: 0,
      display: "none",
    });
    Object.assign(tooltipPayload.target.style, {
      outline: "",
      "outline-offset": "",
    });
    tooltipPayload = { ...defaultTooltipPayload };
  };

  const cancelTooltip = () => {
    finishTooltip();
  };

  const saveTooltip = () => {
    scene.tooltips = [
      ...scene.tooltips,
      {
        content: tooltipPayload.content,
        timeOffset: tooltipPayload.timeOffset,
        id: tooltipPayload.id,
      },
    ];
    finishTooltip();
  };

  const cancelSelect = () => {
    selecting = false;
    removeHighlight();
    replayer.getReplayer().disableInteract();
    iframe.contentWindow.removeEventListener("mousemove", over, {
      capture: true,
    });
    iframe.contentWindow.removeEventListener("click", click, { capture: true });
  };

  function handleSelect() {
    replayer.pause();
    if (selecting) {
      cancelSelect();
    } else {
      selecting = true;
      replayer.getReplayer().enableInteract();
      iframe.contentWindow.addEventListener("mousemove", over, {
        capture: true,
      });
      iframe.contentWindow.addEventListener("click", click, { capture: true });
    }
  }

  let panelCoords = {
    x: 0,
    y: 0,
  };
  function handlePanMove(event) {
    const { x, y } = wrapper.getBoundingClientRect();
    panelCoords.x = event.detail.x - x;
    panelCoords.y = event.detail.y - y;
  }

  let percent = 0;
  let metas: playerMetaData[];
  $: metas = chapter.sequence.map((scene) => {
    if (scene.events.length < 2) {
      return {
        startTime: 0,
        endTime: 0,
        totalTime: 0,
      };
    }
    const firstEvent = scene.events[0];
    const lastEvent = scene.events[scene.events.length - 1];
    return {
      startTime: firstEvent.timestamp,
      endTime: lastEvent.timestamp,
      totalTime: lastEvent.timestamp - firstEvent.timestamp,
    };
  });
  let accumulatedCurrentTime = 0;
  $: {
    let passedTotal = 0;
    for (let i = 0; i < sceneIdx; i++) {
      passedTotal += metas[i].totalTime;
    }
    accumulatedCurrentTime = currentTime + passedTotal;
  }
  $: accumulatedTotalTime = metas.reduce((prev, cur) => {
    return prev + cur.totalTime;
  }, 0);

  async function changeScene(idx: number) {
    sceneIdx = idx;
    await tick();
    playScene(scene);
  }

  let currentTime = 0;
  let playerState: "playing" | "paused" | "live" = "paused";

  function playScene(s: Scene | null) {
    if (!s || !wrapper) {
      return;
    }
    if (s.url === null) {
      return;
    }
    wrapper.innerHTML = "";
    const { width, height } = wrapper.getBoundingClientRect();
    percent = 0;
    replayer = new Replayer({
      target: wrapper,
      props: {
        events: s.events,
        skipInactive: false,
        autoPlay: false,
        showController: false,
        width,
        height,
        mouseTail: false,
      },
    });
    replayer.addEventListener("ui-update-current-time", (event) => {
      currentTime = event.payload;
    });
    replayer.addEventListener("ui-update-progress", (event) => {
      percent = event.payload;
    });
    replayer.addEventListener("ui-update-player-state", (event) => {
      playerState = event.payload;
    });
    replayer.addEventListener("finish", async (event) => {
      percent = 1;
      if (sceneIdx < chapter.sequence.length - 1) {
        sceneIdx++;
        await tick();
        playScene(scene);
      }
    });
    replayer.addEventListener("resize", () => {
      const matchedArr = getComputedStyle(
        rrwebReplayer.wrapper
      ).transform.match(/matrix\(([\d\.]+)/);
      if (matchedArr) {
        scale = parseFloat(matchedArr[1]);
      }
    });
  }

  let tracksEl: HTMLDivElement;
  let tracksWidth = 0;
  function handleTracksClick(event: MouseEvent) {
    const { left } = tracksEl.getBoundingClientRect();
    // 8 for padding
    const x = event.clientX - left - 8;
    percent = x / tracksWidth;
    if (percent < 0) {
      percent = 0;
    } else if (percent > 1) {
      percent = 1;
    }
    replayer.goto(metas[sceneIdx].totalTime * percent);
  }

  function handleSkip() {
    replayer.pause();
    modalPayload = {
      type: "skip",
      payload: {
        timeOffset: Math.round(currentTime),
        duration: 1000,
      },
      // new one
      index: -1,
    };
  }

  let modalPayload:
    | {
        type: "tooltip";
        payload: Tooltip;
        index: number;
      }
    | {
        type: "skip";
        payload: Skip;
        index: number;
      }
    | null = null;

  function editTooltip(tooltip: Tooltip, idx: number) {
    modalPayload = {
      type: "tooltip",
      payload: tooltip,
      index: idx,
    };
  }

  function removeTooltip(tooltip: Tooltip) {
    scene.tooltips = scene.tooltips.filter((t) => t !== tooltip);
  }

  function editSkip(skip: Skip, idx: number) {
    modalPayload = {
      type: "skip",
      payload: skip,
      index: idx,
    };
  }

  function removeSkip(skip: Skip) {
    scene.skips = scene.skips.filter((t) => t !== skip);
  }

  let webview:
    | ({
        executeJavaScript(script: string): Promise<any>;
        openDevTools(): void;
      } & HTMLElement)
    | undefined;

  let startedRecord = false;

  async function startRecordWeb() {
    startedRecord = true;
    await webview.executeJavaScript("ST.startRecord()");
  }

  async function stopRecordWeb() {
    await webview.executeJavaScript("ST.stopRecord()");
    scene.events = await webview.executeJavaScript("ST.getEvents()");
    scene.totalTime =
      scene.events[scene.events.length - 1].timestamp -
      scene.events[0].timestamp;
    chapter.sequence = chapter.sequence.slice();
    await tick();
    playScene(scene);
  }

  function addScene() {
    chapter.sequence = chapter.sequence.concat(newScene);
    sceneIdx = chapter.sequence.length - 1;
  }

  onMount(() => {
    playScene(scene);
  });
</script>

<style>
  :global(iframe) {
    border: none;
  }

  :global(.player) {
    float: none;
    border-radius: 0;
    box-shadow: none;
  }

  .we-mask {
    position: absolute;
    left: 0;
    top: 0;
    pointer-events: none;
    z-index: 999999;
    background: rgba(136, 194, 232, 0.75);
    border: 1px solid #3399ff;
    display: none;
  }

  .we-preview {
    flex: 1;
    display: flex;
    position: relative;
  }

  .we-wrapper {
    position: relative;
    flex: 1;
  }

  .we-tooltip {
    position: absolute;
    z-index: 999999;
    background: rgba(0, 0, 0, 0.75);
    color: white;
    padding: 1em;
    display: none;
  }

  .we-tooltip-btns {
    display: flex;
    justify-content: flex-end;
  }

  .we-panel {
    background: white;
    border-top: 1px solid #e3e6f0;
  }

  .we-panel-top {
    padding: 0.5em;
    border-bottom: 1px solid #e3e6f0;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .we-panel-scenes {
    padding: 0.5em;
    border-bottom: 1px solid #e3e6f0;
    display: flex;
    overflow: auto;
  }

  .we-panel-top .icon {
    font-size: 24px;
    cursor: pointer;
  }

  .web-editor {
    height: 100%;
    display: flex;
    flex-direction: column;
    position: relative;
  }

  .we-scene-card {
    width: 200px;
    overflow: hidden;
    border-left: 0.25rem solid white;
  }

  .we-scene-meta {
    display: flex;
    align-items: center;
  }

  .we-scene-name {
    flex: 1;
    overflow: hidden;
    white-space: nowrap;
    word-break: keep-all;
    text-overflow: ellipsis;
  }

  .we-scene-progress {
    flex: 1;
    height: 6px;
    background: #eee;
    position: relative;
    border-radius: 3px;
    box-sizing: border-box;
  }

  .we-scene-progress-step {
    height: 100%;
    position: absolute;
    left: 0;
    top: 0;
    background: rgba(78, 115, 223, 0.5);
    border-radius: 3px;
  }

  .we-tracks {
    height: 120px;
    overflow: auto;
    padding: 0 0.5em;
    position: relative;
  }

  .we-track {
    padding: 0.5em 0;
    border-bottom: 1px solid #e3e6f0;
    display: flex;
    overflow: auto;
  }

  .we-tracks-handler {
    height: 100%;
    width: 2px;
    background: #e74a3b;
    position: fixed;
    z-index: 9;
  }

  .we-tracks-handler::before {
    content: "";
    position: absolute;
    top: -4[x];
    left: -4px;
    display: block;
    width: 10px;
    height: 10px;
    border-radius: 5px;
    background: #e74a3b;
  }

  .we-track .alert {
    padding: 0.5em;
    overflow: hidden;
    display: flex;
  }

  .we-tooltip-track .alert {
    max-width: 130px;
  }

  .we-track .alert > div {
    overflow: hidden;
    white-space: nowrap;
    word-break: keep-all;
    text-overflow: ellipsis;
  }

  :global(.modal) {
    position: absolute;
    display: inherit;
  }

  .we-panel-effects {
    display: flex;
    align-items: center;
  }

  webview {
    height: 100%;
  }

  .we-url-input {
    max-width: 500px;
    margin: auto;
  }

  .record-panel {
    position: absolute;
    background: white;
    right: 1em;
    bottom: 1em;
  }
</style>

<div class="web-editor">
  {#if scene.events.length === 0 && scene.url}
    <div class="record-panel">
      <button
        class="btn btn-primary"
        on:click={startedRecord ? stopRecordWeb : startRecordWeb}>
        {startedRecord ? '完成' : '开始录制'}
      </button>
      <button class="btn btn-dark" on:click={() => webview.openDevTools()}>
        debug
      </button>
    </div>
    <webview bind:this={webview} src={scene.url} preload="./record.js" />
  {:else}
    {#if scene.url}
      <div class="we-preview">
        <div class="we-wrapper" bind:this={wrapper} />
        <div bind:this={tooltip} class="we-tooltip">
          <textarea
            class="form-control form-control-sm mb-1"
            bind:value={tooltipPayload.content} />
          <div class="we-tooltip-btns mt-1">
            <button
              class="btn btn-light mr-1"
              on:click={cancelTooltip}>取消</button>
            <button class="btn btn-primary" on:click={saveTooltip}>保存</button>
          </div>
        </div>
        <div bind:this={mask} class="we-mask" />
      </div>
    {:else}
      <input
        type="text"
        class="form-control we-url-input"
        placeholder="请输入网址"
        on:blur={(e) => (scene.url = e.target.value)} />
    {/if}
    <div class="we-panel">
      <div class="we-panel-top">
        <div>
          {formatTime(accumulatedCurrentTime)}/{formatTime(accumulatedTotalTime)}
        </div>
        <div class="icon" on:click={() => replayer.toggle()}>
          {@html playerState === 'paused' ? Play : Pause}
        </div>
        <div class="we-panel-effects">
          <div class="icon mr-2" on:click={handleSelect}>
            {@html ChatLeftText}
          </div>
          <div class="icon" on:click={handleSkip}>
            {@html Scissors}
          </div>
        </div>
      </div>
      <div class="we-panel-scenes">
        {#each chapter.sequence as scene, idx}
          <div
            class="card mr-1 shadow bg-light text-black we-scene-card {idx === sceneIdx ? 'border-left-primary' : ''}"
            on:click={() => changeScene(idx)}
            role="button">
            <div class="card-body">
              <div class="we-scene-meta">
                <div class="we-scene-name">{scene.name}</div>
                <small>{formatTime(scene.totalTime)}</small>
              </div>
              <div class="we-scene-progress mt-2">
                <div
                  class="we-scene-progress-step"
                  style="width: {idx < sceneIdx ? 100 : idx > sceneIdx ? 0 : 100 * percent}%" />
              </div>
            </div>
          </div>
        {/each}
        <button class="btn btn-light add-scene" on:click={() => addScene()}>
          {@html PlusCircle}
        </button>
      </div>
      <div
        class="we-tracks"
        bind:this={tracksEl}
        bind:clientWidth={tracksWidth}
        on:click={handleTracksClick}>
        <div
          class="we-tracks-handler"
          style="transform: translateX({tracksWidth * percent}px)" />
        {#each scene.tooltips as tooltip, idx}
          <div class="we-tooltip-track we-track">
            <div
              class="alert alert-info mb-0 mr-1"
              style="transform: translateX({Math.max(0, (tracksWidth * tooltip.timeOffset) / scene.totalTime)}px)"
              on:click|stopPropagation={() => editTooltip(tooltip, idx)}
              role="button">
              <div class="flex-fill">
                {@html ChatLeftText}
                {tooltip.content}
              </div>
              <button
                type="button"
                class="close"
                data-dismiss="alert"
                aria-label="Close"
                on:click|stopPropagation={() => removeTooltip(tooltip)}>
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
          </div>
        {/each}
        {#each scene.skips || [] as skip, idx}
          <div class="we-skip-track we-track">
            <div
              class="alert alert-danger mb-0 mr-1"
              style="transform: translateX({Math.max(0, (tracksWidth * skip.timeOffset) / scene.totalTime)}px);width: {(tracksWidth * skip.duration) / scene.totalTime}px"
              on:click|stopPropagation={() => editSkip(skip, idx)}
              role="button">
              <div class="flex-fill">
                {@html Scissors}
                {formatTime(skip.duration)}
              </div>
              <button
                type="button"
                class="close"
                data-dismiss="alert"
                aria-label="Close"
                on:click|stopPropagation={() => removeSkip(skip)}>
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
          </div>
        {/each}
      </div>
    </div>
  {/if}
</div>

{#if modalPayload}
  <Modal
    on:close={() => (modalPayload = null)}
    on:ok={() => {
      if (modalPayload.type === 'tooltip') {
        scene.tooltips[modalPayload.index] = modalPayload.payload;
      }
      if (modalPayload.type === 'skip') {
        if (!scene.skips) {
          scene.skips = [];
        }
        modalPayload.index === -1 ? (scene.skips = scene.skips.concat(modalPayload.payload)) : (scene.skips[modalPayload.index] = modalPayload.payload);
      }
      modalPayload = null;
    }}>
    <div slot="title">
      {{ tooltip: '编辑 Tooltip', skip: '编辑快进' }[modalPayload.type] || ''}
    </div>
    <div slot="body">
      {#if modalPayload.type === 'tooltip'}
        <textarea
          class="form-control form-control-sm mb-1"
          bind:value={modalPayload.payload.content} />
      {/if}
      {#if modalPayload.type === 'skip'}
        <div class="form-row">
          <div class="col">
            <input
              type="number"
              class="form-control"
              placeholder="起始"
              bind:value={modalPayload.payload.timeOffset} />
          </div>
          <div class="col">
            <input
              type="number"
              class="form-control"
              placeholder="持续时间"
              bind:value={modalPayload.payload.duration} />
          </div>
        </div>
      {/if}
    </div>
    <span slot="ok">保存</span>
  </Modal>
{/if}
