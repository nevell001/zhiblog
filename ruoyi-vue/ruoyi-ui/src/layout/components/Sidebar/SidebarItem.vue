<template>
  <div v-if="!item.hidden">
    <!-- 调试信息 -->
    <div v-if="false" style="color: red; font-size: 12px; margin-bottom: 10px; padding: 5px; border: 1px solid red;">
      <div>菜单标题: {{ (item.meta && item.meta.title) || '无标题' }}</div>
      <div>菜单路径: {{ item.path || '无路径' }}</div>
      <div>是否有子菜单: {{ item.children ? '有' : '无' }}</div>
      <div>子菜单数量: {{ (item.children && item.children.length) || 0 }}</div>
      <div>是否隐藏: {{ item.hidden ? '是' : '否' }}</div>
      <div>hasOneShowingChild结果: {{ hasOneShowingChild(item.children, item) }}</div>
      <div>onlyOneChild: {{ onlyOneChild }}</div>
    </div>
    
    <template v-if="false && hasOneShowingChild(item.children, item)">
      <div
        class="menu-item"
        :class="{ 'submenu-title-noDropdown': !isNest, 'no-permission': !hasMenuPermission(onlyOneChild) }"
        @click="handleMenuClick(onlyOneChild, $event)"
      >
        <svg-icon :icon-class="(onlyOneChild.meta && onlyOneChild.meta.icon) || (item.meta && item.meta.icon) || ''"/>
        <span
          class="menu-title"
          :title="hasTitle(onlyOneChild.meta && onlyOneChild.meta.title)"
          :class="{ 'permission-denied': !hasMenuPermission(onlyOneChild) }"
        >
          {{ (onlyOneChild.meta && onlyOneChild.meta.title) || '' }}
          <el-tag v-if="!hasMenuPermission(onlyOneChild)" size="small" type="warning" class="permission-tag">
            权限不足
          </el-tag>
        </span>
      </div>
    </template>

    <!-- 简单菜单项 -->
    <div 
      v-if="item.path && (item.meta && item.meta.title)" 
      class="menu-item"
      :class="{ 
        'no-permission': !hasMenuPermission(item),
        'is-active': isActive(item)
      }"
      @click="handleMenuClick(item, $event)"
    >
      <svg-icon :icon-class="(item.meta && item.meta.icon) || 'documentation'"/>
      <span class="menu-title">{{ (item.meta && item.meta.title) || '' }}</span>
    </div>

    <el-sub-menu
      v-else-if="item.children && item.children.length > 0"
      ref="subMenu"
      :index="resolvePath(item.path)"
      :popper-append-to-body="false"
      :class="{ 'no-permission': !hasMenuPermission(item) }"
    >
      <template v-if="item.meta" #title>
        <svg-icon :icon-class="(item.meta && item.meta.icon) || 'documentation'" />
        <span
          class="menu-title"
          :title="hasTitle(item.meta && item.meta.title)"
          :class="{ 'permission-denied': !hasMenuPermission(item) }"
        >
          {{ (item.meta && item.meta.title) || '' }}
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
        :base-path="resolvePath(item.path + '/' + child.path)"
        class="nest-menu"
      />
    </el-sub-menu>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElNotification } from 'element-plus'
import { isExternal } from '@/utils/validate'
import { getNormalPath } from '@/utils/ruoyi'
import useUserStore from '@/store/modules/user'
import { useRoute } from 'vue-router'

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
const route = useRoute()

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

// 判断当前菜单项是否为活动状态
function isActive(menuItem) {
  if (!menuItem || !menuItem.path) return false
  
  const currentPath = route.path
  const menuPath = menuItem.path
  
  // 如果路径完全匹配
  if (currentPath === menuPath) return true
  
  // 如果当前路径是菜单路径的子路径
  if (currentPath.startsWith(menuPath + '/')) return true
  
  // 特殊处理：如果菜单有redirect属性，检查当前路径是否匹配redirect
  if (menuItem.redirect && currentPath === menuItem.redirect) return true
  
  return false
}

