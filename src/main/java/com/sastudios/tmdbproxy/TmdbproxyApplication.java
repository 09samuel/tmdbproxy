package com.sastudios.tmdbproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TmdbproxyApplication {

	public static void main(String[] args) {
		//System.out.println("DNS servers: " + System.getProperty("sun.net.spi.nameservice.nameservers"));
//		System.setProperty("java.net.preferIPv4Stack", "true");
//		System.setProperty("java.net.preferIPv6Addresses", "false");


		SpringApplication.run(TmdbproxyApplication.class, args);
	}

}
