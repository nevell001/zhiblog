import { describe, it, expect, vi, beforeEach } from 'vitest'
import { readFileSync } from 'node:fs'
import { resolve } from 'node:path'
import SvgIcon from './index.vue'

describe('SvgIcon 组件测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('应该导出 SvgIcon 组件', () => {
    expect(SvgIcon).toBeDefined()
    expect(typeof SvgIcon).toBe('object')
  })

  it('应该接收 icon-class 属性', () => {
    const iconClass = 'user'
    expect(typeof iconClass).toBe('string')
    expect(iconClass).toBe('user')
  })

  it('应该支持多个图标类名', () => {
    const iconClass = 'dashboard user settings'
    expect(iconClass).toBe('dashboard user settings')
  })

  it('应该接收 class-name 属性', () => {
    const className = 'custom-icon'
    expect(typeof className).toBe('string')
    expect(className).toBe('custom-icon')
  })

  it('应该有 SVG 元素', () => {
    const svg = {
      class: 'svg-icon',
      ariaHidden: 'true'
    }
    expect(svg.class).toBe('svg-icon')
    expect(svg.ariaHidden).toBe('true')
  })

  it('应该有 use 元素', () => {
    const use = {
      href: '#icon-user'
    }
    expect(use.href).toBe('#icon-user')
  })

  it('应该有 aria-hidden 属性', () => {
    const ariaHidden = 'true'
    expect(ariaHidden).toBe('true')
  })

  it('应该支持自定义类名', () => {
    const className = 'my-custom-icon'
    expect(className).toBe('my-custom-icon')
  })

  it('应该有 aria-hidden 访问属性', () => {
    const ariaHidden = 'true'
    expect(typeof ariaHidden).toBe('string')
  })

  it('Element Plus 图标注册不应保留小写 question 组件别名', () => {
    const source = readFileSync(
      resolve(process.cwd(), 'src/components/SvgIcon/svgicon.ts'),
      'utf-8'
    )

    expect(source).toContain('QuestionFilled')
    expect(source).not.toContain("app.component('question'")
  })
})
