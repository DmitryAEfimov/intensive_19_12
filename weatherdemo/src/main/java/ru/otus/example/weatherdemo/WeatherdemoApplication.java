package ru.otus.example.weatherdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/*
О формате логов
http://openjdk.java.net/jeps/158


-Xms256m
-Xmx256m
-Xlog:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=./logs/dump
-XX:+UseG1GC
-XX:MaxGCPauseMillis=10
*/

@SpringBootApplication
@Slf4j
public class WeatherdemoApplication {
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	public static void main(String[] args) {
		log.info("starting app...");
		SpringApplication.run(WeatherdemoApplication.class, args);
	}

}
