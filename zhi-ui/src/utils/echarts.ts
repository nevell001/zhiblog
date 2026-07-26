import type { init as initEcharts } from 'echarts/core'

type EChartsModule = {
  init: typeof initEcharts
}
type EChartsImporter = () => Promise<EChartsModule>

export function createEchartsLoader(importEcharts: EChartsImporter) {
  let echartsPromise: Promise<EChartsModule> | null = null

  return () => {
    if (!echartsPromise) {
      echartsPromise = importEcharts()
    }
    return echartsPromise
  }
}

export const loadEcharts = createEchartsLoader(async () => {
  const { init } = await import('./echarts-runtime')
  return { init }
})
