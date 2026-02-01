import { describe, it, expect, vi } from 'vitest'
import { mount } from '@vue/test-utils'
import AvatarUpload from './AvatarUpload.vue'

describe('AvatarUpload 组件测试', () => {
  it('应该导出 AvatarUpload 组件', () => {
    expect(AvatarUpload).toBeDefined()
    expect(typeof AvatarUpload).toBe('object')
  })
})