package com.zhi.system.domain;

/**
 * 后台保存邮件（SMTP）配置用的表单对象。
 *
 * <p>{@code password} 为空表示保留数据库中已存的密码，不会被覆盖为空白。</p>
 *
 * @author nevell
 */
public class MailConfigForm
{
    private String host;
    private Integer port;
    private String username;
    private String password;
    private Boolean ssl;
    private Boolean starttls;
    private Boolean enabled;

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public Integer getPort()
    {
        return port;
    }

    public void setPort(Integer port)
    {
        this.port = port;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Boolean getSsl()
    {
        return ssl;
    }

    public void setSsl(Boolean ssl)
    {
        this.ssl = ssl;
    }

    public Boolean getStarttls()
    {
        return starttls;
    }

    public void setStarttls(Boolean starttls)
    {
        this.starttls = starttls;
    }

    public Boolean getEnabled()
    {
        return enabled;
    }

    public void setEnabled(Boolean enabled)
    {
        this.enabled = enabled;
    }
}
