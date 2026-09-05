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

        // 如果Referer为空，拒绝访问
        if (referer == null || referer.isEmpty())
        {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: Referer header is required");
            return;
        }

        // 检查Referer是否在允许的域名列表中
        List<String> allowedDomains = allowedDomainsSupplier.get();
        boolean allowed = false;
        if (allowedDomains != null)
        {
            for (String domain : allowedDomains)
            {
                if (referer.contains(domain))
                {
                    allowed = true;
                    break;
                }
            }
        }

        // 根据检查结果决定是否放行
        if (allowed)
        {
            chain.doFilter(request, response);
        }
        else
        {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied: Referer '" + referer + "' is not allowed");
        }
    }

    @Override
    public void destroy()
    {
        // 无需清理资源
    }
}
