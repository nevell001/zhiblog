import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import SvgIcon from './index.vue'

describe('SvgIcon 组件测试', () => {
  it('应该导出 SvgIcon 组件', () => {
    expect(SvgIcon).toBeDefined()
    expect(typeof SvgIcon).toBe('object')
  })
})
