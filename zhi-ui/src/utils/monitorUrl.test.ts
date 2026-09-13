import { describe, expect, it } from 'vitest'
import {
  ACTUATOR_DEFAULT_PATH,
  MONITOR_DEFAULT_PORTS,
  monitorUrlPort,
  normalizeMonitorUrl,
  resolveActuatorEndpointUrl,
  resolveMonitorUrl
} from './monitorUrl'

describe('监控入口地址解析', () => {
  describe('normalizeMonitorUrl', () => {
    it('应该去掉首尾空白与结尾斜杠', () => {
      expect(normalizeMonitorUrl('  https://monitor.example.com:9090/  ')).toBe(
        'https://monitor.example.com:9090'
      )
    })

    it('站内相对路径应保持原样', () => {
      expect(normalizeMonitorUrl('/prometheus/')).toBe('/prometheus')
    })

    it('缺协议的地址应补成协议相对写法', () => {
      expect(normalizeMonitorUrl('monitor.example.com:9090')).toBe('//monitor.example.com:9090')
    })

    it('空值返回空串', () => {
      expect(normalizeMonitorUrl('')).toBe('')
      expect(normalizeMonitorUrl(null)).toBe('')
      expect(normalizeMonitorUrl(undefined)).toBe('')
    })
  })

  describe('resolveMonitorUrl', () => {
    it('后台配置优先于环境变量与自动推导', () => {
      expect(
        resolveMonitorUrl('prometheus', {
          configured: 'https://monitor.example.com/prometheus',
          envUrl: 'https://env.example.com:9090',
          siteUrl: 'https://blog.example.com'
        })
      ).toBe('https://monitor.example.com/prometheus')
    })

    it('未配置时使用构建期环境变量', () => {
      expect(
        resolveMonitorUrl('grafana', {
          configured: '   ',
          envUrl: 'https://env.example.com:3001',
          siteUrl: 'https://blog.example.com'
        })
      ).toBe('https://env.example.com:3001')
    })

    it('都未配置时按站点访问地址推导（域名 + 默认端口）', () => {
      expect(resolveMonitorUrl('prometheus', { siteUrl: 'https://blog.example.com' })).toBe(
        'https://blog.example.com:9090'
      )
      expect(resolveMonitorUrl('grafana', { siteUrl: 'https://blog.example.com/' })).toBe(
        'https://blog.example.com:3001'
      )
    })

    it('站点访问地址带路径时只取协议与主机名', () => {
      expect(resolveMonitorUrl('prometheus', { siteUrl: 'https://example.com/blog' })).toBe(
        'https://example.com:9090'
      )
    })

    it('站点访问地址缺失或非法时回退当前访问域名', () => {
      expect(
        resolveMonitorUrl('grafana', {
          siteUrl: '',
          location: { protocol: 'http:', hostname: '192.168.1.10' }
        })
      ).toBe('http://192.168.1.10:3001')

      expect(
        resolveMonitorUrl('prometheus', {
          siteUrl: 'not a url',
          location: { protocol: 'https:', hostname: 'admin.example.com' }
        })
      ).toBe('https://admin.example.com:9090')
    })

    it('Actuator 默认走同源相对路径（跟随当前域名与反代）', () => {
      expect(resolveMonitorUrl('actuator', { siteUrl: 'https://blog.example.com' })).toBe(
        ACTUATOR_DEFAULT_PATH
      )
    })

    it('Actuator 也支持显式配置或环境变量覆盖', () => {
      expect(
        resolveMonitorUrl('actuator', { configured: 'https://ops.example.com/actuator' })
      ).toBe('https://ops.example.com/actuator')
      expect(resolveMonitorUrl('actuator', { envUrl: '/custom-actuator' })).toBe('/custom-actuator')
    })

    it('完全无法推导时返回空串（由界面提示去配置）', () => {
      expect(resolveMonitorUrl('prometheus', { siteUrl: '', location: null })).toBe('')
    })
  })

  describe('monitorUrlPort', () => {
    it('应返回地址中的显式端口', () => {
      expect(monitorUrlPort('https://monitor.example.com:8443', 'prometheus')).toBe(8443)
    })

    it('没有端口时返回默认端口', () => {
      expect(monitorUrlPort('https://monitor.example.com', 'prometheus')).toBe(
        MONITOR_DEFAULT_PORTS.prometheus
      )
      expect(monitorUrlPort('/manage/actuator', 'actuator')).toBe(0)
    })
  })

  describe('resolveActuatorEndpointUrl', () => {
    it('应该把容器内地址转换成当前域名的相对入口地址', () => {
      expect(
        resolveActuatorEndpointUrl(
          '/manage/actuator',
          'http://zhi-admin:8080/manage/actuator/health'
        )
      ).toBe(`${window.location.origin}/manage/actuator/health`)
    })

    it('配置了对外域名时应该接到该域名上（不再出现 localhost/容器名）', () => {
      expect(
        resolveActuatorEndpointUrl(
          'https://ops.example.com/actuator',
          'http://zhi-admin:8080/manage/actuator/metrics/jvm.memory.used'
        )
      ).toBe('https://ops.example.com/actuator/metrics/jvm.memory.used')
    })

    it('href 已是相对路径时也应该正确处理', () => {
      expect(resolveActuatorEndpointUrl('/manage/actuator', '/manage/actuator/info')).toBe(
        `${window.location.origin}/manage/actuator/info`
      )
    })

    it('href 不含入口前缀时应该原样保留其路径', () => {
      expect(resolveActuatorEndpointUrl('/manage/actuator', '/health')).toBe(
        `${window.location.origin}/manage/actuator/health`
      )
    })

    it('入口地址为空时使用默认同源路径', () => {
      expect(resolveActuatorEndpointUrl('', 'http://zhi-admin:8080/manage/actuator/health')).toBe(
        `${window.location.origin}${ACTUATOR_DEFAULT_PATH}/health`
      )
    })
  })
})
