-- ========================================
-- 检查并修复菜单路径配置
-- ========================================

USE newblog;

-- 1. 查看当前菜单路径配置
SELECT '当前菜单路径配置:' AS '';
SELECT menu_id, menu_name, path, component, menu_type 
FROM sys_menu 
WHERE menu_id BETWEEN 2000 AND 3999
ORDER BY menu_id;

-- 2. 检查问题
SELECT '' AS '';
SELECT '问题检查:' AS '';
SELECT 
    CASE 
        WHEN path LIKE '/%' AND menu_type = 'C' AND parent_id != 0 
        THEN CONCAT('❌ 菜单 ', menu_name, ' (ID:', menu_id, ') 的path应该是相对路径，当前是: ', path)
        ELSE CONCAT('✅ 菜单 ', menu_name, ' (ID:', menu_id, ') 路径正确')
    END AS '检查结果'
FROM sys_menu 
WHERE menu_id BETWEEN 2000 AND 3999
ORDER BY menu_id;

-- 3. 修复路径（如果需要）
SELECT '' AS '';
SELECT '开始修复路径...' AS '';

-- 修复博客管理子菜单的路径（应该是相对路径，不带/）
UPDATE sys_menu SET path = 'article' WHERE menu_id = 2001 AND path != 'article';
UPDATE sys_menu SET path = 'category' WHERE menu_id = 2002 AND path != 'category';
UPDATE sys_menu SET path = 'tag' WHERE menu_id = 2003 AND path != 'tag';
UPDATE sys_menu SET path = 'comment' WHERE menu_id = 2004 AND path != 'comment';
UPDATE sys_menu SET path = 'friendLink' WHERE menu_id = 2005 AND path != 'friendLink';
UPDATE sys_menu SET path = 'setting' WHERE menu_id = 2006 AND path != 'setting';
UPDATE sys_menu SET path = 'about' WHERE menu_id = 2007 AND path != 'about';

-- 修复数据统计子菜单的路径
UPDATE sys_menu SET path = 'visit' WHERE menu_id = 3001 AND path != 'visit';
UPDATE sys_menu SET path = 'article' WHERE menu_id = 3002 AND path != 'article';
UPDATE sys_menu SET path = 'user' WHERE menu_id = 3003 AND path != 'user';

SELECT '路径修复完成!' AS '';

-- 4. 验证修复结果
SELECT '' AS '';
SELECT '修复后的路径配置:' AS '';
SELECT menu_id, menu_name, path, component, menu_type 
FROM sys_menu 
WHERE menu_id BETWEEN 2000 AND 3999
ORDER BY menu_id;

-- 5. 显示完整的菜单树结构
SELECT '' AS '';
SELECT '完整菜单树结构:' AS '';
SELECT 
    CASE 
        WHEN parent_id = 0 THEN CONCAT('📁 ', menu_name, ' (', path, ')')
        ELSE CONCAT('   └─ ', menu_name, ' (', path, ')')
    END AS '菜单结构'
FROM sys_menu 
WHERE menu_id BETWEEN 2000 AND 3999
ORDER BY 
    CASE WHEN parent_id = 0 THEN menu_id ELSE parent_id END,
    CASE WHEN parent_id = 0 THEN 0 ELSE menu_id END;

SELECT '' AS '';
SELECT '提示: 修复完成后，请:' AS '';
SELECT '1. 退出登录' AS '';
SELECT '2. 清除浏览器缓存 (Ctrl+Shift+Delete)' AS '';
SELECT '3. 重新登录系统' AS '';
SELECT '4. 检查菜单是否正常显示' AS '';