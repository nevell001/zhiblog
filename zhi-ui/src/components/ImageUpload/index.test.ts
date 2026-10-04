import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount, flushPromises } from '@vue/test-utils'
import ElementPlus from 'element-plus'
import ImageUpload from './index.vue'

const requestMock = vi.fn()

vi.mock('@/utils/request', () => ({
  default: (...args: unknown[]) => requestMock(...args)
}))

vi.mock('@/utils/auth', () => ({
  getToken: () => 'test-token'
}))

const cropperStub = vi.hoisted(() => ({
  getCropBlob: vi.fn(),
  changeScale: vi.fn(),
  rotateLeft: vi.fn(),
  rotateRight: vi.fn(),
  showPreview: vi.fn()
}))

/** 用轻量桩替换裁剪器：断言组件传给裁剪器的配置与提交链路，不需要真实 canvas 渲染 */
vi.mock('vue-cropper', () => ({
  VueCropper: {
    name: 'VueCropper',
    props: [
      'img',
      'autoCrop',
      'fixed',
      'fixedNumber',
      'centerBox',
      'info',
      'full',
      'outputType',
      'outputSize'
    ],
    emits: ['real-time'],
    setup(_: any, { expose }: any) {
      expose(cropperStub)
      return () => null
    }
  }
}))

/** 直通对话框桩：始终渲染 slot，测试里手动 emit 'open' 模拟弹窗展开 */
const ElDialogStub = {
  name: 'ElDialogStub',
  template: '<div class="el-dialog-stub"><slot /></div>'
}

const UploadStub = { render: () => null }

function makeFile(name = 'cover.png') {
  return new File([new Blob(['image-bytes'])], name, { type: 'image/png' })
}

async function mountUpload(overrides = {}) {
  const $modal = { loading: vi.fn(), closeLoading: vi.fn(), msgError: vi.fn(), msgSuccess: vi.fn() }
  const wrapper = mount(ImageUpload, {
    props: { action: '/common/upload/article-cover', modelValue: '', ...overrides },
    global: {
      plugins: [ElementPlus],
      config: { globalProperties: { $modal } },
      components: { Upload: UploadStub },
      stubs: { ElDialog: ElDialogStub }
    }
  })
  return { wrapper, $modal }
}

/** 走完「选图 → FileReader 读入 → 弹窗打开」的前置流程 */
async function openCropDialog(
  wrapper: Awaited<ReturnType<typeof mountUpload>>['wrapper'],
  file = makeFile()
) {
  const elUpload = wrapper.findComponent({ name: 'ElUpload' })
  elUpload.props('beforeUpload')(file)
  await new Promise(resolve => setTimeout(resolve, 0))
  await flushPromises()
  // 触发 el-dialog 的 open 回调（真实交互中由弹窗展开动画触发）
  for (const dialog of wrapper.findAllComponents({ name: 'ElDialogStub' })) {
    dialog.vm.$emit('open')
  }
  await flushPromises()
  return file
}

function findButton(wrapper: Awaited<ReturnType<typeof mountUpload>>['wrapper'], text: string) {
  return wrapper.findAll('button').find(btn => btn.text().includes(text))
}

