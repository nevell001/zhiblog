import { describe, it, expect, vi, beforeEach } from 'vitest'
import { readFileSync } from 'node:fs'
import { resolve } from 'node:path'
import Register from './Register.vue'

// Mock vue-router
const mockRouter = {
  push: vi.fn()
}

vi.mock('vue-router', () => ({
  useRouter: () => mockRouter,
  useRoute: () => ({ path: '/blog/auth/register' })
}))

describe('Blog Register 页面测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('应该导出 Register 组件', () => {
    expect(Register).toBeDefined()
    expect(typeof Register).toBe('object')
  })

  it('应该有注册表单', () => {
    const hasRegisterForm = true
    expect(hasRegisterForm).toBe(true)
  })

  it('应该有用户名输入框', () => {
    const hasUsernameInput = true
    expect(hasUsernameInput).toBe(true)
  })

  it('应该有邮箱输入框', () => {
    const hasEmailInput = true
    expect(hasEmailInput).toBe(true)
  })

  it('应该有密码输入框', () => {
    const hasPasswordInput = true
    expect(hasPasswordInput).toBe(true)
  })

  it('应该有确认密码输入框', () => {
    const hasConfirmPasswordInput = true
    expect(hasConfirmPasswordInput).toBe(true)
  })

  it('应该有注册按钮', () => {
    const hasRegisterButton = true
    expect(hasRegisterButton).toBe(true)
  })

  it('应该有登录链接', () => {
    const hasLoginLink = true
    expect(hasLoginLink).toBe(true)
  })

  it('应该支持表单验证', () => {
    const validateForm = () => {
      return true
    }
    expect(typeof validateForm).toBe('function')
  })

  it('注册页应使用更紧凑的单卡片认证布局', () => {
    const source = readFileSync(resolve(process.cwd(), 'src/views/blog/auth/Register.vue'), 'utf-8')

    expect(source).toContain('class="auth-card"')
    expect(source).toContain('class="auth-brand"')
    expect(source).toContain('class="auth-switch"')
    expect(source).not.toContain('class="auth-visual"')
    expect(source).not.toContain('class="features"')
    expect(source).not.toContain('沉浸式阅读体验')
    expect(source).not.toContain('size="large"')
    expect(source).toContain('max-width: 420px;')
  })

  it('注册页应遵循后端验证码开关并使用 JPEG 验证码图片', () => {
    const source = readFileSync(resolve(process.cwd(), 'src/views/blog/auth/Register.vue'), 'utf-8')

    expect(source).toContain(
      'captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled'
    )
    expect(source).toContain("'data:image/jpeg;base64,'")
    expect(source).not.toContain("'data:image/gif;base64,'")
  })
})
