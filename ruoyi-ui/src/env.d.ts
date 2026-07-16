/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_APP_TITLE: string
  readonly VITE_APP_ENV: string
  readonly VITE_APP_BASE_API: string
  readonly VITE_BUILD_COMPRESS: string
  readonly VITE_API_BASE_URL: string
  readonly VITE_GRAFANA_URL: string
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}

declare module '*.vue' {
  import type { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
  export default component
}

declare global {
  interface Window {
    hljs?: any
    on?: any
    webkitRequestAnimationFrame?: typeof requestAnimationFrame
    mozRequestAnimationFrame?: typeof requestAnimationFrame
    testDatabaseConnection?: () => Promise<void>
  }

  interface Document {
    on?: any
  }

  interface Object {
    on?: any
  }
}

declare module '@vue/runtime-core' {
  interface ComponentCustomProperties {
    $tab: any
    $modal: any
    $download: any
    $auth: any
    $prompt: any
    $alert: any
    $refs: Record<string, any>
    $router: any
    $route: any
    useDict: (...args: any[]) => any
    download: (...args: any[]) => any
    parseTime: (...args: any[]) => any
    resetForm: (refName: string) => void
    handleTree: (...args: any[]) => any
    addDateRange: (...args: any[]) => any
    getConfigKey: (...args: any[]) => any
    selectDictLabel: (...args: any[]) => any
    selectDictLabels: (...args: any[]) => any
  }
}

export {}
