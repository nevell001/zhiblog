 /**
 * v-hasPermi 操作权限处理
 * Copyright (c) 2019 ruoyi
 */
import { watch } from 'vue'
import useUserStore from '@/store/modules/user'

export default {
  mounted(el, binding) {
    const { value } = binding
    const all_permission = "*:*:*"
    const userStore = useUserStore()

    const checkPermission = () => {
      const permissions = userStore.permissions

      if (value && value instanceof Array && value.length > 0) {
        const permissionFlag = value

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
        throw new Error(`请设置操作权限标签值`)
      }
    }

    // 初始检查
    checkPermission()

    // 监听权限变化
    el._unwatch = watch(
      () => userStore.permissions,
      () => {
        checkPermission()
      },
      { deep: true }
    )
  },
  updated(el, binding) {
    const { value } = binding
    const all_permission = "*:*:*"
    const userStore = useUserStore()

    const checkPermission = () => {
      const permissions = userStore.permissions

      if (value && value instanceof Array && value.length > 0) {
        const permissionFlag = value

        const hasPermissions = permissions.some(permission => {
          return all_permission === permission || permissionFlag.includes(permission)
        })

        if (!hasPermissions) {
          el.style.display = 'none'
        } else {
          el.style.display = ''
        }
      }
    }

    checkPermission()
  },
  unmounted(el) {
    // 清理监听器
    if (el._unwatch) {
      el._unwatch()
    }
  }
}
