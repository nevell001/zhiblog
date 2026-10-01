import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount, flushPromises } from '@vue/test-utils'
import { createPinia, setActivePinia } from 'pinia'
import BlogSetting from './index.vue'

/**
 * 后台博客设置页行为测试（挂载真实组件）。
 *
 * 取代原来的源码字符串断言：改版把「功能设置」做成卡片网格、「界面主题」加了明暗切换，
 * 继续 grep 源码文本没有意义。这里断言可观察行为：分组渲染、开关触发落库接口、
 * 主题/明暗切换真的作用到 <html> 与 store。
 */
const {
  listSetting,
  updateSettingValueByKey,
  clearBlogCache,
  getMailConfig,
  saveMailConfig,
  testMailConfig,
  ElMessage
} = vi.hoisted(() => ({
  listSetting: vi.fn(),
  updateSettingValueByKey: vi.fn(),
  clearBlogCache: vi.fn(),
  getMailConfig: vi.fn(),
  saveMailConfig: vi.fn(),
  testMailConfig: vi.fn(),
  ElMessage: { success: vi.fn(), warning: vi.fn(), error: vi.fn() }
}))

vi.mock('@/api/admin/blog/setting', () => ({
  listSetting: (...args: unknown[]) => listSetting(...args),
  updateSettingValueByKey: (...args: unknown[]) => updateSettingValueByKey(...args),
  getMailConfig: (...args: unknown[]) => getMailConfig(...args),
  saveMailConfig: (...args: unknown[]) => saveMailConfig(...args),
  testMailConfig: (...args: unknown[]) => testMailConfig(...args)
}))

vi.mock('@/api/blog/setting', () => ({
  clearBlogCache: (...args: unknown[]) => clearBlogCache(...args)
}))

vi.mock('@/plugins/element-plus-service', () => ({
  ElMessage
}))

// el-radio-group 用 stub 暴露 change，测试通过 $emit 选中值，驱动页面真正的处理逻辑
const ElRadioGroupStub = {
  name: 'ElRadioGroupStub',
  props: ['modelValue'],
  emits: ['change'],
  template: '<div class="rg"><slot /></div>'
}

function mountPage() {
  return mount(BlogSetting, {
    global: {
      plugins: [createPinia()],
      stubs: {
        'el-card': { template: '<div class="card"><slot /></div>' },
        'el-tabs': { template: '<div><slot /></div>' },
        'el-tab-pane': { template: '<div class="pane"><slot /></div>' },
        'el-form': { template: '<form><slot /></form>' },
        'el-form-item': { template: '<div class="fi"><slot /></div>' },
        'el-input': { props: ['modelValue'], template: '<input />' },
        'el-input-number': { props: ['modelValue'], template: '<input type="number" />' },
        'el-button': { template: '<button><slot /></button>' },
        'el-icon': { template: '<i><slot /></i>' },
        'el-alert': { template: '<div><slot /></div>' },
        'el-divider': { template: '<hr />' },
        'el-select': { template: '<div><slot /></div>' },
        'el-option': { template: '<span><slot /></span>' },
        'el-color-picker': { props: ['modelValue'], template: '<span class="cp" />' },
        'el-radio-group': ElRadioGroupStub,
        'el-radio-button': { template: '<span class="rb"><slot /></span>' },
        'el-switch': {
          props: ['modelValue', 'disabled'],
          emits: ['update:modelValue', 'change'],
          // data-feature / data-mail 通过默认 inheritAttrs 落到根元素上，供测试精确选取；
          // 点击时按真实 el-switch 的口径同时抛出新值与 change(新值)
          template:
            '<button class="sw" :disabled="disabled" @click="$emit(\'update:modelValue\', !modelValue); $emit(\'change\', !modelValue)"></button>'
        },
        editor: { template: '<div class="editor-stub" />' }
      }
    }
  })
}

