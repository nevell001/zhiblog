<template>
  <div ref="rightPanel" :class="{ show: show }" class="rightPanel-container">
    <div class="rightPanel-background"></div>
    <div class="rightPanel">
      <div class="rightPanel-items">
        <slot></slot>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted, onBeforeUnmount } from 'vue'

const props = defineProps({
  clickNotClose: {
    default: false,
    type: Boolean
  },
  buttonTop: {
    default: 250,
    type: Number
  }
})

const show = ref(false)
const rightPanel = ref(null)

function addEventClick() {
  window.addEventListener('click', closeSidebar)
}

function closeSidebar(evt) {
  if (props.clickNotClose) return
  // 如果点击的是面板内部，不关闭
  if (rightPanel.value && rightPanel.value.contains(evt.target)) return
  show.value = false
}

function insertToBody() {
  const el = rightPanel.value
  el.style.right = '0'
  document.body.appendChild(el)
}

onMounted(() => {
  insertToBody()
  addEventClick()
})

onBeforeUnmount(() => {
  if (rightPanel.value && rightPanel.value.parentNode) {
    rightPanel.value.parentNode.removeChild(rightPanel.value)
  }
  window.removeEventListener('click', closeSidebar)
})

defineExpose({
  show
})
</script>

<style>
.showRightPanel {
  overflow: hidden;
  position: relative;
  width: calc(100% - 15px);
}
</style>

<style lang="scss" scoped>
.rightPanel-background {
  position: fixed;
  top: 0;
  left: 0;
  opacity: 0;
  transition: opacity 0.3s cubic-bezier(0.7, 0.3, 0.1, 1);
  background: rgba(0, 0, 0, 0.2);
  z-index: -1;
}

.rightPanel {
  position: fixed;
  top: 0;
  right: 0;
  z-index: 2000;
  width: 320px;
  max-width: 100%;
  height: 100vh;
  box-shadow: 0 0 15px 0 rgba(0, 0, 0, 0.05);
  transition: all 0.25s cubic-bezier(0.7, 0.3, 0.1, 1);
  transform: translate(100%);
  background: #fff;
}

.rightPanel-container {
  transition: all 0.25s cubic-bezier(0.7, 0.3, 0.1, 1);
  position: relative;
  z-index: 2000;
}

.rightPanel-container.show {
  .rightPanel-background {
    z-index: 20000;
    opacity: 1;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
  }

  .rightPanel {
    transform: translate(0);
  }
}

.rightPanel-items {
  height: 100vh;
  overflow-y: auto;
  padding: 20px;
  box-sizing: border-box;
}
</style>
