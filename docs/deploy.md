# 部署与运维说明

## 一、本地开发环境
1. 安装 JDK 8+、Node.js、MySQL、Redis
2. 克隆项目，参考 readme.md 快速开始部分
3. 后端：`cd ruoyi-vue && mvn clean install -U && cd ruoyi-admin && mvn spring-boot:run`
4. 前端：`cd ruoyi-vue/ruoyi-ui && npm install && npm run dev`
5. 访问 http://localhost:8080（后台）和 http://localhost:8000（前台）

## 二、测试环境部署
- 可使用内网服务器，配置数据库、Redis，按本地方式启动
- 推荐使用 Docker Compose 管理多服务

## 三、生产环境部署
1. **后端打包**：
   ```bash
   cd ruoyi-vue/ruoyi-admin
   mvn clean package -Pprod
   # 生成 target/ruoyi-admin.jar
   ```
2. **前端打包**：
   ```bash
   cd ruoyi-vue/ruoyi-ui
   npm run build:prod
   # 生成 dist 目录，部署到 Nginx
   ```
3. **数据库/Redis**：
   - 初始化数据库，导入 sql/ 目录下脚本
   - 配置 Redis 服务
4. **Docker 部署**：
   - 推荐使用 Docker Compose，示例配置：
   ```yaml
   version: '3'
   services:
     blog-admin:
       image: openjdk:8-jre
       volumes:
         - ./ruoyi-admin.jar:/app/app.jar
       command: java -jar /app/app.jar
       ports:
         - "8080:8080"
     blog-ui:
       image: nginx:alpine
       volumes:
         - ./dist:/usr/share/nginx/html
       ports:
         - "8000:80"
     mysql:
       image: mysql:5.7
       environment:
         MYSQL_ROOT_PASSWORD: root
         MYSQL_DATABASE: blog
       ports:
         - "3306:3306"
     redis:
       image: redis:alpine
       ports:
         - "6379:6379"
   ```
5. **环境变量**：
   - 数据库、Redis、存储等配置建议通过环境变量或配置文件管理
   - 生产环境关闭调试、开启 HTTPS

## 四、运维建议
- 定期备份数据库和上传文件
- 监控服务状态、日志、磁盘空间
- 及时更新依赖和安全补丁

---

> 详细配置和运维脚本请结合实际环境调整。 