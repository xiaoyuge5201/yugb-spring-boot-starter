package config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author: 小余哥
 **/
@Configuration
@EnableConfigurationProperties(ThreadPoolProperties.class)
public class ExcutePoolAutoConfiguration {

    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(ThreadPoolProperties poolProperties) {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setKeepAliveSeconds(poolProperties.getKeep_alive_seconds());
        pool.setCorePoolSize(poolProperties.getCore_pool_size());
        pool.setMaxPoolSize(poolProperties.getMax_pool_size());
        pool.setQueueCapacity(poolProperties.getQueue_capacity());
        //队列满，线程被拒绝执行策略
        pool.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy());
        return pool;
    }
}
