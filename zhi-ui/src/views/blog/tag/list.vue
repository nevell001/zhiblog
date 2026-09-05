<template>
  <div class="tag-overview-page">
    <BlogLayout>
      <div class="tag-overview-container">
        <header class="ov-header">
          <h1>文章标签</h1>
          <p>点击标签查看该主题下的全部文章</p>
        </header>

        <div v-if="loading" v-loading="loading" class="ov-loading"></div>

        <template v-else-if="tags.length > 0">
          <div class="tag-cloud-grid">
            <router-link
              v-for="(tag, index) in tags"
              :key="tag.id || tag.name"
              :to="tag.id ? `/blog/tag/${tag.id}` : '/blog/tag'"
              class="tag-card"
              :class="tagSize(index)"
            >
              <span class="tag-name">{{ tag.name }}</span>
              <span v-if="tag.articleCount" class="tag-count">{{ tag.articleCount }}</span>
            </router-link>
          </div>
        </template>

        <el-empty v-else description="暂无标签">
          <el-button type="primary" @click="$router.push('/blog')">返回首页</el-button>
        </el-empty>
      </div>
    </BlogLayout>
  </div>
</template>

<script setup lang="ts" name="BlogTagOverview">
import { onMounted, ref } from 'vue'
import BlogLayout from '@/components/BlogLayout.vue'
import { getTagCloud } from '@/api/blog/tag'

interface TagItem {
  id?: number
  name: string
  articleCount?: number
}

const loading = ref(false)
const tags = ref<TagItem[]>([])

const tagSize = (index: number) => {
  if (index % 7 === 0) return 'is-lg'
  if (index % 5 === 0) return 'is-md'
  if (index % 3 === 0) return 'is-sm'
  return ''
}

const loadTags = async () => {
  loading.value = true
  try {
    const response = (await getTagCloud()) as any
    const list = Array.isArray(response)
      ? response
      : Array.isArray(response?.data)
        ? response.data
        : response?.rows || []
    tags.value = Array.isArray(list) ? list : []
  } catch {
    tags.value = []
  } finally {
    loading.value = false
  }
}

onMounted(loadTags)
</script>

<style scoped>
.tag-overview-page {
  min-height: 100vh;
}
.tag-overview-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 96px 20px 48px;
}
.ov-header {
  margin-bottom: 24px;
}
.ov-header h1 {
  margin: 0 0 8px;
  font-size: 26px;
  font-weight: 700;
  color: var(--el-text-color-primary, #303133);
}
.ov-header p {
  margin: 0;
  color: var(--el-text-color-secondary, #606266);
  font-size: 14px;
}
.ov-loading {
  min-height: 160px;
}
.tag-cloud-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}
.tag-card {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  background: var(--mo-p50);
  color: var(--mo-p600);
  border-radius: 999px;
  text-decoration: none;
  transition:
    transform 0.15s ease,
    background 0.15s ease;
}
.tag-card:hover {
  background: var(--mo-p100);
  transform: translateY(-2px);
}
.tag-name {
  font-size: 14px;
}
.tag-card.is-lg .tag-name {
  font-size: 17px;
  font-weight: 700;
}
.tag-card.is-md .tag-name {
  font-size: 15px;
  font-weight: 600;
}
.tag-count {
  font-size: 11px;
  opacity: 0.7;
}
</style>
