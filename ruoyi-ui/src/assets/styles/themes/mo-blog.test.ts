import { describe, expect, it } from 'vitest'
import { readFileSync } from 'node:fs'
import { resolve } from 'node:path'

const stylesheet = readFileSync(resolve(__dirname, 'mo-blog.scss'), 'utf8')

describe('mo-blog theme stylesheet', () => {
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
})
