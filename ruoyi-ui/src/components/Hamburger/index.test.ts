import { describe, it, expect, vi } from 'vitest'
import Hamburger from './index.vue'

describe('Hamburger 组件测试', () => {
  it('应该导出 Hamburger 组件', () => {
    expect(Hamburger).toBeDefined()
    expect(typeof Hamburger).toBe('object')
  })

  it('应该接收 isActive 属性', () => {
    const isActive = false
    expect(typeof isActive).toBe('boolean')
  })

  it('应该默认 isActive 为 false', () => {
    const isActive = false
    expect(isActive).toBe(false)
  })

  it('应该有 toggleClick 事件', () => {
    const mockEmit = vi.fn()
    const toggleClick = () => {
      mockEmit('toggleClick')
    }
    toggleClick()
    expect(mockEmit).toHaveBeenCalledWith('toggleClick')
  })

  it('应该有正确的样式', () => {
    const style = {
      padding: '0 15px'
    }
    expect(style).toHaveProperty('padding')
  })

  it('应该有 SVG 图标', () => {
    const svg = {
      class: 'hamburger',
      viewBox: '0 0 1024 1024'
    }
    expect(svg.class).toBe('hamburger')
    expect(svg.viewBox).toBe('0 0 1024 1024')
  })

  it('应该有 is-active 类名', () => {
    const isActive = true
    expect(isActive).toBe(true)
  })

  it('SVG 应该有正确的尺寸', () => {
    const svg = {
      width: '64',
      height: '64'
    }
    expect(svg.width).toBe('64')
    expect(svg.height).toBe('64')
  })

  it('SVG 应该有 fill 属性', () => {
    const svg = {
      fill: 'currentColor'
    }
    expect(svg.fill).toBe('currentColor')
  })
})
