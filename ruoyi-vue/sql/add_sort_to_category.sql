-- 添加sort字段到blog_category表
ALTER TABLE blog_category ADD COLUMN sort INT(11) DEFAULT 0 COMMENT '排序';

-- 将sort_order的值复制到sort字段，保持数据一致性
UPDATE blog_category SET sort = sort_order;

-- 验证字段是否添加成功
SELECT id, name, sort, sort_order FROM blog_category LIMIT 5;

-- 显示操作完成信息
SELECT 'blog_category表添加sort字段成功！' AS result;