package config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: 小余哥
 * @description:
 * @create: 2020-07-01 09:27
 **/
@ConfigurationProperties(prefix = "yugb.thread")
public class ThreadPoolProperties {
    /**
     * 核心线程池数
     */
    private int corePoolSize = 10;
    /**
     * 最大线程
     */
    private int maxPoolSize = 20;
    /**
     * 队列容量
     */
    private int queueCapacity = 1000;
    /**
     * 存货时间
     */
    private int keepAliveSeconds = 300;

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public int getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    public void setKeepAliveSeconds(int keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }
}
