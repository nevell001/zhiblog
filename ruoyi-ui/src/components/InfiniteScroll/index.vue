<template>
  <div ref="scrollContainer" class="infinite-scroll-container">
    <slot :items="items" :loading="loading" :has-more="hasMore"></slot>

    <!-- 加载状态指示器 -->
    <div v-if="loading" class="loading-indicator">
      <el-icon class="is-loading">
        <Loading />
      </el-icon>
      <span>加载中...</span>
    </div>

    <!-- 没有更多内容 -->
    <div v-else-if="!hasMore && items.length > 0" class="no-more-indicator">
      <span>没有更多内容了</span>
    </div>

    <!-- 空状态 -->
    <div v-else-if="!loading && items.length === 0" class="empty-indicator">
      <slot name="empty">
        <el-empty description="暂无数据" />
      </slot>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import { Loading } from '@element-plus/icons-vue'

const props = defineProps({
  // 加载更多数据的函数
  loadMore: {
    type: Function,
    required: true
  },
  // 数据列表
  items: {
    type: Array,
    default: () => []
  },
  // 是否还有更多数据
  hasMore: {
    type: Boolean,
    default: true
  },
  // 当前加载状态
  loading: {
    type: Boolean,
    default: false
  },
  // 触发加载的距离阈值（像素）
  threshold: {
    type: Number,
    default: 100
  },
  // 是否启用无限滚动
  enabled: {
    type: Boolean,
    default: true
  }
})

const emit = defineEmits(['load-more'])

const scrollContainer = ref(null)
let observer = null

// 处理滚动事件
const handleScroll = () => {
  if (!props.enabled || props.loading || !props.hasMore) return

  const container = scrollContainer.value
  if (!container) return

  const scrollTop = container.scrollTop || window.pageYOffset || document.documentElement.scrollTop
  const scrollHeight = container.scrollHeight || document.documentElement.scrollHeight
  const clientHeight = container.clientHeight || window.innerHeight

  // 当滚动到距离底部threshold像素时触发加载
  if (scrollTop + clientHeight >= scrollHeight - props.threshold) {
    emit('load-more')
  }
}

// 使用Intersection Observer优化性能
const setupIntersectionObserver = () => {
  if (!props.enabled || !window.IntersectionObserver) return

  // 创建一个底部的哨兵元素
  const sentinel = document.createElement('div')
  sentinel.style.height = '1px'
  sentinel.style.width = '1px'
  sentinel.style.position = 'absolute'
  sentinel.style.bottom = '0'
  sentinel.style.left = '0'
  sentinel.style.visibility = 'hidden'
  sentinel.className = 'scroll-sentinel'

  const container = scrollContainer.value
  if (container === document.documentElement) {
    document.body.appendChild(sentinel)
  } else {
    container.appendChild(sentinel)
  }

  observer = new IntersectionObserver(
    entries => {
      entries.forEach(entry => {
        if (entry.isIntersecting && props.enabled && !props.loading && props.hasMore) {
          emit('load-more')
        }
      })
    },
    {
      root: container === document.documentElement ? null : container,
      rootMargin: `${props.threshold}px`,
      threshold: 0.1
    }
  )

  observer.observe(sentinel)
}

// 清理观察器
const cleanupObserver = () => {
  if (observer) {
    observer.disconnect()
    observer = null
  }

  // 移除哨兵元素
  const sentinel = document.querySelector('.scroll-sentinel')
  if (sentinel) {
    sentinel.remove()
  }
}

// 防抖函数
const debounce = (func, wait) => {
  let timeout
  return function executedFunction(...args) {
    const later = () => {
      clearTimeout(timeout)
      func(...args)
    }
    clearTimeout(timeout)
    timeout = setTimeout(later, wait)
  }
}

// 防抖处理的滚动事件
const debouncedScroll = debounce(handleScroll, 100)

onMounted(() => {
  nextTick(() => {
    // 优先使用Intersection Observer（性能更好）
    if (window.IntersectionObserver) {
      setupIntersectionObserver()
    } else {
      // 降级到滚动事件监听
      const container = scrollContainer.value
      if (container === document.documentElement) {
        window.addEventListener('scroll', debouncedScroll, { passive: true })
      } else {
        container.addEventListener('scroll', debouncedScroll, { passive: true })
      }
    }
  })
})

onUnmounted(() => {
  cleanupObserver()
  const container = scrollContainer.value
  if (container === document.documentElement) {
    window.removeEventListener('scroll', debouncedScroll)
  } else {
    container.removeEventListener('scroll', debouncedScroll)
  }
})
</script>

<style scoped>
.infinite-scroll-container {
  position: relative;
  min-height: 200px;
}

.loading-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  color: #909399;
  font-size: 14px;
}

.loading-indicator .el-icon {
  margin-right: 8px;
  animation: rotating 2s linear infinite;
}

.no-more-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  color: #909399;
  font-size: 14px;
  position: relative;
}

.no-more-indicator::before,
.no-more-indicator::after {
  content: '';
  flex: 1;
  height: 1px;
  background: #e4e7ed;
  margin: 0 16px;
}

.empty-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
}

@keyframes rotating {
  0% {
    transform: rotateZ(0deg);
  }
  100% {
    transform: rotateZ(360deg);
  }
}
</style>
