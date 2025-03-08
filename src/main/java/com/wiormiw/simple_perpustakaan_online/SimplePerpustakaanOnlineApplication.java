package com.wiormiw.simple_perpustakaan_online;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SimplePerpustakaanOnlineApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimplePerpustakaanOnlineApplication.class, args);
	}

}
