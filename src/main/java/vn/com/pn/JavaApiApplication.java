package vn.com.pn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JavaApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(JavaApiApplication.class, args);
	}
}
