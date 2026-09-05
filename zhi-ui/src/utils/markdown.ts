import { marked } from 'marked'

/**
 * 将 Markdown 源码渲染为 HTML（供管理端预览/双写）
 */
export function renderMarkdown(markdown?: string | null): string {
  if (!markdown) return ''
  try {
    const result = marked.parse(markdown)
    return typeof result === 'string' ? result : String(result)
  } catch {
    return markdown
  }
}
