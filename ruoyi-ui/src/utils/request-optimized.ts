import axios, {
  AxiosInstance,
  AxiosRequestConfig,
  AxiosResponse,
  InternalAxiosRequestConfig
} from 'axios'
import { ElNotification, ElMessage } from 'element-plus'
import { getToken } from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import { tansParams, blobValidate } from '@/utils/ruoyi'
import cache from '@/plugins/cache'
import { saveAs } from 'file-saver'

interface ErrorObject {
  type: string
  message: string
  details?: any
  timestamp: string
}

interface RequestOptions extends AxiosRequestConfig {
  [key: string]: any
}

interface ValidationError {
  [field: string]: string | string[]
}

interface ErrorResponse {
  data?: {
    message?: string
    msg?: string
    errors?: ValidationError
  }
  message?: string
}

let downloadLoadingInstance: any
// 是否显示重新登录
export const isRelogin = { show: false }

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'

// 创建axios实例
const service: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_API,
  timeout: 30000 // 增加超时时间
})

// 错误类型枚举
export const ErrorTypes = {
  NETWORK_ERROR: 'NETWORK_ERROR',
  TIMEOUT_ERROR: 'TIMEOUT_ERROR',
  AUTH_ERROR: 'AUTH_ERROR',
  PERMISSION_ERROR: 'PERMISSION_ERROR',
  BUSINESS_ERROR: 'BUSINESS_ERROR',
  SERVER_ERROR: 'SERVER_ERROR',
  VALIDATION_ERROR: 'VALIDATION_ERROR'
}

// 错误处理类
class ErrorHandler {
  /**
   * 处理HTTP错误
   * @param error - 错误对象
   * @returns 错误消息
   */
  static handleHttpError(error: any): ErrorObject {
    const { response, message } = error

    if (!response) {
      // 网络错误
      if (message.includes('timeout')) {
        return this.createError(ErrorTypes.TIMEOUT_ERROR, '请求超时，请检查网络连接')
      } else if (message.includes('Network Error')) {
        return this.createError(ErrorTypes.NETWORK_ERROR, '网络连接失败，请检查网络设置')
      }
      return this.createError(ErrorTypes.NETWORK_ERROR, '网络异常，请稍后重试')
    }

    const { status, data } = response

    switch (status) {
      case 400:
        return this.handleBadRequest(data)
      case 401:
        return this.handleUnauthorized()
      case 403:
        return this.handleForbidden()
      case 404:
        return this.createError(ErrorTypes.BUSINESS_ERROR, '请求的资源不存在')
      case 422:
        return this.handleValidationError(data)
      case 500:
        return this.handleServerError(data)
      case 502:
      case 503:
      case 504:
        return this.createError(ErrorTypes.SERVER_ERROR, '服务器暂时不可用，请稍后重试')
      default:
        return this.createError(ErrorTypes.SERVER_ERROR, `请求失败: ${status}`)
    }
  }

  /**
   * 处理400错误
   */
  static handleBadRequest(data: any): ErrorObject {
    const message = data?.message || data?.msg || '请求参数错误'
    return this.createError(ErrorTypes.BUSINESS_ERROR, message)
  }

  /**
   * 处理401错误
   */
  static handleUnauthorized(): ErrorObject {
    // 静默处理401错误，不显示重新登录弹窗
    console.warn('登录状态已过期，但未显示重新登录弹窗')
    return this.createError(ErrorTypes.AUTH_ERROR, '登录状态已过期')
  }

  /**
   * 处理403错误
   */
  static handleForbidden(): ErrorObject {
    return this.createError(ErrorTypes.PERMISSION_ERROR, '权限不足，无法执行此操作')
  }

