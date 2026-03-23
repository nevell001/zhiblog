import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { mount } from '@vue/test-utils'
import Login from './index.vue'

// Mock APIs and stores
vi.mock('@/api/login', () => ({
  login: vi.fn(),
  getCodeImg: vi.fn()
}))
vi.mock('@/stores/user', () => ({
  useUserStore: vi.fn(),
  setUser: vi.fn()
}))
vi.mock('@/router', () => ({
  useRouter: vi.fn()
}))

describe('Login 组件测试', () => {
  const mockLogin = vi.mocked(login)
  const mockGetCodeImg = vi.mocked(getCodeImg)
  const mockUseUserStore = vi.mocked(useUserStore)
  const mockSetUser = vi.mocked(setUser)
  const mockUseRouter = vi.mocked(useRouter)

  beforeEach(() => {
    vi.clearAllMocks()

    // Mock store
    mockUseUserStore.mockReturnValue({
      name: '',
      token: '',
      roles: [],
      permissions: []
    })

    // Mock router
    mockUseRouter.mockReturnValue({
      push: vi.fn(),
      replace: vi.fn()
    })
  })

  afterEach(() => {
    vi.restoreAllMocks()
  })

  it('应该正确渲染登录表单', () => {
    const wrapper = mount(Login, {
      global: {
        stubs: {
          'el-form': true,
          'el-form-item': true,
          'el-input': true,
          'el-button': true,
          'el-checkbox': true,
          'el-tooltip': true
        }
      }
    })

    expect(wrapper.find('.login').exists()).toBe(true)
    expect(wrapper.find('.title').exists()).toBe(true)
  })

  it('应该初始化登录表单', () => {
    const wrapper = mount(Login, {
      global: {
        stubs: {
          'el-form': true,
          'el-form-item': true,
          'el-input': true,
          'el-button': true,
          'el-checkbox': true,
          'el-tooltip': true
        }
      }
    })

    expect(wrapper.vm.loginForm).toEqual({
      username: '',
      password: '',
      code: ''
    })
  })

  it('应该处理用户名输入', () => {
    const wrapper = mount(Login, {
      global: {
        stubs: {
          'el-form': true,
          'el-form-item': true,
          'el-input': true,
          'el-button': true,
          'el-checkbox': true,
          'el-tooltip': true
        }
      }
    })

    const usernameInput = wrapper.find('input[placeholder="账号"]')
    usernameInput.setValue('testuser')

    expect(wrapper.vm.loginForm.username).toBe('testuser')
  })

  it('应该处理密码输入', () => {
    const wrapper = mount(Login, {
      global: {
        stubs: {
          'el-form': true,
          'el-form-item': true,
          'el-input': true,
          'el-button': true,
          'el-checkbox': true,
          'el-tooltip': true
        }
      }
    })

    const passwordInput = wrapper.find('input[placeholder="密码"]')
    passwordInput.setValue('testpass')

    expect(wrapper.vm.loginForm.password).toBe('testpass')
  })

  it('应该处理验证码输入', () => {
    const wrapper = mount(Login, {
      global: {
        stubs: {
          'el-form': true,
          'el-form-item': true,
          'el-input': true,
          'el-button': true,
          'el-checkbox': true,
          'el-tooltip': true
        }
      }
    })

    const codeInput = wrapper.find('input[placeholder="验证码"]')
    codeInput.setValue('1234')

    expect(wrapper.vm.loginForm.code).toBe('1234')
  })

  it('应该切换密码可见性', () => {
    const wrapper = mount(Login, {
      global: {
        stubs: {
          'el-form': true,
          'el-form-item': true,
          'el-input': true,
          'el-button': true,
          'el-checkbox': true,
          'el-tooltip': true
        }
      }
    })

    expect(wrapper.vm.showPassword).toBe(false)

    const toggleBtn = wrapper.find('span.password-eye')
    await toggleBtn.trigger('click')

    expect(wrapper.vm.showPassword).toBe(true)
  })

  it('应该提交登录表单', async () => {
    mockLogin.mockResolvedValue({ code: 200, token: 'test-token' })

    const wrapper = mount(Login, {
      global: {
        stubs: {
          'el-form': true,
          'el-form-item': true,
          'el-input': true,
          'el-button': true,
          'el-checkbox': true,
          'el-tooltip': true
        }
      }
    })

    await wrapper.vm.handleLogin()

    expect(mockLogin).toHaveBeenCalled()
    expect(mockSetUser).toHaveBeenCalled()
    expect(mockUseRouter().push).toHaveBeenCalled()
  })

  it('应该获取验证码图片', async () => {
    mockGetCodeImg.mockResolvedValue({ img: 'base64-image-data' })

    const wrapper = mount(Login, {
      global: {
        stubs: {
          'el-form': true,
          'el-form-item': true,
          'el-input': true,
          'el-button': true,
          'el-checkbox': true,
          'el-tooltip': true
        }
      }
    })

    await wrapper.vm.getCode()

    expect(mockGetCodeImg).toHaveBeenCalled()
    expect(wrapper.vm.codeUrl).toBe('base64-image-data')
  })
})
