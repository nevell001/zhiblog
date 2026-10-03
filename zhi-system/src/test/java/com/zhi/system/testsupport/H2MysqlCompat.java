package com.zhi.system.testsupport;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

/**
 * H2 不实现 MySQL 的 DATE_FORMAT，而 mapper XML 里有 8 处依赖它（归档、登录日志按月统计等）。
 * schema.sql 用 CREATE ALIAS 把函数名指向这里，让这些 SQL 能在 H2 上真实执行而不是被禁用。
 */
public final class H2MysqlCompat {

    private H2MysqlCompat() {
    }

    public static String dateFormat(Timestamp value, String mysqlPattern) {
        if (value == null || mysqlPattern == null) {
            return null;
        }
        return value.toLocalDateTime().format(DateTimeFormatter.ofPattern(toJavaPattern(mysqlPattern)));
    }

    private static String toJavaPattern(String mysqlPattern) {
        StringBuilder java = new StringBuilder(mysqlPattern.length());
        for (int i = 0; i < mysqlPattern.length(); i++) {
            char ch = mysqlPattern.charAt(i);
            if (ch == '%' && i + 1 < mysqlPattern.length()) {
                switch (mysqlPattern.charAt(++i)) {
                    case 'Y' -> java.append("yyyy");
                    case 'm' -> java.append("MM");
                    case 'd' -> java.append("dd");
                    default -> throw new IllegalArgumentException(
                            "H2MysqlCompat 未实现该 MySQL 日期格式符，请按需补充: " + mysqlPattern);
                }
            } else {
                java.append(ch);
            }
        }
        return java.toString();
    }
}