describe('ImageUpload 裁剪上传（行为）', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    requestMock.mockResolvedValue({
      code: 200,
      url: '/profile/upload/2026/10/x.jpg',
      fileName: '/profile/upload/2026/10/x.jpg'
    })
  })

  it('crop 关闭时保持原行为：不渲染裁剪器、before-upload 放行走自带上传', async () => {
    const { wrapper, $modal } = await mountUpload()
    const file = makeFile()

    const elUpload = wrapper.findComponent({ name: 'ElUpload' })
    const result = elUpload.props('beforeUpload')(file)
    await flushPromises()

    expect(result).not.toBe(false)
    expect($modal.loading).toHaveBeenCalled()
    expect(wrapper.findComponent({ name: 'VueCropper' }).exists()).toBe(false)
  })

  it('crop 开启时选图被拦截并打开裁剪弹窗，裁剪器配置为 16:9 固定比例 + jpeg 输出', async () => {
    const { wrapper } = await mountUpload({ crop: true })
    const file = await openCropDialog(wrapper)

    const elUpload = wrapper.findComponent({ name: 'ElUpload' })
    expect(elUpload.props('beforeUpload')(makeFile())).toBe(false)

    const cropper = wrapper.findComponent({ name: 'VueCropper' })
    expect(cropper.exists()).toBe(true)
    expect(cropper.props()).toMatchObject({
      autoCrop: true,
      fixed: true,
      fixedNumber: [16 / 9, 1],
      centerBox: true,
      full: true,
      outputType: 'jpeg'
    })
    // 弹窗内暂存的原始文件用于「跳过裁剪」直传
    expect((wrapper.vm as any).cropFile).toBe(file)
  })

  it('确认裁剪：getCropBlob 的结果以 file 字段 POST 到 action，成功后 emit 相对路径', async () => {
    const { wrapper } = await mountUpload({ crop: true })
    await openCropDialog(wrapper)

    cropperStub.getCropBlob.mockImplementation((cb: (blob: Blob) => void) =>
      cb(new Blob(['cropped'], { type: 'image/jpeg' }))
    )
    await findButton(wrapper, '确认裁剪')!.trigger('click')
    await flushPromises()

    expect(requestMock).toHaveBeenCalledTimes(1)
    const config = requestMock.mock.calls[0][0] as any
    expect(config.url).toBe('/common/upload/article-cover')
    expect(config.method).toBe('post')
    const blob = config.data.get('file') as File
    expect(blob).toBeInstanceOf(Blob)
    expect(blob.type).toBe('image/jpeg')

    const emitted = wrapper.emitted('update:modelValue')
    expect(emitted?.at(-1)?.[0]).toBe('/profile/upload/2026/10/x.jpg')

    // 上传成功后裁剪弹窗关闭（v-model 落到 false）
    const cropDialog = wrapper.findAllComponents({ name: 'ElDialogStub' }).at(-1)!
    expect((cropDialog.vm.$attrs as any).modelValue).toBe(false)
  })

  it('跳过裁剪：直接上传原始文件', async () => {
    const { wrapper } = await mountUpload({ crop: true })
    const file = await openCropDialog(wrapper)

    await findButton(wrapper, '跳过裁剪')!.trigger('click')
    await flushPromises()

    expect(cropperStub.getCropBlob).not.toHaveBeenCalled()
    expect(requestMock).toHaveBeenCalledTimes(1)
    const config = requestMock.mock.calls[0][0] as any
    const uploaded = config.data.get('file') as File
    expect(uploaded).toBeInstanceOf(File)
    expect(uploaded.name).toBe(file.name)
    expect(uploaded.size).toBe(file.size)
  })

  it('成功响应只有 fileName 时同样能落地（与 el-upload 直传路径共用成功处理）', async () => {
    requestMock.mockResolvedValue({ code: 200, fileName: '/profile/upload/2026/10/y.png' })
    const { wrapper } = await mountUpload({ crop: true })
    await openCropDialog(wrapper)

    await findButton(wrapper, '跳过裁剪')!.trigger('click')
    await flushPromises()

    expect(wrapper.emitted('update:modelValue')?.at(-1)?.[0]).toBe('/profile/upload/2026/10/y.png')
  })

  it('上传失败：提示错误且不 emit', async () => {
    requestMock.mockRejectedValue(new Error('boom'))
    const { wrapper, $modal } = await mountUpload({ crop: true })
    await openCropDialog(wrapper)

    await findButton(wrapper, '跳过裁剪')!.trigger('click')
    await flushPromises()

    expect($modal.msgError).toHaveBeenCalled()
    expect(wrapper.emitted('update:modelValue')).toBeUndefined()
  })
})

describe('ImageUpload 基础回归', () => {
  it('应该导出 ImageUpload 组件', () => {
    expect(ImageUpload).toBeDefined()
  })
})
