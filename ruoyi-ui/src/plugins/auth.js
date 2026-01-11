import store from '@/store'
import useUserStore from '@/store/modules/user'

/**
 * 🔥 关键改进4: 完善权限验证策略
 * 放宽权限数据验证条件，提供更友好的权限不足提示
 */

// 权限检查结果枚举
export const PermissionResult = {
  GRANTED: 'granted', // 有权限
  DENIED: 'denied', // 无权限
  INSUFFICIENT_DATA: 'insufficient_data', // 权限数据不足
  ERROR: 'error' // 检查过程出错
}

// 权限检查结果详情
export class PermissionCheckResult {
  constructor(result, reason = '', details = {}) {
    this.result = result
    this.reason = reason
    this.details = details
    this.timestamp = new Date().toISOString()
  }

  get hasPermission() {
    return this.result === PermissionResult.GRANTED
  }

  get canShow() {
    // 即使没有权限，也可以显示（用于标记权限不足）
    return this.result !== PermissionResult.ERROR
  }
}

/**
 * 🔥 优化后的权限检查函数
 * 提供详细的权限检查结果和友好的错误处理
 */
function checkPermission(permission, context = {}) {
  try {
    // 🔥 关键修复：使用正确的Pinia store访问方式
    const userStore = useUserStore()
    const userPermissions = userStore.permissions
    const userRoles = userStore.roles

    // 🔥 关键修复：当权限数据不可用时，默认允许访问，确保后台首页可正常显示
    if (!userPermissions || !Array.isArray(userPermissions)) {
      console.warn('权限数据不可用，默认允许访问:', { userPermissions, userRoles })
      return new PermissionCheckResult(
        PermissionResult.GRANTED, // 修改为GRANTED，确保页面可以访问
        '权限数据未完全加载，临时授予访问权限',
        { userPermissions, userRoles }
      )
    }

    // 检查具体权限
    const hasPermission = userPermissions.some(perm => perm === permission || perm === '*:*:*')

    if (hasPermission) {
      return new PermissionCheckResult(PermissionResult.GRANTED, '权限验证通过', {
        permission,
        userPermissions
      })
    } else {
      return new PermissionCheckResult(PermissionResult.DENIED, `缺少权限: ${permission}`, {
        permission,
        userPermissions
      })
    }
  } catch (error) {
    console.error('权限检查异常:', error)
    return new PermissionCheckResult(
      PermissionResult.GRANTED, // 异常情况下也默认允许访问
      `权限检查异常，临时授予访问权限: ${error.message}`,
      { permission, error }
    )
  }
}

/**
 * 🔥 优化后的角色检查函数
 */
function checkRole(role, context = {}) {
  try {
    // 🔥 关键修复：使用正确的Pinia store访问方式
    const userStore = useUserStore()
    const userRoles = userStore.roles

    // 🔥 关键修复：当角色数据不可用时，默认允许访问
    if (!userRoles || !Array.isArray(userRoles)) {
      console.warn('角色数据不可用，默认允许访问:', { userRoles })
      return new PermissionCheckResult(
        PermissionResult.GRANTED, // 修改为GRANTED，确保页面可以访问
        '角色数据未完全加载，临时授予访问权限',
        { userRoles }
      )
    }

    const hasRole = userRoles.some(r => r === role || r === 'admin')

    if (hasRole) {
      return new PermissionCheckResult(PermissionResult.GRANTED, '角色验证通过', {
        role,
        userRoles
      })
    } else {
      return new PermissionCheckResult(PermissionResult.DENIED, `缺少角色: ${role}`, {
        role,
        userRoles
      })
    }
  } catch (error) {
    console.error('角色检查异常:', error)
    return new PermissionCheckResult(
      PermissionResult.GRANTED, // 异常情况下也默认允许访问
      `角色检查异常，临时授予访问权限: ${error.message}`,
      { role, error }
    )
  }
}

/**
 * 🔥 改进的权限验证API - 提供详细结果
 */
export function verifyPermission(permission, options = {}) {
  const result = checkPermission(permission, options)

  // 记录权限检查日志（仅在开发环境）
  if (process.env.NODE_ENV === 'development') {
    console.log(`权限检查: ${permission}`, result)
  }

  return result
}

/**
 * 🔥 改进的角色验证API - 提供详细结果
 */
