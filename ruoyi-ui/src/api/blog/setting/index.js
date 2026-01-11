import request from '@/utils/request'

// 查询博客设置列表
export function listSetting(query) {
  return request({
    url: '/system/setting/list',
    method: 'get',
    params: query
  })
}

// 根据设置键获取设置值
export function getSetting(settingKey) {
  return request({
    url: `/system/setting/value/${settingKey}`,
    method: 'get'
  })
}

// 根据设置键更新设置值
export function updateSettingValueByKey(settingKey, settingValue) {
  console.log('执行updateSettingValueByKey请求:', {
    settingKey: settingKey,
    settingValueLength: settingValue ? settingValue.length : 0,
    envBaseApi: import.meta.env.VITE_APP_BASE_API
  })

  // 将参数封装为BlogSetting对象格式，以匹配后端API要求
  const blogSetting = {
    settingKey: settingKey,
    settingValue: settingValue
  }

  // 尝试多种方法，确保兼容性
  return request({
    url: '/system/setting/updateByKey',
    method: 'put', // 使用PUT方法，与后端注解匹配
    headers: {
      'Content-Type': 'application/json',
      isToken: true
    },
    data: blogSetting,
    timeout: 15000
  }).catch(error => {
    console.error('更新设置失败（PUT方法），尝试POST方法:', error)

    // 如果PUT方法失败，尝试POST方法
    return request({
      url: '/system/setting/updateByKey',
      method: 'post', // 备用POST方法
      headers: {
        'Content-Type': 'application/json',
        isToken: true
      },
      data: blogSetting,
      timeout: 15000
    }).catch(postError => {
      console.error('更新设置失败（POST方法），详细错误:', postError)

      // 抛出包含更多上下文信息的错误
      const enhancedError = new Error(
        `更新设置[${settingKey}]失败: ${postError.message || '未知错误'}`
      )
      enhancedError.originalError = postError
      enhancedError.settingKey = settingKey
      throw enhancedError
    })
  })
}

// 新增博客设置
export function addSetting(data) {
  return request({
    url: '/system/setting',
    method: 'post',
    data: data
  })
}

// 修改博客设置
export function updateSetting(data) {
  return request({
    url: '/system/setting',
    method: 'put',
    data: data
  })
}

// 删除博客设置
export function delSetting(ids) {
  return request({
    url: `/system/setting/${ids}`,
    method: 'delete'
  })
}
