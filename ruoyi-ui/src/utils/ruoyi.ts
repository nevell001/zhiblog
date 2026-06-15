/**
 * 通用js方法封装处理
 * Copyright (c) 2025-2026 Nevell
 */

interface TreeNode {
  [key: string]: any
  children?: TreeNode[]
}

interface DateRangeParams {
  [key: string]: any
  params?: Record<string, any>
}

interface DictData {
  value: string | number
  label: string
}

// 日期格式化
export function parseTime(time?: string | Date | number, pattern?: string): string | null {
  if (arguments.length === 0 || !time) {
    return null
  }
  const format = pattern || '{y}-{m}-{d} {h}:{i}:{s}'
  let date: Date
  if (typeof time === 'object') {
    date = time as Date
  } else {
    if (typeof time === 'string' && /^[0-9]+$/.test(time)) {
      time = parseInt(time as string)
    } else if (typeof time === 'string') {
      time = (time as string)
        .replace(new RegExp(/-/gm), '/')
        .replace('T', ' ')
        .replace(new RegExp(/\.[\d]{3}/gm), '')
    }
    if (typeof time === 'number' && time.toString().length === 10) {
      time = time * 1000
    }
    date = new Date(time)
  }
  const formatObj: Record<string, number> = {
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    a: date.getDay()
  }
  const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key): string => {
    const value = formatObj[key]
    // Note: getDay() returns 0 on Sunday
    if (key === 'a') {
      return ['日', '一', '二', '三', '四', '五', '六'][value]
    }
    if (result.length > 0 && value < 10) {
      return '0' + value
    }
    return String(value || 0)
  })
  return time_str
}

// 表单重置
export function resetForm(refName: string): void {
  if (this && (this as any).$refs && (this as any).$refs[refName]) {
    ;(this as any).$refs[refName].resetFields()
  }
}

// 添加日期范围
export function addDateRange(
  params: DateRangeParams,
  dateRange: string[],
  propName?: string
): DateRangeParams {
  const search = params
  search.params =
    typeof search.params === 'object' && search.params !== null && !Array.isArray(search.params)
      ? search.params
      : {}
  dateRange = Array.isArray(dateRange) ? dateRange : []
  if (typeof propName === 'undefined') {
    search.params['beginTime'] = dateRange[0]
    search.params['endTime'] = dateRange[1]
  } else {
    search.params['begin' + propName] = dateRange[0]
    search.params['end' + propName] = dateRange[1]
  }
  return search
}

// 回显数据字典
export function selectDictLabel(datas: DictData[], value: string | number): string {
  if (value === undefined) {
    return ''
  }
  const actions: string[] = []
  Object.keys(datas).some(key => {
    if (datas[key].value == '' + value) {
      actions.push(datas[key].label)
      return true
    }
    return false
  })
  if (actions.length === 0) {
    actions.push(value as string)
  }
  return actions.join('')
}

// 回显数据字典（字符串、数组）
export function selectDictLabels(
  datas: DictData[],
  value: string | string[],
  separator?: string
): string {
  if (value === undefined || (typeof value === 'string' && value.length === 0)) {
    return ''
  }
  const currentSeparator = separator === undefined ? ',' : separator
  if (Array.isArray(value)) {
    value = value.join(currentSeparator)
  }
  const actions: string[] = []
  const temp = (value as string).split(currentSeparator)
  Object.keys((value as string).split(currentSeparator)).some(val => {
    let match = false
    Object.keys(datas).some(key => {
      if (datas[key].value == '' + temp[val]) {
        actions.push(datas[key].label + currentSeparator)
        match = true
      }
      return false
    })
    if (!match) {
      actions.push(temp[val] + currentSeparator)
    }
    return false
  })
  return actions.join('').substring(0, actions.join('').length - 1)
}

// 字符串格式化(%s )
export function sprintf(str: string, ...args: any[]): string {
  let i = 0
  str = str.replace(/%s/g, function () {
    const arg = args[i++]
    if (typeof arg === 'undefined') {
      return ''
    }
    return arg
  })
  return str
}

// 转换字符串，undefined,null等转化为""
export function parseStrEmpty(str: number | string | null | undefined): string {
  if (!str || str == 'undefined' || str == 'null') {
    return ''
  }
  return String(str)
}

// 数据合并
export function mergeRecursive(source: any, target: any): any {
  for (const p in target) {
    try {
      if (target[p].constructor == Object) {
        source[p] = mergeRecursive(source[p], target[p])
      } else {
        source[p] = target[p]
      }
    } catch (e) {
      source[p] = target[p]
    }
  }
  return source
}

/**
 * 构造树型结构数据
 * @param data 数据源
 * @param id id字段 默认 'id'
 * @param parentId 父节点字段 默认 'parentId'
 * @param children 孩子节点字段 默认 'children'
 */
export function handleTree(
  data: TreeNode[],
  id?: string,
  parentId?: string,
  children?: string
): TreeNode[] {
  const config = {
    id: id || 'id',
    parentId: parentId || 'parentId',
    childrenList: children || 'children'
  }

  const childrenListMap: Record<string, TreeNode> = {}
  const tree: TreeNode[] = []
  for (const d of data) {
    const nodeId = d[config.id]
    childrenListMap[nodeId] = d
    if (!d[config.childrenList]) {
      d[config.childrenList] = []
    }
  }

  for (const d of data) {
    const parentNodeId = d[config.parentId]
    const parentObj = childrenListMap[parentNodeId]
    if (!parentObj) {
      tree.push(d)
    } else {
      parentObj[config.childrenList]!.push(d)
    }
  }
  return tree
}

/**
 * 参数处理
 * @param params  参数
 */
export function tansParams(params: Record<string, any>): string {
  let result = ''
  for (const propName of Object.keys(params)) {
    const value = params[propName]
    const part = encodeURIComponent(propName) + '='
    if (value !== null && value !== '' && typeof value !== 'undefined') {
      if (typeof value === 'object') {
        for (const key of Object.keys(value)) {
          if (value[key] !== null && value[key] !== '' && typeof value[key] !== 'undefined') {
            const params = propName + '[' + key + ']'
            const subPart = encodeURIComponent(params) + '='
            result += subPart + encodeURIComponent(value[key]) + '&'
          }
        }
      } else {
        result += part + encodeURIComponent(value) + '&'
      }
    }
  }
  return result
}

// 返回项目路径
export function getNormalPath(p?: string): string | undefined {
  if (!p || p.length === 0 || p == 'undefined') {
    return p
  }
  const res = p.replace(/\/+/g, '/')
  if (res[res.length - 1] === '/') {
    return res.slice(0, res.length - 1)
  }
  return res
}

// 验证是否为blob格式
export function blobValidate(data: Blob): boolean {
  return data.type !== 'application/json'
}
