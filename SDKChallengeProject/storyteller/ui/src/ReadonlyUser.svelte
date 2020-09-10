<script lang="ts">
  import { hashStringToColor, invertColor } from "./utils";
  import type { User } from "./store";

  export let user: User;
  export let small: boolean = false;

  $: normalizedName = user.username;
  $: char = normalizedName.slice(0, 1);
  $: color = hashStringToColor(normalizedName);
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

  .user-sm {
    width: 16px;
    height: 16px;
    border-radius: 8px;
    font-size: 9px;
  }
</style>

<div
  class="user {small ? 'user-sm' : ''}"
  role="button"
  title={normalizedName}
  style="background: {color}; color: {invertColor(color)}">
  {char}
</div>
