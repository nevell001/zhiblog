import { parseTime } from './ruoyi'

/**
 * 表格时间格式化
 */
export function formatDate(cellValue: string | Date): string {
  if (cellValue == null || cellValue == '') return ''
  const date = new Date(cellValue)
  const year = date.getFullYear()
  const month = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1
  const day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate()
  const hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours()
  const minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()
  const seconds = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds()
  return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds
}

/**
 * 格式化时间为相对时间
 * @param time 时间戳
 * @param option 格式化选项
 * @returns 格式化后的时间字符串
 */
export function formatTime(time: string | number, option?: string): string {
  if (('' + time).length === 10) {
    time = parseInt(time as string) * 1000
  } else {
    time = +time
  }
  const d = new Date(time)
  const now = Date.now()

  const diff = (now - d.getTime()) / 1000

  if (diff < 30) {
    return '刚刚'
  } else if (diff < 3600) {
    // less 1 hour
    return Math.ceil(diff / 60) + '分钟前'
  } else if (diff < 3600 * 24) {
    return Math.ceil(diff / 3600) + '小时前'
  } else if (diff < 3600 * 24 * 2) {
    return '1天前'
  }
  if (option) {
    return parseTime(time, option) || ''
  } else {
    return (
      d.getMonth() + 1 + '月' + d.getDate() + '日' + d.getHours() + '时' + d.getMinutes() + '分'
    )
  }
}

/**
 * 获取URL查询参数对象
 * @param url URL字符串
 * @returns 查询参数对象
 */
export function getQueryObject(url?: string): Record<string, string> {
  url = url == null ? window.location.href : url
  const search = url.substring(url.lastIndexOf('?') + 1)
  const obj: Record<string, string> = {}
  const reg = /([^?&=]+)=([^?&=]*)/g
  search.replace(reg, (rs, $1, $2) => {
    const name = decodeURIComponent($1)
    let val = decodeURIComponent($2)
    val = String(val)
    obj[name] = val
    return rs
  })
  return obj
}

/**
 * 计算UTF8字符串的字节长度
 * @param str 字符串
 * @returns 字节长度
 */
export function byteLength(str: string): number {
  // returns the byte length of an utf8 string
  let s = str.length
  for (let i = str.length - 1; i >= 0; i--) {
    const code = str.charCodeAt(i)
    if (code > 0x7f && code <= 0x7ff) s++
    else if (code > 0x7ff && code <= 0xffff) s += 2
    if (code >= 0xdc00 && code <= 0xdfff) i--
  }
  return s
}

/**
 * 清理数组中的空值
 * @param actual 原始数组
 * @returns 清理后的数组
 */
export function cleanArray(actual: any[]): any[] {
  const newArray: any[] = []
  for (let i = 0; i < actual.length; i++) {
    if (actual[i]) {
      newArray.push(actual[i])
    }
  }
  return newArray
}

/**
 * 将对象转换为URL参数字符串
 * @param json 对象
 * @returns URL参数字符串
 */
export function param(json: Record<string, any>): string {
  if (!json) return ''
  return cleanArray(
    Object.keys(json).map(key => {
      if (json[key] === undefined) return ''
      return encodeURIComponent(key) + '=' + encodeURIComponent(json[key])
    })
  ).join('&')
}

/**
 * 将URL参数字符串转换为对象
 * @param url URL字符串
 * @returns 参数对象
 */
export function param2Obj(url: string): Record<string, string> {
  const search = decodeURIComponent(url.split('?')[1]).replace(/\+/g, ' ')
  if (!search) {
    return {}
  }
  const obj: Record<string, string> = {}
  const searchArr = search.split('&')
  searchArr.forEach(v => {
    const index = v.indexOf('=')
    if (index !== -1) {
      const name = v.substring(0, index)
      const val = v.substring(index + 1, v.length)
      obj[name] = val
    }
  })
  return obj
}

