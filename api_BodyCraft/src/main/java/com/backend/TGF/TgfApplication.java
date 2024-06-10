package com.backend.TGF;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TgfApplication {

	public static void main(String[] args) {
		SpringApplication.run(TgfApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	@Bean
	public OpenAPI customOpenAPI()
	{
		return new OpenAPI()
				.components(new Components())
				.info(new Info().title("TFG API")
						.description("Proyecto de API REST")
						.contact(new Contact()
								.name("Joseluis")
								.email("joseluis@hotmail.com")
								.url("https://portal.edu.gva.es/iesmaciaabela"))
						.version("1.0"));
	}
}
