import { useTagsViewStore } from '@/stores/tagsView'
import router from '@/router'
import type { RouteLocationNormalized } from 'vue-router'

interface TabObject {
  name?: string
  path?: string
  query?: any
  meta?: {
    title?: string
  }
}

export default {
  // 刷新当前tab页签
  refreshPage(obj?: TabObject): Promise<any> {
    const { path, query, matched } = router.currentRoute.value
    if (obj === undefined) {
      matched.forEach(m => {
        if (m.components && m.components.default && m.components.default.name) {
          if (!['Layout', 'ParentView'].includes(m.components.default.name)) {
            obj = { name: m.components.default.name, path: path, query: query }
          }
        }
      })
    }
    useTagsViewStore().delCachedView(obj)
    return Promise.resolve().then(() => {
        const { path, query } = obj as TabObject
        return router.replace({
          path: '/redirect' + path,
          query: query
        })
      })
  },
  // 关闭当前tab页签，打开新页签
  closeOpenPage(obj: string): Promise<any> {
    useTagsViewStore().delView(router.currentRoute.value)
    if (obj !== undefined) {
      return router.push(obj)
    }
    return Promise.resolve()
  },
  // 关闭指定tab页签
  closePage(obj?: TabObject): Promise<any> {
    if (obj === undefined) {
      return useTagsViewStore()
        .delView(router.currentRoute.value)
        .then(({ visitedViews }) => {
          const latestView = visitedViews.slice(-1)[0] as RouteLocationNormalized
          if (latestView) {
            return router.push(latestView.fullPath)
          }
          return router.push('/')
        })
    }
    return useTagsViewStore().delView(obj)
  },
  // 关闭所有tab页签
  closeAllPage(): Promise<any> {
    return useTagsViewStore().delAllViews()
  },
  // 关闭左侧tab页签
  closeLeftPage(obj?: TabObject): Promise<any> {
    return useTagsViewStore().delLeftTags(obj || router.currentRoute.value)
  },
  // 关闭右侧tab页签
  closeRightPage(obj?: TabObject): Promise<any> {
    return useTagsViewStore().delRightTags(obj || router.currentRoute.value)
  },
  // 关闭其他tab页签
  closeOtherPage(obj?: TabObject): Promise<any> {
    return useTagsViewStore().delOthersViews(obj || router.currentRoute.value)
  },
  // 打开tab页签
  openPage(title: string, url: string, params?: any): Promise<any> {
    const obj: TabObject = { path: url, meta: { title: title } }
    useTagsViewStore().addView(obj)
    return router.push({ path: url, query: params })
  },
  // 修改tab页签
  updatePage(obj: TabObject): Promise<any> {
    useTagsViewStore().updateVisitedView(obj)
    return Promise.resolve()
  }
}
