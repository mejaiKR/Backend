package mejai.mejaigg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableFeignClients
@EnableJpaAuditing
@PropertySource("classpath:db.properties")
public class MejaiGgApplication {
	public static void main(String[] args) {
		SpringApplication.run(MejaiGgApplication.class, args);
	}
}
