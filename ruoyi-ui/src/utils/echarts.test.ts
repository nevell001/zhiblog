import { describe, expect, it, vi } from 'vitest'
import { createEchartsLoader } from './echarts'

describe('createEchartsLoader', () => {
  it('loads echarts only once and reuses the same module', async () => {
    const module = { init: vi.fn() }
    const importEcharts = vi.fn().mockResolvedValue(module)
    const loadEcharts = createEchartsLoader(importEcharts)

    await expect(loadEcharts()).resolves.toBe(module)
    await expect(loadEcharts()).resolves.toBe(module)

    expect(importEcharts).toHaveBeenCalledTimes(1)
  })
})
