package com.pinup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // JPA Auditing 어노테이션 전체 활성화하는 기능
public class PinupApplication {

	public static void main(String[] args) {
		SpringApplication.run(PinupApplication.class, args);
	}

}
