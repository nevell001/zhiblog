<template>
  <div v-if="!item.hidden">
    <template v-if="hasOneShowingChild(item.children, item) && (!onlyOneChild.children || onlyOneChild.noShowingChildren) && !item.alwaysShow">
      <app-link v-if="onlyOneChild.meta" :to="resolvePath(onlyOneChild.path, onlyOneChild.query)">
        <el-menu-item 
          :index="resolvePath(onlyOneChild.path)" 
          :class="{ 'submenu-title-noDropdown': !isNest, 'no-permission': !hasMenuPermission(onlyOneChild) }"
          @click="handleMenuClick(onlyOneChild, $event)"
        >
          <svg-icon :icon-class="onlyOneChild.meta.icon || (item.meta && item.meta.icon)"/>
          <template v-if="onlyOneChild.meta" #title>
            <span 
              class="menu-title" 
              :title="hasTitle(onlyOneChild.meta.title)" 
              :class="{ 'permission-denied': !hasMenuPermission(onlyOneChild) }"
            >
              {{ onlyOneChild.meta.title || '' }}
              <el-tag v-if="!hasMenuPermission(onlyOneChild)" size="small" type="warning" class="permission-tag">
                权限不足
              </el-tag>
            </span>
          </template>
        </el-menu-item>
      </app-link>
    </template>

    <el-sub-menu 
      v-else 
      ref="subMenu" 
      :index="resolvePath(item.path)" 
      :popper-append-to-body="false"
      :class="{ 'no-permission': !hasMenuPermission(item) }"
    >
      <template v-if="item.meta" #title>
        <svg-icon :icon-class="item.meta && item.meta.icon" />
        <span 
          class="menu-title" 
          :title="hasTitle(item.meta.title)"
          :class="{ 'permission-denied': !hasMenuPermission(item) }"
        >
          {{ item.meta.title || '' }}
          <el-tag v-if="!hasMenuPermission(item)" size="small" type="warning" class="permission-tag">
            权限不足
          </el-tag>
        </span>
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
import { useRouter } from 'vue-router'
import { ElMessage, ElNotification } from 'element-plus'
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

const router = useRouter()
const onlyOneChild = ref({})
const subMenu = ref(null)

// 🔥 关键改进2: 优化事件绑定处理
onMounted(() => {
  if (subMenu.value) {
    subMenu.value.updatePopper && subMenu.value.updatePopper()
  }
})

// 🔥 关键改进4: 权限检查函数
function hasMenuPermission(menuItem) {
  if (!menuItem || !menuItem.meta) {
    return true
  }
  
  // 如果meta中明确标记了权限状态
  if (menuItem.meta.hasPermission !== undefined) {
    return menuItem.meta.hasPermission
  }
  
  // 检查用户角色和权限
  const userStore = useUserStore()
  const roles = userStore.roles || []
  const permissions = userStore.permissions || []
  
  console.log(`🔍 检查菜单权限:`, {
    menuItem: menuItem.meta?.title,
    path: menuItem.path,
    userRoles: roles,
    userPermissions: permissions,
    requiredRoles: menuItem.meta?.roles,
    requiredPermissions: menuItem.meta?.permissions
  })
  
  // 检查角色权限
  if (menuItem.meta?.roles && menuItem.meta.roles.length > 0) {
    const hasRole = menuItem.meta.roles.some(role => roles.includes(role))
    if (!hasRole) {
      console.log(`❌ 角色权限不足: ${menuItem.meta.title}`)
      return false
    }
  }
  
  // 检查具体权限
  if (menuItem.meta?.permissions && menuItem.meta.permissions.length > 0) {
    const hasPermission = menuItem.meta.permissions.some(permission => permissions.includes(permission))
    if (!hasPermission) {
      console.log(`❌ 具体权限不足: ${menuItem.meta.title}`)
      return false
    }
  }
  
  console.log(`✅ 菜单权限验证通过: ${menuItem.meta?.title}`)
  return true
}

function hasOneShowingChild(children = [], parent) {
  if (!Array.isArray(children)) {
    return false
  }
  
  const showingChildren = children.filter(item => {
    if (!item) return false
    if (item.hidden) return false
    onlyOneChild.value = item
    return true
  })

  if (showingChildren.length === 1) {
    return true
  }

  if (showingChildren.length === 0) {
    // 🔥 关键修复: 为父菜单生成默认路径
    let defaultPath = ''
    if (parent.meta?.title) {
      const nameToPath = {
        '系统管理': '/admin/system',
        '系统监控': '/admin/monitor', 
        '系统工具': '/admin/tool',
        '博客管理': '/admin/blog',
        '数据统计': '/admin/statistics'
      }
      defaultPath = nameToPath[parent.meta.title] || ''
    }
    
    onlyOneChild.value = { 
      ...parent, 
      path: defaultPath, 
      noShowingChildren: true 
    }
    return true
  }

  return false
}

