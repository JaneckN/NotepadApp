package pl.janeck.notepadapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@EnableSwagger2
@SpringBootApplication
public class NotepadAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotepadAppApplication.class, args);
    }

    @Bean
    public Docket getDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("pl.janeck.notepadapp"))
                .paths(PathSelectors.any())
                .build().apiInfo(createApiInfo());
    }

    //setting api info
    private ApiInfo createApiInfo() {
        return new ApiInfo("Car Api",
                "TEAI Homework(8th week)",
                "1.00",
                "janek@protonmail.com",
                new Contact("Jan", "janek@protonmail.com", "janek@protonmail.com"),
                "my own license",
                "janek@protonmail.com",
                Collections.emptyList());
    }

}
