#!/bin/bash

# 系统管理后台菜单系统部署脚本
# 使用方法: ./deploy_menu_system.sh [mysql_user] [mysql_password] [database]

set -e  # 遇到错误立即退出

# 默认配置
MYSQL_USER=${1:-root}
MYSQL_PASSWORD=${2:-}
DATABASE=${3:-newblog}
MYSQL_HOST=${4:-localhost}
MYSQL_PORT=${5:-3306}

# 颜色输出函数
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查MySQL连接
check_mysql_connection() {
    log_info "检查MySQL连接..."
    if [ -z "$MYSQL_PASSWORD" ]; then
        mysql -h$MYSQL_HOST -P$MYSQL_PORT -u$MYSQL_USER -e "SELECT 1;" $DATABASE > /dev/null 2>&1
    else
        mysql -h$MYSQL_HOST -P$MYSQL_PORT -u$MYSQL_USER -p$MYSQL_PASSWORD -e "SELECT 1;" $DATABASE > /dev/null 2>&1
    fi
    
    if [ $? -eq 0 ]; then
        log_success "MySQL连接正常"
    else
        log_error "MySQL连接失败，请检查配置"
        exit 1
    fi
}

# 备份数据库
backup_database() {
    log_info "备份数据库..."
    BACKUP_FILE="backup_${DATABASE}_$(date +%Y%m%d_%H%M%S).sql"
    
    if [ -z "$MYSQL_PASSWORD" ]; then
        mysqldump -h$MYSQL_HOST -P$MYSQL_PORT -u$MYSQL_USER $DATABASE > $BACKUP_FILE
    else
        mysqldump -h$MYSQL_HOST -P$MYSQL_PORT -u$MYSQL_USER -p$MYSQL_PASSWORD $DATABASE > $BACKUP_FILE
    fi
    
    if [ $? -eq 0 ]; then
        log_success "数据库备份完成: $BACKUP_FILE"
    else
        log_warning "数据库备份失败，继续执行部署..."
    fi
}

# 执行SQL脚本
execute_sql_script() {
    local script_file=$1
    local script_name=$2
    
    log_info "执行脚本: $script_name"
    
    if [ -z "$MYSQL_PASSWORD" ]; then
        mysql -h$MYSQL_HOST -P$MYSQL_PORT -u$MYSQL_USER $DATABASE < $script_file
    else
        mysql -h$MYSQL_HOST -P$MYSQL_PORT -u$MYSQL_USER -p$MYSQL_PASSWORD $DATABASE < $script_file
    fi
    
    if [ $? -eq 0 ]; then
        log_success "$script_name 执行成功"
    else
        log_error "$script_name 执行失败"
        exit 1
    fi
}

