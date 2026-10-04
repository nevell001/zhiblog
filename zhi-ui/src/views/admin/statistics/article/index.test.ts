import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { flushPromises, mount, type VueWrapper } from '@vue/test-utils'
import ArticleStatistics from './index.vue'
import {
  getArticleStatistics,
  getArticleCategoryDistribution,
  getHotTags,
  getDailyPvUv
} from '@/api/statistics'

vi.mock('@/api/statistics', () => ({
  getArticleStatistics: vi.fn(),
  getArticleCategoryDistribution: vi.fn(),
  getHotTags: vi.fn(),
  getDailyPvUv: vi.fn()
}))

vi.mock('@/stores/settings', () => ({
  useSettingsStore: () => ({ isDark: false })
}))

// echarts 在 jsdom 下画不出内容，桩掉 init，只断言「挂到了哪个 DOM 节点」
const setOptionMock = vi.fn()
const initMock = vi.fn(() => ({
  setOption: setOptionMock,
  resize: vi.fn(),
  dispose: vi.fn()
}))

vi.mock('@/utils/echarts', () => ({
  loadEcharts: () =>
    Promise.resolve({
      init: (...args: unknown[]) => initMock(...args)
    }),
  getChartThemeColors: () => ({
    textColor: '#303133',
    secondaryColor: '#909399',
    borderColor: '#e4e7ed',
    splitLineColor: '#ebeef5'
  })
}))

let wrapper: VueWrapper | null = null

/** 挂到真实 document：页面用 getElementById 取容器，不附着就区分不出「容器存在」和「还在 v-if 里」 */
function mountView() {
  wrapper = mount(ArticleStatistics, { attachTo: document.body })
  return wrapper
}

const ok = (data: unknown) => ({ code: 200, msg: 'ok', data })
const initializedIds = () => initMock.mock.calls.map(call => (call[0] as HTMLElement)?.id)

describe('文章统计页图表', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    vi.mocked(getArticleStatistics).mockResolvedValue(
      ok({ publishedCount: 3, draftCount: 1, avgViews: 10 })
    )
    vi.mocked(getArticleCategoryDistribution).mockResolvedValue(ok({ labels: ['技术'], data: [3] }))
    vi.mocked(getHotTags).mockResolvedValue(ok({ labels: ['Vue'], data: [2] }))
    vi.mocked(getDailyPvUv).mockResolvedValue(ok([{ statDate: '2026-10-01', pv: 5, uv: 2 }]))
  })

  afterEach(() => {
    wrapper?.unmount()
    wrapper = null
  })

  it('挂载后三张图都挂到真实容器上并写入配置', async () => {
    mountView()
    await flushPromises()

    expect(initializedIds()).toEqual(
      expect.arrayContaining(['categoryChart', 'tagChart', 'dailyPvUvChart'])
    )
    expect(setOptionMock).toHaveBeenCalled()
  })

  it('分类接口返回非 200 时显示空态而不是空白卡片', async () => {
    vi.mocked(getArticleCategoryDistribution).mockResolvedValue({ code: 500, msg: 'boom' })

    const view = mountView()
    await flushPromises()

    const descriptions = view.findAll('el-empty').map(node => node.attributes('description'))
    expect(descriptions).toContain('暂无分类数据')
    expect(initializedIds()).not.toContain('categoryChart')
  })
})
