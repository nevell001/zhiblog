import { describe, it, expect, vi } from 'vitest'
import { readFileSync } from 'node:fs'
import { resolve } from 'node:path'
import { mount } from '@vue/test-utils'
import AuthRole from './authRole.vue'

const sourcePath = resolve(process.cwd(), 'src/views/admin/system/user/user/authRole.vue')

describe('AuthRole 视图组件测试', () => {
  it('应该导出 AuthRole 组件', () => {
    expect(AuthRole).toBeDefined()
    expect(typeof AuthRole).toBe('object')
  })

  it('返回按钮应该使用带 admin 前缀的真实菜单路径', () => {
    const source = readFileSync(sourcePath, 'utf-8')

    expect(source).toContain("{ path: '/admin/system/user' }")
  })
})
