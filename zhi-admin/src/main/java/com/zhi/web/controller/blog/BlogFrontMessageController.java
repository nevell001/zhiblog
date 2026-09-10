package com.zhi.web.controller.blog;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zhi.common.annotation.Anonymous;
import com.zhi.common.annotation.RateLimiter;
import com.zhi.common.core.controller.BaseController;
import com.zhi.common.core.domain.AjaxResult;
import com.zhi.common.core.domain.model.LoginUser;
import com.zhi.common.core.page.TableDataInfo;
import com.zhi.common.enums.LimitType;
import com.zhi.common.utils.StringUtils;
import com.zhi.common.utils.ip.IpUtils;
import com.zhi.system.domain.BlogMessage;
import com.zhi.system.service.IBlogMessageService;
import com.zhi.system.service.IBlogSettingService;

/**
 * 留言板前台接口
 * 
 * @author nevell
 * @date 2026-09-10
 */
@RestController
@RequestMapping("/blog/message")
public class BlogFrontMessageController extends BaseController
{
    /** 昵称最大长度 */
    private static final int MAX_NICKNAME_LENGTH = 50;

    /** 留言内容最大长度 */
    private static final int MAX_CONTENT_LENGTH = 500;

    /** 网站地址最大长度 */
    private static final int MAX_WEBSITE_LENGTH = 255;

    /** 邮箱最大长度 */
    private static final int MAX_EMAIL_LENGTH = 100;

    /** UA 存储最大长度 */
    private static final int MAX_USER_AGENT_LENGTH = 255;

    @Autowired
    private IBlogMessageService blogMessageService;

    @Autowired
    private IBlogSettingService blogSettingService;

    /**
     * 查询留言列表（前台用，仅已发布，支持分页）
     */
    @Anonymous
    @GetMapping("/list")
    public TableDataInfo list(BlogMessage blogMessage)
    {
        startPage();
        List<BlogMessage> list = blogMessageService.selectPublishedMessageList();
        return getDataTable(list);
    }

    /**
     * 查询已发布留言总数
     */
    @Anonymous
    @GetMapping("/count")
    public AjaxResult count()
    {
        return success(blogMessageService.selectPublishedMessageCount());
    }

    /**
     * 提交留言（匿名可提交，提交后按评论审核开关决定是否需要审核）
     */
    @Anonymous
    @RateLimiter(key = "blog:message:", time = 60, count = 5, limitType = LimitType.IP)
    @PostMapping
    public AjaxResult add(@RequestBody BlogMessage blogMessage, HttpServletRequest request)
    {
        String nickname = blogMessage.getNickname();
        String content = blogMessage.getContent();
        if (StringUtils.isEmpty(nickname) || nickname.trim().isEmpty())
        {
            return error("昵称不能为空");
        }
        if (nickname.trim().length() > MAX_NICKNAME_LENGTH)
        {
            return error("昵称长度不能超过" + MAX_NICKNAME_LENGTH + "个字符");
        }
        if (StringUtils.isEmpty(content) || content.trim().isEmpty())
        {
            return error("留言内容不能为空");
        }
        if (content.length() > MAX_CONTENT_LENGTH)
        {
            return error("留言内容长度不能超过" + MAX_CONTENT_LENGTH + "个字符");
        }
        if (blogMessage.getEmail() != null && blogMessage.getEmail().length() > MAX_EMAIL_LENGTH)
        {
            return error("邮箱长度不能超过" + MAX_EMAIL_LENGTH + "个字符");
        }
        if (blogMessage.getWebsite() != null && blogMessage.getWebsite().length() > MAX_WEBSITE_LENGTH)
        {
            return error("网站地址长度不能超过" + MAX_WEBSITE_LENGTH + "个字符");
        }

        // 登录用户自动补全 userId 与昵称
        try
        {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof LoginUser loginUser)
            {
                if (blogMessage.getUserId() == null)
                {
                    blogMessage.setUserId(loginUser.getUserId());
                }
                if (StringUtils.isEmpty(blogMessage.getNickname()))
                {
                    blogMessage.setNickname(loginUser.getUser().getNickName());
                }
            }
        }
        catch (Exception ignored)
        {
            // 匿名用户，无需填充
        }

        // 复用评论审核开关：为 true/1/未配置时进入待审核
        String reviewSetting = blogSettingService.selectSettingValueByKey("comment_review");
        boolean needsReview = reviewSetting == null
                || "true".equalsIgnoreCase(reviewSetting)
                || "1".equals(reviewSetting);
        blogMessage.setStatus(needsReview ? "0" : "1");

        blogMessage.setId(null);
        blogMessage.setDelFlag("0");
        blogMessage.setIp(IpUtils.getIpAddr(request));
        blogMessage.setReplyContent(null);
        blogMessage.setReplyTime(null);
        blogMessage.setReplyBy(null);
        String userAgent = request.getHeader("User-Agent");
        blogMessage.setUserAgent(truncate(userAgent, MAX_USER_AGENT_LENGTH));
        blogMessage.setContent(content.trim());
        blogMessage.setNickname(nickname.trim());

        logger.info("新留言提交: nickname={}, status={} (审核开关={})",
                blogMessage.getNickname(), blogMessage.getStatus(), reviewSetting);

        return toAjax(blogMessageService.insertBlogMessage(blogMessage));
    }

    /**
     * 字符串截断
     */
    private String truncate(String value, int maxLength)
    {
        if (value == null)
        {
            return null;
        }
        return value.length() <= maxLength ? value : value.substring(0, maxLength);
    }
}
