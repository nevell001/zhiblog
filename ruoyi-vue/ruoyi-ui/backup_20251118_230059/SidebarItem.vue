<template>
  <div v-if="!item.hidden">
    <template v-if="hasOneShowingChild(item.children, item) && (!onlyOneChild.children || onlyOneChild.noShowingChildren) && !item.alwaysShow">
      <app-link v-if="onlyOneChild.meta" :to="resolvePath(onlyOneChild.path, onlyOneChild.query)">
        <el-menu-item :index="resolvePath(onlyOneChild.path)" :class="{ 'submenu-title-noDropdown': !isNest }">
          <svg-icon :icon-class="onlyOneChild.meta.icon || (item.meta && item.meta.icon)"/>
          <template v-if="onlyOneChild.meta" #title><span class="menu-title" :title="hasTitle(onlyOneChild.meta.title)" @click="handleTitleClick(onlyOneChild)" @mousedown="handleTitleMouseDown" @mouseup="handleTitleMouseUp" style="cursor: pointer; display: inline-block; padding: 4px 0;">{{ onlyOneChild.meta.title || '' }}</span></template>
        </el-menu-item>
      </app-link>
    </template>

    <el-sub-menu v-else ref="subMenu" :index="resolvePath(item.path)" :popper-append-to-body="false">
      <template v-if="item.meta" #title>
        <svg-icon :icon-class="item.meta && item.meta.icon" />
        <span class="menu-title" :title="hasTitle(item.meta.title)" @click="handleTitleClick(item)" @mousedown="handleTitleMouseDown" @mouseup="handleTitleMouseUp" style="cursor: pointer; display: inline-block; padding: 4px 0;">{{ item.meta.title || '' }}</span>
      </template>

      <sidebar-item
        v-for="(child, index) in (item.children || [])"
        :key="child.path + index"
        :is-nest="true"
        :item="child"
        :base-path="resolvePath(child.path)"
        class="nest-menu"
      />
    </el-sub-menu>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { isExternal } from '@/utils/validate'
import AppLink from './Link'
import { getNormalPath } from '@/utils/ruoyi'

const props = defineProps({
  // route object
  item: {
    type: Object,
    required: true
  },
  isNest: {
    type: Boolean,
    default: false
  },
  basePath: {
    type: String,
    default: ''
  }
})

const onlyOneChild = ref({})
const subMenu = ref(null)

// 确保subMenu正确初始化
onMounted(() => {
  // 在组件挂载后，可以访问subMenu的DOM引用
  if (subMenu.value) {
    // 确保子菜单正确渲染
    subMenu.value.updatePopper && subMenu.value.updatePopper()
  }
})

function hasOneShowingChild(children = [], parent) {
  // 确保 children 是数组
  if (!Array.isArray(children)) {
    return false
  }
  
  const showingChildren = children.filter(item => {
    if (!item) return false
    if (item.hidden) {
      return false
    }
    onlyOneChild.value = item
    return true
  })

  // When there is only one child router, the child router is displayed by default
  if (showingChildren.length === 1) {
    return true
  }

  // Show parent if there are no child router to display
  if (showingChildren.length === 0) {
    onlyOneChild.value = { ...parent, path: '', noShowingChildren: true }
    return true
  }

  return false
}

