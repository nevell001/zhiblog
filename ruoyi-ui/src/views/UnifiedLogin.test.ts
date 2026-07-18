import { readFileSync } from 'node:fs'
import { resolve } from 'node:path'
import { describe, it, expect } from 'vitest'

const sourcePath = resolve(process.cwd(), 'src/views/UnifiedLogin.vue')

describe('UnifiedLogin 组件测试', () => {
  it('应该导出 UnifiedLogin 组件', async () => {
    const module = await import('./UnifiedLogin.vue')
    expect(module.default).toBeDefined()
  })

  it('应该遵循后端验证码开关并使用 JPEG 验证码图片', () => {
    const source = readFileSync(sourcePath, 'utf-8')

    expect(source).toContain('captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled')
    expect(source).toContain("'data:image/jpeg;base64,'")
    expect(source).not.toContain("'data:image/gif;base64,'")
  })

  it('登录页应使用更紧凑的单卡片认证布局', () => {
    const source = readFileSync(sourcePath, 'utf-8')

    expect(source).toContain('class="auth-card"')
    expect(source).toContain('class="auth-brand"')
    expect(source).toContain('class="auth-switch"')
    expect(source).not.toContain('class="auth-visual"')
    expect(source).not.toContain('class="features"')
    expect(source).not.toContain('沉浸式阅读体验')
    expect(source).not.toContain('size="large"')
    expect(source).toContain('max-width: 380px;')
  })
})
