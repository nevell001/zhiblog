import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { effectScope, nextTick, ref, type EffectScope } from 'vue'
import { useDraftAutosave } from './useDraftAutosave'

const { confirmMock } = vi.hoisted(() => ({ confirmMock: vi.fn() }))

vi.mock('@/plugins/element-plus-service', () => ({
  ElMessageBox: { confirm: confirmMock }
}))

const KEY = 'zhiblog:article:draft:new'

interface DraftApi {
  beginSession: () => void
  maybeRestore: () => Promise<boolean>
  flush: () => void
  clear: () => void
}

let scopes: EffectScope[] = []

function setup(seed?: { savedAt: number; article: Record<string, unknown> }) {
  const storageKey = ref<string | null>(KEY)
  const form = ref<Record<string, any>>({
    title: '初始标题',
    content: '初始内容',
    categoryId: null,
    extra: '不参与草稿'
  })
  const active = ref(true)
  const dirty = ref(false)

  const scope = effectScope()
  let api!: DraftApi
  scope.run(() => {
    api = useDraftAutosave<Record<string, any>>({
      storageKey,
      form,
      fields: ['title', 'content', 'categoryId'],
      active,
      dirty
    })
  })
  scopes.push(scope)

  if (seed) localStorage.setItem(KEY, JSON.stringify(seed))
  return { storageKey, form, active, dirty, api, scope }
}

const storedDraft = () => {
  const raw = localStorage.getItem(KEY)
  return raw ? JSON.parse(raw) : null
}

/** 模拟「打开编辑器」顺序：先封锁落盘 → 赋值表单 → 走恢复询问 */
async function openEditor(api: DraftApi) {
  api.beginSession()
  await api.maybeRestore()
}

beforeEach(() => {
  vi.clearAllMocks()
  vi.useFakeTimers()
  localStorage.clear()
})

afterEach(() => {
  scopes.forEach(s => s.stop())
  scopes = []
  vi.useRealTimers()
})