  /**
   * 处理422验证错误
   */
  static handleValidationError(data: any): ErrorObject {
    const errors = data?.errors || data?.data?.errors

    if (errors && typeof errors === 'object') {
      // 处理字段验证错误
      const errorMessages = Object.entries(errors).map(([field, messages]) => {
        const fieldMessages = Array.isArray(messages) ? messages : [messages]
        return `${field}: ${(fieldMessages as string[]).join(', ')}`
      })

      return this.createError(
        ErrorTypes.VALIDATION_ERROR,
        `数据验证失败: ${errorMessages.join('; ')}`
      )
    }

    const message = data?.message || data?.msg || '数据验证失败'
    return this.createError(ErrorTypes.VALIDATION_ERROR, message)
  }

  /**
   * 处理500错误
   */
  static handleServerError(data: any): ErrorObject {
    const message = data?.message || data?.msg || '服务器内部错误'
    return this.createError(ErrorTypes.SERVER_ERROR, message)
  }

  /**
   * 创建标准化错误对象
   */
  static createError(type: string, message: string, details: any = null): ErrorObject {
    return {
      type,
      message,
      details,
      timestamp: new Date().toISOString()
    }
  }

  /**
   * 显示错误消息
   * @param error - 错误对象
   * @param fallbackMessage - 备用消息
   */
  static showError(error: ErrorObject | string, fallbackMessage: string = '操作失败'): void {
    let errorMessage = fallbackMessage

    if (typeof error === 'object' && error && error.message) {
      errorMessage = error.message
    } else if (typeof error === 'string') {
      errorMessage = error
    }

    const errorObj = typeof error === 'object' ? error : null

    // 根据错误类型选择显示方式
    if (errorObj?.type === ErrorTypes.VALIDATION_ERROR) {
      ElMessage({
        message: errorMessage,
        type: 'warning',
        duration: 5000
      })
    } else if (errorObj?.type === ErrorTypes.PERMISSION_ERROR) {
      ElMessage({
        message: errorMessage,
        type: 'error',
        duration: 5000
      })
    } else {
      ElNotification({
        title: '错误',
        message: errorMessage,
        type: 'error',
        duration: 5000
      })
    }

    // 记录错误日志
    console.error('API Error:', error)
  }
}

// request拦截器
service.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    // 是否需要设置 token
    const isToken = (config.headers as any)?.isToken === false
    // 是否需要防止数据重复提交
    const isRepeatSubmit = (config.headers as any)?.repeatSubmit === false

    // 添加请求ID用于追踪
    ;(config.headers as any)['X-Request-ID'] = generateRequestId()

    if (getToken() && !isToken) {
      ;(config.headers as any).Authorization = 'Bearer ' + getToken()
    }

    // get请求映射params参数
    if (config.method === 'get' && config.params) {
      let url = config.url + '?' + tansParams(config.params)
      url = url.slice(0, -1)
      config.params = {}
      config.url = url
    }

    // 防重复提交检查
    if (!isRepeatSubmit && (config.method === 'post' || config.method === 'put')) {
      const isHtmlContent =
        config.data &&
        typeof config.data === 'object' &&
        (config.data as any).content &&
        (config.data as any).content.includes('<')

      if (isHtmlContent) {
        return config
      }

      const requestObj = {
        url: config.url,
        data: typeof config.data === 'object' ? JSON.stringify(config.data) : config.data,
        time: new Date().getTime()
      }

      const sessionObj = cache.session.getJSON('sessionObj')
      if (sessionObj === undefined || sessionObj === null || sessionObj === '') {
        cache.session.setJSON('sessionObj', requestObj)
      } else {
        const s_url = sessionObj.url
        const s_data = sessionObj.data
        const s_time = sessionObj.time

        if (s_url === requestObj.url && s_data === requestObj.data && s_time === requestObj.time) {
          console.warn('数据正在处理，请勿重复提交')
          return Promise.reject(new Error('数据正在处理，请勿重复提交'))
        } else {
          cache.session.setJSON('sessionObj', requestObj)
        }
      }
    }

    return config
  },
  error => {
    console.error('Request Error:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (res: AxiosResponse) => {
    // 下载文件处理
    if (res.config.responseType === 'blob') {
      return handleBlobResponse(res)
    }

    const code = res.data.code || 200
    const msg = errorCode[code] || res.data.msg || errorCode['default']

    // 处理业务错误
    if (code === 401) {
      const error = ErrorHandler.handleUnauthorized()
      return Promise.reject(error)
    } else if (code !== 200) {
      const error = ErrorHandler.createError(ErrorTypes.BUSINESS_ERROR, msg, res.data)
      ErrorHandler.showError(error)
      return Promise.reject(error)
    }

    // 清除防重复提交标记
    cache.session.setJSON('sessionObj', '')

    return Promise.resolve(res.data)
  },
  error => {
    const handledError = ErrorHandler.handleHttpError(error)

    // 不显示认证错误的重复提示（已经在ErrorHandler中处理）
    if (handledError.type !== ErrorTypes.AUTH_ERROR) {
      ErrorHandler.showError(handledError)
    }

    return Promise.reject(handledError)
  }
)

