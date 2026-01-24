import defaultSettings from '@/settings'
import { useSettingsStore } from '@/stores/settings'
import { getConfigKey } from '@/api/system/config'
import type { Config } from '@/api/system/config'

/**
 * 动态修改标题和SEO元标签
 */
export function useDynamicTitle(): void {
  const settingsStore = useSettingsStore()

  // 设置页面标题
  if (settingsStore.dynamicTitle) {
    document.title = settingsStore.title + ' - ' + defaultSettings.title
  } else {
    document.title = defaultSettings.title
  }

  // 异步获取并设置SEO元标签
  setSeoMetaTags()
}

/**
 * 设置SEO元标签
 */
async function setSeoMetaTags(): Promise<void> {
  try {
    // 获取SEO设置
    const [seoTitle, seoDescription, seoKeywords] = await Promise.all([
      getConfigKey('blog.seo.title').catch(
        (): Config => ({ configKey: '', configValue: '', configName: '' })
      ),
      getConfigKey('blog.seo.description').catch(
        (): Config => ({ configKey: '', configValue: '', configName: '' })
      ),
      getConfigKey('blog.seo.keywords').catch(
        (): Config => ({ configKey: '', configValue: '', configName: '' })
      )
    ])

    // 设置title（如果有专门的SEO标题）
    if (seoTitle.configValue && seoTitle.configValue !== '') {
      const currentTitle = document.title
      const defaultSuffix = ' - ' + defaultSettings.title
      if (currentTitle.includes(defaultSuffix)) {
        document.title = seoTitle.configValue + defaultSuffix
      } else {
        document.title = seoTitle.configValue
      }
    }

    // 设置description
    if (seoDescription.configValue && seoDescription.configValue !== '') {
      let metaDesc = document.querySelector('meta[name="description"]')
      if (!metaDesc) {
        metaDesc = document.createElement('meta')
        metaDesc.name = 'description'
        document.head.appendChild(metaDesc)
      }
      ;(metaDesc as HTMLMetaElement).content = seoDescription.configValue
    }

    // 设置keywords
    if (seoKeywords.configValue && seoKeywords.configValue !== '') {
      let metaKeywords = document.querySelector('meta[name="keywords"]')
      if (!metaKeywords) {
        metaKeywords = document.createElement('meta')
        metaKeywords.name = 'keywords'
        document.head.appendChild(metaKeywords)
      }
      ;(metaKeywords as HTMLMetaElement).content = seoKeywords.data
    }
  } catch (error) {
    console.error('设置SEO元标签失败:', error)
  }
}