/**
 * 将HTML转换为纯文本
 * @param val HTML字符串
 * @returns 纯文本
 */
export function html2Text(val: string): string {
  const div = document.createElement('div')
  div.innerHTML = val
  return div.textContent || div.innerText || ''
}

/**
 * 合并两个对象，后一个对象优先
 * @param target 目标对象
 * @param source 源对象
 * @returns 合并后的对象
 */
export function objectMerge(target: any, source: any): any {
  if (typeof target !== 'object') {
    target = {}
  }
  if (Array.isArray(source)) {
    return source.slice()
  }
  Object.keys(source).forEach(property => {
    const sourceProperty = source[property]
    if (typeof sourceProperty === 'object') {
      target[property] = objectMerge(target[property], sourceProperty)
    } else {
      target[property] = sourceProperty
    }
  })
  return target
}

/**
 * 切换元素的class
 * @param element HTML元素
 * @param className 类名
 */
export function toggleClass(element: HTMLElement, className: string): void {
  if (!element || !className) {
    return
  }
  let classString = element.className
  const nameIndex = classString.indexOf(className)
  if (nameIndex === -1) {
    classString += '' + className
  } else {
    classString =
      classString.substr(0, nameIndex) + classString.substr(nameIndex + className.length)
  }
  element.className = classString
}

/**
 * 获取时间
 * @param type 类型 'start' 或 'end'
 * @returns Date对象
 */
export function getTime(type: 'start' | 'end'): Date | number {
  if (type === 'start') {
    return new Date().getTime() - 3600 * 1000 * 24 * 90
  } else {
    return new Date(new Date().toDateString())
  }
}

/**
 * 防抖函数
 * @param func 要执行的函数
 * @param wait 等待时间
 * @param immediate 是否立即执行
 * @returns 防抖后的函数
 */
export function debounce(
  func: (...args: any[]) => any,
  wait: number,
  immediate?: boolean
): (...args: any[]) => any {
  let timeout: number | null
  let args: any[]
  let context: any
  let timestamp: number
  let result: any

  const later = function () {
    // 据上一次触发时间间隔
    const last = +new Date() - timestamp

    // 上次被包装函数被调用时间间隔 last 小于设定时间间隔 wait
    if (last < wait && last > 0) {
      timeout = window.setTimeout(later, wait - last)
    } else {
      timeout = null
      // 如果设定为immediate===true，因为开始边界已经调用过了此处无需调用
      if (!immediate) {
        result = func.apply(context, args)
        if (!timeout) context = args = null
      }
    }
  }

  return function (this: any, ...args: any[]) {
    context = this
    timestamp = +new Date()
    const callNow = immediate && !timeout
    // 如果延时不存在，重新设定延时
    if (!timeout) timeout = window.setTimeout(later, wait)
    if (callNow) {
      result = func.apply(context, args)
      context = args = null
    }

    return result
  }
}

/**
 * 深拷贝（简单版本）
 * 如果需要完美的深拷贝，请使用 lodash 的 _.cloneDeep
 * @param source 源对象
 * @returns 拷贝后的对象
 */
export function deepClone(source: any): any {
  if (!source && typeof source !== 'object') {
    throw new Error('error arguments')
  }
  const targetObj: any = source.constructor === Array ? [] : {}
  Object.keys(source).forEach(keys => {
    if (source[keys] && typeof source[keys] === 'object') {
      targetObj[keys] = deepClone(source[keys])
    } else {
      targetObj[keys] = source[keys]
    }
  })
  return targetObj
}

/**
 * 数组去重
 * @param arr 原始数组
 * @returns 去重后的数组
 */
export function uniqueArr<T>(arr: T[]): T[] {
  return Array.from(new Set(arr))
}

/**
 * 创建唯一字符串
 * @returns 唯一字符串
 */
