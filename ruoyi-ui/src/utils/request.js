import axios from 'axios'
import { ElNotification, ElMessageBox, ElMessage, ElLoading } from 'element-plus'
import { getToken } from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import { tansParams, blobValidate } from '@/utils/ruoyi'
import cache from '@/plugins/cache'
import { saveAs } from 'file-saver'
import useUserStore from '@/store/modules/user'

let downloadLoadingInstance
// 是否显示重新登录
export const isRelogin = { show: false }
// 是否正在退出登录
let isLoggingOut = false

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
// 创建axios实例
const service = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  baseURL: import.meta.env.VITE_APP_BASE_API || '',
  // 超时
  timeout: 10000
})

// request拦截器
service.interceptors.request.use(
  config => {
    // 是否需要设置 token
    const isToken = (config.headers || {}).isToken === false
    // 是否需要防止数据重复提交
    const isRepeatSubmit = (config.headers || {}).repeatSubmit === false
    if (getToken() && !isToken) {
      config.headers['Authorization'] = 'Bearer ' + getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
    }
    // get请求映射params参数
    if (config.method === 'get' && config.params) {
      let url = config.url + '?' + tansParams(config.params)
      url = url.slice(0, -1)
      config.params = {}
      config.url = url
    }
    if (!isRepeatSubmit && (config.method === 'post' || config.method === 'put')) {
      // 对于包含HTML内容的请求，完全跳过重复提交检查的所有相关操作
      const isHtmlContent =
        config.data &&
        typeof config.data === 'object' &&
        config.data.content &&
        config.data.content.includes('<')

      // 对于登录和退出请求，跳过重复提交检查
      const isLoginRequest = config.url && config.url.includes('/login')
      const isLogoutRequest = config.url && config.url.includes('/logout')

      if (isHtmlContent || isLoginRequest || isLogoutRequest) {
        // 对于HTML内容、登录或退出请求，直接跳过整个重复提交检查逻辑
        return config
      }

      // 对于非HTML内容，进行完整的重复提交检查
      const requestObj = {
        url: config.url,
        data: typeof config.data === 'object' ? JSON.stringify(config.data) : config.data,
        time: new Date().getTime()
      }

      const requestSize = Object.keys(JSON.stringify(requestObj)).length // 请求数据大小
      const limitSize = 5 * 1024 * 1024 // 限制存放数据5M
      if (requestSize >= limitSize) {
        console.warn(`[${config.url}]: ` + '请求数据大小超出允许的5M限制，无法进行防重复提交验证。')
        return config
      }

      const sessionObj = cache.session.getJSON('sessionObj')
      if (sessionObj === undefined || sessionObj === null || sessionObj === '') {
        cache.session.setJSON('sessionObj', requestObj)
      } else {
        const s_url = sessionObj.url // 请求地址
        const s_data = sessionObj.data // 请求数据
        const s_time = sessionObj.time // 请求时间
        const interval = 1000 // 间隔时间(ms)，小于此时间视为重复提交
        if (
          s_data === requestObj.data &&
          requestObj.time - s_time < interval &&
          s_url === requestObj.url
        ) {
          const message = '数据正在处理，请勿重复提交'
          console.warn(`[${s_url}]: ` + message)
          return Promise.reject(new Error(message))
        } else {
          cache.session.setJSON('sessionObj', requestObj)
        }
      }
    }
    return config
  },
  error => {
    console.log(error)
    Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  res => {
    // 未设置状态码则默认成功状态
    const code = res.data.code || 200
    // 获取错误信息
    const msg = errorCode[code] || res.data.msg || errorCode['default']
    // 二进制数据则直接返回
    if (res.request.responseType === 'blob' || res.request.responseType === 'arraybuffer') {
      return res.data
    }
    if (code === 401) {
      // 检查当前访问的路径是否为博客前台页面
      const currentPath = window.location.pathname
      const isBlogPath =
        currentPath.startsWith('/blog') ||
        currentPath === '/' ||
        currentPath === '/index' ||
        currentPath.startsWith('/blog/article/') ||
        currentPath.startsWith('/blog/category/') ||
        currentPath.startsWith('/blog/tag/')

      // 如果是博客前台页面，不显示登录提示，直接返回数据或空结果
      if (isBlogPath) {
        console.warn('匿名用户访问博客页面，返回空数据或默认数据')
        // 对于博客前台页面，返回空数据而不是强制登录
        return Promise.resolve({ code: 200, data: null, msg: '匿名访问' })
      }

      // 如果正在退出登录，不重复处理401错误
      if (isLoggingOut) {
        console.warn('正在退出登录，忽略401错误')
        return Promise.reject('正在退出登录，忽略401错误')
      }

      // 对于后台管理页面，直接重定向到登录页面
      if (!isRelogin.show) {
        isRelogin.show = true
        // 设置退出登录标志
        isLoggingOut = true
        // 直接调用退出登录并重定向到登录页面，不显示弹窗
        useUserStore()
          .logOut()
          .then(() => {
            // 只在当前路径不是登录页面时才重定向，避免重复导航
            if (window.location.pathname !== '/login') {
              window.location.replace('/login')
            }
            isRelogin.show = false
            // 重置退出登录标志
            isLoggingOut = false
          })
          .catch(() => {
            isRelogin.show = false
            // 重置退出登录标志
            isLoggingOut = false
          })
      }
      return Promise.reject('无效的会话，或者会话已过期，请重新登录。')
    } else if (code === 500) {
      console.error('服务器错误:', {
        url: res.config.url,
        method: res.config.method,
        status: res.status,
        response: res.data
      })
      ElMessage({
        message: `服务器错误: ${msg}`,
        type: 'error',
        duration: 5000
      })
      return Promise.reject(new Error(msg))
    } else if (code === 601) {
      ElMessage({ message: msg, type: 'warning' })
      return Promise.reject(new Error(msg))
    } else if (code !== 200) {
      ElNotification.error({ title: msg })
      return Promise.reject('error')
    } else {
      return Promise.resolve(res.data)
    }
  },
  error => {
    console.log('err' + error)
    let { message } = error
    if (message === 'Network Error') {
      message = '后端接口连接异常'
    } else if (message.includes('timeout')) {
      message = '系统接口请求超时'
    } else if (message.includes('Request failed with status code')) {
      message = '系统接口' + message.substr(message.length - 3) + '异常'
    }
    ElMessage({ message: message, type: 'error', duration: 5 * 1000 })
    return Promise.reject(error)
  }
)

// 通用下载方法
export function download(url, params, filename, config) {
  downloadLoadingInstance = ElLoading.service({
    text: '正在下载数据，请稍候',
    background: 'rgba(0, 0, 0, 0.7)'
  })
  return service
    .post(url, params, {
      transformRequest: [
        params => {
          return tansParams(params)
        }
      ],
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      responseType: 'blob',
      ...config
    })
    .then(async data => {
      const isBlob = blobValidate(data)
      if (isBlob) {
        const blob = new Blob([data])
        saveAs(blob, filename)
      } else {
        const resText = await data.text()
        const rspObj = JSON.parse(resText)
        const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode['default']
        ElMessage.error(errMsg)
      }
      downloadLoadingInstance.close()
    })
    .catch(r => {
      console.error(r)
      ElMessage.error('下载文件出现错误，请联系管理员！')
      downloadLoadingInstance.close()
    })
}

export default service
