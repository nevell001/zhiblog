import type { Directive } from 'vue'
import { watch } from 'vue'
import { useUserStore } from '@/stores/user'

interface PermissionElement extends HTMLElement {
  _unwatch?: () => void
}

const hasPermi: Directive<PermissionElement> = {
  mounted(el, binding) {
    const { value } = binding
    const all_permission = '*:*:*'

    try {
      const userStore = useUserStore()

      const checkPermission = () => {
        try {
          const permissions = userStore.permissions || []

          if (value && value instanceof Array && value.length > 0) {
            const permissionFlag = value as string[]

            const hasPermissions = permissions.some(permission => {
              return all_permission === permission || permissionFlag.includes(permission)
            })

            if (!hasPermissions) {
              // 隐藏元素而不是移除，以便权限恢复时可以重新显示
              el.style.display = 'none'
            } else {
              el.style.display = ''
            }
          } else {
            throw new Error('请设置操作权限标签值')
          }
        } catch (error) {
          console.error('检查权限失败:', error)
          el.style.display = 'none'
        }
      }

      // 初始检查
      checkPermission()

      // 监听权限变化
      try {
        const stopWatch = watch(
          () => {
            try {
              return userStore.permissions || []
            } catch (error) {
              console.error('获取权限失败:', error)
              return []
            }
          },
          () => {
            checkPermission()
          },
          { deep: true }
        )

        // 确保 stopWatch 是一个函数
        if (typeof stopWatch === 'function') {
          el._unwatch = stopWatch
        } else {
          console.warn('watch 函数返回的不是一个函数:', stopWatch)
        }
      } catch (error) {
        console.error('创建权限监听器失败:', error)
      }
    } catch (error) {
      console.error('初始化权限指令失败:', error)
      el.style.display = 'none'
    }
  },
  updated(el, binding) {
    const { value } = binding
    const all_permission = '*:*:*'

    try {
      const userStore = useUserStore()

      const checkPermission = () => {
        try {
          const permissions = userStore.permissions || []

          if (value && value instanceof Array && value.length > 0) {
            const permissionFlag = value as string[]

            const hasPermissions = permissions.some(permission => {
              return all_permission === permission || permissionFlag.includes(permission)
            })

            if (!hasPermissions) {
              el.style.display = 'none'
            } else {
              el.style.display = ''
            }
          }
        } catch (error) {
          console.error('更新权限检查失败:', error)
          el.style.display = 'none'
        }
      }

      checkPermission()
    } catch (error) {
      console.error('更新权限指令失败:', error)
      el.style.display = 'none'
    }
  },
  unmounted(el) {
    // 清理监听器
    try {
      if (el && typeof el === 'object' && '_unwatch' in el) {
        const unwatch = el._unwatch
        if (typeof unwatch === 'function') {
          unwatch()
        }
        // 清理属性，避免内存泄漏
        delete el._unwatch
      }
    } catch (error) {
      console.warn('清理权限监听器失败:', error)
      console.warn('错误堆栈:', error.stack)
    }
  }
}

export default hasPermi
