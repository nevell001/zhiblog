declare global {
  interface Window {
    __APP_LOADING_FALLBACK__?: number
  }
}

export function markAppLoaded(doc: Document = document): void {
  const fallbackTimer = window.__APP_LOADING_FALLBACK__
  if (fallbackTimer) {
    window.clearTimeout(fallbackTimer)
    window.__APP_LOADING_FALLBACK__ = undefined
  }
  doc.body.classList.add('loaded')
}

export function isIgnorableStartupError(error: unknown): boolean {
  const message =
    error instanceof Error ? error.message : String((error as { message?: unknown })?.message || error || '')

  return (
    message.includes("Cannot read properties of undefined (reading 'on')") ||
    message.includes("Cannot read properties of null (reading 'on')") ||
    message.includes("reading 'on'")
  )
}

export function renderStartupError(error: unknown, doc: Document = document): void {
  markAppLoaded(doc)

  const appRoot = doc.getElementById('app')
  if (!appRoot) return

  const message = error instanceof Error ? error.message : String(error || '未知错误')
  const detailText = error instanceof Error && error.stack ? error.stack : message
  appRoot.innerHTML = ''
  const panel = doc.createElement('div')
  panel.className = 'app-startup-error'
  panel.style.cssText =
    'min-height:100vh;display:flex;align-items:center;justify-content:center;padding:24px;background:#f8fafc;color:#172033;font-family:system-ui,-apple-system,BlinkMacSystemFont,"Segoe UI",sans-serif;'

  const card = doc.createElement('div')
  card.style.cssText =
    'max-width:560px;width:100%;padding:24px;border:1px solid #d9e2ec;border-radius:8px;background:#fff;box-shadow:0 10px 24px rgba(15,23,42,.08);'

  const title = doc.createElement('h1')
  title.style.cssText = 'margin:0 0 12px;font-size:20px;line-height:1.4;'
  title.textContent = '应用启动失败'

  const description = doc.createElement('p')
  description.style.cssText = 'margin:0 0 12px;font-size:14px;line-height:1.7;color:#526173;'
  description.textContent = '前端运行时遇到错误，页面没有正常挂载。请把下面的错误信息发给开发者定位。'

  const detail = doc.createElement('pre')
  detail.style.cssText =
    'margin:0;white-space:pre-wrap;word-break:break-word;font-size:13px;line-height:1.6;color:#9f1239;background:#fff1f2;border-radius:6px;padding:12px;'
  detail.textContent = detailText

  card.append(title, description, detail)
  panel.appendChild(card)
  appRoot.appendChild(panel)
}
