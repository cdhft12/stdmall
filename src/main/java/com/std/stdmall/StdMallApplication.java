package com.std.stdmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class StdMallApplication {

	public static void main(String[] args) {
		SpringApplication.run(StdMallApplication.class, args);
	}

}
