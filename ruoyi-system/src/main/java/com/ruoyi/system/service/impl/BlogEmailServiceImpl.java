package com.ruoyi.system.service.impl;

import com.ruoyi.common.config.EmailCodeConfig;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BlogEmailCode;
import com.ruoyi.system.mapper.BlogEmailCodeMapper;
import com.ruoyi.system.service.IBlogEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 博客邮件服务实现
 *
 * @author nevell
 * @date 2025-01-26
 */
@Service
public class BlogEmailServiceImpl implements IBlogEmailService
{
    private static final Logger log = LoggerFactory.getLogger(BlogEmailServiceImpl.class);

    /** Redis频率限制键前缀 */
    private static final String RATE_LIMIT_EMAIL_PREFIX = "email:rate:limit:";

    private static final String RATE_LIMIT_IP_PREFIX = "ip:rate:limit:";

    @Autowired
    private BlogEmailCodeMapper emailCodeMapper;

    @Autowired
    private EmailCodeConfig emailCodeConfig;

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${spring.mail.username:}")
    private String mailFrom;

    /**
     * 发送注册验证码
     *
     * @param email 邮箱地址
     * @param ipAddress 请求IP地址
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sendRegisterCode(String email, String ipAddress)
    {
        return sendCode(email, CODE_TYPE_REGISTER, ipAddress, "注册账号");
    }

    /**
     * 发送密码重置验证码
     *
     * @param email 邮箱地址
     * @param ipAddress 请求IP地址
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sendResetCode(String email, String ipAddress)
    {
        return sendCode(email, CODE_TYPE_RESET, ipAddress, "重置密码");
    }

    /**
     * 发送验证码通用方法
     *
     * @param email 邮箱地址
     * @param codeType 验证码类型
     * @param ipAddress 请求IP地址
     * @param subject 邮件主题
     * @return 结果
     */
    private boolean sendCode(String email, String codeType, String ipAddress, String subject)
    {
        // 参数校验
        if (StringUtils.isEmpty(email))
        {
            log.error("发送验证码失败：邮箱地址为空");
            return false;
        }

        // 检查频率限制
        if (!checkEmailRateLimit(email, codeType))
        {
            log.warn("发送验证码失败：邮箱频率限制 email={}", email);
            return false;
        }

        if (!checkIpRateLimit(ipAddress))
        {
            log.warn("发送验证码失败：IP频率限制 ip={}", ipAddress);
            return false;
        }

        // 生成验证码
        String code = generateCode();

        // 计算过期时间
        Date expireTime = calculateExpireTime();

        // 创建验证码记录
        BlogEmailCode emailCode = new BlogEmailCode();
        emailCode.setEmail(email);
        emailCode.setCode(code);
        emailCode.setCodeType(codeType);
        emailCode.setExpireTime(expireTime);
        emailCode.setUsed(0);
        emailCode.setIpAddress(ipAddress);
        emailCode.setCreateTime(new Date());

        try
        {
            // 保存到数据库
            emailCodeMapper.insertBlogEmailCode(emailCode);

            // 设置Redis频率限制
            setRateLimit(email, ipAddress, codeType);

            // 开发环境：打印验证码到控制台
            if (emailCodeConfig.isDevPrintCode())
            {
                log.warn("========== 邮件验证码（开发环境）==========");
                log.warn("邮箱：{}", email);
                log.warn("验证码：{}", code);
                log.warn("类型：{}", codeType);
                log.warn("有效期：{} 分钟", emailCodeConfig.getExpireMinutes());
                log.warn("========================================");
                return true;
            }

            // 生产环境：发送邮件
            if (mailSender != null && StringUtils.isNotEmpty(mailFrom))
            {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(mailFrom);
                message.setTo(email);
                message.setSubject(subject + " - 验证码");
                message.setText("您的验证码是：" + code + "\n" +
                        "有效期：" + emailCodeConfig.getExpireMinutes() + " 分钟\n" +
                        "请勿将验证码告诉他人。");

                mailSender.send(message);
                log.info("验证码邮件发送成功：email={}, type={}", email, codeType);
                return true;
            }
            else
            {
                log.error("邮件发送失败：未配置邮件服务");
                return false;
            }
        }
        catch (Exception e)
        {
            log.error("发送验证码失败：email={}, type={}, error={}", email, codeType, e.getMessage());
            return false;
        }
    }

