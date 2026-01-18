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

const useAppStore = defineStore('app', {
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
    toggleSideBar(withoutAnimation: boolean): boolean {
      if (this.sidebar.hide) {
        return false
      }
      this.sidebar.opened = !this.sidebar.opened
      this.sidebar.withoutAnimation = withoutAnimation
      if (this.sidebar.opened) {
        Cookies.set('sidebarStatus', '1')
      } else {
        Cookies.set('sidebarStatus', '0')
      }
      return true
    },
    closeSideBar({ withoutAnimation }: { withoutAnimation: boolean }): void {
      Cookies.set('sidebarStatus', '0')
      this.sidebar.opened = false
      this.sidebar.withoutAnimation = withoutAnimation
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

export default useAppStore
