package com.leyouxianggou.item.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger 配置
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .host("")
                .apiInfo(apiInfo())
                .select()
                //Api包扫描路径
                .apis(RequestHandlerSelectors.basePackage("com.leyouxianggou.item.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("乐优享购商品服务Api")
                .contact(new Contact("Liu Qingyuan","http://www.leyouxianggou.com","wsyslf1996@qq.com"))
                .version("1.0.0")
                .description("API 描述")
                .build();
    }
}
