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
    private int core_pool_size = 10;
    /**
     * 最大线程
     */
    private int max_pool_size = 20;
    /**
     * 队列容量
     */
    private int queue_capacity = 1000;
    /**
     * 存货时间
     */
    private int keep_alive_seconds = 300;

    public int getCore_pool_size() {
        return core_pool_size;
    }

    public void setCore_pool_size(int core_pool_size) {
        this.core_pool_size = core_pool_size;
    }

    public int getMax_pool_size() {
        return max_pool_size;
    }

    public void setMax_pool_size(int max_pool_size) {
        this.max_pool_size = max_pool_size;
    }

    public int getQueue_capacity() {
        return queue_capacity;
    }

    public void setQueue_capacity(int queue_capacity) {
        this.queue_capacity = queue_capacity;
    }

    public int getKeep_alive_seconds() {
        return keep_alive_seconds;
    }

    public void setKeep_alive_seconds(int keep_alive_seconds) {
        this.keep_alive_seconds = keep_alive_seconds;
    }
}
