package co.uk.bransby.equinetrainingtrackerapi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableWebMvc
@EnableOpenApi
public class EquineTrainingTrackerApiApplication {

//	This Bean enables Swagger UI. See - https://stackoverflow.com/questions/72301571/swagger-error-springboot-could-not-resolve-view-with-name-forward-swagger-ui-i
	@Bean
	public InternalResourceViewResolver defaultViewResolver() {
		return new InternalResourceViewResolver();
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(EquineTrainingTrackerApiApplication.class, args);
	}
}
