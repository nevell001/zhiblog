package com.zhi.common.filter;

import java.io.IOException;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 防盗链过滤器
 *
 * <p>开关与允许域名由调用方（{@code RefererPolicy}）在运行时提供，
 * 以便后台可动态管理域名白名单，无需重启应用。</p>
 *
 * @author nevell
 */
public class RefererFilter implements Filter
{
    /**
     * 防盗链是否启用（运行时读取）
     */
    private final BooleanSupplier enabledSupplier;

    /**
     * 允许的域名列表（运行时读取）
     */
    private final Supplier<List<String>> allowedDomainsSupplier;

    public RefererFilter(BooleanSupplier enabledSupplier, Supplier<List<String>> allowedDomainsSupplier)
    {
        this.enabledSupplier = enabledSupplier;
        this.allowedDomainsSupplier = allowedDomainsSupplier;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        // 域名白名单改由 RefererPolicy 运行时提供
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // 防盗链未启用时直接放行
        if (!enabledSupplier.getAsBoolean())
        {
            chain.doFilter(request, response);
            return;
        }

        String referer = req.getHeader("Referer");
        List<String> allowedDomains = allowedDomainsSupplier.get();

        if (isAllowed(referer, req.getServerName(), allowedDomains))
        {
            chain.doFilter(request, response);
        }
        else
        {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: Referer '" + referer + "' is not allowed");
        }
    }

    /**
     * 防盗链判定。
     *
     * <p>放行规则（按顺序）：</p>
     * <ol>
     *   <li>Referer 为空 —— 爬虫、直接访问与隐私工具会剥离 Referer，而热链请求必然带 Referer，
     *       所以放行空值既不影响防盗链效果，又不会误伤正常访问；</li>
     *   <li>Referer 主机与请求自身主机相同 —— 同源访问永远允许；</li>
     *   <li>Referer 主机命中白名单 —— <b>精确相等</b>，或是白名单域名的子域名。</li>
     * </ol>
     *
     * <p>注意不能用 {@code referer.contains(domain)} 之类的子串匹配：白名单里的
     * {@code example.com} 会连 {@code https://evil-example.com} 与
     * {@code https://evil.io/?u=example.com} 一起放行；这里统一按主机比较，
     * 端口/协议/路径/大小写都不参与判断。</p>
     *
     * @param referer        Referer 请求头（可为 null）
     * @param requestHost    当前请求的主机名（同源判断用）
     * @param allowedDomains 允许的域名列表
     * @return 是否放行
     */
    public static boolean isAllowed(String referer, String requestHost, List<String> allowedDomains)
    {
        if (referer == null || referer.trim().isEmpty())
        {
            return true;
        }

        String refererHost = extractHost(referer);
        if (refererHost == null)
        {
            // 畸形 Referer（无法解析出主机）按不放行处理
            return false;
        }

        String selfHost = normalizeHost(requestHost);
        if (selfHost != null && selfHost.equals(refererHost))
        {
            return true;
        }

        if (allowedDomains == null || allowedDomains.isEmpty())
        {
            return false;
        }

        for (String domain : allowedDomains)
        {
            String allowed = normalizeHost(domain);
            if (allowed == null)
            {
                continue;
            }
            if (refererHost.equals(allowed) || refererHost.endsWith("." + allowed))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 从 Referer 中取出主机名，支持完整 URL、协议相对（//host/x）与裸域名（host:port）
     */
    private static String extractHost(String referer)
    {
        String value = referer.trim();
        String candidate = value;
        if (value.startsWith("//"))
        {
            candidate = "http:" + value;
        }
        else if (value.indexOf("://") < 0)
        {
            candidate = "http://" + value;
        }
        try
        {
            return normalizeHost(new java.net.URI(candidate).getHost());
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * 主机名归一化：去掉协议、路径、端口、前导通配符与首尾点，统一小写
     */
    private static String normalizeHost(String host)
    {
        if (host == null)
        {
            return null;
        }
        String value = host.trim().toLowerCase(java.util.Locale.ROOT);
        if (value.isEmpty())
        {
            return null;
        }
        int scheme = value.indexOf("://");
        if (scheme >= 0)
        {
            value = value.substring(scheme + 3);
        }
        int at = value.indexOf('@');
        if (at >= 0)
        {
            value = value.substring(at + 1);
        }
        int cut = value.length();
        for (char separator : new char[] { '/', '?', '#' })
        {
            int index = value.indexOf(separator);
            if (index >= 0 && index < cut)
            {
                cut = index;
            }
        }
        value = value.substring(0, cut);
        if (value.startsWith("["))
        {
            // IPv6 字面量
            int end = value.indexOf(']');
            value = end > 0 ? value.substring(1, end) : value;
        }
        else
        {
            int colon = value.lastIndexOf(':');
            if (colon >= 0 && value.indexOf(':') == colon)
            {
                value = value.substring(0, colon);
            }
        }
        if (value.startsWith("*."))
        {
            value = value.substring(2);
        }
        while (value.startsWith(".") || value.endsWith("."))
        {
            value = value.startsWith(".") ? value.substring(1) : value.substring(0, value.length() - 1);
        }
        return value.isEmpty() ? null : value;
    }

    @Override
    public void destroy()
    {
        // 无需清理资源
    }
}
