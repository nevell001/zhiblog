import { describe, expect, it, vi } from 'vitest'
import { highlightCodeBlocks } from './highlight'

const createContainer = (html: string): HTMLElement => {
  const container = document.createElement('div')
  container.innerHTML = html
  document.body.appendChild(container)
  return container
}

describe('highlightCodeBlocks', () => {
  it('对已注册语言的代码块上色', () => {
    const container = createContainer(
      '<pre><code class="language-javascript">const a = 1</code></pre>'
    )

    highlightCodeBlocks(container)

    const code = container.querySelector('pre code') as HTMLElement
    expect(code.classList.contains('hljs')).toBe(true)
    expect(code.dataset.highlighted).toBe('yes')
    expect(code.querySelector('.hljs-keyword')?.textContent).toBe('const')
    container.remove()
  })

  it('语言别名（language-js）同样命中', () => {
    const container = createContainer('<pre><code class="language-js">const a = 1</code></pre>')

    highlightCodeBlocks(container)

    expect(container.querySelector('pre code')?.classList.contains('hljs')).toBe(true)
    container.remove()
  })

  it('幂等：重复调用不重复处理已高亮的块', () => {
    const container = createContainer(
      '<pre><code class="language-javascript">const a = 1</code></pre>'
    )

    highlightCodeBlocks(container)
    const afterFirst = container.innerHTML
    highlightCodeBlocks(container)

    expect(container.innerHTML).toBe(afterFirst)
    container.remove()
  })

  it('未注册的语言原样保留，且不触发 hljs 的控制台告警', () => {
    const warnSpy = vi.spyOn(console, 'warn').mockImplementation(() => {})
    const container = createContainer('<pre><code class="language-rust">fn main() {}</code></pre>')

    highlightCodeBlocks(container)

    const code = container.querySelector('pre code') as HTMLElement
    expect(code.classList.contains('hljs')).toBe(false)
    expect(code.innerHTML).toBe('fn main() {}')
    // hljs 对未注册语言默认 warn + 提示回退，本工具应直接跳过（无异响）
    expect(warnSpy).not.toHaveBeenCalled()
    warnSpy.mockRestore()
    container.remove()
  })

  it('未标注语言的代码块不做自动猜测', () => {
    const container = createContainer('<pre><code>print("hello")\n</code></pre>')

    highlightCodeBlocks(container)

    const code = container.querySelector('pre code') as HTMLElement
    expect(code.classList.contains('hljs')).toBe(false)
    expect(code.querySelector('span')).toBeNull()
    container.remove()
  })

  it('未标注语言的块即使内容看起来像代码也被跳过（确定性优先）', () => {
    const container = createContainer('<pre><code>public class A {}</code></pre>')

    highlightCodeBlocks(container)

    expect(container.querySelector('pre code')?.classList.contains('hljs')).toBe(false)
    container.remove()
  })

  it('root 为空时安全返回', () => {
    expect(() => highlightCodeBlocks(null)).not.toThrow()
    expect(() => highlightCodeBlocks(undefined)).not.toThrow()
  })

  it('同时处理多个代码块，互不影响', () => {
    const container = createContainer(
      '<pre><code class="language-java">public class A {}</code></pre>' +
        '<pre><code class="language-rust">fn main() {}</code></pre>' +
        '<pre><code class="language-sql">SELECT 1</code></pre>'
    )

    highlightCodeBlocks(container)

    const blocks = container.querySelectorAll('pre code')
    expect(blocks[0].classList.contains('hljs')).toBe(true)
    expect(blocks[1].classList.contains('hljs')).toBe(false)
    expect(blocks[2].classList.contains('hljs')).toBe(true)
    container.remove()
  })
})
