import { defineStore } from 'pinia'

interface DictState {
  dict: Record<string, any[]>
}

export const useDictStore = defineStore('dict', {
  state: (): DictState => ({
    dict: {}
  }),

  getters: {
    getDict: (state: DictState) => {
      return (dictType: string) => {
        return state.dict[dictType]
      }
    }
  },

  actions: {
    setDict(dict: Record<string, any[]>): void {
      this.dict = { ...this.dict, ...dict }
    },

    removeDict(dictType: string): void {
      delete this.dict[dictType]
    },

    clearDict(): void {
      this.dict = {}
    }
  }
})
