import { describe, expect, it } from 'vitest'
import { readFileSync, readdirSync, statSync } from 'node:fs'
import { join, resolve } from 'node:path'

/**
 * 深色模式下 --mo-* 色阶会被重映射（见 src/assets/styles/theme-dark.css）：
 *   n0 / n50 / n800 / n900  → 背景色（深色下是深色！）
 *   n100 / n200 / n600      → 主文字色
 *   n300 / n400 / n500      → 次要文字色
 *   p300 / p500             → 主题主色（默认主题 = --el-color-primary，Mo-Blog = #a5b4fc）
 *   p600 / p700 / p800 / p900 → 不参与重映射，仍是浅色主题的深靛蓝
 *
 * 因此在 `html.dark` 规则里把「背景色阶」或「未重映射的深色强调色」当作 `color` 用，
 * 深色模式下文字会几乎看不见（曾导致关于页/留言板标题对比度降到 1.0:1）。
 * 本测试锁定这条约定，防止再次写回。
 */
const BG_SCALE_AS_TEXT = /(?:^|[;{\s])color:\s*var\(--mo-n(?:0|50|800|900)\)/
const DARK_ACCENT_AS_TEXT = /(?:^|[;{\s])color:\s*var\(--mo-p(?:600|700|800|900)\)/

interface CssRule {
  selector: string
  body: string
}

function collectVueFiles(dir: string): string[] {
  const found: string[] = []
  for (const entry of readdirSync(dir)) {
    const full = join(dir, entry)
    if (statSync(full).isDirectory()) {
      found.push(...collectVueFiles(full))
    } else if (full.endsWith('.vue')) {
      found.push(full)
    }
  }
  return found
}

/** 极简 CSS 规则解析：忽略注释，能正确处理 @media 嵌套 */
function parseRules(css: string): CssRule[] {
  const rules: CssRule[] = []
  const source = css.replace(/\/\*[\s\S]*?\*\//g, '')
  const stack: string[] = []
  let buffer = ''
  for (const ch of source) {
    if (ch === '{') {
      stack.push(buffer.trim())
      buffer = ''
    } else if (ch === '}') {
      const selector = stack.pop() ?? ''
      if (selector && !selector.startsWith('@')) {
        rules.push({ selector, body: buffer })
      }
      buffer = ''
    } else {
      buffer += ch
    }
  }
  return rules
}

// 覆盖博客前台页面与公共组件（后台编辑器有自己的一套作用域内色阶映射，另行约定）
const blogRoot = resolve(__dirname)
const files = [
  ...collectVueFiles(blogRoot),
  ...collectVueFiles(resolve(__dirname, '../../components'))
].sort()

const violations = (pattern: RegExp) =>
  files.flatMap(file =>
    parseRules(readFileSync(file, 'utf8'))
      .filter(rule => rule.selector.includes('html.dark') && pattern.test(rule.body))
      .map(rule => `${file.replace(`${blogRoot}/`, '')} → ${rule.selector.replace(/\s+/g, ' ')}`)
  )

describe('深色模式 --mo-* 色阶使用约定', () => {
  it('深色规则不得把背景色阶（n0/n50/n800/n900）当作文字颜色', () => {
    expect(violations(BG_SCALE_AS_TEXT)).toEqual([])
  })

  it('深色规则不得用未重映射的深色强调色（p600~p900）作文字颜色', () => {
    expect(violations(DARK_ACCENT_AS_TEXT)).toEqual([])
  })

  it('扫描范围应覆盖博客前台页面与公共组件', () => {
    expect(files.length).toBeGreaterThan(30)
  })
})
