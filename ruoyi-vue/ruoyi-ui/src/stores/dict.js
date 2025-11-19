import { defineStore } from 'pinia'

// 字典管理
export const useDictStore = defineStore('dict', {
  state: () => ({
    dict: {}
  }),
  getters: {
    getDict: (state) => {
      return (dictType) => {
        return state.dict[dictType]
      }
    }
  },
  actions: {
    setDict(dict) {
      this.dict = { ...this.dict, ...dict }
    },
    removeDict(dictType) {
      delete this.dict[dictType]
    },
    clearDict() {
      this.dict = {}
    }
  }
})