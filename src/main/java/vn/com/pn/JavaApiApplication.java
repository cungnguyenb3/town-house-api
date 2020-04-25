package vn.com.pn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import vn.com.pn.domain.HostImage;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableJpaAuditing
public class JavaApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(JavaApiApplication.class, args);
	}

}
