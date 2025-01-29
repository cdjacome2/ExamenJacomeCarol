package com.espe.micro_museos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MicroMuseosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroMuseosApplication.class, args);
	}

}
