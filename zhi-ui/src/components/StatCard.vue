<template>
  <div class="stat-card">
    <div class="stat-card__label">{{ label }}</div>
    <div class="stat-card__value">{{ value }}</div>
  </div>
</template>

<script setup lang="ts">
defineProps<{ label: string; value: string | number }>()
</script>

<style lang="scss" scoped>
.stat-card {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 100%;
  text-align: center;
  padding: 20px;
  border: 1px solid var(--el-border-color);
  border-radius: 4px;
}

.stat-card__label {
  font-size: 14px;
  color: var(--el-text-color-secondary);
  margin-bottom: 10px;
}

.stat-card__value {
  font-size: 24px;
  font-weight: bold;
  color: var(--el-text-color-primary);
}
</style>