describe('useDraftAutosave 草稿自动保存', () => {
  it('表单改动在防抖延迟后写入本地草稿，且只包含白名单字段', async () => {
    const { form, api } = setup()
    await openEditor(api)

    form.value.title = '新标题'
    await nextTick()
    vi.advanceTimersByTime(799)
    expect(storedDraft()).toBeNull()

    vi.advanceTimersByTime(1)
    const draft = storedDraft()
    expect(draft).not.toBeNull()
    expect(typeof draft.savedAt).toBe('number')
    expect(draft.article).toEqual({ title: '新标题', content: '初始内容', categoryId: null })
  })

  it('白名单外的字段变化不触发写入', async () => {
    const { form, api } = setup()
    await openEditor(api)

    form.value.extra = '改了也不保存'
    await nextTick()
    vi.advanceTimersByTime(1000)
    expect(storedDraft()).toBeNull()
  })

  it('编辑器关闭（active=false）后表单变化不再写入', async () => {
    const { form, active, api } = setup()
    await openEditor(api)

    active.value = false
    form.value.title = '关闭后变化'
    await nextTick()
    vi.advanceTimersByTime(1000)
    expect(storedDraft()).toBeNull()
  })

  it('恢复询问结束前的表单赋值（reset/回填）不会覆盖已有草稿', async () => {
    let resolveConfirm!: (value?: unknown) => void
    confirmMock.mockImplementation(
      () =>
        new Promise(res => {
          resolveConfirm = res
        })
    )
    const { form, api } = setup({
      savedAt: 1767225845000,
      article: { title: '草稿标题', content: '草稿内容', categoryId: 5 }
    })

    api.beginSession()
    const restoring = api.maybeRestore()
    // 打开流程里 reset() 先清空表单，稍后用户/初始化再回填
    form.value.title = ''
    form.value.content = ''
    await nextTick()
    vi.advanceTimersByTime(1000)
    expect(storedDraft().article).toEqual({
      title: '草稿标题',
      content: '草稿内容',
      categoryId: 5
    })

    resolveConfirm('confirm')
    expect(await restoring).toBe(true)
    expect(form.value.title).toBe('草稿标题')

    // 恢复完成后落盘恢复，后续改动正常覆盖
    form.value.title = '恢复后继续编辑'
    await nextTick()
    vi.advanceTimersByTime(800)
    expect(storedDraft().article.title).toBe('恢复后继续编辑')
  })

  it('草稿与当前内容不同时询问，确认后恢复内容', async () => {
    confirmMock.mockResolvedValue('confirm')
    const { form, api } = setup({
      savedAt: 1767225845000,
      article: { title: '草稿标题', content: '草稿内容', categoryId: 5 }
    })

    expect(await api.maybeRestore()).toBe(true)
    expect(form.value.title).toBe('草稿标题')
    expect(form.value.content).toBe('草稿内容')

    expect(confirmMock).toHaveBeenCalledTimes(1)
    const [message, title, options] = confirmMock.mock.calls[0]
    expect(message).toContain('本地草稿')
    expect(title).toBe('恢复草稿')
    // 区分「丢弃」与「关闭窗口」：后者要保留草稿
    expect(options.distinguishCancelAndClose).toBe(true)
  })

  it('点「丢弃」时删除草稿且不改动当前内容', async () => {
    confirmMock.mockRejectedValue('cancel')
    const { form, api } = setup({
      savedAt: 1767225845000,
      article: { title: '草稿标题' }
    })

    expect(await api.maybeRestore()).toBe(false)
    expect(form.value.title).toBe('初始标题')
    expect(localStorage.getItem(KEY)).toBeNull()
  })

  it('用 X / ESC 关闭询问时保留草稿，下次打开继续询问', async () => {
    confirmMock.mockRejectedValue('close')
    const { form, api } = setup({
      savedAt: 1767225845000,
      article: { title: '草稿标题' }
    })

    expect(await api.maybeRestore()).toBe(false)
    expect(form.value.title).toBe('初始标题')
    expect(storedDraft().article.title).toBe('草稿标题')
  })

  it('草稿与当前内容一致时静默清除，不弹窗打扰', async () => {
    const { api } = setup({
      savedAt: 1767225845000,
      article: { title: '初始标题', content: '初始内容', categoryId: null }
    })

    expect(await api.maybeRestore()).toBe(false)
    expect(confirmMock).not.toHaveBeenCalled()
    expect(localStorage.getItem(KEY)).toBeNull()
  })

  it('损坏的草稿数据不阻塞打开，且后续编辑照常落盘覆盖', async () => {
    localStorage.setItem(KEY, '{"article":')
    const { form, api } = setup()

    expect(await api.maybeRestore()).toBe(false)
    expect(form.value.title).toBe('初始标题')

    form.value.title = '新标题'
    await nextTick()
    vi.advanceTimersByTime(800)
    expect(storedDraft().article.title).toBe('新标题')
  })

  it('flush 立即落盘未保存改动；dirty 为假时不写', async () => {
    const { form, dirty, api } = setup()
    await openEditor(api)

    dirty.value = true
    form.value.title = '切走前的改动'
    await nextTick()
    api.flush()
    expect(storedDraft().article.title).toBe('切走前的改动')

    localStorage.removeItem(KEY)
    dirty.value = false
    form.value.title = '已保存过的内容'
    await nextTick()
    api.flush()
    expect(storedDraft()).toBeNull()
  })

  it('clear 删除草稿并取消未到期的写入', async () => {
    const { form, api } = setup()
    await openEditor(api)

    form.value.title = '待落盘'
    await nextTick()
    api.clear()
    vi.advanceTimersByTime(1000)
    expect(localStorage.getItem(KEY)).toBeNull()
  })

  it('localStorage 写入失败时静默降级，不打断编辑', async () => {
    const setItem = vi.spyOn(Storage.prototype, 'setItem').mockImplementation(() => {
      throw new Error('QuotaExceededError')
    })
    try {
      const { form, api } = setup()
      await openEditor(api)

      form.value.title = '无法保存的改动'
      await nextTick()
      expect(() => vi.advanceTimersByTime(800)).not.toThrow()
    } finally {
      setItem.mockRestore()
    }
  })

  it('有未保存改动时离开页面触发提醒；无改动、编辑器已关或作用域销毁后不提醒', async () => {
    const { active, dirty, scope } = setup()
    const fire = () => {
      const event = new Event('beforeunload', { cancelable: true })
      window.dispatchEvent(event)
      return event
    }

    dirty.value = true
    expect(fire().defaultPrevented).toBe(true)

    dirty.value = false
    expect(fire().defaultPrevented).toBe(false)

    dirty.value = true
    active.value = false
    expect(fire().defaultPrevented).toBe(false)

    active.value = true
    scope.stop()
    expect(fire().defaultPrevented).toBe(false)
  })
})
