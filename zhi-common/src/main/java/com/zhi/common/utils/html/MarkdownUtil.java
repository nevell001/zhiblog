package com.zhi.common.utils.html;

import java.util.Collections;
import java.util.List;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.builder.Extension;

/**
 * Markdown 渲染工具（flexmark）
 *
 * <p>启用 GFM 表格扩展（与前端 marked 预览保持一致），
 * 用于博客 Markdown 文章的“源码入库 + 服务端渲染 HTML 双写”。</p>
 *
 * @author nevell
 * @date 2026-09-05
 */
public class MarkdownUtil
{
    private static final List<Extension> EXTENSIONS =
            Collections.singletonList(TablesExtension.create());

    private static final Parser PARSER = Parser.builder().extensions(EXTENSIONS).build();

    private static final HtmlRenderer RENDERER = HtmlRenderer.builder().extensions(EXTENSIONS).build();

    /**
     * 将 Markdown 源码渲染为 HTML
     *
     * @param markdown Markdown 源码（可为 null）
     * @return 渲染后的 HTML（空输入返回空串）
     */
    public static String toHtml(String markdown)
    {
        if (markdown == null || markdown.isEmpty())
        {
            return "";
        }
        return RENDERER.render(PARSER.parse(markdown));
    }
}
