<template>
  <component 
    :is="type" 
    v-bind="linkProps()" 
    @click="handleLinkClick"
    @error="handleLinkError"
  >
    <slot />
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
  
  // 对于内部链接，检查路由状态
  try {
    // 检查目标路由是否存在
    const targetPath = typeof props.to === 'string' ? props.to : props.to.path
    const route = router.resolve(targetPath)
    
    if (!route || route.matched.length === 0) {
      console.warn('目标路由不存在:', targetPath)
      event.preventDefault()
      
      ElMessage.warning({
        message: `页面路径不存在: ${targetPath}`,
        duration: 3000
      })
      return false
    }
    
    console.log('路由验证通过:', targetPath)
    return true
    
  } catch (error) {
    console.error('路由验证失败:', error)
    event.preventDefault()
    
    ElMessage.error('路由配置错误，请联系管理员')
    return false
  }
}

// 错误处理
function handleLinkError(error) {
  console.error('Link组件错误:', error)
  ElMessage.error('链接加载失败')
}
</script>