
# 后台管理菜单体系说明文档

## 📋 概述

本文档描述了完整的后台管理菜单体系架构，包括菜单ID规划、权限设计和使用说明。

## 🏗️ 菜单架构

### 三级菜单结构

```
一级菜单（目录）
├── 二级菜单（功能模块/页面）
│   ├── 三级菜单（按钮权限）
│   ├── 三级菜单（按钮权限）
│   └── ...
└── ...
```

## 🔢 菜单ID规划

### ID分配规则

| 模块 | 一级菜单ID | 二级菜单ID | 按钮权限ID | 说明 |
|------|-----------|-----------|-----------|------|
| 博客管理 | 2000 | 2001-2999 | 20000-20999 | 博客内容管理 |
| 数据统计 | 3000 | 3001-3999 | 30000-30999 | 数据分析统计 |
| 用户管理 | 4000 | 4001-4999 | 40000-40999 | 预留扩展 |
| 系统配置 | 5000 | 5001-5999 | 50000-50999 | 预留扩展 |

### 按钮权限ID细分规则

每个功能模块的按钮权限ID按以下规则分配：

- **X0**: 查询权限
- **X1**: 新增权限
- **X2**: 修改权限
- **X3**: 删除权限
- **X4**: 导出权限
- **X5**: 导入权限
- **X6-X9**: 其他特殊权限

例如：
- 20010: 文章查询
- 20011: 文章新增
- 20012: 文章修改
- 20013: 文章删除
- 20014: 文章导出

## 📊 当前菜单体系

### 1. 博客管理模块（2000-2999）

#### 一级菜单
- **menu_id**: 2000
- **menu_name**: 博客管理
- **path**: blog
- **icon**: documentation

#### 二级菜单

| ID | 名称 | 路径 | 组件路径 | 权限标识 | 图标 |
|----|------|------|---------|---------|------|
| 2001 | 文章管理 | article | admin/blog/article/article/index | blog:article:list | edit |
| 2002 | 分类管理 | category | admin/blog/category/category/index | blog:category:list | list |
| 2003 | 标签管理 | tag | system/tag/index | blog:tag:list | tag |
| 2004 | 评论管理 | comment | admin/blog/comment/comment/index | blog:comment:list | message |
| 2005 | 友链管理 | friendLink | admin/blog/friendLink/friendLink/index | blog:friendLink:list | link |
| 2006 | 博客设置 | setting | admin/blog/setting/setting/index | blog:setting:list | setting |
| 2007 | 关于作者 | about | admin/system/about/index | blog:about:list | user |

#### 按钮权限

**文章管理（20010-20019）**
- 20010: 文章查询 (blog:article:query)
- 20011: 文章新增 (blog:article:add)
- 20012: 文章修改 (blog:article:edit)
- 20013: 文章删除 (blog:article:remove)
- 20014: 文章导出 (blog:article:export)
- 20015: 文章发布 (blog:article:publish)

**分类管理（20020-20029）**
- 20020: 分类查询 (blog:category:query)
- 20021: 分类新增 (blog:category:add)
- 20022: 分类修改 (blog:category:edit)
- 20023: 分类删除 (blog:category:remove)
- 20024: 分类导出 (blog:category:export)

**标签管理（20030-20039）**
- 20030: 标签查询 (blog:tag:query)
- 20031: 标签新增 (blog:tag:add)
- 20032: 标签修改 (blog:tag:edit)
- 20033: 标签删除 (blog:tag:remove)
- 20034: 标签导出 (blog:tag:export)

**评论管理（20040-20049）**
- 20040: 评论查询 (blog:comment:query)
- 20041: 评论审核 (blog:comment:approve)
- 20042: 评论回复 (blog:comment:reply)
- 20043: 评论删除 (blog:comment:remove)
- 20044: 评论导出 (blog:comment:export)

**友链管理（20050-20059）**
- 20050: 友链查询 (blog:friendLink:query)
- 20051: 友链新增 (blog:friendLink:add)
- 20052: 友链修改 (blog:friendLink:edit)
- 20053: 友链删除 (blog:friendLink:remove)
- 20054: 友链导出 (blog:friendLink:export)

**博客设置（20060-20069）**
- 20060: 设置查询 (blog:setting:query)
- 20061: 设置修改 (blog:setting:edit)
- 20062: 设置导出 (blog:setting:export)

**关于作者（20070-20079）**
- 20070: 关于查询 (blog:about:query)
- 20071: 关于修改 (blog:about:edit)

### 2. 数据统计模块（3000-3999）

#### 一级菜单
- **menu_id**: 3000
- **menu_name**: 数据统计
- **path**: statistics
- **icon**: chart

#### 二级菜单

| ID | 名称 | 路径 | 组件路径 | 权限标识 | 图标 |
|----|------|------|---------|---------|------|
| 3001 | 访问统计 | visit | admin/statistics/visit/index | statistics:visit:list | eye |
| 3002 | 文章统计 | article | admin/statistics/article/index | statistics:article:list | data-analysis |
| 3003 | 用户统计 | user | admin/statistics/user/index | statistics:user:list | peoples |

#### 按钮权限

