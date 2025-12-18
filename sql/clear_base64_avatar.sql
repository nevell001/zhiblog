-- 清理Base64格式头像，替换为空字符串
-- 这样用户就可以重新上传文件格式的头像

-- 更新头像设置为空字符串
UPDATE `blog_setting`
SET `config_value` = '',
    `update_time` = NOW()
WHERE `config_key` = 'blog_avatar'
AND (`config_value` LIKE 'data:image/%' OR `config_value` LIKE 'data:%');

-- 验证更新结果
SELECT
    config_key,
    CASE
        WHEN config_value LIKE 'data:image/%' THEN 'Base64图片格式'
        WHEN config_value LIKE 'data:%' THEN 'Base64格式'
        WHEN config_value = '' THEN '已清空'
        WHEN config_value IS NULL THEN 'NULL'
        ELSE 'URL格式: ' + LEFT(config_value, 50) + '...'
    END as avatar_status,
    update_time
FROM blog_setting
WHERE config_key = 'blog_avatar';

-- 输出处理结果
SELECT '✅ Base64头像已清空，用户现在可以上传文件格式的头像' as message;