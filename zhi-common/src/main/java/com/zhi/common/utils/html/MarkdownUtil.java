package com.zhi.common.utils.html;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

/**
 * Markdown 渲染工具（flexmark）
 *
 * <p>用于博客 Markdown 文章的“源码入库 + 服务端渲染 HTML 双写”。</p>
 *
 * @author nevell
 * @date 2026-09-05
 */
public class MarkdownUtil
{
    private static final Parser PARSER = Parser.builder().build();

    private static final HtmlRenderer RENDERER = HtmlRenderer.builder().build();

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