# 验证部署结果
verify_deployment() {
    log_info "验证部署结果..."
    
    # 检查菜单数量
    local menu_count=$(mysql -h$MYSQL_HOST -P$MYSQL_PORT -u$MYSQL_USER -p$MYSQL_PASSWORD $DATABASE -sN -e "
        SELECT COUNT(*) FROM sys_menu WHERE menu_id IN (
            1000, 1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008, 1009, 10091, 10092,
            2000, 2101, 2102, 2103, 2104, 2105, 2106,
            3000, 3001, 3002, 3003, 3004, 3005,
            4000, 4001, 4002, 4003, 4004, 4005
        );
    " 2>/dev/null)
    
    if [ "$menu_count" -ge "30" ]; then
        log_success "菜单创建验证通过 ($menu_count个菜单项)"
    else
        log_error "菜单创建验证失败 (期望>=30, 实际=$menu_count)"
        return 1
    fi
    
    # 检查权限分配
    local permission_count=$(mysql -h$MYSQL_HOST -P$MYSQL_PORT -u$MYSQL_USER -p$MYSQL_PASSWORD $DATABASE -sN -e "
        SELECT COUNT(*) FROM sys_role_menu WHERE role_id = 1 AND menu_id IN (
            1000, 1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008, 1009, 10091, 10092,
            2000, 2101, 2102, 2103, 2104, 2105, 2106,
            3000, 3001, 3002, 3003, 3004, 3005,
            4000, 4001, 4002, 4003, 4004, 4005,
            10011, 10012, 10013, 10014, 10015, 10016, 10017,
            10021, 10022, 10023, 10024, 10025,
            10031, 10032, 10033, 10034,
            10041, 10042, 10043, 10044,
            10051, 10052, 10053, 10054, 10055,
            10061, 10062, 10063, 10064, 10065,
            10071, 10072, 10073, 10074, 10075,
            10081, 10082, 10083, 10084,
            10091, 10092, 10093,
            10094, 10095, 10096, 10097,
            30011, 30012, 30013,
            30021, 30022, 30023,
            30031, 30032, 30033,
            30041, 30042, 30043,
            30051, 30052, 30053,
            40011, 40012,
            40021, 40022, 40023, 40024, 40025, 40026, 40027,
            40031, 40041, 40051, 40052, 40053
        );
    " 2>/dev/null)
    
    if [ "$permission_count" -ge "80" ]; then
        log_success "权限分配验证通过 ($permission_count个权限)"
    else
        log_warning "权限分配验证警告 (期望>=80, 实际=$permission_count)"
    fi
}

# 显示部署结果
show_deployment_summary() {
    log_info "生成部署报告..."
    
    echo ""
    echo "========================================"
    echo "         部署结果报告"
    echo "========================================"
    echo ""
    
    # 显示菜单结构
    echo "📁 菜单结构概览:"
    mysql -h$MYSQL_HOST -P$MYSQL_PORT -u$MYSQL_USER -p$MYSQL_PASSWORD $DATABASE -e "
        SELECT 
            CASE 
                WHEN parent_id = 0 THEN CONCAT('📂 ', menu_name)
                WHEN menu_type = 'M' THEN CONCAT('📁 ', menu_name)
                WHEN menu_type = 'C' THEN CONCAT('📄 ', menu_name)
                ELSE CONCAT('🔘 ', menu_name)
            END as menu_structure
        FROM sys_menu 
        WHERE menu_id IN (
            1000, 1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008, 1009, 10091, 10092,
            2000, 2101, 2102, 2103, 2104, 2105, 2106,
            3000, 3001, 3002, 3003, 3004, 3005,
            4000, 4001, 4002, 4003, 4004, 4005
        )
        ORDER BY parent_id, order_num;
    " 2>/dev/null
    
    echo ""
    echo "✅ 部署完成！"
    echo ""
    echo "下一步操作:"
    echo "1. 重启应用服务"
    echo "2. 使用管理员账户登录后台"
    echo "3. 验证菜单显示和权限控制"
    echo "4. 查看详细文档: MENU_IMPLEMENTATION_GUIDE.md"
    echo ""
}

# 主函数
main() {
    echo "========================================"
    echo "    系统管理后台菜单系统部署工具"
    echo "========================================"
    echo ""
    
    log_info "配置信息:"
    echo "  MySQL主机: $MYSQL_HOST"
    echo "  MySQL端口: $MYSQL_PORT"
    echo "  数据库名: $DATABASE"
    echo "  用户名: $MYSQL_USER"
    echo ""
    
    # 检查脚本文件是否存在
    SCRIPT_DIR="ruoyi-vue"
    REQUIRED_FILES=(
        "complete_menu_system.sql"
        "button_permissions_config.sql"
        "verify_menu_setup.sql"
    )
    
    for file in "${REQUIRED_FILES[@]}"; do
        if [ ! -f "$SCRIPT_DIR/$file" ]; then
            log_error "必需文件不存在: $SCRIPT_DIR/$file"
            exit 1
        fi
    done
    
    # 确认执行
    read -p "是否继续执行部署? (y/N): " confirm
    if [[ $confirm != [yY] ]]; then
        log_info "部署已取消"
        exit 0
    fi
    
    # 执行部署步骤
    check_mysql_connection
    backup_database
    
    # 按顺序执行SQL脚本
    execute_sql_script "$SCRIPT_DIR/complete_menu_system.sql" "菜单结构创建"
    execute_sql_script "$SCRIPT_DIR/button_permissions_config.sql" "按钮权限配置"
    execute_sql_script "$SCRIPT_DIR/verify_menu_setup.sql" "部署验证"
    
    # 验证部署结果
    if verify_deployment; then
        show_deployment_summary
        log_success "🎉 菜单系统部署成功！"
    else
        log_error "❌ 部署验证失败，请检查日志"
        exit 1
    fi
}

# 执行主函数
main "$@"