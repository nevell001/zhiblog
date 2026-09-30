import { describe, expect, it } from 'vitest'
import { createRequestGuard } from './requestGuard'

describe('请求序号守卫', () => {
  it('最新一次请求才算有效', () => {
    const guard = createRequestGuard()
    const first = guard.next()
    expect(guard.isLatest(first)).toBe(true)

    const second = guard.next()
    expect(guard.isLatest(first)).toBe(false)
    expect(guard.isLatest(second)).toBe(true)
  })

  it('invalidate 后所有在途请求都失效', () => {
    const guard = createRequestGuard()
    const token = guard.next()
    guard.invalidate()
    expect(guard.isLatest(token)).toBe(false)
  })

  it('模拟乱序返回：先发的慢请求不能覆盖后发的结果', async () => {
    const guard = createRequestGuard()
    const applied: string[] = []

    const request = (label: string, delay: number) => {
      const token = guard.next()
      return new Promise<void>(resolve => {
        setTimeout(() => {
          if (guard.isLatest(token)) applied.push(label)
          resolve()
        }, delay)
      })
    }

    // 先发 A（慢），再发 B（快）
    await Promise.all([request('A', 20), request('B', 5)])
    expect(applied).toEqual(['B'])
  })
})
