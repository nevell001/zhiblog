import useUserStore from '@/store/modules/user'

function authPermission(permission) {
  const all_permission = "*:*:*"
  const userStore = useUserStore()
  const permissions = userStore.permissions || []
  
  // 检查用户权限数组是否存在且有效
  if (!Array.isArray(permissions)) {
    console.warn('用户权限数据格式错误，期望数组但得到:', typeof permissions)
    return false
  }
  
  if (permission && permission.length > 0) {
    return permissions.some(v => {
      return all_permission === v || v === permission
    })
  } else {
    return false
  }
}

function authRole(role) {
  const super_admin = "admin"
  const userStore = useUserStore()
  const roles = userStore.roles || []
  
  // 检查用户角色数组是否存在且有效
  if (!Array.isArray(roles)) {
    console.warn('用户角色数据格式错误，期望数组但得到:', typeof roles)
    return false
  }
  
  if (role && role.length > 0) {
    return roles.some(v => {
      return super_admin === v || v === role
    })
  } else {
    return false
  }
}

export default {
  // 验证用户是否具备某权限
  hasPermi(permission) {
    return authPermission(permission)
  },
  // 验证用户是否含有指定权限，只需包含其中一个
  hasPermiOr(permissions) {
    return permissions.some(item => {
      return authPermission(item)
    })
  },
  // 验证用户是否含有指定权限，必须全部拥有
  hasPermiAnd(permissions) {
    return permissions.every(item => {
      return authPermission(item)
    })
  },
  // 验证用户是否具备某角色
  hasRole(role) {
    return authRole(role)
  },
  // 验证用户是否含有指定角色，只需包含其中一个
  hasRoleOr(roles) {
    return roles.some(item => {
      return authRole(item)
    })
  },
  // 验证用户是否含有指定角色，必须全部拥有
  hasRoleAnd(roles) {
    return roles.every(item => {
      return authRole(item)
    })
  }
}
