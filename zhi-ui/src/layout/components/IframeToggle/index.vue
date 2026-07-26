<template>
  <inner-link
    v-for="(item, index) in tagsViewStore.iframeViews"
    v-show="route.path === item.path"
    :key="item.path"
    :iframe-id="'iframe' + index"
    :src="iframeUrl(item.meta.link, item.query)"
  />
</template>

<script setup lang="ts">
import InnerLink from '../InnerLink/index'
import { useTagsViewStore } from '@/stores/tagsView'

const route = useRoute()
const tagsViewStore = useTagsViewStore()

function iframeUrl(url, query) {
  if (Object.keys(query).length > 0) {
    const params = Object.keys(query)
      .map(key => key + '=' + query[key])
      .join('&')
    return url + '?' + params
  }
  return url
}
</script>
