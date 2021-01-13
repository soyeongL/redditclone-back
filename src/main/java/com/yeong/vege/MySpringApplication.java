package com.yeong.vege;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import com.yeong.vege.config.SwaggerConfiguration;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class)
public class MySpringApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(MySpringApplication.class, args);
	}

}
