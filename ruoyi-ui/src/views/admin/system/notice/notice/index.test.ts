import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import NoticeManage from './index.vue'

describe('NoticeManage 视图组件测试', () => {
  it('应该导出 NoticeManage 组件', () => {
    expect(NoticeManage).toBeDefined()
    expect(typeof NoticeManage).toBe('object')
  })
})