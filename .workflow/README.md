# Gitee Go 流水线说明

本目录是 ZhiBlog 的 Gitee Go（码云 CI/CD）流水线配置，包含四条流水线：

| 文件 | 触发时机 | 作用 |
| --- | --- | --- |
| `MasterPipeline.yml` | push 到 `main` | 后端 + 前端全量测试构建 |
| `BranchPipeline.yml` | push 到非 `main` 分支 | 后端 + 前端全量测试构建 |
| `PRPipeline.yml` | 向 `main` 发起 Pull Request | 后端 + 前端全量测试构建 |
| `ReleasePipeline.yml` | 打 `v*` 开头的 tag（如 `v1.3.7`） | 全量测试构建 + 发布部署骨架 |

## 启用步骤

1. 在 Gitee 仓库页面进入「流水线」，开通 Gitee Go（需要账号已绑定手机号）。开通时会自动生成默认的 `.workflow` 模板并产生一次提交，直接用本目录的文件替换即可。
   - `MasterPipeline.yml` / `BranchPipeline.yml` / `PRPipeline.yml` 会被平台按文件名自动识别；`ReleasePipeline.yml` 属于自定义流水线，如推送后未出现在流水线列表，在控制台「新建流水线」中选择该文件即可。
2. 首次运行流水线前，确认平台构建环境支持：
   - `jdkVersion: 17`（Spring Boot 3.3 要求 JDK 17+；Gitee Go 官方支持列表含 17）。
   - `mavenVersion: 3.6.3`（Gitee Go 官方最高支持 3.6.3，恰好满足 Spring Boot 3.3 的 Maven 3.6.3+ 要求；3.9.x 不在官方支持列表）。
   - `nodeVersion: 22`（前端 Vite 7 要求 Node >= 20.19；若平台版本列表不含 22，在可视化编辑里改选平台支持且 >= 20.19 的版本）。
   - 如果平台报版本不支持，直接在流水线可视化编辑界面修改对应字段，无需改仓库代码。
3. 触发一次构建，验证两条链路：
   - 后端：`mvn -B clean verify` 会执行全部单测、checkstyle 和 JaCoCo 覆盖率门槛（60% 行 / 60% 分支）。
   - 前端：`npm ci` 后依次执行 ESLint、Prettier、Vitest、生产构建。

## 发布部署（ReleasePipeline）

当前 `ReleasePipeline.yml` 的部署阶段是注释掉的骨架，配置方式：

1. 在 Gitee Go 控制台创建「主机组」并在目标服务器安装 Agent，拿到 `hostGroupID`。
2. 取消 `ReleasePipeline.yml` 中 deploy 阶段的注释，把 `hostGroupID` 换成真实值。
3. 在流水线参数/凭据里配置敏感信息（不要提交到仓库）：
   - 服务器 SSH 密钥 / Agent 凭据
   - `R_TOKEN_SECRET`（JWT 密钥，>= 64 字符）
   - `DB_PASSWORD`、`REDIS_PASSWORD`、`DRUID_PASSWORD`
4. 服务器端确认 `docker-compose.prod.yml` 可用（MySQL 8.4、Redis 6.2+、`./uploadPath/` 挂载卷存在）。

发布流程建议：本地/仓库打 tag（`git tag v1.3.7 && git push origin v1.3.7`）→ 流水线构建测试 → 上传制品 → 服务器拉取并 `docker compose up -d`。

## 注意

- 免费额度：单个仓库开通即得 200 分钟构建时长（永久），个人/组织每月另有 500 分钟。全量流水线单次约 5~10 分钟，请勿让每个 commit 都触发发布流水线。
- `.env` 不要提交；服务器环境变量通过容器编排或 Gitee Go 凭据注入。
- tag 匹配规则如与平台实际行为不一致（通配符写法差异），在可视化界面调整触发条件即可。
