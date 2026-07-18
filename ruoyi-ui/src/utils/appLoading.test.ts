import { describe, expect, it } from 'vitest'
import { isIgnorableStartupError, markAppLoaded, renderStartupError } from './appLoading'

describe('app loading state', () => {
  it('marks the initial page loader as loaded', () => {
    document.body.className = ''
    document.body.innerHTML = '<div id="loader-wrapper"></div>'

    markAppLoaded(document)

    expect(document.body.classList.contains('loaded')).toBe(true)
  })

  it('renders a visible startup error instead of leaving a blank page', () => {
    document.body.className = ''
    document.body.innerHTML = '<div id="app"><div id="loader-wrapper"></div></div>'
    const error = new Error('bootstrap failed')
    error.stack = 'Error: bootstrap failed\n    at bootstrap.ts:87:10'

    renderStartupError(error, document)

    expect(document.body.classList.contains('loaded')).toBe(true)
    expect(document.querySelector('.app-startup-error')?.textContent).toContain('应用启动失败')
    expect(document.querySelector('.app-startup-error')?.textContent).toContain('bootstrap failed')
    expect(document.querySelector('.app-startup-error')?.textContent).toContain('bootstrap.ts')
  })

  it('treats known .on runtime noise as ignorable', () => {
    expect(
      isIgnorableStartupError(new Error("Cannot read properties of undefined (reading 'on')"))
    ).toBe(true)
    expect(isIgnorableStartupError(new Error('bootstrap failed'))).toBe(false)
  })
})
