import { defineStore } from 'pinia'
import Cookies from 'js-cookie'

interface SidebarState {
  opened: boolean
  withoutAnimation: boolean
  hide: boolean
}

interface AppState {
  sidebar: SidebarState
  device: string
  size: string
}

export const useAppStore = defineStore('app', {
  state: (): AppState => ({
    sidebar: {
      opened: Cookies.get('sidebarStatus') ? !!+Cookies.get('sidebarStatus') : true,
      withoutAnimation: false,
      hide: false
    },
    device: 'desktop',
    size: Cookies.get('size') || 'default'
  }),

  actions: {
    toggleSideBar(): void {
      this.sidebar.opened = !this.sidebar.opened
      this.sidebar.withoutAnimation = false
      if (this.sidebar.opened) {
        Cookies.set('sidebarStatus', 1)
      } else {
        Cookies.set('sidebarStatus', 0)
      }
    },

    closeSideBar(withoutAnimation: { withoutAnimation: boolean }): void {
      Cookies.set('sidebarStatus', 0)
      this.sidebar.opened = false
      this.sidebar.withoutAnimation = withoutAnimation.withoutAnimation
    },

    toggleDevice(device: string): void {
      this.device = device
    },

    setSize(size: string): void {
      this.size = size
      Cookies.set('size', size)
    },

    toggleSideBarHide(status: boolean): void {
      this.sidebar.hide = status
    }
  }
})
