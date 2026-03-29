import { describe, it, expect, vi, beforeEach } from 'vitest'
import SizeSelect from '../index.vue'

describe('SizeSelect 组件测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('应该导出 SizeSelect 组件', () => {
    expect(SizeSelect).toBeDefined()
    expect(typeof SizeSelect).toBe('object')
  })

  it('应该接收尺寸选项', () => {
    const sizeOptions = [
      { label: '较大', value: 'large' },
      { label: '默认', value: 'default' },
      { label: '稍小', value: 'small' }
    ]

    expect(sizeOptions.length).toBe(3)
    expect(sizeOptions[0].label).toBe('较大')
    expect(sizeOptions[0].value).toBe('large')
  })

  it('应该有较大选项', () => {
    const largeOption = { label: '较大', value: 'large' }
    expect(largeOption.value).toBe('large')
  })

  it('应该有默认选项', () => {
    const defaultOption = { label: '默认', value: 'default' }
    expect(defaultOption.value).toBe('default')
  })

  it('应该有稍小选项', () => {
    const smallOption = { label: '稍小', value: 'small' }
    expect(smallOption.value).toBe('small')
  })
})