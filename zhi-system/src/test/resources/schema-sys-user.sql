-- sys_user 相关表的最小 H2 结构（列取自 sql/00_init_database.sql）
-- 仅覆盖 SysUserMapper 的 selectUserVo / insertUser 所需列

CREATE TABLE IF NOT EXISTS sys_user (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dept_id BIGINT,
    user_name VARCHAR(30),
    nick_name VARCHAR(30),
    email VARCHAR(50),
    avatar VARCHAR(100),
    phonenumber VARCHAR(11),
    password VARCHAR(100),
    sex CHAR(1),
    status CHAR(1),
    del_flag CHAR(1) DEFAULT '0',
    login_ip VARCHAR(128),
    login_date TIMESTAMP,
    pwd_update_date TIMESTAMP,
    create_by VARCHAR(64),
    create_time TIMESTAMP,
    update_by VARCHAR(64),
    update_time TIMESTAMP,
    remark VARCHAR(500),
    user_type VARCHAR(2) DEFAULT '00'
);

CREATE TABLE IF NOT EXISTS sys_dept (
    dept_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_id BIGINT DEFAULT 0,
    ancestors VARCHAR(50),
    dept_name VARCHAR(30),
    order_num INT,
    leader VARCHAR(20),
    status CHAR(1)
);

CREATE TABLE IF NOT EXISTS sys_role (
    role_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(30),
    role_key VARCHAR(100),
    role_sort INT,
    data_scope CHAR(1),
    status CHAR(1)
);

CREATE TABLE IF NOT EXISTS sys_user_role (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL
);
