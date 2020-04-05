package boardSample;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@SpringBootApplication
public class BoardSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardSampleApplication.class, args);
	}
    @Bean("Thread1") // ここで設定した"Thread"を＠Asyncに設定するとその設定が利用される
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // デフォルトのThreadのサイズ。あふれるとQueueCapacityのサイズまでキューイングする
        executor.setQueueCapacity(1); // 待ちのキューのサイズ。あふれるとMaxPoolSizeまでThreadを増やす
        executor.setMaxPoolSize(500); // どこまでThreadを増やすかの設定。この値からあふれるとその処理はリジェクトされてExceptionが発生する
        executor.setThreadNamePrefix("Thread2--");
        executor.initialize();
        return executor;
    }

}
