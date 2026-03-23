import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import ResetPwd from './resetPwd.vue'

describe('ResetPwd 视图组件测试', () => {
  it('应该导出 ResetPwd 组件', () => {
    expect(ResetPwd).toBeDefined()
    expect(typeof ResetPwd).toBe('object')
  })
})