export function createUniqueString(): string {
  const timestamp = +new Date() + ''
  const randomNum = parseInt((1 + Math.random()) * 65536 + '') + ''
  return (+(randomNum + timestamp)).toString(32)
}

/**
 * 检查元素是否有指定的class
 * @param ele HTML元素
 * @param cls 类名
 * @returns 是否有该class
 */
export function hasClass(ele: HTMLElement, cls: string): boolean {
  return !!ele.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'))
}

/**
 * 添加class到元素
 * @param ele HTML元素
 * @param cls 类名
 */
export function addClass(ele: HTMLElement, cls: string): void {
  if (!hasClass(ele, cls)) ele.className += ' ' + cls
}

/**
 * 从元素移除class
 * @param ele HTML元素
 * @param cls 类名
 */
export function removeClass(ele: HTMLElement, cls: string): void {
  if (hasClass(ele, cls)) {
    const reg = new RegExp('(\\s|^)' + cls + '(\\s|$)')
    ele.className = ele.className.replace(reg, ' ')
  }
}

/**
 * 创建一个map对象
 * @param str 逗号分隔的字符串
 * @param expectsLowerCase 是否转换为小写
 * @returns map函数
 */
export function makeMap(str: string, expectsLowerCase?: boolean): (val: string) => boolean {
  const map: Record<string, boolean> = Object.create(null)
  const list = str.split(',')
  for (let i = 0; i < list.length; i++) {
    map[list[i]] = true
  }
  return expectsLowerCase ? val => map[val.toLowerCase()] : val => map[val]
}

export const exportDefault = 'export default '

interface BeautifierConfig {
  indent_size: string
  indent_char: string
  max_preserve_newlines: string
  preserve_newlines: boolean
  keep_array_indentation: boolean
  break_chained_methods: boolean
  indent_scripts: string
  brace_style: string
  space_before_conditional: boolean
  unescape_strings: boolean
  jslint_happy: boolean
  end_with_newline: boolean
  wrap_line_length: string
  indent_inner_html: boolean
  comma_first: boolean
  e4x: boolean
  indent_empty_lines: boolean
}

export const beautifierConf: Record<string, BeautifierConfig> = {
  html: {
    indent_size: '2',
    indent_char: ' ',
    max_preserve_newlines: '-1',
    preserve_newlines: false,
    keep_array_indentation: false,
    break_chained_methods: false,
    indent_scripts: 'separate',
    brace_style: 'end-expand',
    space_before_conditional: true,
    unescape_strings: false,
    jslint_happy: false,
    end_with_newline: true,
    wrap_line_length: '110',
    indent_inner_html: true,
    comma_first: false,
    e4x: true,
    indent_empty_lines: true
  },
  js: {
    indent_size: '2',
    indent_char: ' ',
    max_preserve_newlines: '-1',
    preserve_newlines: false,
    keep_array_indentation: false,
    break_chained_methods: false,
    indent_scripts: 'normal',
    brace_style: 'end-expand',
    space_before_conditional: true,
    unescape_strings: false,
    jslint_happy: true,
    end_with_newline: true,
    wrap_line_length: '110',
    indent_inner_html: true,
    comma_first: false,
    e4x: true,
    indent_empty_lines: true
  }
}

/**
 * 首字母大写
 * @param str 字符串
 * @returns 首字母大写的字符串
 */
export function titleCase(str: string): string {
  return str.replace(/( |^)[a-z]/g, L => L.toUpperCase())
}

/**
 * 下划线转驼峰
 * @param str 下划线命名的字符串
 * @returns 驼峰命名的字符串
 */
export function camelCase(str: string): string {
  return str.replace(/_[a-z]/g, str1 => str1.substr(-1).toUpperCase())
}

/**
 * 判断是否为数字字符串
 * @param str 字符串
 * @returns 是否为数字字符串
 */
export function isNumberStr(str: string): boolean {
  return /^[+-]?(0|([1-9]\d*))(\.\d+)?$/g.test(str)
}
