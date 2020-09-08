<script lang="ts">
  import { createEventDispatcher } from "svelte";
  import { fly } from "svelte/transition";

  const dispatch = createEventDispatcher();

  function handleClose() {
    dispatch("close");
  }

  function handleOk() {
    dispatch("ok");
  }
</script>

<div class="modal" tabindex="-1" transition:fly>
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">
          <slot name="title" />
        </h5>
        <button
          type="button"
          class="close"
          data-dismiss="modal"
          aria-label="Close"
          on:click={handleClose}>
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <slot name="body" />
      </div>
      <div class="modal-footer">
        <button
          type="button"
          class="btn btn-secondary"
          data-dismiss="modal"
          on:click={handleClose}>取消</button>
        <button type="button" class="btn btn-primary" on:click={handleOk}>
          <slot name="ok" />
        </button>
      </div>
    </div>
  </div>
</div>
