<script lang="ts">
  import type { eventWithTime } from "rrweb/typings/types";
  import { onMount } from "svelte";
  import { fade, fly } from "svelte/transition";
  // @ts-ignore
  const { ipcRenderer } = require("electron");
  import WebEditor from "./WebEditor.svelte";
  import Story from "./Story.svelte";
  import User from "./User.svelte";
  import { formatTime, genNewScene, genId } from "./utils";
  import type { Chapter, Scene } from "./utils";
  import PlusCircle from "./icons/plus-circle.svg";
  import Cross from "./icons/cross.svg";
  import ArrowLeft from "./icons/arrow-left.svg";

  let stories: Array<{
    id: string;
    name: string;
    chapters: Array<Chapter>;
  }> = [];
  let currentStory = stories.length ? stories[0] : null;
  let previewStory: typeof stories[0] | null = null;

  function addStory() {
    const newStory = {
      id: genId(),
      name: "",
      chapters: [
        {
          id: genId(),
          name: "",
          sequence: [],
        },
      ],
    };
    stories = [...stories, newStory];
    currentStory = newStory;
  }

  let currentChapter: Chapter | null = null;

  function addChapter() {
    const newChapter = {
      id: genId(),
      name: "",
      sequence: [],
    };
    currentStory.chapters = [...currentStory.chapters, newChapter];
  }

  let currentScene: Scene | null = null;

  function addScene(chapter: Chapter) {
    currentChapter = chapter;
    const newScene = genNewScene();
    chapter.sequence = chapter.sequence.concat(newScene);
    currentScene = newScene;
  }

  function editScene(chapter: Chapter, scene: Scene) {
    currentChapter = chapter;
    currentScene = scene;
  }

  function finishScene() {
    currentChapter = null;
    currentScene = null;
  }

  async function removeScene(chapter, scene) {
    chapter.sequence = chapter.sequence.filter((s) => s !== scene);
    currentStory.chapters = currentStory.chapters.slice();
    await flushStories();
  }

  async function flushStories() {
    stories = stories.slice();
    await ipcRenderer.invoke("setStoreValue", "stories", stories);
  }

  function addStylesheetRules(rules: string[]): void {
    const sheets = document.styleSheets[document.styleSheets.length - 1];
    try {
      rules.forEach((rule) => {
        (sheets as CSSStyleSheet).insertRule(
          rule,
          (sheets as CSSStyleSheet).cssRules.length
        );
      });
      // eslint-disable-next-line
    } catch {}
  }
  function patchStyle() {
    if (!window.navigator.platform.includes("Mac")) {
      // scrollbar style
      const scrollbarArr = [
        "*::-webkit-scrollbar-track {background-color: transparent}",
        "*::-webkit-scrollbar {width: 8px; height: 8px; background-color: transparent}",
        "*::-webkit-scrollbar-thumb {border-radius: 4px; background-color: rgba(225, 229, 235, 0.8)}",
        "*::-webkit-scrollbar-thumb:hover {background-color: rgba(142, 154, 169, 0.8)}",
        "*::-webkit-scrollbar-thumb:active {background-color: rgba(53, 63, 78, 0.8)}",
      ];
      addStylesheetRules(scrollbarArr);
    }
  }

  onMount(async () => {
    patchStyle();
    stories = (await ipcRenderer.invoke("getStoreValue", "stories")) || [];
    currentStory = stories.length ? stories[0] : null;
  });
</script>

<style>
  [contenteditable] {
    padding: 0.5em;
    border: 1px solid #eee;
    border-radius: 4px;
  }
  [contenteditable]:hover {
    border-color: #5a5c69;
  }
  [contenteditable]:empty:before {
    content: attr(placeholder);
    color: rgba(0, 0, 0, 0.4);
  }

  #accordionSidebar .btn-light {
    width: 60%;
    max-width: 100px;
    display: block;
    margin: 0 auto;
  }

  .empty {
    display: flex;
    flex: 1;
    align-items: center;
    justify-content: center;
  }

  .chapter {
    margin-left: 1.5em;
    padding-left: 0.5em;
    margin-bottom: 0.5em;
    border-left: 2px solid rgba(0, 0, 0, 0.2);
  }

  .remove-btn {
    position: absolute;
    right: -0.5em;
    top: -0.5em;
    display: none;
    z-index: 9;
    font-size: 16px;
    padding: 0;
  }

  .card:hover .remove-btn {
    display: inherit;
  }

  .chapters {
    display: flex;
    overflow: auto;
    align-items: center;
    padding: 0.5em 0;
  }

  .add-scene {
    height: 64px;
    font-size: 24px;
  }

  :global(.card-sm) {
    padding: 0.25em 0.5em;
  }

  .editor-wrapper {
    flex: 1;
    position: relative;
  }
