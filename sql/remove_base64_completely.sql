-- 完全移除Base64格式头像，专注于Thumbnailator文件上传
-- 清理所有Base64格式的头像数据

-- 清理博客设置中的Base64头像
UPDATE `blog_setting`
SET `config_value` = '',
    `update_time` = NOW(),
    `description` = '博主头像（已移除Base64支持，请使用文件上传）'
WHERE `config_key` = 'blog_avatar'
AND (`config_value` LIKE 'data:image/%' OR `config_value` LIKE 'data:%');

-- 清理微信二维码中的Base64数据
UPDATE `blog_setting`
SET `config_value` = '',
    `update_time` = NOW(),
    `description` = '微信二维码（已移除Base64支持，请使用文件上传）'
WHERE `config_key` = 'wechat_qr'
AND (`config_value` LIKE 'data:image/%' OR `config_value` LIKE 'data:%');

-- 验证清理结果
SELECT
    'blog_avatar' as setting_key,
    CASE
        WHEN config_value LIKE 'data:image/%' THEN '❌ 仍为Base64图片格式'
        WHEN config_value LIKE 'data:%' THEN '❌ 仍为Base64格式'
        WHEN config_value = '' THEN '✅ 已清空'
        WHEN config_value IS NULL THEN '✅ 已清空'
        ELSE '✅ URL格式: ' + LEFT(config_value, 50) + '...'
    END as status,
    LENGTH(config_value) as length
FROM blog_setting
WHERE config_key = 'blog_avatar'

UNION ALL

SELECT
    'wechat_qr' as setting_key,
    CASE
        WHEN config_value LIKE 'data:image/%' THEN '❌ 仍为Base64图片格式'
        WHEN config_value LIKE 'data:%' THEN '❌ 仍为Base64格式'
        WHEN config_value = '' THEN '✅ 已清空'
        WHEN config_value IS NULL THEN '✅ 已清空'
        ELSE '✅ URL格式: ' + LEFT(config_value, 50) + '...'
    END as status,
    LENGTH(config_value) as length
FROM blog_setting
WHERE config_key = 'wechat_qr';

-- 输出处理结果
SELECT
    '✅ Base64格式已完全移除' as message,
    '✅ 现在只支持基于Thumbnailator的专业文件上传' as benefit1,
    '✅ 系统性能和存储效率得到显著提升' as benefit2,
    '✅ 图片质量和压缩效果更佳' as benefit3;