function resolvePath(routePath, routeQuery) {
  // 避免空指针错误
  if (!routePath) {
    return ''
  }
  
  if (isExternal(routePath)) {
    return routePath
  }
  if (isExternal(props.basePath)) {
    return props.basePath
  }
  
  // 处理路径拼接，确保路径格式正确
  let fullPath = ''
  if (props.basePath && routePath) {
    // 避免重复的斜杠
    if (props.basePath.endsWith('/') && routePath.startsWith('/')) {
      fullPath = getNormalPath(props.basePath + routePath.substring(1))
    } else if (!props.basePath.endsWith('/') && !routePath.startsWith('/')) {
      fullPath = getNormalPath(props.basePath + '/' + routePath)
    } else {
      fullPath = getNormalPath(props.basePath + routePath)
    }
  } else if (props.basePath) {
    fullPath = getNormalPath(props.basePath)
  } else if (routePath) {
    fullPath = getNormalPath(routePath)
  }
  
  // 确保路径不以//开头
  if (fullPath.startsWith('//')) {
    fullPath = fullPath.substring(1)
  }
  
  // 标准化路径
  fullPath = fullPath.replace(/\/$/, '') || '/'
  
  if (routeQuery) {
    try {
      let query = JSON.parse(routeQuery)
      return { path: fullPath, query: query }
    } catch (error) {
      console.warn('路由查询参数解析失败:', routeQuery, error)
      return fullPath
    }
  }
  return fullPath
}

function hasTitle(title){
  // 确保正确处理空值
  if (!title) {
    return ""
  }
  if (title.length > 5) {
    return title
  } else {
    return ""
  }
}

// 菜单项点击状态跟踪
let isMenuClicking = false

// 鼠标按下事件处理
function handleTitleMouseDown() {
  isMenuClicking = true
  console.log('菜单标题鼠标按下')
}

// 鼠标释放事件处理
function handleTitleMouseUp() {
  isMenuClicking = false
  console.log('菜单标题鼠标释放')
}

// 直接处理菜单标题点击事件
  function handleTitleClick(menuItem, e) {
  console.log('菜单标题点击事件:', menuItem.meta?.title)
  
  // 如果提供了事件对象，阻止事件冒泡
  if (e) {
    e.stopPropagation()
  }
  
  // 获取目标路径
  let targetPath = ''
  if (menuItem.path) {
    targetPath = resolvePath(menuItem.path, menuItem.query)
  } else if (menuItem.children && menuItem.children.length > 0) {
    // 如果是父菜单，尝试使用第一个子菜单的路径
    const firstChild = menuItem.children[0]
    if (firstChild && firstChild.path) {
      targetPath = resolvePath(firstChild.path)
    }
  }
  
  if (!targetPath) {
    console.warn('无法确定目标路径')
    return
  }
  
  console.log('尝试导航到:', targetPath)
  
  // 获取事件目标元素用于反馈
  const targetElement = e ? e.currentTarget : null
  
  // 添加视觉反馈
  if (targetElement) {
    targetElement.style.backgroundColor = 'rgba(64, 158, 255, 0.2)'
    setTimeout(() => {
      targetElement.style.backgroundColor = ''
    }, 300)
  }
  
  // 添加临时提示
  const feedback = document.createElement('div')
  feedback.style.position = 'fixed'
  feedback.style.top = '20px'
  feedback.style.right = '20px'
  feedback.style.background = 'rgba(0,128,0,0.8)'
  feedback.style.color = 'white'
  feedback.style.padding = '10px'
  feedback.style.borderRadius = '5px'
  feedback.style.zIndex = '9999'
  feedback.textContent = `标题点击: ${menuItem.meta?.title}`
  document.body.appendChild(feedback)
  setTimeout(() => {
    feedback.remove()
  }, 3000)
  
  // 尝试通过多种方式进行导航
  try {
    // 尝试直接通过window.location.href导航
    if (targetPath.startsWith('http')) {
      window.open(targetPath, '_blank')
    } else {
      // 优先尝试Vue Router跳转
      const router = window.__VUE_APP__?.router
      if (router && typeof router.push === 'function') {
        console.log('使用Vue Router导航')
        router.push(targetPath).catch(err => {
          console.error('Vue Router导航失败，尝试location跳转:', err)
          // 备选方案：直接修改location
          window.location.href = targetPath
        })
      } else {
        console.log('使用window.location跳转')
        window.location.href = targetPath
      }
    }
  } catch (error) {
    console.error('导航执行异常:', error)
    // 最后的备选方案
    window.location.href = targetPath
  }
  
  // 无论导航是否成功，都标记为已点击
  console.log('菜单标题点击处理完成:', menuItem.meta?.title)
}
</script>
