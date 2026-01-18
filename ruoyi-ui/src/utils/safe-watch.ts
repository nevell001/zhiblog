import { watch as vueWatch, watchEffect as vueWatchEffect, WatchOptions, WatchSource } from 'vue'

/**
 * 安全的 watch 函数，用于包装 Vue 的 watch 函数，并在清理函数执行时添加错误处理
 * @param source 监听源
 * @param callback 回调函数
 * @param options 配置选项
 * @returns 清理函数
 */
export function safeWatch<T>(
  source: WatchSource<T> | WatchSource<T>[],
  callback: (value: T, oldValue: T, onCleanup: (cleanupFn: () => void) => void) => void,
  options?: WatchOptions
) {
  // 包装回调函数，添加错误处理
  const wrappedCallback = (value: T, oldValue: T, onCleanup: (cleanupFn: () => void) => void) => {
    // 包装 cleanup 函数，添加错误处理
    const wrappedCleanup = (cleanupFn: () => void) => {
      // 创建一个安全的清理函数
      const safeCleanupFn = () => {
        try {
          cleanupFn()
        } catch (error) {
          console.error('watch 清理函数执行出错:', error)
          console.error('清理函数错误堆栈:', error.stack)
          // 可以在这里添加更多的错误处理逻辑，比如发送错误报告等
        }
      }

      // 调用原始的 onCleanup 函数，传入安全的清理函数
      onCleanup(safeCleanupFn)
    }

    // 执行原始的回调函数
    callback(value, oldValue, wrappedCleanup)
  }

  // 调用 Vue 的原始 watch 函数
  return vueWatch(source, wrappedCallback, options)
}

/**
 * 安全的 watchEffect 函数，用于包装 Vue 的 watchEffect 函数，并在清理函数执行时添加错误处理
 * @param effect 副作用函数
 * @param options 配置选项
 * @returns 清理函数
 */
export function safeWatchEffect(
  effect: (onCleanup: (cleanupFn: () => void) => void) => void,
  options?: WatchOptions
) {
  // 包装副作用函数，添加错误处理
  const wrappedEffect = (onCleanup: (cleanupFn: () => void) => void) => {
    // 包装 cleanup 函数，添加错误处理
    const wrappedCleanup = (cleanupFn: () => void) => {
      // 创建一个安全的清理函数
      const safeCleanupFn = () => {
        try {
          cleanupFn()
        } catch (error) {
          console.error('watchEffect 清理函数执行出错:', error)
          console.error('清理函数错误堆栈:', error.stack)
          // 可以在这里添加更多的错误处理逻辑，比如发送错误报告等
        }
      }

      // 调用原始的 onCleanup 函数，传入安全的清理函数
      onCleanup(safeCleanupFn)
    }

    // 执行原始的副作用函数
    effect(wrappedCleanup)
  }

  // 调用 Vue 的原始 watchEffect 函数
  return vueWatchEffect(wrappedEffect, options)
}
