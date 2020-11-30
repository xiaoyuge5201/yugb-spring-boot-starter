package threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程池配置
 */
@Configuration
@ConfigurationProperties(prefix = "threadpool")
public class ExecutePoolConfiguration {

    private static Logger logger = LoggerFactory.getLogger(ExecutePoolConfiguration.class);

    /**
     * application.yml 配置方式
     *     threadpool:
     *       core-pool-size: 10
     *       max-pool-size: 20
     *       queue-capacity: 1000
     *       keep-alive-seconds: 300
     */
    @Value("${threadpool.core-pool-size}")
    private int corePoolSize;

    @Value("${threadpool.max-pool-size}")
    private int maxPoolSize;

    @Value("${threadpool.queue-capacity}")
    private int queueCapacity;

    @Value("${threadpool.keep-alive-seconds}")
    private int keepAliveSeconds;


    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setKeepAliveSeconds(keepAliveSeconds);
        pool.setCorePoolSize(corePoolSize);//核心线程池数
        pool.setMaxPoolSize(maxPoolSize); // 最大线程
        pool.setQueueCapacity(queueCapacity);//队列容量
        pool.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy()); //队列满，线程被拒绝执行策略
        return pool;
    }

}