// 🔥 关键改进2: 优化路径解析
function resolvePath(routePath, routeQuery) {
  // 🔥 关键修复: 处理空路径和undefined
  if (!routePath || routePath === 'undefined' || routePath === 'null') {
    console.warn('路径为空，尝试使用basePath:', props.basePath)
    if (props.basePath) {
      routePath = props.basePath
    } else {
      return ''
    }
  }
  
  if (isExternal(routePath)) {
    return routePath
  }
  if (isExternal(props.basePath)) {
    return props.basePath
  }
  
  let fullPath = ''
  if (props.basePath && routePath) {
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
  
  if (fullPath.startsWith('//')) {
    fullPath = fullPath.substring(1)
  }
  
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

function hasTitle(title) {
  if (!title) {
    return ""
  }
  if (title.length > 5) {
    return title
  } else {
    return ""
  }
}

// 🔥 关键改进2: 重构菜单点击事件处理逻辑
function handleMenuClick(menuItem, event) {
  console.log('菜单点击事件:', menuItem.meta?.title)
  console.log('菜单项完整数据:', menuItem)
  
  // 检查权限
  if (!hasMenuPermission(menuItem)) {
    event?.preventDefault()
    event?.stopPropagation()
    
    const reason = menuItem.meta?.permissionReason || '权限不足'
    
    // 友好的权限提示
    ElNotification({
      title: '访问受限',
      message: `您没有权限访问"${menuItem.meta?.title}"功能。\n原因：${reason}`,
      type: 'warning',
      duration: 4000,
      showClose: true
    })
    
    return false
  }
  
  // 🔥 关键修复: 智能路径生成
  let targetPath = ''
  
  // 1. 优先使用直接路径
  if (menuItem.path) {
    targetPath = resolvePath(menuItem.path, menuItem.query)
  }
  // 2. 如果没有path但有children，使用第一个子路由的路径
  else if (menuItem.children && menuItem.children.length > 0) {
    const firstChild = menuItem.children.find(child => child.path && !child.hidden)
    if (firstChild) {
      targetPath = resolvePath(firstChild.path, firstChild.query)
      console.log('使用第一个子路由路径:', firstChild.path)
    }
  }
  // 3. 如果有redirect，使用redirect
  else if (menuItem.redirect && menuItem.redirect !== 'noRedirect') {
    targetPath = resolvePath(menuItem.redirect)
    console.log('使用redirect路径:', menuItem.redirect)
  }
  // 4. 根据菜单名称生成默认路径
  else {
    const nameToPath = {
      '系统管理': '/admin/system',
      '系统监控': '/admin/monitor', 
      '系统工具': '/admin/tool',
      '博客管理': '/admin/blog',
      '数据统计': '/admin/statistics'
    }
    
    const defaultPath = nameToPath[menuItem.meta?.title]
    if (defaultPath) {
      targetPath = defaultPath
      console.log('使用默认路径:', defaultPath)
    }
  }
  
  if (!targetPath) {
    console.warn('无法确定目标路径，菜单数据:', menuItem)
    ElMessage.warning(`"${menuItem.meta?.title}"菜单路径配置错误，请联系管理员`)
    return false
  }
  
  console.log('尝试导航到:', targetPath)
  
  // 添加视觉反馈
  if (event && event.currentTarget) {
    const targetElement = event.currentTarget
    targetElement.style.backgroundColor = 'rgba(64, 158, 255, 0.1)'
    setTimeout(() => {
      targetElement.style.backgroundColor = ''
    }, 200)
  }
  
  // 🔥 关键改进2: 简化导航逻辑，减少嵌套层级
  try {
    // 优先使用Vue Router
    if (router && typeof router.push === 'function') {
      console.log('使用Vue Router导航')
      
      // 使用Promise处理导航结果
      router.push(targetPath).then(() => {
        console.log('导航成功:', targetPath)
        ElMessage.success(`已跳转到 ${menuItem.meta?.title}`)
      }).catch(error => {
        console.error('Vue Router导航失败:', error)
        
        // 根据错误类型提供不同处理
        if (error.name === 'NavigationDuplicated') {
          // 重复导航，不是错误
          return
        } else if (error.message?.includes('permission')) {
          ElMessage.error('权限验证失败，无法访问该页面')
        } else {
          // 备选方案：直接修改location
          console.log('使用window.location跳转')
          if (typeof targetPath === 'string') {
            window.location.href = targetPath
          } else if (targetPath.path) {
            window.location.href = targetPath.path
          }
        }
      })
    } else {
      console.log('Vue Router不可用，使用window.location跳转')
      const path = typeof targetPath === 'string' ? targetPath : targetPath.path
      window.location.href = path
    }
  } catch (error) {
    console.error('导航执行异常:', error)
    ElMessage.error('页面跳转失败，请重试')
  }
  
  return true
}
</script>

<style scoped>
.permission-denied {
  opacity: 0.6;
  color: #e6a23c;
}

.permission-tag {
  margin-left: 8px;
  font-size: 10px;
}

.no-permission {
  opacity: 0.7;
}

.menu-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

/* 增加点击反馈效果 */
.el-menu-item:hover {
  transition: all 0.3s ease;
}

.el-sub-menu__title:hover {
  transition: all 0.3s ease;
}
</style>