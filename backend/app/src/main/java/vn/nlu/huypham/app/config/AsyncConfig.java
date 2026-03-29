package vn.nlu.huypham.app.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean(name = "mailExecutor")
    public Executor mailExecutor() {
        ThreadFactory factory = Thread.ofVirtual()
                .name("MailThread-", 1)
                .factory();
        return Executors.newThreadPerTaskExecutor(factory);
    }
}
