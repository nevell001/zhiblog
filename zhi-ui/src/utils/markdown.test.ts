import { describe, expect, it } from 'vitest'
import { renderMarkdown } from './markdown'

describe('renderMarkdown', () => {
  it('renders markdown source to html', () => {
    const html = renderMarkdown('# Hello\n\n- a\n- b')
    expect(html).toContain('<h1')
    expect(html).toContain('Hello')
    expect(html).toContain('<ul>')
  })

  it('returns empty string for empty input', () => {
    expect(renderMarkdown('')).toBe('')
    expect(renderMarkdown(null)).toBe('')
  })
})
