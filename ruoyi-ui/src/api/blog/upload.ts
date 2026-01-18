import request from '@/utils/request'

/**
 * 通用上传
 */
export function upload(data: FormData): Promise<any> {
  return request({
    url: '/common/upload',
    method: 'post',
    data: data,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

/**
 * 带压缩的图片上传
 */
export function uploadCompressed(data: FormData): Promise<any> {
  return request({
    url: '/common/upload/compressed',
    method: 'post',
    data: data,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

/**
 * 头像上传
 */
export function uploadAvatar(data: FormData): Promise<any> {
  return request({
    url: '/common/upload/avatar',
    method: 'post',
    data: data,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

/**
 * 缩略图上传
 */
export function uploadThumbnail(data: FormData): Promise<any> {
  return request({
    url: '/common/upload/thumbnail',
    method: 'post',
    data: data,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

/**
 * 文章封面图上传
 */
export function uploadArticleCover(data: FormData): Promise<any> {
  return request({
    url: '/common/upload/article-cover',
    method: 'post',
    data: data,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

/**
 * 移动端适配图片上传
 */
export function uploadMobileImage(data: FormData): Promise<any> {
  return request({
    url: '/common/upload/mobile',
    method: 'post',
    data: data,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

/**
 * 带水印图片上传
 */
export function uploadWatermarkImage(data: FormData, watermarkText: string): Promise<any> {
  return request({
    url: '/common/upload/watermark',
    method: 'post',
    data: data,
    params: { watermarkText: watermarkText },
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

/**
 * 图片格式转换
 */
export function convertImageFormat(data: FormData, targetFormat: string): Promise<any> {
  return request({
    url: '/common/convert-format',
    method: 'post',
    data: data,
    params: { targetFormat: targetFormat },
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}
