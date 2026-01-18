import type { App, Directive } from 'vue'
import auth from '@/plugins/auth'

/**
 * 优化后的权限指令 - 支持细粒度权限控制
 */

interface ActionValue {
  resource: string
  action: string
}

interface PermissionElement extends HTMLElement {
  disabled?: boolean
  readOnly?: boolean
}

// v-hasPermi - 基础权限指令
const hasPermi: Directive = {
  mounted(el, binding) {
    const { value } = binding
    const permissions = Array.isArray(value) ? value : [value]

    if (permissions.length > 0) {
      const hasPermission = auth.hasPermiOr(permissions)
      if (!hasPermission) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    } else {
      throw new Error('权限指令需要权限值，如 v-hasPermi="[\'system:user:add\']"')
    }
  },
  updated(el, binding) {
    const { value, oldValue } = binding

    // 权限值发生变化时重新检查
    if (JSON.stringify(value) !== JSON.stringify(oldValue)) {
      const permissions = Array.isArray(value) ? value : [value]
      const hasPermission = auth.hasPermiOr(permissions)

      if (!hasPermission) {
        el.style.display = 'none'
      } else {
        el.style.display = ''
      }
    }
  }
}

// v-hasRole - 角色权限指令
const hasRole: Directive = {
  mounted(el, binding) {
    const { value } = binding
    const roles = Array.isArray(value) ? value : [value]

    if (roles.length > 0) {
      const hasRolePermission = auth.hasRoleOr(roles)
      if (!hasRolePermission) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    } else {
      throw new Error('角色指令需要角色值，如 v-has-role="[\'admin\']"')
    }
  },
  updated(el, binding) {
    const { value, oldValue } = binding

    if (JSON.stringify(value) !== JSON.stringify(oldValue)) {
      const roles = Array.isArray(value) ? value : [value]
      const hasRolePermission = auth.hasRoleOr(roles)

      if (!hasRolePermission) {
        el.style.display = 'none'
      } else {
        el.style.display = ''
      }
    }
  }
}

// v-has-action - 功能级权限指令
const hasAction: Directive = {
  mounted(el, binding) {
    const { value } = binding
    const { resource, action } = value as ActionValue

    if (!resource || !action) {
      throw new Error(
        "功能权限指令需要resource和action，如 v-has-action=\"{ resource: 'system:user', action: 'add' }\""
      )
    }

    const hasActionPermission = auth.hasAction ? auth.hasAction(resource, action) : false
    if (!hasActionPermission) {
      el.parentNode && el.parentNode.removeChild(el)
    }
  },
  updated(el, binding) {
    const { value, oldValue } = binding

    if (JSON.stringify(value) !== JSON.stringify(oldValue)) {
      const { resource, action } = value as ActionValue
      const hasActionPermission = auth.hasAction ? auth.hasAction(resource, action) : false

      if (!hasActionPermission) {
        el.style.display = 'none'
      } else {
        el.style.display = ''
      }
    }
  }
}

// v-has-all-permi - 需要所有权限的指令
const hasAllPermi: Directive = {
  mounted(el, binding) {
    const { value } = binding
    const permissions = Array.isArray(value) ? value : [value]

    if (permissions.length > 0) {
      const hasAllPermission = auth.hasPermiAnd(permissions)
      if (!hasAllPermission) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    } else {
      throw new Error(
        "权限指令需要权限值，如 v-has-all-permi=\"['system:user:add', 'system:user:edit']\""
      )
    }
  }
}

// v-disabled-permi - 禁用权限指令（不删除元素，只是禁用）
const disabledPermi: Directive<PermissionElement> = {
  mounted(el, binding) {
    const { value } = binding
    const permissions = Array.isArray(value) ? value : [value]

    if (permissions.length > 0) {
      const hasPermission = auth.hasPermiOr(permissions)
      if (!hasPermission) {
        el.disabled = true
        el.classList.add('is-disabled')

        // 添加提示信息
        el.title = '权限不足，无法执行此操作'
      }
    }
  },
  updated(el, binding) {
    const { value, oldValue } = binding

    if (JSON.stringify(value) !== JSON.stringify(oldValue)) {
      const permissions = Array.isArray(value) ? value : [value]
      const hasPermission = auth.hasPermiOr(permissions)

      if (!hasPermission) {
        el.disabled = true
        el.classList.add('is-disabled')
        el.title = '权限不足，无法执行此操作'
      } else {
        el.disabled = false
        el.classList.remove('is-disabled')
        el.title = ''
      }
    }
  }
}

// v-readonly-permi - 只读权限指令
const readonlyPermi: Directive<PermissionElement> = {
  mounted(el, binding) {
    const { value } = binding
    const permissions = Array.isArray(value) ? value : [value]

    if (permissions.length > 0) {
      const hasPermission = auth.hasPermiOr(permissions)
      if (!hasPermission) {
        if (el.tagName === 'INPUT' || el.tagName === 'TEXTAREA') {
          const input = el as HTMLInputElement | HTMLTextAreaElement
          input.readOnly = true
          el.classList.add('is-readonly')
        } else {
          el.style.pointerEvents = 'none'
          el.style.opacity = '0.6'
        }

        el.title = '权限不足，只读模式'
      }
    }
  }
}

// 权限检查工具函数
export const checkPermission = (permissions: string | string[]): boolean => {
  return auth.hasPermiOr(Array.isArray(permissions) ? permissions : [permissions])
}

export const checkRole = (roles: string | string[]): boolean => {
  return auth.hasRoleOr(Array.isArray(roles) ? roles : [roles])
}

export const checkAction = (resource: string, action: string): boolean => {
  return auth.hasAction ? auth.hasAction(resource, action) : false
}

export const checkAllPermissions = (permissions: string | string[]): boolean => {
  return auth.hasPermiAnd(Array.isArray(permissions) ? permissions : [permissions])
}

export default {
  install(app: App): void {
    // 注册权限指令
    app.directive('hasPermi', hasPermi)
    app.directive('hasRole', hasRole)
    app.directive('hasAction', hasAction)
    app.directive('hasAllPermi', hasAllPermi)
    app.directive('disabledPermi', disabledPermi)
    app.directive('readonlyPermi', readonlyPermi)

    // 全局属性
    app.config.globalProperties.$checkPermission = checkPermission
    app.config.globalProperties.$checkRole = checkRole
    app.config.globalProperties.$checkAction = checkAction
    app.config.globalProperties.$checkAllPermissions = checkAllPermissions
  }
}
