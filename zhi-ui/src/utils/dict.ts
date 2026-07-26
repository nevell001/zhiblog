import { ref, toRefs } from 'vue'
import { useDictStore } from '@/stores/dict'
import { getDicts } from '@/api/system/dict/data'

/**
 * 获取字典数据
 * @param args 字典类型
 * @returns 字典数据
 */
export function useDict(...args: string[]) {
  const res = ref<Record<string, any>>({})
  return (() => {
    args.forEach((dictType, _index) => {
      res.value[dictType] = []
      const dicts = useDictStore().getDict(dictType)
      if (dicts) {
        res.value[dictType] = dicts
      } else {
        getDicts(dictType).then(resp => {
          const dictData = Array.isArray(resp) ? resp : (resp as any).data || []
          res.value[dictType] = dictData.map((p: any) => ({
            label: p.dictLabel,
            value: p.dictValue,
            elTagType: p.listClass,
            elTagClass: p.cssClass
          }))
          useDictStore().setDict(dictType, res.value[dictType])
        })
      }
    })
    return toRefs(res.value)
  })()
}
