// 扩展Math对象添加缓动函数
declare global {
  interface Math {
    easeInOutQuad(t: number, b: number, c: number, d: number): number
  }
}

Math.easeInOutQuad = function (t: number, b: number, c: number, d: number): number {
  t /= d / 2
  if (t < 1) {
    return (c / 2) * t * t + b
  }
  t--
  return (-c / 2) * (t * (t - 2) - 1) + b
}

// requestAnimationFrame for Smart Animating http://goo.gl/sx5sts
const requestAnimFrame =
  window.requestAnimationFrame ||
  window.webkitRequestAnimationFrame ||
  window.mozRequestAnimationFrame ||
  (callback => window.setTimeout(callback, 1000 / 60))

/**
 * Because it's so fucking difficult to detect the scrolling element, just move them all
 * @param amount 滚动位置
 */
function move(amount: number): void {
  const docEl = document.documentElement
  const bodyParentNode = document.body.parentNode as HTMLElement
  const body = document.body

  if (docEl) docEl.scrollTop = amount
  if (bodyParentNode) bodyParentNode.scrollTop = amount
  if (body) body.scrollTop = amount
}

function position(): number {
  const docEl = document.documentElement
  const bodyParentNode = document.body.parentNode as HTMLElement
  const body = document.body

  return docEl?.scrollTop || bodyParentNode?.scrollTop || body?.scrollTop || 0
}

/**
 * 平滑滚动到指定位置
 * @param to 目标位置
 * @param duration 动画持续时间
 * @param callback 回调函数
 */
export function scrollTo(to: number, duration?: number, callback?: () => void): void {
  const start = position()
  const change = to - start
  const increment = 20
  let currentTime = 0
  duration = typeof duration === 'undefined' ? 500 : duration

  const animateScroll = function (): void {
    // increment the time
    currentTime += increment
    // find the value with the quadratic in-out easing function
    const val = Math.easeInOutQuad(currentTime, start, change, duration)
    // move the document.body
    move(val)
    // do the animation unless its over
    if (currentTime < duration) {
      requestAnimFrame(animateScroll)
    } else {
      if (callback && typeof callback === 'function') {
        // the animation is done so lets callback
        callback()
      }
    }
  }
  animateScroll()
}
