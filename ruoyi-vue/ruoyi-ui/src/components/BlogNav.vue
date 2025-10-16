<template>
  <div class="blog-nav">
    <router-link to="/index" class="nav-item">博客首页</router-link>
    <router-link to="/blog/simple" class="nav-item">简洁版</router-link>
    <router-link to="/admin" class="nav-item">后台管理</router-link>
  </div>
</template>

<script setup>
// 博客导航组件
</script>

<style scoped>
.blog-nav {
  position: fixed;
  top: 20px;
  right: 20px;
  display: flex;
  gap: 15px;
  z-index: 1000;
}

.nav-item {
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 20px;
  text-decoration: none;
  color: #333;
  font-size: 0.9rem;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.nav-item:hover {
  background: #409eff;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

@media (max-width: 768px) {
  .blog-nav {
    top: 10px;
    right: 10px;
    gap: 10px;
  }
  
  .nav-item {
    padding: 6px 12px;
    font-size: 0.8rem;
  }
}
</style>