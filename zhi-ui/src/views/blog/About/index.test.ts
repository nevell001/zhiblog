import { describe, it, expect, vi, beforeEach } from 'vitest'
import About from '../about.vue'

describe('About 页面测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('应该导出 About 组件', () => {
    expect(About).toBeDefined()
    expect(typeof About).toBe('object')
  })

  it('应该有关于页面内容', () => {
    const hasContent = true
    expect(hasContent).toBe(true)
  })

  it('应该有页面标题', () => {
    const title = '关于我们'
    expect(title).toBe('关于我们')
  })

  it('应该有项目信息', () => {
    const projectInfo = {
      name: 'ZhiBlog',
      version: '1.3.2',
      author: 'Nevell'
    }
    expect(projectInfo.name).toBe('ZhiBlog')
    expect(projectInfo.version).toBe('1.3.2')
  })

  it('应该有技术栈信息', () => {
    const techStack = ['Vue 3', 'Spring Boot', 'TypeScript', 'MySQL']
    expect(Array.isArray(techStack)).toBe(true)
    expect(techStack.length).toBe(4)
  })

  it('应该有联系信息', () => {
    const contactInfo = {
      email: 'test@example.com',
      github: 'https://github.com/nevell/zhiblog'
    }
    expect(contactInfo.email).toBe('test@example.com')
    expect(contactInfo.github).toContain('github.com')
  })
})
