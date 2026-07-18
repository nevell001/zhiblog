import { describe, expect, it } from 'vitest'
import { readFileSync } from 'node:fs'
import { resolve } from 'node:path'

const stylesheet = readFileSync(resolve(__dirname, 'mo-blog.scss'), 'utf8')

describe('mo-blog theme stylesheet', () => {
  it('应该在 root 上提供博客设计 token，避免默认主题下页面变量失效', () => {
    expect(stylesheet).toContain(':root {')
    expect(stylesheet).toContain('--mo-p50: #eef2ff')
    expect(stylesheet).toContain('--mo-n900: #1c1917')
  })

  it('应该为博客首页和布局提供高优先级覆盖以压过 scoped 样式', () => {
    expect(stylesheet).toContain('.theme-mo-blog .blog-layout .blog-top-nav')
    expect(stylesheet).toContain('.theme-mo-blog .blog-home-v2 .hero-section')
    expect(stylesheet).toContain('.theme-mo-blog .blog-home-v2 .article-row')
  })

  it('应该覆盖后台新增按钮常用的 plain primary 状态', () => {
    expect(stylesheet).toContain('.theme-mo-blog .el-button--primary.is-plain')
    expect(stylesheet).toContain('--el-button-bg-color: var(--mo-p50)')
    expect(stylesheet).toContain('--el-button-text-color: var(--mo-p700)')
    expect(stylesheet).toContain('--el-button-hover-bg-color: var(--mo-p600)')
  })

  it('应该把管理后台框架和常用控件纳入当前主题', () => {
    expect(stylesheet).toContain('.theme-mo-blog .app-wrapper')
    expect(stylesheet).toContain('.theme-mo-blog .navbar')
    expect(stylesheet).toContain('.theme-mo-blog .tags-view-container')
    expect(stylesheet).toContain('.theme-mo-blog .sidebar-logo-container')
    expect(stylesheet).toContain('.theme-mo-blog .sidebar-container .el-menu-item.is-active')
    expect(stylesheet).toContain('.theme-mo-blog .app-container')
    expect(stylesheet).toContain('.theme-mo-blog .el-table')
    expect(stylesheet).toContain('.theme-mo-blog .el-form-item__label')
    expect(stylesheet).toContain('.theme-mo-blog .el-tag')
    expect(stylesheet).toContain('background: var(--mo-n50)')
  })
})
