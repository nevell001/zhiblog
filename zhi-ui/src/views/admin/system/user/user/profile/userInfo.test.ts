import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount, flushPromises } from '@vue/test-utils'
import ElementPlus from 'element-plus'
import UserInfo from './userInfo.vue'

const updateUserProfileMock = vi.fn()

vi.mock('@/api/system/user', () => ({
  updateUserProfile: (...args: unknown[]) => updateUserProfileMock(...args)
}))

/**
 * 挂真实 Element Plus：本文件断言的是规则驱动的校验结果（空手机号放行、
 * 非法手机号拦截），stub 掉 el-form 后 validate 恒真，测试会失去意义。
 */
function mountForm(user: Record<string, any> = {}) {
  const $modal = { msgSuccess: vi.fn(), msgError: vi.fn() }
  const wrapper = mount(UserInfo, {
    props: {
      user: {
        userId: 101,
        nickName: '博客用户',
        email: 'blog@example.com',
        phonenumber: '',
        sex: '0',
        ...user
      }
    },
    global: {
      plugins: [ElementPlus],
      config: { globalProperties: { $modal } }
    }
  })
  return { wrapper, $modal }
}

function clickSave(wrapper: ReturnType<typeof mountForm>['wrapper']) {
  return wrapper.find('button').trigger('click')
}

describe('基本资料表单（行为）', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    updateUserProfileMock.mockResolvedValue({ code: 200 })
  })

  it('没有手机号的博客用户也能保存资料（空手机号放行），并把改动同步回页面', async () => {
    const { wrapper, $modal } = mountForm()
    await flushPromises()

    await wrapper.find('input').setValue('新昵称')
    await clickSave(wrapper)
    await flushPromises()

    expect(updateUserProfileMock).toHaveBeenCalledTimes(1)
    expect(updateUserProfileMock.mock.calls[0][0]).toMatchObject({
      nickName: '新昵称',
      phonenumber: ''
    })
    expect($modal.msgSuccess).toHaveBeenCalled()
    expect((wrapper.props('user') as Record<string, any>).nickName).toBe('新昵称')
  })

  it('手机号格式不合法时仍然拦截保存', async () => {
    const { wrapper } = mountForm({ phonenumber: '12345' })
    await flushPromises()

    await clickSave(wrapper)
    await flushPromises()

    expect(updateUserProfileMock).not.toHaveBeenCalled()
  })
})
