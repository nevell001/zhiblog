import { describe, expect, it } from 'vitest'
import { buildPageItems } from './pageItems'

describe('buildPageItems', () => {
  it('总页数 ≤ 7 时全部展示', () => {
    expect(buildPageItems(1, 1)).toEqual([1])
    expect(buildPageItems(2, 1)).toEqual([1, 2])
    expect(buildPageItems(7, 5)).toEqual([1, 2, 3, 4, 5, 6, 7])
  })

  it('总页数 8 起在间隔处插入省略号', () => {
    expect(buildPageItems(8, 1)).toEqual([1, 2, '…', 8])
    expect(buildPageItems(8, 8)).toEqual([1, '…', 7, 8])
  })

  it('中间页显示当前页邻域', () => {
    expect(buildPageItems(8, 4)).toEqual([1, '…', 3, 4, 5, '…', 8])
    expect(buildPageItems(20, 10)).toEqual([1, '…', 9, 10, 11, '…', 20])
  })

  it('邻域与端点相邻时不产生省略号', () => {
    expect(buildPageItems(8, 2)).toEqual([1, 2, 3, '…', 8])
    expect(buildPageItems(8, 7)).toEqual([1, '…', 6, 7, 8])
  })

  it('当前页越界时收敛到有效范围', () => {
    expect(buildPageItems(5, 99)).toEqual([1, 2, 3, 4, 5])
    expect(buildPageItems(5, 0)).toEqual([1, 2, 3, 4, 5])
  })
})
