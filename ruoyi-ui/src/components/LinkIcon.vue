<template>
  <div>
    <svg-icon
      :icon-class="iconClass"
      @click="goto"
    />
  </div>
</template>

<script setup lang="ts">
import { defineProps } from 'vue'

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
