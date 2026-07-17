import type { App } from 'vue'
import { provideGlobalConfig } from 'element-plus/es/components/config-provider/src/hooks/use-global-config'
import type { ConfigProviderContext } from 'element-plus/es/components/config-provider/src/constants'

export default function installElementPlus(app: App, options: ConfigProviderContext): void {
  provideGlobalConfig(options, app, true)
}
