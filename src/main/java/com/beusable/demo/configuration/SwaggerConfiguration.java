package com.beusable.demo.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger configuration.
 *
 * @author Branko Ostojic (bostojic@boothub.io)
 */
@Configuration
public class SwaggerConfiguration {

    /**
     * Configures OpenAPI.
     *
     * @return OpenAPI's configuration bean.
     */
    @Bean
    public OpenAPI api() {
        return new OpenAPI().info(
                new Info()
                        .title("Beusable coding challenge demo API")
                        .description("Beusable coding challenge demo API")
                        .version("1.0.0"));
    }
}
