/**
 * 图片处理工具类
 * 提供图片上传、压缩、格式转换等功能
 */

/**
 * 检查文件是否为图片
 * @param {File} file 文件对象
 * @returns {boolean}
 */
export function isImageFile(file) {
  if (!file) return false
  const type = file.type
  return type && type.startsWith('image/')
}

/**
 * 获取图片格式
 * @param {File} file 文件对象
 * @returns {string}
 */
export function getImageFormat(file) {
  if (!file) return ''
  const type = file.type
  if (type.includes('jpeg') || type.includes('jpg')) return 'jpg'
  if (type.includes('png')) return 'png'
  if (type.includes('gif')) return 'gif'
  if (type.includes('webp')) return 'webp'
  if (type.includes('bmp')) return 'bmp'
  return type.split('/')[1] || ''
}

/**
 * 获取图片文件大小（MB）
 * @param {File} file 文件对象
 * @returns {number}
 */
export function getImageSizeMB(file) {
  if (!file) return 0
  return Math.round(file.size / 1024 / 1024 * 100) / 100
}

/**
 * 格式化文件大小显示
 * @param {number} size 字节数
 * @returns {string}
 */
export function formatFileSize(size) {
  if (!size) return '0 B'
  if (size < 1024) return size + ' B'
  if (size < 1024 * 1024) return Math.round(size / 1024 * 100) / 100 + ' KB'
  if (size < 1024 * 1024 * 1024) return Math.round(size / (1024 * 1024) * 100) / 100 + ' MB'
  return Math.round(size / (1024 * 1024 * 1024) * 100) / 100 + ' GB'
}

/**
 * 生成图片预览URL
 * @param {File} file 文件对象
 * @returns {string}
 */
export function createPreviewUrl(file) {
  if (!file) return ''
  return URL.createObjectURL(file)
}

/**
 * 释放预览URL
 * @param {string} url URL
 */
export function revokePreviewUrl(url) {
  if (url) {
    URL.revokeObjectURL(url)
  }
}

/**
 * 读取图片信息
 * @param {File} file 文件对象
 * @returns {Promise}
 */
export function readImageInfo(file) {
  return new Promise((resolve, reject) => {
    const img = new Image()
    img.onload = () => {
      const info = {
        width: img.width,
        height: img.height,
        aspectRatio: img.width / img.height,
        size: file.size,
        sizeMB: getImageSizeMB(file),
        format: getImageFormat(file),
        name: file.name,
        type: file.type
      }
      resolve(info)
    }
    img.onerror = () => reject(new Error('无法读取图片信息'))
    img.src = createPreviewUrl(file)
  })
}

/**
 * 压缩图片（前端压缩）
 * @param {File} file 文件对象
 * @param {Object} options 压缩选项
 * @returns {Promise<File>}
 */
export function compressImage(file, options = {}) {
  return new Promise((resolve, reject) => {
    const {
      maxWidth = 1920,
      maxHeight = 1080,
      quality = 0.8,
      format = 'image/jpeg'
    } = options

    const canvas = document.createElement('canvas')
    const ctx = canvas.getContext('2d')
    const img = new Image()

    img.onload = () => {
      // 计算压缩后的尺寸
      let { width, height } = calculateCompressedSize(img.width, img.height, maxWidth, maxHeight)

      // 设置canvas尺寸
      canvas.width = width
      canvas.height = height

      // 绘制压缩后的图片
      ctx.drawImage(img, 0, 0, width, height)

      // 转换为Blob
      canvas.toBlob((blob) => {
        if (blob) {
          const compressedFile = new File([blob], file.name, {
            type: format,
            lastModified: Date.now()
          })
          resolve(compressedFile)
        } else {
          reject(new Error('图片压缩失败'))
        }
      }, format, quality)

      // 释放预览URL
      revokePreviewUrl(img.src)
    }

    img.onerror = () => reject(new Error('图片加载失败'))
    img.src = createPreviewUrl(file)
  })
}

/**
 * 计算压缩后的尺寸
 * @param {number} originalWidth 原始宽度
 * @param {number} originalHeight 原始高度
 * @param {number} maxWidth 最大宽度
 * @param {number} maxHeight 最大高度
 * @returns {Object}
 */
function calculateCompressedSize(originalWidth, originalHeight, maxWidth, maxHeight) {
  let width = originalWidth
  let height = originalHeight

  // 按宽度压缩
  if (width > maxWidth) {
    height = (height * maxWidth) / width
    width = maxWidth
  }

  // 按高度压缩
  if (height > maxHeight) {
    width = (width * maxHeight) / height
    height = maxHeight
  }

  return { width: Math.round(width), height: Math.round(height) }
}

