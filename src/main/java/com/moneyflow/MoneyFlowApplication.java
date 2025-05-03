package com.moneyflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class MoneyFlowApplication {
	@Autowired
	private Environment environment;

	public static void main(String[] args) {
		SpringApplication.run(MoneyFlowApplication.class, args);
	}

}
