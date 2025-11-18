<template>
  <div v-if="!item.hidden">
    <template v-if="hasOneShowingChild(item.children, item) && (!onlyOneChild.children || onlyOneChild.noShowingChildren) && !item.alwaysShow">
      <app-link v-if="onlyOneChild.meta" :to="resolvePath(onlyOneChild.path, onlyOneChild.query)">
        <el-menu-item :index="resolvePath(onlyOneChild.path)" :class="{ 'submenu-title-noDropdown': !isNest }">
          <svg-icon :icon-class="onlyOneChild.meta.icon || (item.meta && item.meta.icon)"/>
          <template #title><span class="menu-title" :title="hasTitle(onlyOneChild.meta.title)">{{ onlyOneChild.meta.title || '' }}</span></template>
        </el-menu-item>
      </app-link>
    </template>

    <el-sub-menu v-else ref="subMenu" :index="resolvePath(item.path)" :popper-append-to-body="false">
      <template v-if="item.meta" #title>
        <svg-icon :icon-class="item.meta && item.meta.icon" />
        <span class="menu-title" :title="hasTitle(item.meta.title)">{{ item.meta.title || '' }}</span>
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
</script>
