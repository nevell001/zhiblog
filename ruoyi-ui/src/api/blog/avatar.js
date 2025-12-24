import request from '@/utils/request'
import { uploadImage, validateImage } from '@/utils/imageUtils'

/**
 * 上传头像
 * @param {File} file 头像文件
 * @param {Object} options 上传选项
 * @returns {Promise}
 */
export async function uploadAvatar(file, options = {}) {
  try {
    // 验证图片
    const validation = validateImage(file, {
      maxSize: 5, // 头像最大5MB
      allowedTypes: ['image/jpeg', 'image/jpg', 'image/png', 'image/gif', 'image/webp'],
      minWidth: 50,
      minHeight: 50,
      maxWidth: 2000,
      maxHeight: 2000
    })

    if (!validation.valid) {
      throw new Error(validation.errors.join(', '))
    }

    // 创建FormData
    const formData = new FormData()
    formData.append('avatarfile', file)

    // 使用项目中的request工具上传图片，确保使用正确的API路径
    const response = await request({
      url: '/system/user/profile/avatar',
      method: 'post',
      headers: { 'Content-Type': 'multipart/form-data' },
      data: formData
    })

    if (response.code === 200) {
      // 返回正确的URL格式
      return {
        success: true,
        data: {
          url: response.imgUrl, // 直接使用后端返回的URL
          originalSize: file.size,
          compressedSize: file.size, // 这里无法获取压缩后的大小，因为压缩是在后端进行的
          compressionRatio: 0, // 后端压缩比例未知
          fileInfo: validation.fileInfo
        }
      }
    } else {
      throw new Error(response.msg || '头像上传失败')
    }
  } catch (error) {
    console.error('头像上传失败:', error)
    return {
      success: false,
      error: error.message || '头像上传失败'
    }
  }
}

/**
 * 更新用户头像
 * @param {Object} data 头像数据
 * @returns {Promise}
 */
export function updateUserAvatar(data) {
  return request({
    url: '/system/user/avatar',
    method: 'post',
    data: data
  })
}

/**
 * 获取用户头像信息
 * @param {string} userId 用户ID
 * @returns {Promise}
 */
export function getUserAvatar(userId) {
  return request({
    url: `/system/user/avatar/${userId}`,
    method: 'get'
  })
}

/**
 * 删除用户头像
 * @param {string} userId 用户ID
 * @returns {Promise}
 */
export function deleteUserAvatar(userId) {
  return request({
    url: `/system/user/avatar/${userId}`,
    method: 'delete'
  })
}

/**
 * 批量处理头像（用于多用户场景）
 * @param {File[]} files 头像文件数组
 * @param {Object} options 处理选项
 * @returns {Promise}
 */
export async function batchProcessAvatars(files, options = {}) {
  const results = []

  for (let i = 0; i < files.length; i++) {
    try {
      const result = await uploadAvatar(files[i], options)
      results.push({
        index: i,
        success: true,
        data: result.data
      })
    } catch (error) {
      results.push({
        index: i,
        success: false,
        error: error.message
      })
    }
  }

  return results
}

/**
 * 生成头像缩略图（用于不同显示场景）
 * @param {string} avatarUrl 原始头像URL
 * @param {number} size 缩略图尺寸
 * @returns {string} 缩略图URL
 */
export function getAvatarThumbnail(avatarUrl, size = 80) {
  if (!avatarUrl || avatarUrl.startsWith('data:')) {
    return avatarUrl
  }

  // 如果是本地服务器上的图片，添加缩略图参数
  if (avatarUrl.includes('/profile/')) {
    // 使用相对路径，在开发环境通过Vite代理转发
    const separator = avatarUrl.includes('?') ? '&' : '?'
    return `${avatarUrl}${separator}size=${size}`
  }

  return avatarUrl
}

/**
 * 预处理头像URL（优化显示）
 * @param {string} avatarUrl 头像URL
 * @returns {string} 处理后的头像URL
 */
export function processAvatarUrl(avatarUrl) {
  if (!avatarUrl) {
    // 返回默认头像
    return "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='80' height='80' viewBox='0 0 80 80'%3E%3Crect width='80' height='80' fill='%23409EFF' rx='40'/%3E%3Ccircle cx='40' cy='30' r='14' fill='white'/%3E%3Cellipse cx='40' cy='58' rx='20' ry='16' fill='white'/%3E%3C/svg%3E"
  }

  // 处理相对路径 - 这是最常见的情况，对应上传到服务器的头像
  if (avatarUrl.startsWith('/profile/') || avatarUrl.startsWith('/static/')) {
    // 使用相对路径，在开发环境通过Vite代理转发，在生产环境直接访问
    // 添加时间戳防止缓存
    const timestamp = Date.now()
    const separator = avatarUrl.includes('?') ? '&' : '?'
    const finalUrl = avatarUrl + `${separator}_t=${timestamp}`

    // 开发环境下输出调试信息
    if (process.env.NODE_ENV === 'development') {
      console.log('🔗 头像URL处理:', {
        original: avatarUrl,
        timestamp,
        finalUrl,
        note: '使用相对路径，开发环境通过Vite代理转发'
      })
    }

    return finalUrl
  }

  // 其他以 / 开头的相对路径
  if (avatarUrl.startsWith('/')) {
    return avatarUrl
  }

  // 完整的HTTP/HTTPS URL直接返回
  if (avatarUrl.startsWith('http://') || avatarUrl.startsWith('https://')) {
    return avatarUrl
  }

  // Base64格式也可以接受（虽然不推荐用于头像）
  if (avatarUrl.startsWith('data:')) {
    return avatarUrl
  }

  // 其他情况，尝试作为相对路径处理
  return avatarUrl
}

/**
 * 检查头像是否存在
 * @param {string} avatarUrl 头像URL
 * @returns {Promise<boolean>}
 */
export async function checkAvatarExists(avatarUrl) {
  if (!avatarUrl || avatarUrl.startsWith('data:')) {
    return false
  }

  try {
    const response = await fetch(avatarUrl, { method: 'HEAD' })
    return response.ok
  } catch (error) {
    return false
  }
}