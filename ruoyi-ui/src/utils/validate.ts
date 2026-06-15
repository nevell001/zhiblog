/**
 * 路径匹配器
 * @param pattern 匹配模式
 * @param path 路径
 * @returns 是否匹配
 */
export function isPathMatch(pattern: string, path: string): boolean {
  const regexPattern = pattern
    .replace(/\//g, '\\/')
    .replace(/\*\*/g, '.*')
    .replace(/\*/g, '[^\\/]*')
  const regex = new RegExp(`^${regexPattern}$`)
  return regex.test(path)
}

/**
 * 判断value字符串是否为空
 * @param value 字符串值
 * @returns 是否为空
 */
export function isEmpty(value: any): boolean {
  if (value === null || value === undefined || value === '' || value === 'undefined') {
    return true
  }
  return false
}

/**
 * 判断url是否是http或https
 * @param url URL字符串
 * @returns 是否为HTTP/HTTPS URL
 */
export function isHttp(url: string): boolean {
  return url.indexOf('http://') !== -1 || url.indexOf('https://') !== -1
}

/**
 * 判断path是否为外链
 * @param path 路径
 * @returns 是否为外链
 */
export function isExternal(path: string): boolean {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * 验证用户名
 * @param str 用户名
 * @returns 是否为有效用户名
 */
export function validUsername(str: string): boolean {
  const valid_map = ['admin', 'editor']
  return valid_map.indexOf(str.trim()) >= 0
}

/**
 * 验证URL
 * @param url URL字符串
 * @returns 是否为有效URL
 */
export function validURL(url: string): boolean {
  try {
    const parsedUrl = new URL(url)
    return ['http:', 'https:'].includes(parsedUrl.protocol)
  } catch {
    return false
  }
}

/**
 * 验证小写字母
 * @param str 字符串
 * @returns 是否为小写字母
 */
export function validLowerCase(str: string): boolean {
  const reg = /^[a-z]+$/
  return reg.test(str)
}

/**
 * 验证大写字母
 * @param str 字符串
 * @returns 是否为大写字母
 */
export function validUpperCase(str: string): boolean {
  const reg = /^[A-Z]+$/
  return reg.test(str)
}

/**
 * 验证字母
 * @param str 字符串
 * @returns 是否为字母
 */
export function validAlphabets(str: string): boolean {
  const reg = /^[A-Za-z]+$/
  return reg.test(str)
}

/**
 * 验证邮箱
 * @param email 邮箱地址
 * @returns 是否为有效邮箱
 */
export function validEmail(email: string): boolean {
  const reg =
    /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
  return reg.test(email)
}

/**
 * 判断是否为字符串
 * @param str 任意值
 * @returns 是否为字符串
 */
export function isString(str: any): boolean {
  return typeof str === 'string' || str instanceof String
}

/**
 * 判断是否为数组
 * @param arg 任意值
 * @returns 是否为数组
 */
export function isArray(arg: any): boolean {
  if (typeof Array.isArray === 'undefined') {
    return Object.prototype.toString.call(arg) === '[object Array]'
  }
  return Array.isArray(arg)
}
