package com.wallet_SpringBoot.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.wallet_SpringBoot")
public class WalletSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletSpringBootApplication.class, args);
	}

}
