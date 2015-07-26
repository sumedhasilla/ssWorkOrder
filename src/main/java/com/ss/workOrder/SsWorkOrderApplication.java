package com.ss.workOrder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;


@SpringBootApplication
public class SsWorkOrderApplication extends SpringBootServletInitializer{

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
		return application.sources(SsWorkOrderApplication.class);
	}

    public static void main(String[] args) {
        SpringApplication.run(SsWorkOrderApplication.class, args);
    }
}
