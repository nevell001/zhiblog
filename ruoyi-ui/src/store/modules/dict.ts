import { defineStore } from 'pinia'

interface DictItem {
  key: string
  value: any
}

interface DictState {
  dict: DictItem[]
}

const useDictStore = defineStore('dict', {
  state: (): DictState => ({
    dict: []
  }),
  actions: {
    // 获取字典
    getDict(_key: string): any {
      if (_key === null && _key === '') {
        return null
      }
      try {
        for (let i = 0; i < this.dict.length; i++) {
          if (this.dict[i].key === _key) {
            return this.dict[i].value
          }
        }
      } catch {
        return null
      }
      return null
    },
    // 设置字典
    setDict(_key: string, value: any): void {
      if (_key !== null && _key !== '') {
        this.dict.push({
          key: _key,
          value: value
        })
      }
    },
    // 删除字典
    removeDict(_key: string): boolean {
      let bln = false
      try {
        for (let i = 0; i < this.dict.length; i++) {
          if (this.dict[i].key === _key) {
            this.dict.splice(i, 1)
            return true
          }
        }
      } catch {
        bln = false
      }
      return bln
    },
    // 清空字典
    cleanDict(): void {
      this.dict = []
    },
    // 初始字典
    initDict(): void {}
  }
})

export default useDictStore
