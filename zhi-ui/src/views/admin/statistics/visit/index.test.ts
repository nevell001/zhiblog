import { describe, it, expect, vi, beforeEach } from 'vitest'
import { flushPromises, mount } from '@vue/test-utils'
import type { App } from 'vue'
import VisitLog from './index.vue'
import { getVisitLogList, getVisitSummary } from '@/api/statistics'

vi.mock('@/api/statistics', () => ({
  getVisitLogList: vi.fn(),
  getVisitSummary: vi.fn(),
  exportVisitLog: vi.fn(),
  cleanVisitLog: vi.fn(),
  delVisitLog: vi.fn()
}))

const emptySummary = {
  code: 200,
  msg: 'ok',
  data: {
    beginDate: '',
    endDate: '',
    days: 30,
    pv: 0,
    uv: 0,
    todayPv: 0,
    todayUv: 0,
    topTargets: []
  }
}

const modalMock = {
  msgError: vi.fn(),
  msgSuccess: vi.fn(),
  confirm: vi.fn().mockResolvedValue(undefined)
}

/** 模拟 main.ts 中挂载的全局方法（$modal / download / parseTime） */
const globalPropertiesPlugin = {
  install(app: App) {
    app.config.globalProperties.$modal = modalMock
    app.config.globalProperties.download = vi.fn()
    app.config.globalProperties.parseTime = (value?: string) => value || ''
  }
}

function mountView() {
  return mount(VisitLog, {
    global: {
      plugins: [globalPropertiesPlugin],
      stubs: {
        pagination: true,
        'right-toolbar': true,
        'router-link': true,
        // el-table-column 需要 scope 参数，测试环境下用桩避免未注册组件渲染报错
        'el-table-column': true
      },
      directives: {
        hasPermi: {}
      }
    }
  })
}

describe('VisitLog 访问明细视图组件测试', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    vi.mocked(getVisitLogList).mockResolvedValue({ code: 200, msg: 'ok', rows: [], total: 0 })
    vi.mocked(getVisitSummary).mockResolvedValue(emptySummary)
  })

  it('应该导出 VisitLog 组件', () => {
    expect(VisitLog).toBeDefined()
    expect(typeof VisitLog).toBe('object')
  })

  it('挂载后应该加载访问明细与汇总数据', async () => {
    mountView()
    await flushPromises()

    expect(getVisitLogList).toHaveBeenCalledTimes(1)
    expect(getVisitLogList).toHaveBeenCalledWith(
      expect.objectContaining({ pageNum: 1, pageSize: 10, uniqueOnly: false })
    )
    expect(getVisitSummary).toHaveBeenCalledWith(30)
  })

  it('接口失败时应该提示错误且保持空态（不使用假数据）', async () => {
    vi.mocked(getVisitLogList).mockRejectedValue(new Error('boom'))
    vi.mocked(getVisitSummary).mockRejectedValue(new Error('boom'))

    mountView()
    await flushPromises()

    expect(modalMock.msgError).toHaveBeenCalledWith('获取访问明细失败')
    expect(modalMock.msgError).toHaveBeenCalledWith('获取访问汇总失败')
  })

  it('切换汇总天数时应该以新的天数请求汇总接口', async () => {
    const wrapper = mountView()
    await flushPromises()

    const vm = wrapper.vm as unknown as { summaryDays: number; loadSummary: () => void }
    vm.summaryDays = 7
    vm.loadSummary()
    await flushPromises()

    expect(getVisitSummary).toHaveBeenLastCalledWith(7)
  })
})
