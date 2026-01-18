import { defineStore } from 'pinia'
import type { RouteLocationNormalized } from 'vue-router'

interface TagView extends Partial<RouteLocationNormalized> {
  title?: string
}

interface TagsViewState {
  visitedViews: TagView[]
  cachedViews: string[]
  iframeViews: TagView[]
}

interface TagsViewResult {
  visitedViews: TagView[]
  cachedViews: string[]
}

export const useTagsViewStore = defineStore('tags-view', {
  state: (): TagsViewState => ({
    visitedViews: [],
    cachedViews: [],
    iframeViews: []
  }),

  actions: {
    addView(view: TagView): void {
      this.addVisitedView(view)
      if (view.meta && view.meta.keepAlive) {
        this.addCachedView(view)
      }
    },

    addIframeView(view: TagView): void {
      this.iframeViews.push(view)
    },

    addVisitedView(view: TagView): void {
      if (this.visitedViews.some(v => v.path === view.path)) {
        if (view.meta && view.meta.link) {
          this.visitedViews.forEach(v => {
            if (v.path === view.path && v.meta) v.meta.link = view.meta.link
          })
        }
        return
      }
      if (view.meta && !view.meta.link) {
        this.visitedViews.push({
          ...view,
          title: view.meta.title || 'no-name'
        })
      }
    },

    addCachedView(view: TagView): void {
      if (this.cachedViews.includes(view.name as string)) {
        return
      }
      if (view.name) {
        this.cachedViews.push(view.name as string)
      }
    },

    delView(view: TagView): Promise<TagsViewResult> {
      return new Promise(resolve => {
        this.delVisitedView(view)
        this.delCachedView(view)
        resolve({
          visitedViews: [...this.visitedViews],
          cachedViews: [...this.cachedViews]
        })
      })
    },

    delVisitedView(view: TagView): TagView | undefined {
      const index = this.visitedViews.findIndex(v => v.path === view.path)
      if (index > -1) {
        const [r] = this.visitedViews.splice(index, 1)
        const iframeIndex = this.iframeViews.findIndex(v => v.path === view.path)
        if (iframeIndex > -1) {
          this.iframeViews.splice(iframeIndex, 1)
        }
        return r
      }
    },

    delCachedView(view: TagView): void {
      const index = this.cachedViews.indexOf(view.name as string)
      index > -1 && this.cachedViews.splice(index, 1)
    },

    delOthersViews(view: TagView): Promise<TagsViewResult> {
      return new Promise(resolve => {
        this.delOthersVisitedViews(view)
        this.delOthersCachedViews(view)
        resolve({
          visitedViews: [...this.visitedViews],
          cachedViews: [...this.cachedViews]
        })
      })
    },

    delOthersVisitedViews(view: TagView): void {
      this.visitedViews = this.visitedViews.filter(v => {
        return (v.meta && v.meta.affix) || v.path === view.path
      })
      const affixPath = this.visitedViews.map(v => v.path)
      this.iframeViews = this.iframeViews.filter(v => affixPath.includes(v.path))
    },

    delOthersCachedViews(view: TagView): void {
      const index = this.cachedViews.indexOf(view.name as string)
      if (index > -1) {
        this.cachedViews = this.cachedViews.slice(index, index + 1)
      } else {
        this.cachedViews = []
      }
    },

    delAllViews(): Promise<TagsViewResult> {
      return new Promise(resolve => {
        this.delAllVisitedViews()
        this.delAllCachedViews()
        resolve({
          visitedViews: [...this.visitedViews],
          cachedViews: [...this.cachedViews]
        })
      })
    },

    delAllVisitedViews(): void {
      const affixTags = this.visitedViews.filter(tag => tag.meta && tag.meta.affix)
      this.visitedViews = affixTags
      const affixPath = affixTags.map(tag => tag.path)
      this.iframeViews = this.iframeViews.filter(v => affixPath.includes(v.path))
    },

    delAllCachedViews(): void {
      this.cachedViews = []
    },

    updateVisitedView(view: TagView): void {
      for (const v of this.visitedViews) {
        if (v.path === view.path) {
          Object.assign(v, view)
          break
        }
      }
    },

    delRightTags(view: TagView): Promise<TagsViewResult> {
      return new Promise(resolve => {
        const index = this.visitedViews.findIndex(v => v.path === view.path)
        if (index === -1) {
          resolve({
            visitedViews: [...this.visitedViews],
            cachedViews: [...this.cachedViews]
          })
          return
        }
        this.visitedViews = this.visitedViews.filter((item, idx) => {
          return idx <= index || (item.meta && item.meta.affix)
        })
        const newVisitPaths = this.visitedViews.map(v => v.path)
        this.iframeViews = this.iframeViews.filter(v => newVisitPaths.includes(v.path))

        const newCachedNames = this.visitedViews.map(item => item.name).filter(name => name)
        this.cachedViews = this.cachedViews.filter(name => {
          return newCachedNames.includes(name)
        })
        resolve({
          visitedViews: [...this.visitedViews],
          cachedViews: [...this.cachedViews]
        })
      })
    },

    delLeftTags(view: TagView): Promise<TagsViewResult> {
      return new Promise(resolve => {
        const index = this.visitedViews.findIndex(v => v.path === view.path)
        if (index === -1) {
          resolve({
            visitedViews: [...this.visitedViews],
            cachedViews: [...this.cachedViews]
          })
          return
        }
        this.visitedViews = this.visitedViews.filter((item, idx) => {
          return idx >= index || (item.meta && item.meta.affix)
        })
        const newVisitPaths = this.visitedViews.map(v => v.path)
        this.iframeViews = this.iframeViews.filter(v => newVisitPaths.includes(v.path))

        const newCachedNames = this.visitedViews.map(item => item.name).filter(name => name)
        this.cachedViews = this.cachedViews.filter(name => {
          return newCachedNames.includes(name)
        })
        resolve({
          visitedViews: [...this.visitedViews],
          cachedViews: [...this.cachedViews]
        })
      })
    }
  }
})
