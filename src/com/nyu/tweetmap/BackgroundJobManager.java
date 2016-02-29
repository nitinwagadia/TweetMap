package com.nyu.tweetmap;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
@WebListener
public class BackgroundJobManager implements ServletContextListener {

	private ScheduledExecutorService scheduler;
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		System.out.println("Inside contextDestroyed");
		scheduler.shutdownNow();

	}
// This is test to check git
	@Override
	public void contextInitialized(ServletContextEvent event) {
		System.out.println("Inside contextInitialized");
		scheduler = Executors.newSingleThreadScheduledExecutor();
		Resources mResources = new Resources();
		System.out.println("Resource Object created");
		
		scheduler.scheduleAtFixedRate(mResources, 0, 30,TimeUnit.DAYS);
		

	}

}
