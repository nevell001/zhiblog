// 博客前台 SEO 工具：动态设置 document.title / description / keywords / canonical / OG

export interface SeoData {
  title?: string
  description?: string
  keywords?: string
  /** 绝对地址，例如文章页完整 URL；不传则移除 canonical */
  canonical?: string
}

function findMeta(name: string, attribute = 'name'): HTMLMetaElement | null {
  return document.head.querySelector(`meta[${attribute}="${name}"]`)
}

function ensureMeta(name: string, attribute = 'name'): HTMLMetaElement {
  const existing = findMeta(name, attribute)
  if (existing) return existing
  const meta = document.createElement('meta')
  meta.setAttribute(attribute, name)
  document.head.appendChild(meta)
  return meta
}

function setMetaContent(name: string, content?: string, attribute = 'name') {
  if (!content) {
    const meta = findMeta(name, attribute)
    if (meta) meta.remove()
    return
  }
  ensureMeta(name, attribute).setAttribute('content', content)
}

/**
 * 应用页面 SEO 信息（title/description/keywords/canonical/OG）
 */
export function applySeo(data: SeoData) {
  if (data.title) {
    document.title = data.title
  }
  setMetaContent('description', data.description || '')
  setMetaContent('keywords', data.keywords || '')

  // Open Graph
  setMetaContent('og:title', data.title || document.title, 'property')
  setMetaContent('og:description', data.description || '', 'property')
  setMetaContent('og:type', 'website', 'property')
  if (data.canonical) {
    setMetaContent('og:url', data.canonical, 'property')
  }

  // Canonical
  const existing = document.head.querySelector('link[rel="canonical"]') as HTMLLinkElement | null
  if (data.canonical) {
    if (existing) {
      existing.setAttribute('href', data.canonical)
    } else {
      const link = document.createElement('link')
      link.setAttribute('rel', 'canonical')
      link.setAttribute('href', data.canonical)
      document.head.appendChild(link)
    }
  } else if (existing) {
    existing.remove()
  }
}

/**
 * 生成当前页面的绝对 URL（不含 query/hash），用于 canonical
 */
export function canonicalUrl(): string {
  return window.location.origin + window.location.pathname
}