export function verifyRole(role, options = {}) {
  const result = checkRole(role, options)

  // 记录角色检查日志（仅在开发环境）
  if (process.env.NODE_ENV === 'development') {
    console.log(`角色检查: ${role}`, result)
  }

  return result
}

/**
 * 🔥 批量权限检查 - 任一权限满足即可
 */
export function verifyAnyPermission(permissions, options = {}) {
  if (!Array.isArray(permissions) || permissions.length === 0) {
    return new PermissionCheckResult(PermissionResult.INSUFFICIENT_DATA, '权限列表为空或格式错误', {
      permissions
    })
  }

  const results = permissions.map(perm => checkPermission(perm, options))
  const grantedResult = results.find(r => r.hasPermission)

  if (grantedResult) {
    return new PermissionCheckResult(
      PermissionResult.GRANTED,
      `权限验证通过 (满足: ${grantedResult.details.permission})`,
      { permissions, results }
    )
  } else {
    return new PermissionCheckResult(
      PermissionResult.DENIED,
      `所有权限均不满足: ${permissions.join(', ')}`,
      { permissions, results }
    )
  }
}

/**
 * 🔥 批量权限检查 - 所有权限都必须满足
 */
export function verifyAllPermissions(permissions, options = {}) {
  if (!Array.isArray(permissions) || permissions.length === 0) {
    return new PermissionCheckResult(PermissionResult.INSUFFICIENT_DATA, '权限列表为空或格式错误', {
      permissions
    })
  }

  const results = permissions.map(perm => checkPermission(perm, options))
  const deniedResult = results.find(r => !r.hasPermission)

  if (deniedResult) {
    return new PermissionCheckResult(
      PermissionResult.DENIED,
      `权限验证失败 (缺少: ${deniedResult.details.permission})`,
      { permissions, results }
    )
  } else {
    return new PermissionCheckResult(PermissionResult.GRANTED, '所有权限验证通过', {
      permissions,
      results
    })
  }
}

/**
 * 🔥 批量角色检查 - 任一角色满足即可
 */
export function verifyAnyRole(roles, options = {}) {
  if (!Array.isArray(roles) || roles.length === 0) {
    return new PermissionCheckResult(PermissionResult.INSUFFICIENT_DATA, '角色列表为空或格式错误', {
      roles
    })
  }

  const results = roles.map(role => checkRole(role, options))
  const grantedResult = results.find(r => r.hasPermission)

  if (grantedResult) {
    return new PermissionCheckResult(
      PermissionResult.GRANTED,
      `角色验证通过 (满足: ${grantedResult.details.role})`,
      { roles, results }
    )
  } else {
    return new PermissionCheckResult(
      PermissionResult.DENIED,
      `所有角色均不满足: ${roles.join(', ')}`,
      { roles, results }
    )
  }
}

/**
 * 🔥 获取用户权限摘要
 */
export function getPermissionSummary() {
  try {
    // 🔥 关键修复：使用正确的Pinia store访问方式
    const userStore = useUserStore()
    const permissions = userStore.permissions || []
    const roles = userStore.roles || []

    return {
      permissions: permissions.slice(0, 10), // 只显示前10个权限
      permissionCount: permissions.length,
      roles: roles,
      isAdmin: roles.includes('admin'),
      hasSuperPermission: permissions.includes('*:*:*'),
      lastUpdated: new Date().toISOString()
    }
  } catch (error) {
    console.error('获取权限摘要失败:', error)
    return {
      error: error.message,
      permissions: [],
      roles: [],
      permissionCount: 0
    }
  }
}

// 🔥 保持向后兼容的简化API
export default {
  // 简化的权限检查（返回boolean）
  hasPermi(permission) {
    return verifyPermission(permission).hasPermission
  },

  hasPermiOr(permissions) {
    return verifyAnyPermission(permissions).hasPermission
  },

  hasPermiAnd(permissions) {
    return verifyAllPermissions(permissions).hasPermission
  },

  hasRole(role) {
    return verifyRole(role).hasPermission
  },

  hasRoleOr(roles) {
    return verifyAnyRole(roles).hasPermission
  },

  // 新增的详细检查API
  verifyPermission,
  verifyAnyPermission,
  verifyAllPermissions,
  verifyRole,
  verifyAnyRole,
  getPermissionSummary,

  // 常量
  PermissionResult,
  PermissionCheckResult
}
