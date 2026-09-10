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
