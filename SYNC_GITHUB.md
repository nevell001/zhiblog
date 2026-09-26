# 同步代码到 GitHub

## 使用方法

### 1. 创建 GitHub 仓库

在 GitHub 上创建一个新仓库 `zhiblog`（不要初始化 README）

### 2. 配置 GitHub 仓库地址

编辑 `sync-github.sh`，修改 GitHub 仓库地址：

```bash
GITHUB_REPO="https://github.com/你的用户名/zhiblog.git"
```

### 3. 添加执行权限

```bash
chmod +x sync-github.sh
```

### 4. 运行同步脚本

```bash
./sync-github.sh
```

## 首次推送

首次运行时可能需要 GitHub 认证：

```bash
# 使用 GitHub Token（推荐）
git push github main

# 或使用 SSH（推荐：不受全球 GitHub 加速代理影响）——只需给 push 配 SSH，
# fetch 仍走 https 加速代理也可以：
#   git remote set-url --push github git@github.com:你的用户名/zhiblog.git
#   ssh -T git@github.com    # 应显示 Hi <用户名>! You've successfully authenticated
#
# 注意：若 ~/.gitconfig 里有 url.https://ghproxy.net/https://github.com/.insteadOf 这类
# 加速重写规则，https 推送会被改写到第三方代理而无法认证（且会把令牌交给代理），
# 此时必须用 SSH 推送，或临时剥离规则：
#   GIT_CONFIG_GLOBAL=<(grep -v insteadOf ~/.gitconfig) git push github main
```

## GitHub Token 生成

1. 访问 GitHub Settings → Developer settings → Personal access tokens
2. 生成新 Token，勾选 `repo` 权限
3. 推送时使用 Token 作为密码

## 一键命令

也可以直接使用 git 命令同步：

```bash
# 添加 GitHub 远端
git remote add github https://github.com/你的用户名/zhiblog.git

# 推送到两个仓库（--follow-tags 会一并推送 main 上可达的注释标签）
git push origin main --follow-tags && git push github main --follow-tags
```

## 同步标签与创建 Release

`git push main` **不会**推送标签，而 GitHub 的 Releases 是按标签生成的，需要单独处理：

```bash
# 1) 打注释标签（消息用 docs/releases/<版本>.md 的发行说明）
git tag -a v1.4.0 -F docs/releases/v1.4.0.md

# 2) 推送到两个远端
git push origin v1.4.0 && git push github v1.4.0

# 3) 校验
git ls-remote --tags origin && git ls-remote --tags github
```

最后在 GitHub → Releases → *Draft a new release* 选择该标签、粘贴发行说明并 Publish（Gitee 的「发行版」同理）。完整流程见 [版本管理指南](docs/VERSION_MANAGEMENT.md#发布流程tag-与-release)。

