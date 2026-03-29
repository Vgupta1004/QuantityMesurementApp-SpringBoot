package com.seveneleven.quantitymeasurement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;


@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Quantity Measurement API",
				version = "1.0",
				description = "REST API for quantity measurements — " +
                        "length, weight, volume, temperature"
		)
)
public class QuantityMesurementAppSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuantityMesurementAppSpringBootApplication.class, args);
	}

}
