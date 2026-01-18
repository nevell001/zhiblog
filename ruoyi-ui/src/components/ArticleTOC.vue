<template>
  <div
    class="article-toc"
    :class="{ 'toc-fixed': isFixed, 'toc-hidden': isHidden }"
  >
    <div class="toc-header">
      <h4 class="toc-title">
        <el-icon><List /></el-icon>
        目录
      </h4>
      <el-button
        class="toc-toggle"
        :icon="isExpanded ? ArrowUp : ArrowDown"
        circle
        size="small"
        @click="toggleExpand"
      />
    </div>

    <transition name="slide-fade">
      <div
        v-show="isExpanded"
        class="toc-content"
      >
        <ul
          v-if="tocItems.length > 0"
          class="toc-list"
        >
          <li
            v-for="(item, index) in tocItems"
            :key="index"
            class="toc-item"
            :class="{
              'toc-item-active': activeIndex === index,
              [`toc-level-${item.level}`]: true
            }"
            @click="scrollToHeading(item.id)"
          >
            <span
              class="toc-dot"
              :class="{ 'dot-active': activeIndex === index }"
            ></span>
            <span class="toc-text">{{ item.text }}</span>
          </li>
        </ul>
        <div
          v-else
          class="toc-empty"
        >
          <el-icon :size="32">
            <DocumentCopy />
          </el-icon>
          <p>暂无目录</p>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick, watch } from 'vue'
import { List, ArrowUp, ArrowDown, DocumentCopy } from '@element-plus/icons-vue'

const props = defineProps({
  content: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['toc-ready'])

// 响应式数据
const tocItems = ref([])
const activeIndex = ref(-1)
const isFixed = ref(false)
const isExpanded = ref(true)
const isHidden = ref(false)
const headings = ref([])

// 切换展开/收起
const toggleExpand = () => {
  isExpanded.value = !isExpanded.value
}

// 提取文章标题
const extractHeadings = () => {
  if (!props.content) {
    tocItems.value = []
    return
  }

  // 创建临时DOM元素来解析HTML
  const tempDiv = document.createElement('div')
  tempDiv.innerHTML = props.content

  const headingElements = tempDiv.querySelectorAll('h1, h2, h3, h4, h5, h6')
  const items = []
  const idSet = new Set()

  headingElements.forEach((heading, index) => {
    const level = parseInt(heading.tagName.charAt(1))
    const text = heading.textContent.trim()

    if (text) {
      // 生成唯一ID
      let id = `heading-${index}`
      if (idSet.has(id)) {
        let counter = 1
        while (idSet.has(`${id}-${counter}`)) {
          counter++
        }
        id = `${id}-${counter}`
      }
      idSet.add(id)

      items.push({
        id,
        text,
        level
      })
    }
  })

  tocItems.value = items

  // 通知父组件目录已生成
  nextTick(() => {
    emit('toc-ready', items)
  })
}

// 滚动到指定标题
const scrollToHeading = id => {
  const element = document.getElementById(id)
  if (element) {
    const offset = 100 // 顶部偏移量
    const elementPosition = element.getBoundingClientRect().top
    const offsetPosition = elementPosition + window.pageYOffset - offset

    window.scrollTo({
      top: offsetPosition,
      behavior: 'smooth'
    })
  }
}

// 检查哪个标题在视口中
const checkActiveHeading = () => {
  if (headings.value.length === 0) return

  const scrollPosition = window.pageYOffset + 150
  let activeIdx = -1

  for (let i = 0; i < headings.value.length; i++) {
    const heading = headings.value[i]
    if (heading.offsetTop <= scrollPosition) {
      activeIdx = i
    } else {
      break
    }
  }

  activeIndex.value = activeIdx
}

// 检查是否固定目录
const checkFixedPosition = () => {
  const articleContent = document.querySelector('.content-body')
  if (!articleContent) return

  const articleRect = articleContent.getBoundingClientRect()
  const tocElement = document.querySelector('.article-toc')

  if (!tocElement) return

  const tocRect = tocElement.getBoundingClientRect()

  // 当文章内容顶部进入视口时，固定目录
  if (articleRect.top <= 100 && tocRect.top <= 100) {
    isFixed.value = true
  } else {
    isFixed.value = false
  }

  // 当滚动到底部时隐藏目录
  if (articleRect.bottom <= window.innerHeight) {
    isHidden.value = true
  } else {
    isHidden.value = false
  }
}

// 获取实际的标题元素
const getHeadingElements = () => {
  const articleContent = document.querySelector('.content-body')
  if (!articleContent) {
    headings.value = []
    return
  }

  const headingElements = articleContent.querySelectorAll('h1, h2, h3, h4, h5, h6')
  headings.value = Array.from(headingElements)
}

// 设置 watch 监听器，Vue 3 会自动清理
watch(
  () => props.content,
  () => {
    extractHeadings()
    nextTick(() => {
      getHeadingElements()
    })
  },
  { immediate: true }
)

// 组件挂载
onMounted(() => {
  extractHeadings()

  nextTick(() => {
    getHeadingElements()
    checkFixedPosition()
  })

  // 添加滚动监听
  window.addEventListener('scroll', () => {
    checkActiveHeading()
    checkFixedPosition()
  })
})

// 组件卸载
onUnmounted(() => {
  window.removeEventListener('scroll', checkActiveHeading)
  window.removeEventListener('scroll', checkFixedPosition)
})
</script>

<style scoped>
.article-toc {
  position: absolute;
  right: -180px;
  top: 20px;
  width: 160px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  border: 1px solid #e9ecef;
  z-index: 100;
  transition: all 0.3s ease;
}

.article-toc.toc-fixed {
  position: fixed;
  right: 20px;
  top: 100px;
}

.article-toc.toc-hidden {
  opacity: 0;
  pointer-events: none;
}

.toc-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  border-bottom: 1px solid #e9ecef;
  background: linear-gradient(135deg, #f8f9fa, #ffffff);
  border-radius: 12px 12px 0 0;
}

.toc-title {
  margin: 0;
  font-size: 0.95rem;
  font-weight: 600;
  color: #1a1a1a;
  display: flex;
  align-items: center;
  gap: 6px;
}

.toc-toggle {
  background: #f0f0f0;
  border: none;
  color: #666;
  transition: all 0.3s ease;
}

.toc-toggle:hover {
  background: #409eff;
  color: white;
  transform: scale(1.1);
}

.toc-content {
  padding: 12px 16px;
  max-height: 400px;
  overflow-y: auto;
}

.toc-content::-webkit-scrollbar {
  width: 4px;
}

.toc-content::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 2px;
}

