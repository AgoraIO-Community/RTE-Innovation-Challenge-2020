<script lang="ts">
  import { onMount, tick } from "svelte";
  // @ts-ignore
  const { ipcRenderer } = require("electron");
  import type { User } from "./utils";

  export let username: string = "?";
  let user: User = {
    username: "#",
  };
  $: normalizedName = user ? user.username : username;
  $: char = normalizedName.slice(0, 1);
  $: color = hashStringToColor(normalizedName);

  function djb2(str: string) {
    var hash = 5381;
    for (var i = 0; i < str.length; i++) {
      hash = (hash << 5) + hash + str.charCodeAt(i); /* hash * 33 + c */
    }
    return hash;
  }

  function hashStringToColor(str: string) {
    var hash = djb2(str);
    var r = (hash & 0xff0000) >> 16;
    var g = (hash & 0x00ff00) >> 8;
    var b = hash & 0x0000ff;
    return (
      "#" +
      ("0" + r.toString(16)).substr(-2) +
      ("0" + g.toString(16)).substr(-2) +
      ("0" + b.toString(16)).substr(-2)
    );
  }

  function invertColor(hex: string) {
    if (hex.indexOf("#") === 0) {
      hex = hex.slice(1);
    }
    // convert 3-digit hex to 6-digits.
    if (hex.length === 3) {
      hex = hex[0] + hex[0] + hex[1] + hex[1] + hex[2] + hex[2];
    }
    if (hex.length !== 6) {
      throw new Error("Invalid HEX color.");
    }
    var r = parseInt(hex.slice(0, 2), 16),
      g = parseInt(hex.slice(2, 4), 16),
      b = parseInt(hex.slice(4, 6), 16);
    return r * 0.299 + g * 0.587 + b * 0.114 > 186 ? "#000000" : "#FFFFFF";
  }

  let editing = false;
  let input: HTMLSpanElement;
  async function edit() {
    editing = true;
    await tick();
    input.focus();
  }

  async function save() {
    editing = false;
    user.username = input.innerText;
    await ipcRenderer.invoke("setStoreValue", "user", user);
  }

  onMount(async () => {
    const storedUser = await ipcRenderer.invoke("getStoreValue", "user");
    if (storedUser) {
      user = storedUser;
    }
  });
</script>

<style>
  .user {
    width: 32px;
    height: 32px;
    border-radius: 16px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 18px;
    font-weight: bold;
    transition: ease-in-out 0.18s;
  }

  .user.editable {
    padding: 0 16px;
    width: auto;
    min-width: 32px;
  }
</style>

<div
  class="user mr-2 {editing ? 'editable' : ''}"
  role="button"
  style="background: {color}; color: {invertColor(color)}"
  on:click={edit}>
  <span
    title={user.username === '#' ? '点击修改用户名' : normalizedName}
    contenteditable={editing}
    bind:this={input}
    on:blur={save}>{char}</span>
</div>