</style>

<div id="wrapper">
  <ul
    class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion"
    id="accordionSidebar">
    <div class="sidebar-brand d-flex align-items-center justify-content-center">
      Storyteller
    </div>
    <hr class="sidebar-divider" />
    <div class="sidebar-heading">故事</div>
    {#each stories as story}
      <li class="nav-item">
        <!-- svelte-ignore a11y-invalid-attribute -->
        <a
          class="nav-link"
          href="#"
          on:click={() => {
            currentStory = story;
          }}>
          <span>{story.name}</span>
        </a>
        <div class={`collapse ${currentStory === story ? 'show' : ''}`}>
          <div class="bg-white py-2 collapse-inner rounded">
            <h6 class="collapse-header">章节：</h6>
            {#each story.chapters as chapter}
              <div class="collapse-item" href="#">{chapter.name}</div>
            {/each}
          </div>
        </div>
      </li>
    {/each}
    <hr class="sidebar-divider" />
    <button type="button" class="btn btn-light" on:click={addStory}>
      新增故事
    </button>
  </ul>
  <div id="content-wrapper" class="d-flex flex-column">
    <div id="content">
      <nav
        class="navbar navbar-expand navbar-light bg-white topbar static-top mb-4
          shadow justify-content-end">
        {#if currentChapter}
          <div class="navbar-nav mr-auto" transition:fade>
            <span
              role="button"
              class="mr-2"
              on:click={finishScene}>{@html ArrowLeft}</span>
            <strong>{currentChapter.name}</strong>
          </div>
        {/if}
        <User />
        <button class="btn btn-success mr-3">协作</button>
        {#if currentStory}
          <button
            class="btn btn-light mr-3"
            on:click={() => {
              if (previewStory) {
                previewStory = null;
              } else {
                previewStory = currentStory;
              }
            }}>
            {previewStory ? '返回' : '预览'}
          </button>
        {/if}
      </nav>
      {#if !currentStory}
        <div class="empty">
          <button type="button" class="btn btn-primary" on:click={addStory}>
            新增故事
          </button>
        </div>
      {:else if previewStory}
        <Story story={previewStory} />
      {:else if currentChapter}
        <div class="editor-wrapper" in:fly>
          <WebEditor
            chapter={currentChapter}
            sceneIdx={currentChapter.sequence.indexOf(currentScene)}
            on:update={flushStories} />
        </div>
      {:else}
        <div class="container">
          <h1
            contenteditable
            bind:innerHTML={currentStory.name}
            placeholder="请输入故事标题"
            on:blur={() => {
              flushStories();
            }} />
          <div />
          {#each currentStory.chapters as chapter}
            <div class="chapter">
              <h2
                contenteditable
                bind:innerHTML={chapter.name}
                placeholder="请输入章节标题"
                on:blur={() => {
                  flushStories();
                }} />
              <div class="chapters">
                {#each chapter.sequence as scene}
                  {#if currentScene === scene}
                    <div class="col-12" />
                  {:else}
                    <div class="col-3">
                      <div
                        class="card border-left-primary shadow h-100 py-2"
                        on:click={() => editScene(chapter, scene)}
                        role="button">
                        <button
                          class="btn btn-sm btn-danger btn-circle remove-btn"
                          on:click|stopPropagation={() => removeScene(chapter, scene)}>
                          {@html Cross}
                        </button>
                        <div class="card-body card-sm">
                          <div class="row no-gutters align-items-center">
                            <div class="col mr-2">
                              <div
                                class="text-xs font-weight-bold text-primary
                                  text-uppercase mb-1"
                                contenteditable
                                bind:innerHTML={scene.name}
                                placeholder="请输入场景名称"
                                on:blur={() => {
                                  flushStories();
                                }}
                                on:click={(e) => e.stopPropagation()}>
                                {scene.name}
                              </div>
                              <div
                                class="h5 mb-0 font-weight-bold text-gray-800">
                                时长：{formatTime(scene.totalTime)}
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  {/if}
                {/each}
                {#if !currentScene}
                  <button
                    class="btn btn-light add-scene"
                    on:click={() => addScene(chapter)}>
                    {@html PlusCircle}
                  </button>
                {/if}
              </div>
            </div>
          {/each}
          <div>
            <button type="button" class="btn btn-light" on:click={addChapter}>
              新增章节
            </button>
          </div>
        </div>
      {/if}
    </div>
  </div>
</div>
