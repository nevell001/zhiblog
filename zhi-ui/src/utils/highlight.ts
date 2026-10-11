/**
 * 文章代码块语法高亮（highlight.js，按需注册语言）。
 *
 * 只注册常用语言；未标注语言或语言未注册的代码块保持原样展示，
 * 避免 highlightAuto 对中文注释/配置片段做出风格不稳定的猜测。
 * 主题为 github-dark：文章详情页的 pre 在两种模式下都是深色底
 * （--mo-n900 色阶），配套的「透明背景」覆写在 detail.vue 内。
 */
import hljs from 'highlight.js/lib/core'
import bash from 'highlight.js/lib/languages/bash'
import css from 'highlight.js/lib/languages/css'
import dockerfile from 'highlight.js/lib/languages/dockerfile'
import go from 'highlight.js/lib/languages/go'
import ini from 'highlight.js/lib/languages/ini'
import java from 'highlight.js/lib/languages/java'
import javascript from 'highlight.js/lib/languages/javascript'
import json from 'highlight.js/lib/languages/json'
import markdown from 'highlight.js/lib/languages/markdown'
import plaintext from 'highlight.js/lib/languages/plaintext'
import python from 'highlight.js/lib/languages/python'
import sql from 'highlight.js/lib/languages/sql'
import typescript from 'highlight.js/lib/languages/typescript'
import xml from 'highlight.js/lib/languages/xml'
import yaml from 'highlight.js/lib/languages/yaml'
import 'highlight.js/styles/github-dark.css'

hljs.registerLanguage('bash', bash)
hljs.registerLanguage('css', css)
hljs.registerLanguage('dockerfile', dockerfile)
hljs.registerLanguage('go', go)
hljs.registerLanguage('ini', ini)
hljs.registerLanguage('java', java)
hljs.registerLanguage('javascript', javascript)
hljs.registerLanguage('json', json)
hljs.registerLanguage('markdown', markdown)
hljs.registerLanguage('plaintext', plaintext)
hljs.registerLanguage('python', python)
hljs.registerLanguage('sql', sql)
hljs.registerLanguage('typescript', typescript)
hljs.registerLanguage('xml', xml)
hljs.registerLanguage('yaml', yaml)

/**
 * 高亮容器内所有「已标注受支持语言」的代码块（pre > code.language-*）。
 * 幂等：已高亮的块（hljs 会写 data-highlighted）直接跳过。
 */
export function highlightCodeBlocks(
  root: Element | Document | DocumentFragment | ShadowRoot | null | undefined
): void {
  if (!root) return
  root.querySelectorAll<HTMLElement>('pre code').forEach(el => {
    if (el.dataset.highlighted) return
    const language = /(?:^|\s)language-([\w-]+)/.exec(el.className)?.[1]
    if (!language || !hljs.getLanguage(language)) return
    hljs.highlightElement(el)
  })
}
