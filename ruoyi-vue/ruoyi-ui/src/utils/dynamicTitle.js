import defaultSettings from '@/settings'
import useSettingsStore from '@/store/modules/settings'
import { getConfigKey } from "@/api/system/config"

/**
 * 动态修改标题和SEO元标签
 */
export function useDynamicTitle() {
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
async function setSeoMetaTags() {
  try {
    // 获取SEO设置
    const [seoTitle, seoDescription, seoKeywords] = await Promise.all([
      getConfigKey('blog.seo.title'),
      getConfigKey('blog.seo.description'),
      getConfigKey('blog.seo.keywords')
    ])
    
    // 设置title（如果有专门的SEO标题）
    if (seoTitle.data && seoTitle.data !== '') {
      const currentTitle = document.title
      const defaultSuffix = ' - ' + defaultSettings.title
      if (currentTitle.includes(defaultSuffix)) {
        document.title = seoTitle.data + defaultSuffix
      } else {
        document.title = seoTitle.data
      }
    }
    
    // 设置description
    if (seoDescription.data && seoDescription.data !== '') {
      let metaDesc = document.querySelector('meta[name="description"]')
      if (!metaDesc) {
        metaDesc = document.createElement('meta')
        metaDesc.name = 'description'
        document.head.appendChild(metaDesc)
      }
      metaDesc.content = seoDescription.data
    }
    
    // 设置keywords
    if (seoKeywords.data && seoKeywords.data !== '') {
      let metaKeywords = document.querySelector('meta[name="keywords"]')
      if (!metaKeywords) {
        metaKeywords = document.createElement('meta')
        metaKeywords.name = 'keywords'
        document.head.appendChild(metaKeywords)
      }
      metaKeywords.content = seoKeywords.data
    }
  } catch (error) {
    console.error('设置SEO元标签失败:', error)
  }
}