    /**
     * 验证验证码是否有效
     *
     * @param email 邮箱地址
     * @param code 验证码
     * @param codeType 验证码类型
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean verifyCode(String email, String code, String codeType)
    {
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(code))
        {
            return false;
        }

        // 查询最新的未使用验证码
        BlogEmailCode emailCode = emailCodeMapper.selectLatestUnusedCode(email, codeType);

        if (emailCode == null)
        {
            log.warn("验证码验证失败：未找到有效验证码 email={}, type={}", email, codeType);
            return false;
        }

        // 检查是否过期
        if (new Date().after(emailCode.getExpireTime()))
        {
            log.warn("验证码验证失败：验证码已过期 email={}, code={}", email, code);
            return false;
        }

        // 验证码匹配
        if (!code.equals(emailCode.getCode()))
        {
            log.warn("验证码验证失败：验证码错误 email={}, input={}, db={}", email, code, emailCode.getCode());
            return false;
        }

        // 标记为已使用
        emailCodeMapper.markCodeAsUsed(emailCode.getId(), new Date());

        log.info("验证码验证成功：email={}, type={}", email, codeType);
        return true;
    }

    /**
     * 获取验证码
     *
     * @param email 邮箱地址
     * @param code 验证码
     * @param codeType 验证码类型
     * @return 验证码对象
     */
    @Override
    public Object getCode(String email, String code, String codeType)
    {
        return emailCodeMapper.selectLatestUnusedCode(email, codeType);
    }

    /**
     * 删除过期的验证码
     *
     * @return 结果
     */
    @Override
    public int cleanExpiredCodes()
    {
        return emailCodeMapper.deleteExpiredCodes();
    }

    /**
     * 检查邮箱频率限制
     *
     * @param email 邮箱地址
     * @param codeType 验证码类型
     * @return true=允许发送, false=频率限制
     */
    @Override
    public boolean checkEmailRateLimit(String email, String codeType)
    {
        String redisKey = RATE_LIMIT_EMAIL_PREFIX + email + ":" + codeType;

        // 检查Redis中是否存在限制
        Boolean hasKey = redisTemplate.hasKey(redisKey);
        if (Boolean.TRUE.equals(hasKey))
        {
            Long ttl = redisTemplate.getExpire(redisKey, TimeUnit.SECONDS);
            log.warn("邮箱频率限制：email={}, remainingSeconds={}", email, ttl);
            return false;
        }

        // 检查数据库中的发送记录
        Date startTime = new Date(System.currentTimeMillis() - emailCodeConfig.getRateLimitSeconds() * 1000L);
        int count = emailCodeMapper.countCodesByEmailAndTime(email, startTime);

        if (count > 0)
        {
            // 设置Redis限制
            redisTemplate.opsForValue().set(redisKey, "1", emailCodeConfig.getRateLimitSeconds(), TimeUnit.SECONDS);
            log.warn("邮箱频率限制：email={}, count={}", email, count);
            return false;
        }

        return true;
    }

    /**
     * 检查IP频率限制
     *
     * @param ipAddress IP地址
     * @return true=允许发送, false=频率限制
     */
    @Override
    public boolean checkIpRateLimit(String ipAddress)
    {
        // 开发模式下跳过IP频率限制（因为Docker环境中所有请求来自同一IP）
        if (emailCodeConfig.isDevPrintCode())
        {
            log.debug("开发模式：跳过IP频率限制 ip={}", ipAddress);
            return true;
        }

        if (StringUtils.isEmpty(ipAddress))
        {
            return true;
        }

        String redisKey = RATE_LIMIT_IP_PREFIX + ipAddress;

        // 检查Redis中是否存在限制
        Boolean hasKey = redisTemplate.hasKey(redisKey);
        if (Boolean.TRUE.equals(hasKey))
        {
            Long ttl = redisTemplate.getExpire(redisKey, TimeUnit.SECONDS);
            log.warn("IP频率限制：ip={}, remainingSeconds={}", ipAddress, ttl);
            return false;
        }

        // 检查数据库中的发送记录（1小时内）
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        Date startTime = calendar.getTime();

        int count = emailCodeMapper.countCodesByIpAndTime(ipAddress, startTime);

        if (count >= emailCodeConfig.getIpRateLimit())
        {
            // 设置Redis限制（1小时）
            redisTemplate.opsForValue().set(redisKey, "1", 1, TimeUnit.HOURS);
            log.warn("IP频率限制：ip={}, count={}", ipAddress, count);
            return false;
        }

        return true;
    }

    /**
     * 生成随机验证码
     *
     * @return 验证码
     */
    private String generateCode()
    {
        int length = emailCodeConfig.getCodeLength();
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < length; i++)
        {
            code.append(random.nextInt(10));
        }

        return code.toString();
    }

    /**
     * 计算过期时间
     *
     * @return 过期时间
     */
    private Date calculateExpireTime()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, emailCodeConfig.getExpireMinutes());
        return calendar.getTime();
    }

    /**
     * 设置Redis频率限制
     *
     * @param email 邮箱地址
     * @param ipAddress IP地址
     * @param codeType 验证码类型
     */
    private void setRateLimit(String email, String ipAddress, String codeType)
    {
        // 设置邮箱频率限制
        String emailKey = RATE_LIMIT_EMAIL_PREFIX + email + ":" + codeType;
        redisTemplate.opsForValue().set(emailKey, "1", emailCodeConfig.getRateLimitSeconds(), TimeUnit.SECONDS);
    }
}
