/**
 * 请求序号守卫：并发/竞态请求下只允许"最新一次"结果落地。
 *
 * 典型场景（都出过问题）：快速切换文章详情、连续输入触发的搜索、翻页 + 加载更多并发——
 * 先发出的慢请求后返回，会把新数据覆盖成旧数据。用法：
 *
 * ```ts
 * const guard = createRequestGuard()
 * const token = guard.next()
 * const data = await fetchSomething()
 * if (!guard.isLatest(token)) return   // 已被更新的请求取代，丢弃
 * list.value = data
 * ```
 */
export interface RequestGuard {
  /** 取得本次请求的序号（自增） */
  next(): number
  /** 该序号是否仍是最新一次请求 */
  isLatest(token: number): boolean
  /** 作废当前所有在途请求（例如组件卸载/条件清空） */
  invalidate(): void
}

export function createRequestGuard(): RequestGuard {
  let current = 0
  return {
    next: () => ++current,
    isLatest: (token: number) => token === current,
    invalidate: () => {
      current++
    }
  }
}
