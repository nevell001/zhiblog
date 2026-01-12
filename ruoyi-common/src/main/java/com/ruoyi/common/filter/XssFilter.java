package com.ruoyi.common.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.enums.HttpMethod;

/**
 * 防止XSS攻击的过滤器
 * 
 * @author ruoyi
 */
public class XssFilter implements Filter
{
    /**
     * 排除链接
     */
    public List<String> excludes = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        String tempExcludes = filterConfig.getInitParameter("excludes");
        System.out.println("[XssFilter] init - excludes parameter: " + tempExcludes);
        if (StringUtils.isNotEmpty(tempExcludes))
        {
            String[] urls = tempExcludes.split(",");
            for (String url : urls)
            {
                excludes.add(url);
            }
        }
        System.out.println("[XssFilter] init - excludes list: " + excludes);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String url = req.getServletPath();
        String method = req.getMethod();
        System.out.println("[XssFilter] doFilter - url: " + url + ", method: " + method);

        if (handleExcludeURL(req, resp))
        {
            System.out.println("[XssFilter] doFilter - URL excluded, skipping XSS filter");
            chain.doFilter(request, response);
            return;
        }
        System.out.println("[XssFilter] doFilter - URL NOT excluded, applying XSS filter");
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);
        chain.doFilter(xssRequest, response);
    }

    private boolean handleExcludeURL(HttpServletRequest request, HttpServletResponse response)
    {
        String url = request.getServletPath();
        String method = request.getMethod();
        // GET DELETE 不过滤
        if (method == null || HttpMethod.GET.matches(method) || HttpMethod.DELETE.matches(method))
        {
            return true;
        }
        boolean matches = StringUtils.matches(url, excludes);
        System.out.println("[XssFilter] handleExcludeURL - url: " + url + ", matches: " + matches + ", excludes: " + excludes);
        return matches;
    }

    @Override
    public void destroy()
    {

    }
}