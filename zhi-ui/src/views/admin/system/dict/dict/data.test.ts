import { describe, it, expect, vi } from 'vitest'
import { readFileSync } from 'node:fs'
import { resolve } from 'node:path'
import { mount } from '@vue/test-utils'
import DictData from './data.vue'

const sourcePath = resolve(process.cwd(), 'src/views/admin/system/dict/dict/data.vue')

describe('DictData 视图组件测试', () => {
  it('应该导出 DictData 组件', () => {
    expect(DictData).toBeDefined()
    expect(typeof DictData).toBe('object')
  })

  it('返回按钮应该使用带 admin 前缀的真实菜单路径', () => {
    const source = readFileSync(sourcePath, 'utf-8')

    expect(source).toContain("{ path: '/admin/system/dict' }")
  })
})
