#!/bin/bash
# 同步代码到 GitHub 脚本

set -e

# 颜色定义
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${BLUE}======================================${NC}"
echo -e "${BLUE}    同步代码到 GitHub${NC}"
echo -e "${BLUE}======================================${NC}"
echo ""

# GitHub 仓库地址（请修改为你的仓库地址）
GITHUB_REPO="https://github.com/nevell001/zhiblog.git"

# 检查是否已添加 github 远端
if git remote | grep -q "^github$"; then
    echo -e "${GREEN}✓ GitHub 远端已存在${NC}"
    git remote set-url github "$GITHUB_REPO"
else
    echo -e "${YELLOW}添加 GitHub 远端...${NC}"
    git remote add github "$GITHUB_REPO"
    echo -e "${GREEN}✓ GitHub 远端已添加${NC}"
fi

echo ""
echo -e "${BLUE}当前远端配置：${NC}"
git remote -v

echo ""
echo -e "${YELLOW}正在推送到 Gitee (origin)...${NC}"
# --follow-tags：一并推送 main 上可达的注释标签（普通 git push 不会带标签）
git push origin main --follow-tags

echo ""
echo -e "${YELLOW}正在推送到 GitHub (github)...${NC}"
git push github main --follow-tags

echo ""
echo -e "${BLUE}已推送的标签：${NC}"
git ls-remote --tags origin | sed 's/^/  origin  /' | grep -v '\^{}' || true
git ls-remote --tags github | sed 's/^/  github  /' | grep -v '\^{}' || true

echo ""
echo -e "${YELLOW}提示：标签不会自动变成 Release，需在 GitHub/Gitee 手动创建发行版${NC}"
echo -e "${YELLOW}      （说明见 docs/VERSION_MANAGEMENT.md 的「发布流程」）${NC}"

echo ""
echo -e "${GREEN}======================================${NC}"
echo -e "${GREEN}    ✓ 同步完成！${NC}"
echo -e "${GREEN}======================================${NC}"
echo ""
echo -e "Gitee:  ${BLUE}https://gitee.com/nevell/zhiblog${NC}"
echo -e "GitHub: ${BLUE}https://github.com/nevell/zhiblog${NC}"