describe('BlogSetting 视图（行为）', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    vi.clearAllMocks()
    document.documentElement.classList.remove('dark', 'theme-mo-blog')
    // 返回一行，跳过空结果时的重试；其余键回落到默认值
    listSetting.mockResolvedValue({
      code: 200,
      rows: [{ configKey: 'blog_name', configValue: 'X' }]
    })
    updateSettingValueByKey.mockResolvedValue({ code: 200 })
    clearBlogCache.mockResolvedValue({ code: 200 })
    getMailConfig.mockResolvedValue({
      code: 200,
      data: {
        host: 'smtp.example.com',
        port: 465,
        username: 'a@b.com',
        hasPassword: true,
        ssl: true,
        starttls: false,
        enabled: true
      }
    })
    saveMailConfig.mockResolvedValue({ code: 200, data: { hasPassword: true } })
    testMailConfig.mockResolvedValue({ code: 200, msg: '连接成功' })
  })

  it('导出组件', () => {
    expect(BlogSetting).toBeDefined()
  })

  it('功能设置以分组卡片渲染出各功能开关', async () => {
    const wrapper = mountPage()
    await flushPromises()

    const text = wrapper.text()
    expect(text).toContain('评论功能')
    expect(text).toContain('评论审核')
    expect(text).toContain('浏览统计')
    expect(text).toContain('友链申请入口')
    expect(text).toContain('邮件通知')

    // 每个功能开关都带 data-feature，供真实交互
    expect(wrapper.find('[data-feature="comment_enabled"]').exists()).toBe(true)
    expect(wrapper.find('[data-feature="like_enabled"]').exists()).toBe(true)
  })

  it('评论功能关闭时，评论审核开关被置灰禁用', async () => {
    listSetting.mockResolvedValue({
      code: 200,
      rows: [{ configKey: 'comment_enabled', configValue: 'false' }]
    })
    const wrapper = mountPage()
    await flushPromises()

    const review = wrapper.find('[data-feature="comment_review"]')
    expect(review.exists()).toBe(true)
    expect((review.element as HTMLButtonElement).disabled).toBe(true)
  })

  it('切换功能开关会把开关的新值落库', async () => {
    const wrapper = mountPage()
    await flushPromises()

    // comment_enabled 默认 true，点击 → 关掉并写入 'false'
    // （真实 el-switch 先更新 v-model 再抛 change，stub 按此口径发出新值）
    await wrapper.find('[data-feature="comment_enabled"]').trigger('click')
    await flushPromises()

    expect(updateSettingValueByKey).toHaveBeenCalledWith('comment_enabled', 'false')
    expect(ElMessage.success).toHaveBeenCalled()
  })

  it('选择「深色」外观模式会给 <html> 加上 dark 类', async () => {
    const wrapper = mountPage()
    await flushPromises()

    // 主题页第一个 el-radio-group 是外观模式
    const groups = wrapper.findAllComponents(ElRadioGroupStub)
    groups[0].vm.$emit('change', 'dark')
    await flushPromises()

    expect(document.documentElement.classList.contains('dark')).toBe(true)
  })

  it('选择 Mo-Blog 应用主题会给 <html> 加上 theme-mo-blog 类', async () => {
    const wrapper = mountPage()
    await flushPromises()

    const groups = wrapper.findAllComponents(ElRadioGroupStub)
    // 第二个 el-radio-group 是应用主题
    groups[1].vm.$emit('change', 'mo-blog')
    await flushPromises()

    expect(document.documentElement.classList.contains('theme-mo-blog')).toBe(true)
  })

  it('挂载时读取邮件服务配置', async () => {
    mountPage()
    await flushPromises()
    expect(getMailConfig).toHaveBeenCalled()
  })

  it('保存邮件配置走专用接口且空密码不提交', async () => {
    const wrapper = mountPage()
    await flushPromises()

    const saveBtn = wrapper.findAll('button').find(b => b.text().includes('保存并生效'))
    expect(saveBtn).toBeTruthy()
    await saveBtn!.trigger('click')
    await flushPromises()

    expect(saveMailConfig).toHaveBeenCalledTimes(1)
    const payload = saveMailConfig.mock.calls[0][0] as Record<string, unknown>
    // fetchMailConfig 未回显密码，保存时空密码不应作为 password 提交（保留原密码由后端处理）
    expect(payload.password === undefined || payload.password === '').toBe(true)
    expect(payload.host).toBe('smtp.example.com')
    expect(ElMessage.success).toHaveBeenCalled()
  })

  it('测试连接会把当前表单发给后端', async () => {
    const wrapper = mountPage()
    await flushPromises()

    const testBtn = wrapper.findAll('button').find(b => b.text().includes('测试连接'))
    expect(testBtn).toBeTruthy()
    await testBtn!.trigger('click')
    await flushPromises()

    expect(testMailConfig).toHaveBeenCalledTimes(1)
    const payload = testMailConfig.mock.calls[0][0] as Record<string, unknown>
    expect(payload.host).toBe('smtp.example.com')
  })

  it('打开 STARTTLS 会自动关闭 SSL，提交不会出现两者同开', async () => {
    // 回填：ssl=true / starttls=false
    const wrapper = mountPage()
    await flushPromises()

    await wrapper.find('[data-mail="starttls"]').trigger('click')
    await flushPromises()

    const saveBtn = wrapper.findAll('button').find(b => b.text().includes('保存并生效'))
    await saveBtn!.trigger('click')
    await flushPromises()

    const payload = saveMailConfig.mock.calls[0][0] as Record<string, unknown>
    expect(payload.starttls).toBe(true)
    expect(payload.ssl).toBe(false)
  })

  it('后端回显两者同开时，只按 SSL 呈现', async () => {
    // 环境变量基线可能让后端返回冲突值，表单不能照着画成两个都开
    getMailConfig.mockResolvedValue({
      code: 200,
      data: { host: 'smtp.example.com', port: 465, username: 'a@b.com', ssl: true, starttls: true }
    })
    const wrapper = mountPage()
    await flushPromises()

    const saveBtn = wrapper.findAll('button').find(b => b.text().includes('保存并生效'))
    await saveBtn!.trigger('click')
    await flushPromises()

    const payload = saveMailConfig.mock.calls[0][0] as Record<string, unknown>
    expect(payload.ssl).toBe(true)
    expect(payload.starttls).toBe(false)
  })
})
