package vn.nlu.huypham.app.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer
{

	@Override
	public Executor getAsyncExecutor()
	{
		return Executors.newVirtualThreadPerTaskExecutor();
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler()
	{
		return (
			ex,
			method,
			params) ->
		{
			System.err.println("Async error in " + method.getName() + " : " + ex.getMessage());
		};
	}
}