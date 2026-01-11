<template>
  <component :is="type" v-bind="linkProps()" @click="handleLinkClick" @error="handleLinkError">
    <slot></slot>
  </component>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { isExternal } from '@/utils/validate'

const props = defineProps({
  to: {
    type: [String, Object],
    required: true
  }
})

const router = useRouter()

const isExt = computed(() => {
  return isExternal(props.to)
})

const type = computed(() => {
  if (isExt.value) {
    return 'a'
  }
  return 'router-link'
})

function linkProps() {
  if (isExt.value) {
    return {
      href: props.to,
      target: '_blank',
      rel: 'noopener'
    }
  }
  return {
    to: props.to
  }
}

// 🔥 关键改进2: 优化事件绑定处理
function handleLinkClick(event) {
  console.log('Link组件点击事件:', props.to)

  // 对于外部链接，让浏览器默认处理
  if (isExt.value) {
    return true
  }

  // 对于内部链接，直接使用window.location跳转
  try {
    // 阻止默认事件
    event.preventDefault()

    // 获取目标路径
    const targetPath = typeof props.to === 'string' ? props.to : props.to.path

    // 直接使用window.location跳转
    console.log('使用window.location跳转到:', targetPath)
    window.location.href = targetPath

    return false
  } catch (error) {
    console.error('跳转失败:', error)
    ElMessage.error('页面跳转失败，请重试')
    return false
  }
}

// 错误处理
function handleLinkError(error) {
  console.error('Link组件错误:', error)
  ElMessage.error('链接加载失败')
}
</script>
