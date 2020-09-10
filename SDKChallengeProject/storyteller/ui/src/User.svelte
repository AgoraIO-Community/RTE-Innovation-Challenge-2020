<script lang="ts">
  import { onMount, tick } from "svelte";
  // @ts-ignore
  const { ipcRenderer } = require("electron");
  import type { User } from "./utils";
  import { hashStringToColor, invertColor } from "./utils";
  import { users } from "./store";

  export let username: string = "?";
  let user: User = {
    username: "#",
  };
  $: normalizedName = user ? user.username : username;
  $: char = normalizedName.slice(0, 1);
  $: color = hashStringToColor(normalizedName);

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
    users.update((prev) =>
      prev.concat({
        ...user,
        readonly: false,
        uid: "",
        uiMap: {},
      })
    );
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
