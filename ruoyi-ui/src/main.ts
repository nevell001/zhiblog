import { isIgnorableStartupError, markAppLoaded, renderStartupError } from '@/utils/appLoading'

// Install these before loading the application graph. Static imports in the app
// can fail before bootstrap starts, so the entry module must stay tiny.
window.addEventListener(
  'error',
  event => {
    if (isIgnorableStartupError(event.message)) {
      console.warn('已拦截并忽略 .on() 错误（不影响功能）')
      event.preventDefault()
      return true
    }

    if (
      event.filename &&
      (event.filename.includes('.jpg') ||
        event.filename.includes('.png') ||
        event.filename.includes('.css') ||
        event.filename.includes('.js'))
    ) {
      return true
    }

    if (event.message) {
      console.error('全局错误:', event.message)
    }
  },
  true
)

window.addEventListener('unhandledrejection', event => {
  if (isIgnorableStartupError(event.reason)) {
    console.warn('已拦截 Promise .on() 错误')
    event.preventDefault()
    return true
  }

  console.error('未处理的Promise错误:', event.reason)
})

window.onerror = function (message) {
  if (isIgnorableStartupError(message)) {
    console.warn('已拦截 window.onerror .on() 错误')
    return true
  }
  return false
}

import('./bootstrap').catch(error => {
  console.error('应用入口加载失败:', error)
  renderStartupError(error)
})
