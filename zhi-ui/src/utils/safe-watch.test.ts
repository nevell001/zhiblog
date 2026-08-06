import { describe, it, expect, vi, beforeEach } from 'vitest'
import { safeWatch, safeWatchEffect } from './safe-watch'
import { logger } from '@/utils/logger'

vi.mock('@/utils/logger', () => ({
  logger: { error: vi.fn(), info: vi.fn(), warn: vi.fn(), debug: vi.fn() }
}))

let capturedWatchCb: any = null
let capturedEffectCb: any = null

vi.mock('vue', () => ({
  watch: vi.fn((_source: any, cb: any) => {
    capturedWatchCb = cb
    return () => {}
  }),
  watchEffect: vi.fn((cb: any) => {
    capturedEffectCb = cb
    return () => {}
  })
}))

describe('safe-watch 工具', () => {
  beforeEach(() => {
    capturedWatchCb = null
    capturedEffectCb = null
    vi.clearAllMocks()
  })

  it('safeWatch 应包装回调并把 cleanup 传给 Vue watch', () => {
    const cleanup = vi.fn()
    const callback = vi.fn((_value: number, _old: number | undefined, onCleanup: any) => {
      onCleanup(cleanup)
    })

    const stop = safeWatch(() => 1, callback)
    expect(typeof stop).toBe('function')

    capturedWatchCb(1, undefined, (fn: any) => fn())
    expect(callback).toHaveBeenCalled()
    expect(cleanup).toHaveBeenCalled()
  })

  it('safeWatch 的 cleanup 抛错时应记录日志而不向上抛出', () => {
    const callback = vi.fn((_value: number, _old: number | undefined, onCleanup: any) => {
      onCleanup(() => {
        throw new Error('cleanup boom')
      })
    })

    safeWatch(() => 1, callback)
    expect(() => capturedWatchCb(1, undefined, (fn: any) => fn())).not.toThrow()
    expect(logger.error).toHaveBeenCalled()
  })

  it('safeWatchEffect 应包装 effect', () => {
    const effect = vi.fn((_onCleanup: any) => {})

    safeWatchEffect(effect)
    capturedEffectCb((fn: any) => fn())
    expect(effect).toHaveBeenCalled()
  })

  it('safeWatchEffect 的 cleanup 抛错时应记录日志而不向上抛出', () => {
    const effect = vi.fn((onCleanup: any) => {
      onCleanup(() => {
        throw new Error('effect cleanup boom')
      })
    })

    safeWatchEffect(effect)
    expect(() => capturedEffectCb((fn: any) => fn())).not.toThrow()
    expect(logger.error).toHaveBeenCalled()
  })
})
