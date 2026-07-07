<template>
  <!-- HMR 测试 - 第二次修改 - Vite 5.4.21 -->
  <div class="link-icon">
    <svg-icon
      :icon-class="iconClass"
      @click="goto"
    />
  </div>
</template>

<script setup lang="ts">
const props = defineProps({
  iconClass: {
    type: String,
    required: true
  },
  url: {
    type: String,
    required: true
  }
})

function goto() {
  window.open(props.url)
}
</script>
