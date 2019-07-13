package com.exercise.toggler.config;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .tags(new Tag("ToggleController",
                                "Operations pertaining to **Toggles** for all services and applications"),
                        new Tag("ToggleExclusionController",
                                "Operations pertaining to **Exclusions** of Services or Applications from Toggles"),
                        new Tag("ToggleOverrideController",
                                "Operations pertaining to **Overrides** of services and applications for Toggles"));
    }

    private ApiInfo apiInfo() {

        return new ApiInfoBuilder().title("Toggler REST Web API")
                .description("Real-time, dynamic and hierarchical management of toggles for services and applications.")
                .contact(new Contact("Rodrigo Pio", "https://linkedin.com/in/rrapio/", "rrapio@gmail.com"))
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version("1.0.0")
                .build();
    }
}