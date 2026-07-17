import { describe, expect, it } from 'vitest'
import { readFileSync } from 'node:fs'
import { resolve } from 'node:path'

const sourcePath = resolve(process.cwd(), 'vite.config.ts')

describe('Vite dev proxy route boundaries', () => {
  it('should keep public blog page routes out of API proxy matching', () => {
    const source = readFileSync(sourcePath, 'utf-8')

    expect(source).toContain("'^/blog/api/'")
    expect(source).not.toContain("'^/blog/(article|tag|category|setting|comment)/'")
  })

  it('should only generate visualizer report when ANALYZE is enabled', () => {
    const source = readFileSync(sourcePath, 'utf-8')

    expect(source).toContain("process.env.ANALYZE === 'true'")
    expect(source).toContain('analyzePlugins')
    expect(source).toContain('...analyzePlugins')
  })

  it('should keep chart chunks explicit without forcing UI/editor libraries into entry preload', () => {
    const source = readFileSync(sourcePath, 'utf-8')

    expect(source).not.toContain("'element-plus': ['element-plus']")
    expect(source).not.toContain("quill: ['@vueup/vue-quill']")
    expect(source).toContain("'element-icons': ['@element-plus/icons-vue']")
    expect(source).toContain("echarts: ['echarts']")
  })
})
