package org.pingle.pingleserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class PingleserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(PingleserverApplication.class, args);
	}

}
