-- 修改sys_config表的config_value字段长度限制
ALTER TABLE sys_config MODIFY COLUMN config_value varchar(1000) DEFAULT '' COMMENT '参数键值';

-- 添加索引以提高查询性能
CREATE INDEX idx_config_key ON sys_config(config_key);