import { ElLoading, ElMessage, type LoadingInstance } from 'element-plus'
import axios from 'axios'
import { saveAs } from 'file-saver'
import { getToken } from '@/utils/auth'
import errorCode from '@/utils/errorCode'
import { blobValidate } from '@/utils/ruoyi'

const baseURL = import.meta.env.VITE_APP_BASE_API
let downloadLoadingInstance: LoadingInstance | null = null

export default {
  name(name: string, isDelete = true): void {
    const url =
      baseURL + '/common/download?fileName=' + encodeURIComponent(name) + '&delete=' + isDelete
    axios({
      method: 'get',
      url: url,
      responseType: 'blob',
      headers: { Authorization: 'Bearer ' + getToken() }
    }).then(res => {
      const isBlob = blobValidate(res.data)
      if (isBlob) {
        const blob = new Blob([res.data])
        this.saveAs(blob, decodeURIComponent(res.headers['download-filename']))
      } else {
        this.printErrMsg(res.data)
      }
    })
  },
  resource(resource: string): void {
    const url = baseURL + '/common/download/resource?resource=' + encodeURIComponent(resource)
    axios({
      method: 'get',
      url: url,
      responseType: 'blob',
      headers: { Authorization: 'Bearer ' + getToken() }
    }).then(res => {
      const isBlob = blobValidate(res.data)
      if (isBlob) {
        const blob = new Blob([res.data])
        this.saveAs(blob, decodeURIComponent(res.headers['download-filename']))
      } else {
        this.printErrMsg(res.data)
      }
    })
  },
  zip(url: string, name: string): void {
    const downloadUrl = baseURL + url
    downloadLoadingInstance = ElLoading.service({
      text: '正在下载数据，请稍候',
      background: 'rgba(0, 0, 0, 0.7)'
    })
    axios({
      method: 'get',
      url: downloadUrl,
      responseType: 'blob',
      headers: { Authorization: 'Bearer ' + getToken() }
    })
      .then(res => {
        const isBlob = blobValidate(res.data)
        if (isBlob) {
          const blob = new Blob([res.data], { type: 'application/zip' })
          this.saveAs(blob, name)
        } else {
          this.printErrMsg(res.data)
        }
        downloadLoadingInstance?.close()
      })
      .catch(r => {
        console.error(r)
        ElMessage.error('下载文件出现错误，请联系管理员！')
        downloadLoadingInstance?.close()
      })
  },
  saveAs(text: Blob, name: string, opts?: any): void {
    saveAs(text, name, opts)
  },
  async printErrMsg(data: Blob): Promise<void> {
    const resText = await data.text()
    const rspObj = JSON.parse(resText)
    const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode['default']
    ElMessage.error(errMsg)
  }
}
