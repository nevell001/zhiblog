import { ref, watch, onScopeDispose, type Ref } from 'vue'
import { ElMessageBox } from '@/plugins/element-plus-service'
import { parseTime } from '@/utils/zhi'

interface DraftAutosaveOptions<T extends Record<string, any>> {
  /** localStorage 键；null 表示当前没有可归属的编辑目标（如编辑器未打开） */
  storageKey: Ref<string | null>
  /** 参与草稿的表单对象 */
  form: Ref<T>
  /** 纳入草稿的字段（恢复时覆盖回 form） */
  fields: readonly string[]
  /** 是否处于编辑中（编辑器已打开） */
  active: Ref<boolean>
  /** 当前是否有未保存改动（用于关闭页面提醒） */
  dirty: Ref<boolean>
  /** 防抖延迟（ms） */
  delay?: number
}

interface DraftPayload {
  savedAt?: number
  article?: Record<string, unknown>
}

/**
 * 表单草稿自动保存：改动防抖落 localStorage，重新打开时询问恢复，
 * 关闭页面前在有未保存改动时提醒。
 *
 * 使用顺序：打开编辑器时 beginSession()（在赋值表单之前）→
 * 赋值表单 → await maybeRestore()（内部在流程结束后解除落盘封锁，
 * 避免打开时的赋值/空白重置覆盖旧草稿）。
 */
export function useDraftAutosave<T extends Record<string, any>>(options: DraftAutosaveOptions<T>) {
  const { storageKey, form, fields, active, dirty, delay = 800 } = options

  // 恢复询问结束前不落盘：否则对话框刚打开的空表单会被自己写进草稿
  let ready = false
  let timer: ReturnType<typeof setTimeout> | undefined

  const pickFields = (source: Record<string, any>) => {
    const draft: Record<string, unknown> = {}
    fields.forEach(key => {
      draft[key] = source[key]
    })
    return draft
  }

  const stopTimer = () => {
    if (timer) {
      clearTimeout(timer)
      timer = undefined
    }
  }

  const save = () => {
    if (!storageKey.value) return
    try {
      const payload: DraftPayload = { savedAt: Date.now(), article: pickFields(form.value) }
      localStorage.setItem(storageKey.value, JSON.stringify(payload))
    } catch {
      // 存储不可用（隐私模式/超配额）：静默降级为无自动保存，不打断编辑
    }
  }

  const read = (): DraftPayload | null => {
    if (!storageKey.value) return null
    try {
      const raw = localStorage.getItem(storageKey.value)
      return raw ? (JSON.parse(raw) as DraftPayload) : null
    } catch {
      return null
    }
  }

  const scheduleSave = () => {
    if (!active.value || !ready || !storageKey.value) return
    stopTimer()
    timer = setTimeout(save, delay)
  }

  /** 关闭/切走前把最后一次改动立即落盘（只对有未保存改动的情形） */
  const flush = () => {
    stopTimer()
    if (active.value && ready && dirty.value) save()
  }

  /** 清除当前目标对应的草稿 */
  const clear = () => {
    stopTimer()
    if (!storageKey.value) return
    try {
      localStorage.removeItem(storageKey.value)
    } catch {
      // 忽略存储异常
    }
  }

  /** 打开编辑器（赋值表单前）调用：在恢复询问结束前封锁落盘 */
  const beginSession = () => {
    ready = false
    stopTimer()
  }

  /**
   * 打开编辑器后调用：本地草稿与当前表单内容不同时询问是否恢复。
   * 结束后解除落盘封锁（无论是否有草稿）。
   */
  const maybeRestore = async (): Promise<boolean> => {
    try {
      const payload = read()
      if (!payload?.article) return false

      const differs = fields.some(
        key =>
          JSON.stringify(payload.article![key] ?? null) !== JSON.stringify(form.value[key] ?? null)
      )
      if (!differs) {
        clear()
        return false
      }

      const time = parseTime(payload.savedAt, '{y}-{m}-{d} {h}:{i}')
      await ElMessageBox.confirm(
        `检测到 ${time || ''} 保存的本地草稿与当前内容不同，是否恢复？`.replace(' 保存', '保存'),
        '恢复草稿',
        {
          confirmButtonText: '恢复',
          cancelButtonText: '丢弃',
          type: 'warning',
          distinguishCancelAndClose: true
        }
      )
      Object.assign(form.value, payload.article)
      return true
    } catch (action) {
      // 点「丢弃」才删除；点 X/ESC 视为稍后再决定，保留草稿下次再问
      if (action === 'cancel') clear()
      return false
    } finally {
      ready = true
    }
  }

  watch(() => JSON.stringify(pickFields(form.value)), scheduleSave)

  const handleBeforeUnload = (event: BeforeUnloadEvent) => {
    if (active.value && dirty.value) {
      event.preventDefault()
      event.returnValue = ''
    }
  }
  window.addEventListener('beforeunload', handleBeforeUnload)
  onScopeDispose(() => {
    window.removeEventListener('beforeunload', handleBeforeUnload)
    stopTimer()
  })

  return { beginSession, maybeRestore, flush, clear }
}
