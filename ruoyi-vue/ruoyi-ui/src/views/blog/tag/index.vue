<template>
  <div class="blog-container">
    <!-- 页面头部 -->
    <div class="blog-header">
      <h1 class="blog-title">标签：{{ tagName }}</h1>
      <p class="blog-subtitle">共 {{ articleList.length }} 篇文章</p>
    </div>

    <!-- 文章列表 -->
    <div v-loading="loading" class="article-list">
      <div v-for="article in articleList" :key="article.id" class="article-item">
        <div class="article-content">
          <h2 class="article-title">
            <router-link :to="`/blog/article/${article.id}`" class="article-link">
              {{ article.title }}
            </router-link>
          </h2>
          <p class="article-summary">{{ article.summary }}</p>
          <div class="article-meta">
            <span class="meta-item">
              <i class="el-icon-date"></i>
              {{ parseTime(article.createTime, '{y}-{m}-{d}') }}
            </span>
            <span class="meta-item">
              <i class="el-icon-view"></i>
              {{ article.viewCount }} 阅读
            </span>
            <span class="meta-item">
              <i class="el-icon-chat-dot-round"></i>
              {{ article.commentCount }} 评论
            </span>
            <span class="meta-item">
              <i class="el-icon-star-off"></i>
              {{ article.likeCount }} 点赞
            </span>
          </div>
        </div>
        <div v-if="article.coverUrl" class="article-cover">
          <img :src="article.coverUrl" :alt="article.title" />
        </div>
      </div>

      <!-- 空状态 -->
      <div v-if="articleList.length === 0" class="empty-state">
        <i class="el-icon-document"></i>
        <p>该标签下暂无文章</p>
      </div>
    </div>

    <!-- 分页 -->
    <div v-if="total > 0" class="pagination-container">
      <el-pagination
        v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize"
        :total="total"
        :page-sizes="[10, 20, 30, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup name="BlogTag">
import { ref, reactive, onMounted, getCurrentInstance, watch, toRefs } from 'vue'
import { useRoute } from 'vue-router'
import { getTagDetail } from "@/api/blog/tag";
import { getArticlesByTag } from "@/api/blog/article";

const { proxy } = getCurrentInstance();
const route = useRoute();

const tagName = ref('');
const articleList = ref([]);
const loading = ref(true);
const total = ref(0);

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    tagId: null
  }
});

const { queryParams } = toRefs(data);

/** 获取标签详情 */
function getTagInfo() {
  const tagId = route.params.id;
  if (!tagId) return;
  
  getTagDetail(tagId).then(response => {
    if (response.data) {
      tagName.value = response.data.tagName;
      document.title = `标签：${response.data.tagName} - 博客`;
    }
  }).catch(() => {
    tagName.value = '未知标签';
  });
}

/** 获取标签下的文章列表 */
function getArticleList() {
  loading.value = true;
  const tagId = route.params.id;
  if (!tagId) return;
  
  getArticlesByTag(tagId, queryParams.value).then(response => {
    articleList.value = response.rows || [];
    total.value = response.total || 0;
    loading.value = false;
  }).catch(() => {
    loading.value = false;
  });
}

/** 页面大小改变 */
function handleSizeChange(val) {
  queryParams.value.pageSize = val;
  queryParams.value.pageNum = 1;
  getArticleList();
}

/** 当前页改变 */
function handleCurrentChange(val) {
  queryParams.value.pageNum = val;
  getArticleList();
}

onMounted(() => {
  getTagInfo();
  getArticleList();
});

// 监听路由变化
watch(() => route.params.id, (newId) => {
  if (newId) {
    queryParams.value.pageNum = 1;
    getTagInfo();
    getArticleList();
  }
});
</script>

<style scoped>
.blog-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.blog-header {
  text-align: center;
  margin-bottom: 40px;
  padding: 40px 0;
  border-bottom: 1px solid #f0f0f0;
}

.blog-title {
  font-size: 2.5rem;
  color: #333;
  margin-bottom: 10px;
  font-weight: 600;
}

.blog-subtitle {
  font-size: 1.1rem;
  color: #666;
  margin: 0;
}

.article-list {
  margin-bottom: 40px;
}

.article-item {
  display: flex;
  margin-bottom: 30px;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.article-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.15);
}

.article-content {
  flex: 1;
  margin-right: 20px;
}

.article-title {
  margin: 0 0 15px 0;
  font-size: 1.4rem;
  line-height: 1.4;
}

.article-link {
  color: #333;
  text-decoration: none;
  transition: color 0.3s ease;
}

.article-link:hover {
  color: #409eff;
}

.article-summary {
  color: #666;
  line-height: 1.6;
  margin-bottom: 15px;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.article-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  font-size: 0.9rem;
  color: #999;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 5px;
}

.article-cover {
  width: 200px;
  height: 120px;
  border-radius: 6px;
  overflow: hidden;
  flex-shrink: 0;
}

.article-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #999;
}

.empty-state i {
  font-size: 4rem;
  margin-bottom: 20px;
  display: block;
}

.empty-state p {
  font-size: 1.2rem;
  margin: 0;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}

@media (max-width: 768px) {
  .blog-container {
    padding: 10px;
  }
  
  .article-item {
    flex-direction: column;
  }
  
  .article-content {
    margin-right: 0;
    margin-bottom: 15px;
  }
  
  .article-cover {
    width: 100%;
    height: 200px;
  }
  
  .article-meta {
    gap: 10px;
  }
}
</style>