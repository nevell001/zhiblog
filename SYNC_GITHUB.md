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

# 或使用 SSH（需要配置 SSH 密钥）
# 将脚本中的 https://github.com/... 改为 git@github.com:...
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

# 推送到两个仓库
git push origin main && git push github main
```
