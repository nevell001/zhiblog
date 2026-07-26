import { describe, it, expect } from 'vitest'
import { readFileSync } from 'node:fs'
import { resolve } from 'node:path'
import TagsView from './index.vue'

const tagsViewSource = readFileSync(
  resolve(process.cwd(), 'src/layout/components/TagsView/index.vue'),
  'utf-8'
)

describe('TagsView 组件测试', () => {
  it('应该导出 TagsView 组件', () => {
    expect(TagsView).toBeDefined()
    expect(typeof TagsView).toBe('object')
  })

  it('上下文菜单方向图标不应使用未注册的小写组件名', () => {
    expect(tagsViewSource).not.toContain('<back ')
    expect(tagsViewSource).not.toContain('<right ')
    expect(tagsViewSource).toContain('<Back ')
    expect(tagsViewSource).toContain('<Right ')
    expect(tagsViewSource).toContain("import { Back, Right } from '@element-plus/icons-vue'")
  })
})
