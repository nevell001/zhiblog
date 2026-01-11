import { computed, onMounted, onUnmounted, ref } from 'vue'

export function useDevice() {
  const isMobile = ref(false)
  const isTablet = ref(false)
  const isDesktop = ref(true)

  const updateDevice = () => {
    const width = window.innerWidth || document.documentElement.clientWidth

    if (width < 768) {
      isMobile.value = true
      isTablet.value = false
      isDesktop.value = false
    } else if (width >= 768 && width < 1024) {
      isMobile.value = false
      isTablet.value = true
      isDesktop.value = false
    } else {
      isMobile.value = false
      isTablet.value = false
      isDesktop.value = true
    }
  }

  onMounted(() => {
    updateDevice()
    window.addEventListener('resize', updateDevice)
  })

  onUnmounted(() => {
    window.removeEventListener('resize', updateDevice)
  })

  return {
    isMobile: computed(() => isMobile.value),
    isTablet: computed(() => isTablet.value),
    isDesktop: computed(() => isDesktop.value)
  }
}
