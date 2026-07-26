import { describe, expect, it } from 'vitest'
import { readFileSync } from 'node:fs'
import { resolve } from 'node:path'

const html = readFileSync(resolve(process.cwd(), 'index.html'), 'utf-8')

describe('index.html loading fallback', () => {
  it('应该在入口脚本未执行时也能超时清理启动遮罩', () => {
    expect(html).toContain('window.__APP_LOADING_FALLBACK__')
    expect(html).toContain("document.body.classList.add('loaded')")
  })
})
