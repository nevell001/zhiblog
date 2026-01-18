import { defineStore } from 'pinia'
import type { RouteLocationNormalized } from 'vue-router'

interface IFrameView {
  path: string
  title: string
}

interface TagsViewState {
  visitedViews: RouteLocationNormalized[]
  cachedViews: string[]
  iframeViews: IFrameView[]
}

const useTagsViewStore = defineStore('tags-view', {
  state: (): TagsViewState => ({
    visitedViews: [],
    cachedViews: [],
    iframeViews: []
  }),
  actions: {
    addView(view: RouteLocationNormalized): void {
      this.addVisitedView(view)
      this.addCachedView(view)
    },
    addIframeView(view: RouteLocationNormalized): void {
      if (this.iframeViews.some(v => v.path === view.path)) return
      this.iframeViews.push(
        Object.assign({}, view, {
          title: view.meta.title || 'no-name'
        })
      )
    },
    addVisitedView(view: RouteLocationNormalized): void {
      if (this.visitedViews.some(v => v.path === view.path)) return
      this.visitedViews.push(
        Object.assign({}, view, {
          title: view.meta.title || 'no-name'
        })
      )
    },
    addCachedView(view: RouteLocationNormalized): void {
      if (this.cachedViews.includes(view.name as string)) return
      if (!view.meta.noCache) {
        this.cachedViews.push(view.name as string)
      }
    },
    delView(
      view: RouteLocationNormalized
    ): Promise<{ visitedViews: RouteLocationNormalized[]; cachedViews: string[] }> {
      return new Promise(resolve => {
        this.delVisitedView(view)
        this.delCachedView(view)
        resolve({
          visitedViews: [...this.visitedViews],
          cachedViews: [...this.cachedViews]
        })
      })
    },
    delVisitedView(view: RouteLocationNormalized): Promise<RouteLocationNormalized[]> {
      return new Promise(resolve => {
        for (const [i, v] of this.visitedViews.entries()) {
          if (v.path === view.path) {
            this.visitedViews.splice(i, 1)
            break
          }
        }
        this.iframeViews = this.iframeViews.filter(item => item.path !== view.path)
        resolve([...this.visitedViews])
      })
    },
    delIframeView(view: RouteLocationNormalized): Promise<IFrameView[]> {
      return new Promise(resolve => {
        this.iframeViews = this.iframeViews.filter(item => item.path !== view.path)
        resolve([...this.iframeViews])
      })
    },
    delCachedView(view: RouteLocationNormalized): Promise<string[]> {
      return new Promise(resolve => {
        const index = this.cachedViews.indexOf(view.name as string)
        index > -1 && this.cachedViews.splice(index, 1)
        resolve([...this.cachedViews])
      })
    },
    delOthersViews(
      view: RouteLocationNormalized
    ): Promise<{ visitedViews: RouteLocationNormalized[]; cachedViews: string[] }> {
      return new Promise(resolve => {
        this.delOthersVisitedViews(view)
        this.delOthersCachedViews(view)
        resolve({
          visitedViews: [...this.visitedViews],
          cachedViews: [...this.cachedViews]
        })
      })
    },
    delOthersVisitedViews(view: RouteLocationNormalized): Promise<RouteLocationNormalized[]> {
      return new Promise(resolve => {
        this.visitedViews = this.visitedViews.filter(v => {
          return v.meta.affix || v.path === view.path
        })
        this.iframeViews = this.iframeViews.filter(item => item.path === view.path)
        resolve([...this.visitedViews])
      })
    },
    delOthersCachedViews(view: RouteLocationNormalized): Promise<string[]> {
      return new Promise(resolve => {
        const index = this.cachedViews.indexOf(view.name as string)
        if (index > -1) {
          this.cachedViews = this.cachedViews.slice(index, index + 1)
        } else {
          this.cachedViews = []
        }
        resolve([...this.cachedViews])
      })
    },
    delAllViews(
      view: RouteLocationNormalized
    ): Promise<{ visitedViews: RouteLocationNormalized[]; cachedViews: string[] }> {
      return new Promise(resolve => {
        this.delAllVisitedViews(view)
        this.delAllCachedViews(view)
        resolve({
          visitedViews: [...this.visitedViews],
          cachedViews: [...this.cachedViews]
        })
      })
    },
    delAllVisitedViews(_view: RouteLocationNormalized): Promise<RouteLocationNormalized[]> {
      return new Promise(resolve => {
        const affixTags = this.visitedViews.filter(tag => tag.meta.affix)
        this.visitedViews = affixTags
        this.iframeViews = []
        resolve([...this.visitedViews])
      })
    },
    delAllCachedViews(_view: RouteLocationNormalized): Promise<string[]> {
      return new Promise(resolve => {
        this.cachedViews = []
        resolve([...this.cachedViews])
      })
    },
    updateVisitedView(view: RouteLocationNormalized): void {
      for (let v of this.visitedViews) {
        if (v.path === view.path) {
          v = Object.assign(v, view)
          break
        }
      }
    },
    delRightTags(view: RouteLocationNormalized): Promise<RouteLocationNormalized[]> {
      return new Promise(resolve => {
        const index = this.visitedViews.findIndex(v => v.path === view.path)
        if (index === -1) {
          resolve([...this.visitedViews])
          return
        }
        this.visitedViews = this.visitedViews.filter((item, idx) => {
          if (idx <= index || (item.meta && item.meta.affix)) {
            return true
          }
          const i = this.cachedViews.indexOf(item.name as string)
          if (i > -1) {
            this.cachedViews.splice(i, 1)
          }
          if (item.meta.link) {
            const fi = this.iframeViews.findIndex(v => v.path === item.path)
            this.iframeViews.splice(fi, 1)
          }
          return false
        })
        resolve([...this.visitedViews])
      })
    },
    delLeftTags(view: RouteLocationNormalized): Promise<RouteLocationNormalized[]> {
      return new Promise(resolve => {
        const index = this.visitedViews.findIndex(v => v.path === view.path)
        if (index === -1) {
          resolve([...this.visitedViews])
          return
        }
        this.visitedViews = this.visitedViews.filter((item, idx) => {
          if (idx >= index || (item.meta && item.meta.affix)) {
            return true
          }
          const i = this.cachedViews.indexOf(item.name as string)
          if (i > -1) {
            this.cachedViews.splice(i, 1)
          }
          if (item.meta.link) {
            const fi = this.iframeViews.findIndex(v => v.path === item.path)
            this.iframeViews.splice(fi, 1)
          }
          return false
        })
        resolve([...this.visitedViews])
      })
    }
  }
})

export { useTagsViewStore }

export default useTagsViewStore
