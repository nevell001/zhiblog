<template>
  <div class="druid-disabled-container">
    <div class="alert alert-warning">
      <el-icon><WarningFilled /></el-icon>
      <span>Druid 监控功能已禁用</span>
    </div>
    <div class="info-card">
      <h3>📊 推荐使用以下监控方式</h3>
      <ul>
        <li>
          <el-link type="primary" href="/admin/monitor/prometheus" target="_blank">
            Prometheus 监控
          </el-link>
          - 提供更全面的系统监控指标
        </li>
        <li>
          <el-link type="primary" href="/admin/monitor/actuator" target="_blank">
            Actuator 监控
          </el-link>
          - Spring Boot 应用健康检查和指标
        </li>
        <li>
          <el-link type="primary" href="http://localhost:9090" target="_blank">
            Grafana 可视化
          </el-link>
          - 数据可视化仪表板（端口：9090）
        </li>
      </ul>
      <div class="note">
        <p><strong>说明：</strong></p>
        <p>Druid 数据库连接池监控功能已禁用，建议使用 Prometheus + Grafana 进行更现代化的监控。</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { WarningFilled } from '@element-plus/icons-vue'
</script>

<style scoped>
.druid-disabled-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.alert {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 15px 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.alert-warning {
  background-color: #fdf6ec;
  border: 1px solid #faecd8;
  color: #e6a23c;
}

.alert .el-icon {
  font-size: 20px;
}

.info-card {
  background: white;
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.info-card h3 {
  margin-top: 0;
  margin-bottom: 20px;
  color: #303133;
  font-size: 18px;
}

.info-card ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.info-card li {
  padding: 12px 0;
  border-bottom: 1px solid #ebeef5;
  display: flex;
  align-items: center;
  gap: 10px;
}

.info-card li:last-child {
  border-bottom: none;
}

.note {
  margin-top: 20px;
  padding: 15px;
  background-color: #f4f4f5;
  border-radius: 4px;
  color: #606266;
}

.note p {
  margin: 5px 0;
}

.note strong {
  color: #303133;
}
</style>