**访问统计（30010-30019）**
- 30010: 访问查询 (statistics:visit:query)
- 30011: 访问导出 (statistics:visit:export)

**文章统计（30020-30029）**
- 30020: 文章统计查询 (statistics:article:query)
- 30021: 文章统计导出 (statistics:article:export)

**用户统计（30030-30039）**
- 30030: 用户统计查询 (statistics:user:query)
- 30031: 用户统计导出 (statistics:user:export)

## 🔐 字段说明

### 菜单表字段

| 字段名 | 类型 | 说明 | 示例值 |
|--------|------|------|--------|
| menu_id | bigint | 菜单ID | 2001 |
| menu_name | varchar(50) | 菜单名称 | 文章管理 |
| parent_id | bigint | 父菜单ID | 2000 |
| order_num | int | 显示顺序 | 1 |
| path | varchar(200) | 路由地址 | article |
| component | varchar(255) | 组件路径 | admin/blog/article/article/index |
| query | varchar(255) | 路由参数 | '' |
| route_name | varchar(50) | 路由名称 | '' |
| is_frame | int | 是否外链 | 1（否）, 0（是） |
| is_cache | int | 是否缓存 | 0（缓存）, 1（不缓存） |
| menu_type | char(1) | 菜单类型 | M（目录）, C（菜单）, F（按钮） |
| visible | char(1) | 显示状态 | '0'（显示）, '1'（隐藏） |
| status | char(1) | 菜单状态 | '0'（正常）, '1'（停用） |
| perms | varchar(100) | 权限标识 | blog:article:list |
| icon | varchar(100) | 菜单图标 | edit |

### 重要字段值说明

**menu_type（菜单类型）**
- `M`: 目录（一级菜单）
- `C`: 菜单（二级菜单，可点击的页面）
- `F`: 按钮（三级菜单，页面内的操作权限）

**visible（显示状态）**
- `'0'`: 显示（注意是字符串）
- `'1'`: 隐藏

**status（菜单状态）**
- `'0'`: 正常（注意是字符串）
- `'1'`: 停用

**is_frame（是否外链）**
- `1`: 否（内部路由）
- `0`: 是（外部链接）

**is_cache（是否缓存）**
- `0`: 缓存
- `1`: 不缓存

## 📝 使用说明

### 1. 安装菜单体系

```bash
# 执行完整菜单体系SQL
mysql -u root -p newblog < ruoyi-vue/sql/complete_menu_system.sql
```

**注意**：此脚本会删除所有 menu_id >= 2000 的菜单，请谨慎使用！

### 2. 验证安装

执行后会自动显示验证信息，包括：
- 一级菜单列表
- 各模块菜单详情
- 菜单数量统计
- 管理员权限统计

### 3. 刷新后台

- 退出后台管理系统
- 重新登录
- 或按 Ctrl+F5 强制刷新浏览器

### 4. 查看菜单

登录后应该能看到：
```
├── 系统管理
├── 系统监控
├── 系统工具
├── 若依官网
├── 博客管理 ← 新增
│   ├── 文章管理
│   ├── 分类管理
│   ├── 标签管理
│   ├── 评论管理
│   ├── 友链管理
│   ├── 博客设置
│   └── 关于作者
└── 数据统计 ← 新增
    ├── 访问统计
    ├── 文章统计
    └── 用户统计
```

## 🔧 扩展指南

### 添加新模块

1. **选择ID范围**：根据规划选择未使用的ID段（如4000-4999）

2. **创建一级菜单**：
```sql
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (4000, '新模块', 0, 7, 'newmodule', NULL, '', '', 1, 0, 'M', '0', '0', '', 'icon-name', 'admin', NOW(), '', NULL, '新模块说明');
```

3. **创建二级菜单**：
```sql
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (4001, '功能页面', 4000, 1, 'page', 'path/to/component', '', '', 1, 0, 'C', '0', '0', 'module:page:list', 'icon', 'admin', NOW(), '', NULL, '功能说明');
```

4. **创建按钮权限**：
```sql
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, query, route_name, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
(40010, '查询', 4001, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'module:page:query', '#', 'admin', NOW(), '', NULL, ''),
(40011, '新增', 4001, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'module:page:add', '#', 'admin', NOW(), '', NULL, '');
```

5. **分配权限**：
```sql
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu WHERE menu_id >= 4000 AND menu_id < 5000;
```

## ⚠️ 注意事项

1. **字段类型**：
   - `visible` 和 `status` 必须使用字符串 `'0'` 或 `'1'`
   - `is_frame` 和 `is_cache` 使用数字 `0` 或 `1`

2. **ID规划**：
   - 严格按照规划的ID范围分配
   - 预留足够的ID空间用于扩展

3. **权限标识**：
   - 格式：`模块:功能:操作`
   - 示例：`blog:article:add`

4. **组件路径**：
   - 相对于 `src/views/` 目录
   - 不需要 `.vue` 后缀

5. **备份**：
   - 执行前务必备份数据库
   - 脚本会删除现有菜单数据

## 📞 技术支持

如有问题，请查看：
- 诊断脚本：`ruoyi-vue/sql/diagnose_menu.sql`
- 修