<template>
  <section class="app-main">
    <router-view v-slot="{ Component, route: currentRoute }">
      <transition name="fade-transform" mode="out-in">
        <keep-alive :include="tagsViewStore.cachedViews">
          <component :is="Component" v-if="!currentRoute.meta.link" :key="currentRoute.path" />
        </keep-alive>
      </transition>
    </router-view>
    <iframe-toggle />
    <copyright />
  </section>
</template>

<script setup lang="ts">
import { useRoute } from 'vue-router'
import { onMounted, watch, onUnmounted } from 'vue'
import copyright from './Copyright/index'
import iframeToggle from './IframeToggle/index'
import { useTagsViewStore } from '@/stores/tagsView'

const route = useRoute()
const tagsViewStore = useTagsViewStore()

function addIframe() {
  if (route.meta.link) {
    useTagsViewStore().addIframeView(route)
  }
}

// 在组件挂载时调用
onMounted(() => {
  addIframe()
})

// 监听路由变化
watch(
  () => route.meta.link,
  () => {
    addIframe()
  }
)

// 组件卸载时清理
onUnmounted(() => {
  // Watchers will be automatically cleaned up by Vue 3
})
</script>

<style lang="scss" scoped>
.app-main {
  /* 50= navbar  50  */
  min-height: calc(100vh - 50px);
  width: 100%;
  position: relative;
  overflow: hidden;
}

.app-main:has(.copyright) {
  padding-bottom: 36px;
}

.fixed-header + .app-main {
  padding-top: 50px;
}

.hasTagsView {
  .app-main {
    /* 84 = navbar + tags-view = 50 + 34 */
    min-height: calc(100vh - 84px);
  }

  .fixed-header + .app-main {
    padding-top: 84px;
  }
}
</style>

<style lang="scss">
// fix css style bug in open el-dialog
.el-popup-parent--hidden {
  .fixed-header {
    padding-right: 6px;
  }
}

::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background-color: #f1f1f1;
}

::-webkit-scrollbar-thumb {
  background-color: #c0c0c0;
  border-radius: 3px;
}
</style>
