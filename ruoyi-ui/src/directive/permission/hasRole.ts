import type { Directive } from 'vue'
import { useUserStore } from '@/stores/user'

const hasRole: Directive = {
  mounted(el, binding) {
    const { value } = binding
    const super_admin = 'admin'
    const roles = useUserStore().roles

    if (value && value instanceof Array && value.length > 0) {
      const roleFlag = value as string[]

      const hasRole = roles.some(role => {
        return super_admin === role || roleFlag.includes(role)
      })

      if (!hasRole) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    } else {
      throw new Error('请设置角色权限标签值')
    }
  }
}

export default hasRole