/**
 * 处理文件下载响应
 */
function handleBlobResponse(res: AxiosResponse): Promise<any> {
  const isLogin = res.headers['authorization']
  if (isLogin) {
    const error = ErrorHandler.handleUnauthorized()
    return Promise.reject(error)
  }

  const isBlob = blobValidate(res.data)
  if (!isBlob) {
    const error = ErrorHandler.createError(ErrorTypes.BUSINESS_ERROR, '文件下载失败')
    ErrorHandler.showError(error)
    return Promise.reject(error)
  }

  // 读取文件名
  const contentDisposition = res.headers['content-disposition']
  const fileName = contentDisposition
    ? decodeURIComponent(contentDisposition.split('=')[1])
    : `download_${Date.now()}`

  // 保存文件
  saveAs(new Blob([res.data]), fileName)
  downloadLoadingInstance?.close()

  return Promise.resolve(res.data)
}

/**
 * 生成请求ID
 */
function generateRequestId(): string {
  return `${Date.now()}-${Math.random().toString(36).substr(2, 9)}`
}

/**
 * 封装的请求方法
 */
export const request = {
  /**
   * GET请求
   * @param url - 请求URL
   * @param params - 请求参数
   * @param config - 请求配置
   */
  get(url: string, params: Record<string, any> = {}, config: RequestOptions = {}) {
    return service({
      url,
      method: 'get',
      params,
      ...config
    })
  },

  /**
   * POST请求
   * @param url - 请求URL
   * @param data - 请求数据
   * @param config - 请求配置
   */
  post(url: string, data: Record<string, any> = {}, config: RequestOptions = {}) {
    return service({
      url,
      method: 'post',
      data,
      ...config
    })
  },

  /**
   * PUT请求
   * @param url - 请求URL
   * @param data - 请求数据
   * @param config - 请求配置
   */
  put(url: string, data: Record<string, any> = {}, config: RequestOptions = {}) {
    return service({
      url,
      method: 'put',
      data,
      ...config
    })
  },

  /**
   * DELETE请求
   * @param url - 请求URL
   * @param config - 请求配置
   */
  delete(url: string, config: RequestOptions = {}) {
    return service({
      url,
      method: 'delete',
      ...config
    })
  },

  /**
   * 文件上传
   * @param url - 请求URL
   * @param formData - 表单数据
   * @param config - 请求配置
   */
  upload(url: string, formData: FormData, config: RequestOptions = {}) {
    return service({
      url,
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data',
        ...config.headers
      },
      ...config
    })
  },

  /**
   * 文件下载
   * @param url - 请求URL
   * @param params - 请求参数
   * @param filename - 文件名
   */
  download(url: string, params: Record<string, any> = {}, filename: string = '') {
    return service({
      url,
      method: 'get',
      params,
      responseType: 'blob',
      headers: {
        isToken: true
      }
    }).then(blob => {
      saveAs(blob, filename || `download_${Date.now()}`)
    })
  }
}

export default request
