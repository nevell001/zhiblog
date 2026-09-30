import { beforeEach, describe, expect, it } from 'vitest'
import { applySeo, canonicalUrl } from './seo'

/**
 * SEO 工具测试。
 *
 * 覆盖两处审计问题：后台「规范链接」（seo_canonical_url）此前完全没人使用，
 * 以及「Robots规则」（seo_robots）改了也没反应。
 */
describe('seo 工具', () => {
  beforeEach(() => {
    document.head.innerHTML = ''
    document.title = ''
  })

  describe('canonicalUrl', () => {
    it('未配置规范链接时按当前访问域名推导', () => {
      expect(canonicalUrl()).toBe(`${window.location.origin}${window.location.pathname}`)
      expect(canonicalUrl('')).toBe(`${window.location.origin}${window.location.pathname}`)
      expect(canonicalUrl(null)).toBe(`${window.location.origin}${window.location.pathname}`)
    })

    it('配置了对外域名时只取它的协议与主机，路径仍用当前页面', () => {
      expect(canonicalUrl('https://www.example.com')).toBe(
        `https://www.example.com${window.location.pathname}`
      )
      expect(canonicalUrl('https://www.example.com/some/base/')).toBe(
        `https://www.example.com${window.location.pathname}`
      )
    })

    it('本机地址或非法值一律忽略（老库里默认是 http://localhost:8080）', () => {
      for (const base of [
        'http://localhost:8080',
        'http://127.0.0.1',
        'ftp://example.com',
        'not a url',
        '   '
      ]) {
        expect(canonicalUrl(base)).toBe(`${window.location.origin}${window.location.pathname}`)
      }
    })
  })

  describe('applySeo', () => {
    it('canonical 与 og:url 同步写入', () => {
      applySeo({ title: '标题', description: '描述', canonical: 'https://example.com/a' })

      const link = document.head.querySelector('link[rel="canonical"]') as HTMLLinkElement
      expect(link?.getAttribute('href')).toBe('https://example.com/a')
      expect(document.head.querySelector('meta[property="og:url"]')?.getAttribute('content')).toBe(
        'https://example.com/a'
      )
    })

    it('robots 指令写入 meta，传空则移除', () => {
      applySeo({ robots: 'noindex,nofollow' })
      expect(document.head.querySelector('meta[name="robots"]')?.getAttribute('content')).toBe(
        'noindex,nofollow'
      )

      applySeo({ robots: '' })
      expect(document.head.querySelector('meta[name="robots"]')).toBeNull()
    })
  })
})
