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
    setDict(dictType: string | Record<string, any[]>, dict?: any[]): void {
      if (typeof dictType === 'string') {
        this.dict = { ...this.dict, [dictType]: dict || [] }
      } else {
        this.dict = { ...this.dict, ...dictType }
      }
    },

    removeDict(dictType: string): void {
      delete this.dict[dictType]
    },

    clearDict(): void {
      this.dict = {}
    },

    cleanDict(): void {
      this.clearDict()
    }
  }
})
