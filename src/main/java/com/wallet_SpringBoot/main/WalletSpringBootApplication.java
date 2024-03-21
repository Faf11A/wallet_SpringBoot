package com.wallet_SpringBoot.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.wallet_SpringBoot")
@EnableJpaRepositories(basePackages = "com.wallet_SpringBoot.repository")
@EntityScan("com.wallet_SpringBoot.model")
public class WalletSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletSpringBootApplication.class, args);
	}

}
