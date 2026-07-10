<template>
  <el-scrollbar
    ref="scrollContainer"
    :vertical="false"
    class="scroll-container"
    @wheel.prevent="handleScroll"
  >
    <slot></slot>
  </el-scrollbar>
</template>

<script setup lang="ts">
import { ref, computed, getCurrentInstance, onMounted, onBeforeUnmount } from 'vue'
import { useTagsViewStore } from '@/stores/tagsView'

const tagAndTagSpacing = ref(4)
const { proxy } = getCurrentInstance()

const scrollWrapper = computed(() => {
  if (!proxy.$refs.scrollContainer) {
    return null
  }
  return proxy.$refs.scrollContainer.$refs.wrapRef
})

onMounted(() => {
  if (scrollWrapper.value) {
    scrollWrapper.value.addEventListener('scroll', emitScroll, true)
  }
})

onBeforeUnmount(() => {
  if (scrollWrapper.value) {
    scrollWrapper.value.removeEventListener('scroll', emitScroll)
  }
})

function handleScroll(e) {
  const eventDelta = e.wheelDelta || -e.deltaY * 40
  const $scrollWrapper = scrollWrapper.value
  if ($scrollWrapper) {
    $scrollWrapper.scrollLeft = $scrollWrapper.scrollLeft + eventDelta / 4
  }
}

const emits = defineEmits(['scroll'])
const emitScroll = () => {
  emits('scroll')
}

const tagsViewStore = useTagsViewStore()
const visitedViews = computed(() => tagsViewStore.visitedViews)

function moveToTarget(currentTag) {
  // 安全检查：确保所有必要的元素都存在
  if (!proxy.$refs.scrollContainer) return

  const $container = proxy.$refs.scrollContainer.$el
  if (!$container) return

  const $containerWidth = $container.offsetWidth
  const $scrollWrapper = scrollWrapper.value
  if (!$scrollWrapper) return

  let firstTag = null
  let lastTag = null

  // find first tag and last tag
  if (visitedViews.value.length > 0) {
    firstTag = visitedViews.value[0]
    lastTag = visitedViews.value[visitedViews.value.length - 1]
  }

  if (firstTag === currentTag) {
    $scrollWrapper.scrollLeft = 0
  } else if (lastTag === currentTag) {
    $scrollWrapper.scrollLeft = $scrollWrapper.scrollWidth - $containerWidth
  } else {
    const tagListDom = document.getElementsByClassName('tags-view-item')
    const currentIndex = visitedViews.value.findIndex(item => item === currentTag)
    let prevTag = null
    let nextTag = null
    for (const k in tagListDom) {
      if (k !== 'length' && Object.hasOwnProperty.call(tagListDom, k)) {
        if (
          visitedViews.value[currentIndex - 1] &&
          tagListDom[k].dataset.path === visitedViews.value[currentIndex - 1].path
        ) {
          prevTag = tagListDom[k]
        }
        if (
          visitedViews.value[currentIndex + 1] &&
          tagListDom[k].dataset.path === visitedViews.value[currentIndex + 1].path
        ) {
          nextTag = tagListDom[k]
        }
      }
    }

    // 安全检查：确保 prevTag 和 nextTag 存在
    if (prevTag && nextTag) {
      // the tag's offsetLeft after of nextTag
      const afterNextTagOffsetLeft =
        nextTag.offsetLeft + nextTag.offsetWidth + tagAndTagSpacing.value

      // the tag's offsetLeft before of prevTag
      const beforePrevTagOffsetLeft = prevTag.offsetLeft - tagAndTagSpacing.value
      if (afterNextTagOffsetLeft > $scrollWrapper.scrollLeft + $containerWidth) {
        $scrollWrapper.scrollLeft = afterNextTagOffsetLeft - $containerWidth
      } else if (beforePrevTagOffsetLeft < $scrollWrapper.scrollLeft) {
        $scrollWrapper.scrollLeft = beforePrevTagOffsetLeft
      }
    }
  }
}

defineExpose({
  moveToTarget
})
</script>

<style lang="scss" scoped>
.scroll-container {
  white-space: nowrap;
  position: relative;
  overflow: hidden;
  width: 100%;
  :deep(.el-scrollbar__bar) {
    bottom: 0px;
  }
  :deep(.el-scrollbar__wrap) {
    height: 39px;
  }
}
</style>