/**
 * 创建FormData用于上传
 * @param {File} file 文件对象
 * @param {Object} additionalFields 额外字段
 * @returns {FormData}
 */
export function createUploadFormData(file, additionalFields = {}) {
  const formData = new FormData()
  formData.append('file', file)

  // 添加额外字段
  Object.keys(additionalFields).forEach(key => {
    if (additionalFields[key] !== null && additionalFields[key] !== undefined) {
      formData.append(key, additionalFields[key])
    }
  })

  return formData
}

/**
 * 选择合适的上传接口
 * @param {string} type 上传类型
 * @returns {string}
 */
export function getUploadApi(type) {
  const apiMap = {
    'avatar': '/common/upload/avatar',
    'thumbnail': '/common/upload/thumbnail',
    'article-cover': '/common/upload/article-cover',
    'mobile': '/common/upload/mobile',
    'watermark': '/common/upload/watermark',
    'compressed': '/common/upload/compressed',
    'default': '/common/upload'
  }
  return apiMap[type] || apiMap.default
}

/**
 * 上传图片
 * @param {File} file 文件对象
 * @param {Object} options 上传选项
 * @returns {Promise}
 */
export async function uploadImage(file, options = {}) {
  const {
    type = 'default',
    onProgress,
    watermarkText,
    targetFormat
  } = options

  try {
    // 前端预压缩检查
    let uploadFile = file
    if (options.compress !== false && getImageSizeMB(file) > 2) {
      uploadFile = await compressImage(file, {
        maxWidth: options.maxWidth || 1920,
        maxHeight: options.maxHeight || 1080,
        quality: options.quality || 0.8
      })
    }

    // 创建FormData
    const formData = createUploadFormData(uploadFile, {
      type,
      watermarkText,
      targetFormat
    })

    // 选择上传接口
    const apiUrl = getUploadApi(type)

    // 使用request上传
    const response = await fetch(apiUrl, {
      method: 'POST',
      body: formData,
      // 如果需要上传进度，可以在这里实现
    })

    if (!response.ok) {
      throw new Error(`上传失败: ${response.status} ${response.statusText}`)
    }

    const result = await response.json()

    if (result.code === 200) {
      return {
        success: true,
        data: result,
        originalSize: file.size,
        compressedSize: uploadFile.size,
        compressionRatio: Math.round((1 - uploadFile.size / file.size) * 100)
      }
    } else {
      throw new Error(result.msg || '上传失败')
    }
  } catch (error) {
    return {
      success: false,
      error: error.message
    }
  }
}

/**
 * 批量上传图片
 * @param {File[]} files 文件数组
 * @param {Object} options 上传选项
 * @returns {Promise}
 */
export async function uploadImages(files, options = {}) {
  const { type = 'default', maxConcurrency = 3 } = options
  const results = []

  // 分批上传，避免同时上传过多文件
  for (let i = 0; i < files.length; i += maxConcurrency) {
    const batch = files.slice(i, i + maxConcurrency)
    const batchPromises = batch.map(file => uploadImage(file, { ...options, type }))

    try {
      const batchResults = await Promise.all(batchPromises)
      results.push(...batchResults)
    } catch (error) {
      // 如果批量上传失败，记录错误但继续处理其他批次
      console.error('批量上传失败:', error)
      batch.forEach(() => {
        results.push({ success: false, error: error.message })
      })
    }
  }

  return results
}

/**
 * 检查图片是否需要压缩
 * @param {File} file 文件对象
 * @param {number} threshold 阈值（MB）
 * @returns {boolean}
 */
export function needsCompression(file, threshold = 2) {
  return getImageSizeMB(file) > threshold
}

/**
 * 验证图片文件
 * @param {File} file 文件对象
 * @param {Object} rules 验证规则
 * @returns {Object}
 */
export function validateImage(file, rules = {}) {
  const {
    maxSize = 10, // MB
    allowedTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif', 'image/webp'],
    minWidth = 0,
    minHeight = 0,
    maxWidth = 5000,
    maxHeight = 5000
  } = rules

  const errors = []
  const warnings = []

  // 检查文件类型
  if (!isImageFile(file)) {
    errors.push('请选择有效的图片文件')
    return { valid: false, errors, warnings }
  }

  // 检查文件大小
  const sizeMB = getImageSizeMB(file)
  if (sizeMB > maxSize) {
    errors.push(`文件大小不能超过 ${maxSize}MB，当前为 ${sizeMB}MB`)
  }

  // 检查文件类型
  if (!allowedTypes.includes(file.type)) {
    errors.push(`不支持的图片格式: ${file.type}`)
  }

  return {
    valid: errors.length === 0,
    errors,
    warnings,
    fileInfo: {
      name: file.name,
      size: file.size,
      sizeMB,
      type: file.type,
      format: getImageFormat(file)
    }
  }
}