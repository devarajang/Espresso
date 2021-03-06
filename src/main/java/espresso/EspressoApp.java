/**
 * 
 */
package espresso;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import espresso.iso.EspressoServer;

/**
 * @author deva
 *
 */
@SpringBootApplication
@EnableScheduling
public class EspressoApp {

	public static void main(String[] args) {
		SpringApplication.run(EspressoApp.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			EspressoServer espressoServer = ctx.getBean(EspressoServer.class);
			espressoServer.run();

		};
	}

}
