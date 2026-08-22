# GitHub Actions 说明

仓库同时托管在 Gitee（`origin`）和 GitHub（`github` 远端，由 `sync-github.sh` 同步），两套 CI/CD 互不干扰：

| 平台 | 配置文件 | 说明 |
| --- | --- | --- |
| Gitee Go | `.workflow/*.yml` | Gitee 侧流水线，由 Gitee Go 执行 |
| GitHub Actions | `.github/workflows/ci.yml` | push / PR 触发，后端 `mvn verify` + 前端 lint/format/test/build |
| GitHub Actions | `.github/workflows/release.yml` | 打 `v*` tag 触发，测试构建 + 自动创建 GitHub Release（附构建产物）+ 部署骨架 |

## 与 Gitee Go 的差异

- 触发方式一样：push 到任意分支、向 `main` 发 PR 跑 CI；打 `v1.3.7` 这样的 tag 跑发布流水线。
- 后端命令相同：`mvn -B clean verify`（编译 + 单测 + checkstyle + JaCoCo 覆盖率门槛），无需 MySQL/Redis。
- 前端命令相同：`npm ci` + lint + format + test + build。
- GitHub Actions 免费额度：公开仓库不限时长，私有仓库每月 2000 分钟。

## 发布部署

打 `v*` tag 时，`release.yml` 除了构建测试，还会用 `gh release create` 自动创建 GitHub Release，并附上 `zhi-admin.jar` 与打包好的 `frontend-dist.zip`（`publish` job，需要 `contents: write` 权限，使用内置 `GITHUB_TOKEN`）。该自动发布只对后续新打的 tag 生效。

`release.yml` 里的 `deploy` job 是注释掉的骨架。启用步骤：

1. 在 GitHub 仓库 Settings → Secrets and variables → Actions 添加：
   - `SERVER_HOST`、`SERVER_USER`、`SSH_PRIVATE_KEY`
   - 如走 Docker 镜像方案，另加镜像仓库凭据（如 `DOCKERHUB_USERNAME` / `DOCKERHUB_TOKEN`）
2. 取消 `release.yml` 中 deploy job 的注释，按需修改部署脚本。

## 注意

- 两个平台都会在 push 时跑一遍测试，耗时翻倍属正常现象；如只想保留一份，可以停用 Gitee Go 的某条流水线或删掉对应 `.workflow` 文件。
- 两套配置是独立的：改 CI 行为时需要同步改 `.workflow/` 和 `.github/workflows/` 两边，或只改你想用的一侧。
