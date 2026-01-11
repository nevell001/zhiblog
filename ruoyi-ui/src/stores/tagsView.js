import { defineStore } from 'pinia'

export const useTagsViewStore = defineStore('tags-view', {
  state: () => ({
    visitedViews: [],
    cachedViews: [],
    iframeViews: []
  }),
  actions: {
    addView(view) {
      this.addVisitedView(view)
      if (view.meta && view.meta.keepAlive) {
        this.addCachedView(view)
      }
    },
    addIframeView(view) {
      this.iframeViews.push(view)
    },
    addVisitedView(view) {
      if (this.visitedViews.some(v => v.path === view.path)) {
        if (view.meta && view.meta.link) {
          this.visitedViews.forEach(v => {
            if (v.path === view.path) v.meta.link = view.meta.link
          })
        }
        return
      }
      // 不添加外链
      if (view.meta && !view.meta.link) {
        this.visitedViews.push({
          ...view,
          title: view.meta.title || 'no-name'
        })
      }
    },
    addCachedView(view) {
      if (this.cachedViews.includes(view.name)) {
        return
      }
      if (view.name) {
        this.cachedViews.push(view.name)
      }
    },
    delView(view) {
      return new Promise(resolve => {
        this.delVisitedView(view)
        this.delCachedView(view)
        resolve({
          visitedViews: [...this.visitedViews],
          cachedViews: [...this.cachedViews]
        })
      })
    },
    delVisitedView(view) {
      const index = this.visitedViews.findIndex(v => v.path === view.path)
      if (index > -1) {
        const [r] = this.visitedViews.splice(index, 1)
        // 同步删除iframe
        const iframeIndex = this.iframeViews.findIndex(v => v.path === view.path)
        if (iframeIndex > -1) {
          this.iframeViews.splice(iframeIndex, 1)
        }
        return r
      }
    },
    delCachedView(view) {
      const index = this.cachedViews.indexOf(view.name)
      index > -1 && this.cachedViews.splice(index, 1)
    },
    delOthersViews(view) {
      return new Promise(resolve => {
        this.delOthersVisitedViews(view)
        this.delOthersCachedViews(view)
        resolve({
          visitedViews: [...this.visitedViews],
          cachedViews: [...this.cachedViews]
        })
      })
    },
    delOthersVisitedViews(view) {
      this.visitedViews = this.visitedViews.filter(v => {
        return (v.meta && v.meta.affix) || v.path === view.path
      })
      // 同步删除iframe
      const affixPath = this.visitedViews.map(v => v.path)
      this.iframeViews = this.iframeViews.filter(v => affixPath.includes(v.path))
    },
    delOthersCachedViews(view) {
      const index = this.cachedViews.indexOf(view.name)
      if (index > -1) {
        this.cachedViews = this.cachedViews.slice(index, index + 1)
      } else {
        this.cachedViews = []
      }
    },
    delAllViews() {
      return new Promise(resolve => {
        this.delAllVisitedViews()
        this.delAllCachedViews()
        resolve({
          visitedViews: [...this.visitedViews],
          cachedViews: [...this.cachedViews]
        })
      })
    },
    delAllVisitedViews() {
      const affixTags = this.visitedViews.filter(tag => tag.meta && tag.meta.affix)
      this.visitedViews = affixTags
      // 同步删除iframe
      const affixPath = affixTags.map(tag => tag.path)
      this.iframeViews = this.iframeViews.filter(v => affixPath.includes(v.path))
    },
    delAllCachedViews() {
      this.cachedViews = []
    },
    updateVisitedView(view) {
      for (let v of this.visitedViews) {
        if (v.path === view.path) {
          v = Object.assign(v, view)
          break
        }
      }
    },
    delRightTags(view) {
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
        // 同步删除iframe
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
    delLeftTags(view) {
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
        // 同步删除iframe
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
