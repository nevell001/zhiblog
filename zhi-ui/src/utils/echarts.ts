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

/**
 * 读取当前主题下的图表配色（跟随 html.dark 切换，浅色/深色自动适配）
 */
export function getChartThemeColors() {
  const rootStyle = getComputedStyle(document.documentElement)
  const cssVar = (name: string, fallback: string) =>
    rootStyle.getPropertyValue(name).trim() || fallback
  return {
    textColor: cssVar('--el-text-color-primary', '#303133'),
    secondaryColor: cssVar('--el-text-color-secondary', '#909399'),
    borderColor: cssVar('--el-border-color', '#e4e7ed'),
    splitLineColor: cssVar('--el-border-color-lighter', '#ebeef5')
  }
}