function hasOneShowingChild(children = [], parent) {
  console.log('🔍 hasOneShowingChild被调用:', {
    parentTitle: (parent.meta && parent.meta.title) || '无标题',
    children: children,
    childrenCount: (children && children.length) || 0
  })
  
  // 如果没有子菜单或子菜单为空数组，则使用父菜单
  if (!children || !Array.isArray(children) || children.length === 0) {
    console.log('📌 没有子菜单或子菜单为空，使用父菜单')
    onlyOneChild.value = { ...parent }
    return true
  }

  // 查找可见的子菜单
  const showingChildren = children.filter(item => {
    if (!item || item.hidden) return false
    return true
  })
  
  console.log('📌 可见的子菜单:', showingChildren)

  // 如果只有一个可见的子菜单，使用该子菜单
  if (showingChildren.length === 1) {
    console.log('📌 只有一个可见的子菜单，使用该子菜单')
    onlyOneChild.value = showingChildren[0]
    return true
  }

  // 如果没有可见的子菜单，使用父菜单
  if (showingChildren.length === 0) {
    console.log('📌 没有可见的子菜单，使用父菜单')
    onlyOneChild.value = { ...parent }
    return true
  }

  // 如果有多个可见的子菜单，不使用hasOneShowingChild逻辑
  console.log('📌 有多个可见的子菜单，不使用hasOneShowingChild逻辑')
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
      return '/'
    }
  }

  if (isExternal(routePath)) {
    return routePath
  }
  if (isExternal(props.basePath)) {
    return props.basePath
  }

  // 如果路径已经是完整路径，直接返回
  if (routePath.startsWith('/admin/')) {
    return routePath
  }

  // 处理相对路径
  let fullPath = ''
  if (props.basePath && routePath) {
    // 确保basePath不以/结尾，routePath以/开头
    const basePath = props.basePath.replace(/\/$/, '')
    const childPath = routePath.startsWith('/') ? routePath : '/' + routePath
    fullPath = getNormalPath(basePath + childPath)
  } else if (props.basePath) {
    fullPath = getNormalPath(props.basePath)
  } else if (routePath) {
    fullPath = getNormalPath(routePath)
  }

  // 确保路径以/开头
  if (!fullPath.startsWith('/')) {
    fullPath = '/' + fullPath
  }

  // 移除末尾的/
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
  console.log('菜单点击事件:', (menuItem.meta && menuItem.meta.title) || '')
  console.log('菜单项完整数据:', menuItem)

  // 检查权限
  if (!hasMenuPermission(menuItem)) {
    event?.preventDefault()
    event?.stopPropagation()

    const reason = (menuItem.meta && menuItem.meta.permissionReason) || '权限不足'

    // 友好的权限提示
    ElNotification({
      title: '访问受限',
      message: `您没有权限访问"${(menuItem.meta && menuItem.meta.title) || ''}"功能。
原因：${reason}`,
      type: 'warning',
      duration: 4000,
      showClose: true
    })

    return false
  }

  // 🔥 关键修复: 智能路径生成
  let targetPath = ''
  
  // 获取菜单标题
  const menuTitle = (menuItem.meta && menuItem.meta.title) || ''
  
  console.log('处理菜单点击:', {
    title: menuTitle,
    path: menuItem.path,
    redirect: menuItem.redirect,
    children: menuItem.children
  })

  // 1. 优先使用redirect
  if (menuItem.redirect && menuItem.redirect !== 'noRedirect') {
    targetPath = menuItem.redirect
    console.log('使用redirect路径:', targetPath)
  }
  // 2. 如果有子菜单，使用第一个子菜单的路径
  else if (menuItem.children && menuItem.children.length > 0) {
    const firstChild = menuItem.children.find(child => child.path && !child.hidden)
    if (firstChild) {
      targetPath = firstChild.path
      console.log('使用第一个子菜单路径:', targetPath)
    }
  }
  // 3. 使用直接路径
  else if (menuItem.path) {
    targetPath = menuItem.path
    console.log('使用直接路径:', targetPath)
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

    targetPath = nameToPath[menuTitle] || ''
    if (targetPath) {
      console.log('使用默认路径:', targetPath)
    }
  }

  if (!targetPath) {
    console.warn('无法确定目标路径，菜单数据:', menuItem)
    ElMessage.warning(`"${(menuItem.meta && menuItem.meta.title) || ''}"菜单路径配置错误，请联系管理员`)
    return false
  }

  console.log('尝试导航到:', targetPath)

  // 添加视觉反馈
  if (event && event.currentTarget) {
    const targetElement = event.currentTarget
    // 添加点击动画效果
    targetElement.style.transform = 'scale(0.98)'
    targetElement.style.transition = 'transform 0.1s ease'
    
    setTimeout(() => {
      targetElement.style.transform = ''
    }, 100)
  }

  // 🔥 关键改进2: 直接使用window.location跳转，避免Vue Router问题
  try {
    console.log('使用window.location跳转到:', targetPath)
    // 确保目标路径是完整的URL
    if (targetPath.startsWith('/')) {
      window.location.href = window.location.origin + targetPath
    } else {
      window.location.href = targetPath
    }
  } catch (error) {
    console.error('导航失败:', error)
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

.menu-item {
  padding: 0 20px;
  height: 56px;
  line-height: 56px;
  font-size: 14px;
  color: var(--menu-text, #bfcbd9);
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  position: relative;
  overflow: hidden;
}

.menu-item:hover {
  background-color: var(--menu-hover, rgba(0, 0, 0, 0.06)) !important;
  color: var(--menu-active-text, #409eff) !important;
  transform: translateX(3px);
  transition: all 0.3s ease;
}

.menu-item.is-active {
  color: var(--menu-active-text, #409eff);
  background-color: var(--menu-hover, rgba(0, 0, 0, 0.06)) !important;
}

.menu-item.is-active::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 4px;
  background-color: var(--menu-active-text, #409eff);
}

.menu-title {
  margin-left: 10px;
  flex: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 图标样式 */
.svg-icon {
  font-size: 18px;
  vertical-align: middle;
  margin-right: 5px;
  width: 1em;
  height: 1em;
  display: inline-block;
}

/* 增加点击反馈效果 */
.el-sub-menu__title:hover {
  transition: all 0.3s ease;
}
</style>
