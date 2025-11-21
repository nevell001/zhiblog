#!/usr/bin/env python3
import mysql.connector
import json

# 数据库配置
DB_CONFIG = {
    'host': 'localhost',
    'port': 3306,
    'user': 'root',
    'password': 'root',
    'database': 'newblog'
}

def check_permissions():
    """检查博客管理权限配置"""
    try:
        # 连接数据库
        conn = mysql.connector.connect(**DB_CONFIG)
        cursor = conn.cursor(dictionary=True)
        
        print("=== 数据库连接成功 ===")
        
        # 1. 检查博客管理菜单
        print("\n1. 检查博客管理菜单:")
        cursor.execute("""
            SELECT menu_id, menu_name, parent_id, path, component, menu_type, perms, icon 
            FROM sys_menu 
            WHERE menu_id = 2000 OR parent_id = 2000
            ORDER BY menu_id
        """)
        
        blog_menus = cursor.fetchall()
        for menu in blog_menus:
            print(f"  ID: {menu['menu_id']}, 名称: {menu['menu_name']}, 路径: {menu['path']}, 权限: {menu['perms']}")
        
        # 2. 检查管理员角色权限
        print("\n2. 检查管理员角色权限:")
        cursor.execute("""
            SELECT rm.role_id, rm.menu_id, m.menu_name, m.perms
            FROM sys_role_menu rm
            JOIN sys_menu m ON rm.menu_id = m.menu_id
            WHERE rm.role_id = 1 AND (m.menu_id = 2000 OR m.parent_id = 2000)
            ORDER BY m.menu_id
        """)
        
        role_permissions = cursor.fetchall()
        for perm in role_permissions:
            print(f"  角色: {perm['role_id']}, 菜单: {perm['menu_name']} ({perm['menu_id']}), 权限: {perm['perms']}")
        
        # 3. 检查按钮权限
        print("\n3. 检查按钮权限:")
        cursor.execute("""
            SELECT menu_id, menu_name, perms
            FROM sys_menu 
            WHERE parent_id IN (2001, 2002, 2003, 2004, 2005, 2006)
            ORDER BY parent_id, menu_id
        """)
        
        button_permissions = cursor.fetchall()
        for perm in button_permissions:
            print(f"  菜单: {perm['menu_name']} ({perm['menu_id']}), 权限: {perm['perms']}")
        
        # 4. 检查角色按钮权限
        print("\n4. 检查角色按钮权限:")
        cursor.execute("""
            SELECT rm.role_id, rm.menu_id, m.menu_name, m.perms
            FROM sys_role_menu rm
            JOIN sys_menu m ON rm.menu_id = m.menu_id
            WHERE rm.role_id = 1 AND m.parent_id IN (2001, 2002, 2003, 2004, 2005, 2006)
            ORDER BY m.parent_id, m.menu_id
        """)
        
        role_button_permissions = cursor.fetchall()
        for perm in role_button_permissions:
            print(f"  角色: {perm['role_id']}, 按钮: {perm['menu_name']} ({perm['menu_id']}), 权限: {perm['perms']}")
        
        # 5. 检查博客数据表
        print("\n5. 检查博客数据表:")
        cursor.execute("""
            SHOW TABLES LIKE 'blog_%'
        """)
        
        blog_tables = cursor.fetchall()
        for table in blog_tables:
            table_name = list(table.values())[0]
            cursor.execute(f"SELECT COUNT(*) as count FROM {table_name}")
            count = cursor.fetchone()['count']
            print(f"  表: {table_name}, 记录数: {count}")
        
        cursor.close()
        conn.close()
        
        print("\n=== 检查完成 ===")
        
    except Exception as e:
        print(f"错误: {e}")

if __name__ == "__main__":
    check_permissions()