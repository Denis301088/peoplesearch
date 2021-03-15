package com.example.peoplesearch;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableAsync
@SpringBootApplication
@EnableCaching    //подключение Spring Cache
public class PeoplesearchApplication {

	public static void main(String[] args) {

		System.setProperty ("webdriver.chrome.driver", "src\\main\\resources\\chromedriver.exe");

		SpringApplication.run(PeoplesearchApplication.class, args);
	}

}
