// 页面动画指令

// 动画类型映射
const animations = {
  'fade-in-up': {
    enter: [
      { opacity: 0, transform: 'translateY(30px)' },
      { opacity: 1, transform: 'translateY(0)' }
    ],
    options: { duration: 0.6, delay: 0, easing: 'ease-out' }
  },
  'fade-in-down': {
    enter: [
      { opacity: 0, transform: 'translateY(-30px)' },
      { opacity: 1, transform: 'translateY(0)' }
    ],
    options: { duration: 0.6, delay: 0, easing: 'ease-out' }
  },
  'fade-in-left': {
    enter: [
      { opacity: 0, transform: 'translateX(-30px)' },
      { opacity: 1, transform: 'translateX(0)' }
    ],
    options: { duration: 0.6, delay: 0, easing: 'ease-out' }
  },
  'fade-in-right': {
    enter: [
      { opacity: 0, transform: 'translateX(30px)' },
      { opacity: 1, transform: 'translateX(0)' }
    ],
    options: { duration: 0.6, delay: 0, easing: 'ease-out' }
  },
  'scale-in': {
    enter: [
      { opacity: 0, transform: 'scale(0.9)' },
      { opacity: 1, transform: 'scale(1)' }
    ],
    options: { duration: 0.5, delay: 0, easing: 'ease-out' }
  },
  'zoom-in': {
    enter: [
      { opacity: 0, transform: 'scale(0.5)' },
      { opacity: 1, transform: 'scale(1)' }
    ],
    options: { duration: 0.4, delay: 0, easing: 'cubic-bezier(0.34, 1.56, 0.64, 1)' }
  }
}

const animateDirective = (el, binding) => {
  const animationType = binding.value || 'fade-in-up'
  const delay = 0

  // 设置初始状态
  el.style.opacity = '0'
  el.style.transform = animations[animationType]?.enter[0].transform || 'translateY(30px)'

  // 创建观察器
  const observer = new IntersectionObserver(
    entries => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          // 延迟执行动画
          setTimeout(() => {
            const animation = animations[animationType]
            if (animation) {
              const [, to] = animation.enter
              const { duration, easing } = animation.options

              // 应用动画
              el.style.transition = `all ${duration}s ${easing}`
              el.style.opacity = to.opacity
              el.style.transform = to.transform
            }
          }, delay * 1000)

          // 动画执行后停止观察
          observer.unobserve(el)
        }
      })
    },
    {
      threshold: 0.1,
      rootMargin: '0px 0px -50px 0px'
    }
  )

  // 开始观察
  observer.observe(el)

  // 清理函数
  el._animationObserver = observer
}

export default animateDirective
