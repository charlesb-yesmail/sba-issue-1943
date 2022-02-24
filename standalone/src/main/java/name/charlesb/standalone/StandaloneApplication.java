package name.charlesb.standalone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class StandaloneApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(StandaloneApplication.class);
		app.run(args);
	}
}
