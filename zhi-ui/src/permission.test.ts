import { describe, it, expect } from 'vitest'
import { readFileSync } from 'node:fs'
import { resolve } from 'node:path'

const permissionSource = readFileSync(resolve(process.cwd(), 'src/permission.ts'), 'utf-8')

const getBlogGuardBranch = () => {
  const start = permissionSource.indexOf("if (to.path.startsWith('/blog'))")
  const end = permissionSource.indexOf("} else if (to.path === '/login')")

  expect(start).toBeGreaterThan(-1)
  expect(end).toBeGreaterThan(start)

  return permissionSource.slice(start, end)
}

describe('Permission 模块测试', () => {
  it('应该导出 permission 模块', async () => {
    const module = await import('./permission')
    expect(module).toBeDefined()
  })

  it('博客公开路由不应等待用户信息接口后再放行', () => {
    const blogGuardBranch = getBlogGuardBranch()

    expect(blogGuardBranch).not.toContain('await userStore.getInfo()')
    expect(blogGuardBranch).toContain('void userStore.getInfo()')
    expect(blogGuardBranch.indexOf('next()')).toBeLessThan(
      blogGuardBranch.indexOf('void userStore.getInfo()')
    )
  })

  it('进入后台时即使用户信息已存在也应确保动态菜单已生成', () => {
    expect(permissionSource).toContain('hasGeneratedRoutes')
    expect(permissionSource).toContain('permissionStore.sidebarRouters.length > 0')
    expect(permissionSource).toContain('permissionStore.addRoutes.length > 0')
    expect(permissionSource).toContain('!hasUserInfo || !hasGeneratedRoutes')
  })
})