.toc-content::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 2px;
}

.toc-content::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

.toc-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.toc-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 6px 8px;
  margin-bottom: 4px;
  cursor: pointer;
  border-radius: 6px;
  transition: all 0.3s ease;
  font-size: 0.85rem;
  color: #666;
  line-height: 1.4;
}

.toc-item:hover {
  background: #f0f0f0;
  color: #409eff;
  transform: translateX(-2px);
}

.toc-item.toc-item-active {
  background: linear-gradient(135deg, #409eff, #337ecc);
  color: white;
  font-weight: 600;
}

.toc-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #ccc;
  flex-shrink: 0;
  transition: all 0.3s ease;
}

.toc-dot.dot-active {
  background: white;
  box-shadow: 0 0 8px rgba(255, 255, 255, 0.5);
}

.toc-text {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.toc-level-1 {
  font-weight: 600;
  padding-left: 8px;
}

.toc-level-2 {
  padding-left: 16px;
}

.toc-level-3 {
  padding-left: 24px;
}

.toc-level-4 {
  padding-left: 32px;
}

.toc-level-5 {
  padding-left: 40px;
}

.toc-level-6 {
  padding-left: 48px;
}

.toc-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 30px 20px;
  color: #999;
  text-align: center;
}

.toc-empty p {
  margin: 10px 0 0 0;
  font-size: 0.85rem;
}

.slide-fade-enter-active,
.slide-fade-leave-active {
  transition: all 0.3s ease;
  max-height: 400px;
  opacity: 1;
}

.slide-fade-enter-from,
.slide-fade-leave-to {
  max-height: 0;
  opacity: 0;
}

/* 深色主题适配 */
@media (prefers-color-scheme: dark) {
  .article-toc {
    background: #1a1a1a;
    border-color: #333;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
  }

  .toc-header {
    background: linear-gradient(135deg, #2a2a2a, #1a1a1a);
    border-bottom-color: #333;
  }

  .toc-title {
    color: #e0e0e0;
  }

  .toc-toggle {
    background: #333;
    color: #999;
  }

  .toc-toggle:hover {
    background: #409eff;
    color: white;
  }

  .toc-item {
    color: #999;
  }

  .toc-item:hover {
    background: #333;
    color: #409eff;
  }

  .toc-dot {
    background: #555;
  }

  .toc-empty {
    color: #666;
  }
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .article-toc {
    display: none;
  }
}
</style>
