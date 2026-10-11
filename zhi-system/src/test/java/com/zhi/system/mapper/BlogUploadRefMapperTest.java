package com.zhi.system.mapper;

import com.zhi.system.domain.BlogUploadReference;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 媒体库删除前的引用检查（countFileReferences）真实 SQL 测试。
 *
 * <p>删除媒体库文件会连带删物理文件，若文章封面/正文、自定义页面、站点设置、友链 Logo、
 * 用户头像里还引用着这个路径，删完引用处就会挂图。这条检查决定「拒绝删除」，
 * 必须在真实 SQL 上验证：等值封面怎么算、正文里绝对 URL 能不能匹配、各表的
 * del_flag 过滤是否生效 —— mock 掉 mapper 的 Service 测试覆盖不到这些语义。</p>
 */
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@MapperScan("com.zhi.system.mapper")
@TestPropertySource(properties = {
    "mybatis.mapperLocations=classpath:mapper/system/BlogUploadRefMapper.xml",
    "mybatis.typeAliasesPackage=com.zhi.system.domain",
    "spring.sql.init.mode=always"
})
@Sql(scripts = "/schema-blog-upload-ref.sql")
class BlogUploadRefMapperTest
{
    private static final String URL = "/profile/upload/2026/10/cover_20261001.jpg";

    @Autowired
    private BlogUploadRefMapper blogUploadRefMapper;

    /**
     * 五个来源全部命中：封面、正文（相对路径）、正文（绝对 URL）、Markdown 源码、页面、
     * 设置、友链 Logo、用户头像都算引用；已删除的行不算。
     */
    @Test
    @Sql(scripts = "/schema-blog-upload-ref.sql", statements = {
        "DELETE FROM blog_article",
        "DELETE FROM blog_page",
        "DELETE FROM blog_setting",
        "DELETE FROM blog_friend_link",
        "DELETE FROM sys_user",
        "INSERT INTO blog_article (id, title, content, content_md, cover_url, del_flag) VALUES (1, '封面引用', '<p>正文</p>', NULL, '" + URL + "', '0')",
        "INSERT INTO blog_article (id, title, content, content_md, cover_url, del_flag) VALUES (2, '正文绝对URL', '<img src=\"https://example.com" + URL + "\">', NULL, NULL, '0')",
        "INSERT INTO blog_article (id, title, content, content_md, cover_url, del_flag) VALUES (3, 'Markdown引用', '<p>正文</p>', '![](" + URL + ")', NULL, '0')",
        "INSERT INTO blog_article (id, title, content, content_md, cover_url, del_flag) VALUES (4, '已删除文章', '<p>正文</p>', NULL, '" + URL + "', '1')",
        "INSERT INTO blog_page (id, content, del_flag) VALUES (1, '<p>页面引用 " + URL + "</p>', 0)",
        "INSERT INTO blog_page (id, content, del_flag) VALUES (2, '<p>已删除页面 " + URL + "</p>', 1)",
        "INSERT INTO blog_setting (id, config_key, config_value) VALUES (1, 'blog_avatar', '" + URL + "')",
        "INSERT INTO blog_friend_link (id, logo, del_flag) VALUES (1, '" + URL + "', 0)",
        "INSERT INTO blog_friend_link (id, logo, del_flag) VALUES (2, '" + URL + "', 1)",
        "INSERT INTO sys_user (user_id, avatar, del_flag) VALUES (1, '" + URL + "', '0')",
        "INSERT INTO sys_user (user_id, avatar, del_flag) VALUES (2, '" + URL + "', '2')"
    })
    void referencesAcrossAllSourcesAreCountedAndDeletedRowsExcluded()
    {
        Map<String, Long> counts = countByType(URL);

        assertEquals(5, counts.size(), "五个引用来源都应返回一行统计");
        assertEquals(3L, counts.get("article").longValue(), "封面 + 正文相对/绝对路径 + Markdown 共 3 篇，已删除文章不算");
        assertEquals(1L, counts.get("page").longValue(), "已删除的自定义页面不算引用");
        assertEquals(1L, counts.get("setting").longValue(), "站点设置（如博主头像）算引用");
        assertEquals(1L, counts.get("friendLink").longValue(), "已删除的友链不算引用");
        assertEquals(1L, counts.get("userAvatar").longValue(), "已删除的用户不算引用");
    }

    /**
     * 该文件没有任何引用时，五个来源都应返回 0（删除才放行）。
     */
    @Test
    @Sql(scripts = "/schema-blog-upload-ref.sql", statements = {
        "DELETE FROM blog_article",
        "DELETE FROM blog_page",
        "DELETE FROM blog_setting",
        "DELETE FROM blog_friend_link",
        "DELETE FROM sys_user",
        "INSERT INTO blog_article (id, title, content, content_md, cover_url, del_flag) VALUES (1, '别的文件', '<img src=\"/profile/upload/2026/10/other.jpg\">', NULL, '/profile/upload/2026/10/other.jpg', '0')",
        "INSERT INTO blog_page (id, content, del_flag) VALUES (1, '<p>/profile/upload/2026/10/other.jpg</p>', 0)",
        "INSERT INTO sys_user (user_id, avatar, del_flag) VALUES (1, '/profile/avatar/1.png', '0')"
    })
    void unrelatedFilesAreNotCounted()
    {
        Map<String, Long> counts = countByType(URL);

        assertEquals(5, counts.size(), "没有引用时也应返回五个来源的全零统计");
        counts.forEach((type, count) -> assertEquals(0L, count.longValue(), type + " 不应误判为引用"));
    }

    /**
     * 相似文件名不能被误判：检查靠 url 子串匹配，验证不同文件名之间互不影响。
     */
    @Test
    @Sql(scripts = "/schema-blog-upload-ref.sql", statements = {
        "DELETE FROM blog_article",
        "DELETE FROM blog_page",
        "DELETE FROM blog_setting",
        "DELETE FROM blog_friend_link",
        "DELETE FROM sys_user",
        "INSERT INTO blog_article (id, title, content, cover_url, del_flag) VALUES (1, '同目录另一个文件', '<p>正文</p>', '/profile/upload/2026/10/cover_20261002.jpg', '0')"
    })
    void similarFileNamesInSameDirectoryDoNotMatch()
    {
        Map<String, Long> counts = countByType(URL);

        assertEquals(0L, counts.get("article").longValue(), "同目录下另一个文件名不应算作对该文件的引用");
    }

    private Map<String, Long> countByType(String url)
    {
        List<BlogUploadReference> rows = blogUploadRefMapper.countFileReferences(url);
        return rows.stream()
            .collect(Collectors.toMap(BlogUploadReference::getRefType, BlogUploadReference::getRefCount));
    }
}
