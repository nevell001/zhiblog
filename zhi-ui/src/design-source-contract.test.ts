import { describe, expect, it } from 'vitest'
import { readFileSync, readdirSync, statSync } from 'node:fs'
import { join } from 'node:path'
import * as epIcons from '@element-plus/icons-vue'

// 源码契约测试（AGENTS.md 允许的唯一例外形态）：它比较的是「模板标记」与「全局样式表 / 图标注册表」两个产物，
// 挂载单个组件覆盖不到——失效的 Element UI 字体图标、解析不到的 EP 图标、永不命中的 scoped 选择器，
// 都是从上游模板整段复制进来、且只在部分页面暴露的。

const SRC = __dirname

const filesUnder = (dir: string, accept: (name: string) => boolean): string[] => {
  if (!statSync(dir, { throwIfNoEntry: false })?.isDirectory()) return []
  return readdirSync(dir).flatMap(name => {
    const full = join(dir, name)
    if (statSync(full).isDirectory()) return filesUnder(full, accept)
    return accept(name) ? [full] : []
  })
}

const vueFiles = filesUnder(SRC, name => name.endsWith('.vue'))
const relative = (file: string) => file.slice(SRC.length + 1)

// TagsView 的 <close class="el-icon-close"> 依赖全局注册的 EP 图标 + 非 scoped 的 .el-icon-close 复位样式；
// FileUpload 零引用（C 批待删）。新增命中必须显式登记在此。
const LEGACY_ICON_ALLOWLIST = [
  'layout/components/TagsView/index.vue',
  'components/FileUpload/index.vue'
]

// svgicon.ts 的 import 名单 = 运行时全局注册的 EP 图标
const globallyRegistered = new Set(
  readFileSync(join(SRC, 'components/SvgIcon/svgicon.ts'), 'utf8')
    .split("from '@element-plus/icons-vue'")[0]
    .split('\n')
    .map(line => line.trim().replace(/,$/, ''))
    .filter(line => /^[A-Z][A-Za-z0-9]*$/.test(line))
)

const kebab = (name: string) => name.replace(/([a-z0-9])([A-Z])/g, '$1-$2').toLowerCase()
const iconByTag = new Map<string, string>()
Object.keys(epIcons)
  .filter(name => name !== 'default')
  .forEach(name => {
    iconByTag.set(kebab(name).toLowerCase(), name)
    iconByTag.set(name.toLowerCase(), name)
  })

describe('设计源码契约', () => {
  it('模板中不得出现 Element UI 2 的字体图标类（Element Plus 下不渲染）', () => {
    const hits = vueFiles
      .filter(file => /class="[^"]*\bel-icon-[a-z][\w-]*\b/.test(readFileSync(file, 'utf8')))
      .map(relative)
      .sort()

    expect(hits).toEqual([...LEGACY_ICON_ALLOWLIST].sort())
  })

  it('模板里的 EP 图标标签必须能解析（全局注册或本文件引入）', () => {
    const unresolved: string[] = []

    vueFiles.forEach(file => {
      const source = readFileSync(file, 'utf8')
      const scriptStart = source.indexOf('<script')
      const template = (scriptStart > 0 ? source.slice(0, scriptStart) : source).replace(
        /<!--[\s\S]*?-->/g,
        ''
      )
      const script = scriptStart > 0 ? source.slice(scriptStart) : ''

      for (const [, tag] of template.matchAll(/<([A-Za-z][A-Za-z0-9-]*)[\s/>]/g)) {
        if (tag.startsWith('el-') || tag.startsWith('El')) continue
        const icon = iconByTag.get(tag.toLowerCase())
        if (!icon) continue
        if (!globallyRegistered.has(icon) && !new RegExp(`\\b${icon}\\b`).test(script)) {
          unresolved.push(`${relative(file)}  <${tag}>`)
        }
      }
    })

    expect(unresolved).toEqual([])
  })

  it('scoped 样式不得写 html.* :deep(...)（scope 属性会落到 html 上，永不命中）', () => {
    const dead: string[] = []

    vueFiles.forEach(file => {
      const source = readFileSync(file, 'utf8')
      if (!/<style[^>]*\bscoped\b/.test(source)) return
      source.split('\n').forEach(line => {
        if (/^\s*html(\.[\w-]+)+\s+:deep\(/.test(line))
          dead.push(`${relative(file)}  ${line.trim()}`)
      })
    })

    expect(dead).toEqual([])
  })

  it('全局样式不得用 outline: none 抹掉焦点环', () => {
    const offenders: string[] = []
    const styleFiles = filesUnder(
      join(SRC, 'assets/styles'),
      name => name.endsWith('.scss') || name.endsWith('.css')
    )

    styleFiles.forEach(file => {
      const source = readFileSync(file, 'utf8')
      for (const block of source.matchAll(/([^{}]*:focus[^{}]*)\{([^{}]*)\}/g)) {
        if (/outline\s*:\s*(none|0)/.test(block[2]))
          offenders.push(`${relative(file)} -> ${block[1].trim()}`)
      }
    })

    expect(offenders).toEqual([])
  })

  it('index.scss 保留 :focus-visible 描边规则', () => {
    const index = readFileSync(join(SRC, 'assets/styles/index.scss'), 'utf8')
    expect(index).toContain(':focus-visible')
    expect(index).toContain('outline: 2px solid')
  })

  // 全局描边一旦命中 Element Plus 控件，就会在圆角输入框内部再套一个直角框，
  // 看起来像「选中后框子变大/变形」。EP 会把 tabindex="0" 放在 .el-input__inner
  // 这类内部元素上，所以只判断类名不够，必须真的拿选择器去匹配这些元素。
  it('全局焦点描边不得命中 Element Plus 控件（自带焦点样式）', () => {
    const index = readFileSync(join(SRC, 'assets/styles/index.scss'), 'utf8').replace(
      /\/\*[\s\S]*?\*\//g,
      ''
    )
    const block = index.match(/([^{}]*:focus-visible[^{}]*)\{[^}]*outline:\s*2px solid/)
    expect(block).not.toBeNull()

    const selectors = block![1]
      .split(',')
      .map(s => s.trim())
      .filter(Boolean)
    expect(selectors.length).toBeGreaterThan(0)

    const epElements = [
      ['input', 'el-input__inner', { tabindex: '0', type: 'text' }],
      ['textarea', 'el-textarea__inner', { tabindex: '0' }],
      ['div', 'el-select__wrapper', { tabindex: '0', role: 'combobox' }],
      ['span', 'el-select__caret el-input__icon', {}],
      ['button', 'el-button', { tabindex: '0' }],
      ['input', 'el-checkbox__original', { tabindex: '0', type: 'checkbox' }]
    ] as const

    const hits = epElements.flatMap(([tag, className, attrs]) => {
      const node = document.createElement(tag)
      node.setAttribute('class', className)
      Object.entries(attrs).forEach(([name, value]) => node.setAttribute(name, value))
      return selectors
        .filter(selector => {
          try {
            return node.matches(selector.replace(/:focus-visible/g, ''))
          } catch {
            return false
          }
        })
        .map(selector => `${selector} -> ${tag}.${className}`)
    })

    expect(hits).toEqual([])
  })
})
