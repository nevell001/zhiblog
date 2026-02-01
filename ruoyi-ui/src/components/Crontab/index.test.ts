import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import Crontab from './index.vue'

describe('Crontab 组件测试', () => {
  it('应该导出 Crontab 组件', () => {
    expect(Crontab).toBeDefined()
    expect(typeof Crontab).toBe('object')
  })
})