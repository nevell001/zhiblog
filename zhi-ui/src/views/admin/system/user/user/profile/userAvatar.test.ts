import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount, flushPromises } from '@vue/test-utils'
import { createPinia, setActivePinia } from 'pinia'
import ElementPlus from 'element-plus'
import { readFileSync } from 'node:fs'
import { resolve } from 'node:path'
import { useUserStore } from '@/stores/user'
import UserAvatar from './userAvatar.vue'

const uploadAvatarMock = vi.fn()

vi.mock('@/api/system/user', () => ({
  uploadAvatar: (...args: unknown[]) => uploadAvatarMock(...args)
}))

const cropperStub = vi.hoisted(() => ({
  getCropBlob: vi.fn(),
  rotateLeft: vi.fn(),
  rotateRight: vi.fn(),
  showPreview: vi.fn()
}))

/** 用轻量桩替换裁剪器：断言的是组件传给裁剪器的配置与提交链路，不需要真实 canvas 渲染 */
vi.mock('vue-cropper', () => ({
  VueCropper: {
    name: 'VueCropper',
    props: [
      'img',
      'info',
      'autoCrop',
      'autoCropWidth',
      'autoCropHeight',
      'fixedBox',
      'centerBox',
      'canMoveBox',
      'outputType'
    ],
    emits: ['real-time'],
    setup(_: any, { expose }: any) {
      expose(cropperStub)
      return () => null
    }
  }
}))

/** 直通对话框桩：挂载即渲染 slot 并触发 opened，模拟用户点开修改头像弹窗 */
const ElDialogStub = {
  mounted(this: { $emit: (event: string) => void }) {
    this.$emit('opened')
  },
  template: '<div class="el-dialog-stub"><slot /></div>'
}

/** 应用里由 main.ts 全局注册的图标组件，测试环境补桩避免解析警告 */
const UploadStub = { render: () => null }

function mountAvatar() {
  const pinia = createPinia()
  setActivePinia(pinia)
  const $modal = { msgSuccess: vi.fn(), msgError: vi.fn() }
  const wrapper = mount(UserAvatar, {
    global: {
      plugins: [ElementPlus, pinia],
      config: { globalProperties: { $modal } },
      components: { Upload: UploadStub },
      stubs: { ElDialog: ElDialogStub }
    }
  })
  return { wrapper, $modal }
}

describe('修改头像对话框（行为）', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    cropperStub.getCropBlob.mockImplementation((cb: (blob: Blob) => void) =>
      cb(new Blob(['fake'], { type: 'image/png' }))
    )
  })

  it('裁剪器配置为：中心选框 + 拖动图片（框不可拖出图片，导出不带图片外空白）', async () => {
    const { wrapper } = mountAvatar()
    await flushPromises()

    const cropper = wrapper.findComponent({ name: 'VueCropper' })
    expect(cropper.exists()).toBe(true)
    expect(cropper.props()).toMatchObject({
      info: true,
      autoCrop: true,
      autoCropWidth: 200,
      autoCropHeight: 200,
      fixedBox: true,
      centerBox: true,
      canMoveBox: false,
      outputType: 'png'
    })
  })

  it('提交时把裁剪结果以 avatarfile 字段上传，成功后同步 store 头像并提示', async () => {
    uploadAvatarMock.mockResolvedValue({ imgUrl: '/profile/avatar/2026/10/03/x.png' })
    const { wrapper, $modal } = mountAvatar()
    const userStore = useUserStore()
    await flushPromises()

    await wrapper.find('.el-button--primary').trigger('click')
    await flushPromises()

    expect(uploadAvatarMock).toHaveBeenCalledTimes(1)
    const formData = uploadAvatarMock.mock.calls[0][0] as FormData
    expect(formData).toBeInstanceOf(FormData)
    const blob = formData.get('avatarfile') as File
    expect(blob).toBeInstanceOf(Blob)
    expect(blob.name).toBe('avatar')
    expect(userStore.avatar.endsWith('/profile/avatar/2026/10/03/x.png')).toBe(true)
    expect($modal.msgSuccess).toHaveBeenCalled()
  })

  it('旋转后补一次预览刷新（vue-cropper 16ms 节流会丢弃旋转末尾被钳制的选框位置帧）', async () => {
    const { wrapper } = mountAvatar()
    await flushPromises()

    const rotateRightBtn = wrapper
      .findAllComponents({ name: 'ElButton' })
      .find(btn => btn.props('icon') === 'RefreshRight')
    expect(rotateRightBtn).toBeTruthy()

    await rotateRightBtn!.trigger('click')
    expect(cropperStub.rotateRight).toHaveBeenCalledTimes(1)
    expect(cropperStub.showPreview).not.toHaveBeenCalled()

    await new Promise(resolve => setTimeout(resolve, 60))
    expect(cropperStub.showPreview).toHaveBeenCalledTimes(1)
  })

  /**
   * 跨文件 CSS 契约：预览圆定位依赖 global 样式（zhi.scss 里 left/top 50% + translate(-50%,-50%)）
   * 与组件内 .avatar-preview-col{position:relative} 成对出现，配对失效时圆会偏出预览列。
   * jsdom 无布局引擎、getBoundingClientRect 恒为 0，无法行为验证，只能在真实浏览器中人工确认。
   */
  it('预览圆与其定位列成对存在（跨文件 CSS 契约）', () => {
    const scss = readFileSync(resolve(process.cwd(), 'src/assets/styles/zhi.scss'), 'utf-8')
    const previewRule = scss.match(/\.avatar-upload-preview\s*\{[^}]*\}/)?.[0] ?? ''
    expect(previewRule).toMatch(/left:\s*50%/)
    expect(previewRule).toMatch(/translate\(\s*-50%\s*,\s*-50%\s*\)/)

    const vue = readFileSync(
      resolve(process.cwd(), 'src/views/admin/system/user/user/profile/userAvatar.vue'),
      'utf-8'
    )
    expect(vue).toContain('class="avatar-preview-col"')
    expect(vue).toMatch(/\.avatar-preview-col\s*\{[^}]*position:\s*relative/)
  })
})
