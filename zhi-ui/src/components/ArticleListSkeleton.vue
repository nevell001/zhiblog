<template>
  <div class="loading-container">
    <div class="loading-grid">
      <el-skeleton v-for="i in count" :key="i" :loading="true" animated class="skeleton-item">
        <template #template>
          <div class="skeleton-card">
            <div class="skeleton-cover">
              <el-skeleton-item variant="image" style="width: 100%; height: 200px" />
            </div>
            <div class="skeleton-body">
              <el-skeleton-item variant="h3" style="width: 70%; margin-bottom: 15px" />
              <el-skeleton-item variant="text" style="width: 100%; margin-bottom: 10px" />
              <el-skeleton-item variant="text" style="width: 90%; margin-bottom: 10px" />
              <el-skeleton-item variant="text" style="width: 60%; margin-bottom: 15px" />
              <div class="skeleton-chips">
                <el-skeleton-item variant="text" style="width: 60px; height: 24px" />
                <el-skeleton-item variant="text" style="width: 50px; height: 24px" />
              </div>
              <el-skeleton-item variant="text" style="width: 80px; height: 20px" />
            </div>
          </div>
        </template>
      </el-skeleton>
    </div>
  </div>
</template>

<script setup lang="ts">
withDefaults(defineProps<{ count?: number }>(), { count: 6 })
</script>

<style lang="scss" scoped>
.loading-container {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
}

.loading-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(min(100%, 380px), 1fr));
  gap: 30px;
  width: 100%;
  max-width: 1200px;
}

.skeleton-item {
  width: 100%;
}

/* 与 ArticleCard 的卡片外壳保持一致（骨架屏占位外观） */
.skeleton-card {
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background: white;
  border: 1px solid rgba(0, 0, 0, 0.05);
  border-radius: var(--mo-r-md);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  opacity: 0;
  animation: skeletonCardIn 0.6s ease forwards;
}

.skeleton-cover {
  position: relative;
  height: 200px;
  overflow: hidden;
  background: linear-gradient(45deg, var(--mo-n100), var(--mo-n200));
}

.skeleton-body {
  display: flex;
  flex: 1;
  flex-direction: column;
  padding: 25px;
}

.skeleton-chips {
  display: flex;
  gap: 8px;
  margin-bottom: 15px;
}

@media (max-width: 1024px) {
  .skeleton-cover {
    height: 180px;
  }
}

@media (max-width: 768px) {
  .skeleton-card {
    margin-bottom: 20px;
  }

  .skeleton-body {
    padding: 20px;
  }
}

@media (max-width: 480px) {
  .skeleton-card {
    border-radius: var(--mo-r-sm);
    box-shadow: 0 1px 8px rgba(0, 0, 0, 0.08);
  }

  .skeleton-cover {
    height: 160px;
  }

  .skeleton-body {
    padding: 18px;
  }
}

html.dark .skeleton-card {
  background: var(--mo-n800);
  border-color: var(--mo-n800);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}

@keyframes skeletonCardIn {
  from {
    opacity: 0;
    transform: translateY(30px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
