package com.example.DiscordBotTry2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ComponentScan(basePackages= "com.example.DiscordBotTry2")
public class DiscordBotTry2Application {

	public static void main(String[] args) {
		SpringApplication.run(DiscordBotTry2Application.class, args);
	}

}
