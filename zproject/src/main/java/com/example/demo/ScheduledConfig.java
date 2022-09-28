package com.example.demo;

import org.springframework.context.annotation.*;
import org.springframework.scheduling.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.scheduling.concurrent.*;

@Configuration
@EnableScheduling
public class ScheduledConfig {
	@Bean
	public TaskScheduler getTaskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(10);
		return scheduler;
	}
}
