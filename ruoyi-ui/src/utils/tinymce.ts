/**
 * TinyMCE 动态加载工具函数
 */

/**
 * 动态导入 TinyMCE，仅在需要时加载
 * @returns TinyMCE 实例或 null（加载失败时）
 */
export const loadTinymce = async () => {
  try {
    // 动态导入核心库
    const tinymceModule = await import('tinymce/tinymce')
    const tinymce = tinymceModule.default

    // 动态导入主题和核心组件
    await Promise.all([
      import('tinymce/themes/silver'),
      import('tinymce/icons/default'),
      import('tinymce/models/dom')
    ])

    // 动态导入基本插件
    await Promise.all([
      import('tinymce/plugins/advlist'),
      import('tinymce/plugins/autolink'),
      import('tinymce/plugins/lists'),
      import('tinymce/plugins/link'),
      import('tinymce/plugins/image'),
      import('tinymce/plugins/charmap'),
      import('tinymce/plugins/preview'),
      import('tinymce/plugins/anchor'),
      import('tinymce/plugins/searchreplace'),
      import('tinymce/plugins/visualblocks'),
      import('tinymce/plugins/code'),
      import('tinymce/plugins/fullscreen'),
      import('tinymce/plugins/insertdatetime'),
      import('tinymce/plugins/media'),
      import('tinymce/plugins/table'),
      import('tinymce/plugins/help'),
      import('tinymce/plugins/wordcount')
    ])

    return tinymce
  } catch (error) {
    console.error('Failed to load TinyMCE:', error)
    return null
  }
}