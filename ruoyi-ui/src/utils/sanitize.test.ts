import { describe, it, expect, vi } from 'vitest'
import {
  sanitizeHtml,
  sanitizeArticleContent,
  sanitizeComment,
  isSafeUrl,
  createSafeElement
} from './sanitize'

describe('Sanitize Utils 测试', () => {
  describe('sanitizeHtml', () => {
    it('应该导出 sanitizeHtml 函数', () => {
      expect(sanitizeHtml).toBeDefined()
      expect(typeof sanitizeHtml).toBe('function')
    })

    it('应该处理空字符串', () => {
      expect(sanitizeHtml('')).toBe('')
    })

    it('应该处理非字符串输入', () => {
      expect(sanitizeHtml(null as any)).toBe('')
      expect(sanitizeHtml(undefined as any)).toBe('')
    })

    it('应该移除危险标签', () => {
      const input = '<script>alert("xss")</script><p>正常内容</p>'
      const result = sanitizeHtml(input)

      expect(result).toBe('<p>正常内容</p>')
      expect(result).not.toContain('<script>')
      expect(result).not.toContain('alert(')
    })

    it('应该移除危险属性', () => {
      const input = '<div onclick="evil()">内容</div>'
      const result = sanitizeHtml(input)

      expect(result).toBe('<div>内容</div>')
      expect(result).not.toContain('onclick')
    })

    it('应该移除 HTML 注释', () => {
      const input = '<p>内容<!-- comment --></p>'
      const result = sanitizeHtml(input)

      expect(result).toBe('<p>内容</p>')
      expect(result).not.toContain('<!--')
    })

    it('应该保留安全标签', () => {
      const input = '<p><strong>加粗</strong><em>斜体</em><a href="#">链接</a></p>'
      const result = sanitizeHtml(input)

      expect(result).toContain('<strong>')
      expect(result).toContain('<em>')
      expect(result).toContain('<a href="#">')
    })

    it('应该允许 http 协议', () => {
      const input = '<a href="http://example.com">链接</a>'
      const result = sanitizeHtml(input, { ALLOWED_TAGS: ['a'], ALLOWED_ATTR: ['href'] })

      expect(result).toContain('http://')
    })

    it('应该移除 iframe 的 src 属性', () => {
      const input = '<iframe src="javascript:alert(1)"></iframe>'
      const result = sanitizeHtml(input)

      expect(result).not.toContain('src="javascript:')
    })
  })

  describe('sanitizeArticleContent', () => {
    it('应该导出 sanitizeArticleContent 函数', () => {
      expect(sanitizeArticleContent).toBeDefined()
      expect(typeof sanitizeArticleContent).toBe('function')
    })

    it('应该允许更多文章标签', () => {
      const input = '<p><sub>下标</sub><sup>上标</sup><figure>图表</figure><figcaption>说明</figcaption></p>'
      const result = sanitizeArticleContent(input)

      expect(result).toContain('<sub>')
      expect(result).toContain('<sup>')
      expect(result).toContain('<figure>')
      expect(result).toContain('<figcaption>')
    })

    it('应该允许安全的 iframe', () => {
      const input = '<iframe data-safe-iframe="true" src="https://youtube.com/embed"></iframe>'
      const result = sanitizeArticleContent(input)

      expect(result).toContain('data-safe-iframe="true"')
      expect(result).toContain('<iframe')
    })
  })

  describe('sanitizeComment', () => {
    it('应该导出 sanitizeComment 函数', () => {
      expect(sanitizeComment).toBeDefined()
      expect(typeof sanitizeComment).toBe('function')
    })

    it('应该限制评论标签', () => {
      const input = '<p><div>块级</div><br><strong>加粗</strong><em>斜体</em><a href="#">链接</a></p>'
      const result = sanitizeComment(input)

      expect(result).toContain('<strong>')
      expect(result).toContain('<em>')
      expect(result).toContain('<br>')
      expect(result).toContain('<a')
      expect(result).not.toContain('<div>')
    })

    it('应该为链接添加安全属性', () => {
      const input = '<a href="http://example.com">链接</a>'
      const result = sanitizeComment(input)

      expect(result).toContain('target="_blank"')
      expect(result).toContain('rel="noopener noreferrer"')
    })

    it('应该允许 https 链接', () => {
      const input = '<a href="https://example.com">链接</a>'
      const result = sanitizeComment(input)

      expect(result).toContain('href="https://example.com"')
    })
  })

  describe('isSafeUrl', () => {
    it('应该导出 isSafeUrl 函数', () => {
      expect(isSafeUrl).toBeDefined()
      expect(typeof isSafeUrl).toBe('function')
    })

    it('应该验证 http URL', () => {
      expect(isSafeUrl('http://example.com/path')).toBe(true)
    })

    it('应该验证 https URL', () => {
      expect(isSafeUrl('https://example.com/path')).toBe(true)
    })

    it('应该验证 mailto URL', () => {
      expect(isSafeUrl('mailto:test@example.com')).toBe(true)
    })

    it('应该拒绝 javascript 协议', () => {
      expect(isSafeUrl('javascript:alert(1)')).toBe(false)
    })

    it('应该拒绝 data URL', () => {
      expect(isSafeUrl('data:text/html')).toBe(false)
    })

    it('应该处理空字符串', () => {
      expect(isSafeUrl('')).toBe(false)
    })

    it('应该处理相对路径', () => {
      // 相对路径在 isSafeUrl 中会被视为不安全，因为需要绝对URL
      expect(isSafeUrl('/path/to/resource')).toBe(false)
    })
  })

  describe('createSafeElement', () => {
    it('应该导出 createSafeElement 函数', () => {
      expect(createSafeElement).toBeDefined()
      expect(typeof createSafeElement).toBe('function')
    })

    it('应该创建安全元素', () => {
      const element = createSafeElement('p', { class: 'test-class' }, '测试内容')

      expect(element.tagName).toBe('P')
      expect(element.className).toBe('test-class')
      expect(element.textContent).toBe('测试内容')
    })

    it('应该设置 data-* 安全属性', () => {
      const element = createSafeElement('div', { 'data-safe': 'true' }, '内容')

      expect(element.getAttribute('data-safe')).toBe('true')
    })

    it('应该拒绝危险属性', () => {
      const element = createSafeElement('a', { onclick: 'evil()' }, '链接')

      expect(element.getAttribute('onclick')).toBeNull()
      expect(element.textContent).toBe('链接')
    })

    it('应该正确设置内容', () => {
      const element = createSafeElement('p', {}, '第一段 第二段')

      expect(element.textContent).toBe('第一段 第二段')
    })
  })

    it('应该处理空值', () => {
      // 测试 createSafeElement 空属性
      const element = createSafeElement('div', { class: '' }, '内容')
      expect(element.getAttribute('class')).toBe('')
    })
  })
})
