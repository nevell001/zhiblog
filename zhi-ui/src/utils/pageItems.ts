/**
 * 生成首页分页的页码序列。
 * 总页数 ≤ 7 时全部展示；否则压缩为「1 … 当前页邻域 … N」，
 * 相邻页码间隔超过 1 时以省略号占位。
 */
export function buildPageItems(totalPages: number, current: number): (number | '…')[] {
  const n = Math.max(1, Math.floor(totalPages))
  const cur = Math.min(Math.max(1, current), n)
  if (n <= 7) return Array.from({ length: n }, (_, i) => i + 1)
  const pages = new Set<number>([1, n, cur - 1, cur, cur + 1])
  const sorted = [...pages].filter(p => p >= 1 && p <= n).sort((a, b) => a - b)
  const items: (number | '…')[] = []
  sorted.forEach((p, i) => {
    if (i > 0 && p - sorted[i - 1] > 1) items.push('…')
    items.push(p)
  })
  return items
}
