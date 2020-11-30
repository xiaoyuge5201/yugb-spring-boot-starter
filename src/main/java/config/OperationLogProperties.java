package config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: 小余哥
 * @description:
 * @create: 2020-07-01 09:27
 **/
@ConfigurationProperties(prefix = "yugb.log")
public class OperationLogProperties {

    private String type;

    private String url;

    private String username;

    private String password;

    private String driverClassName;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }
}
