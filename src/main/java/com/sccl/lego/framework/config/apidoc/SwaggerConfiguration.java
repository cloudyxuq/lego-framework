package com.sccl.lego.framework.config.apidoc;

import static springfox.documentation.builders.PathSelectors.regex;

import com.sccl.lego.framework.config.LegoConstants;
import com.sccl.lego.framework.config.LegoProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * REST Swagger API配置(springfox-swagger)
 *
 * @author xuq
 * @date 2017-11-28
 */
@Configuration
@ConditionalOnClass({ApiInfo.class, BeanValidatorPluginsConfiguration.class})
@Import(BeanValidatorPluginsConfiguration.class)
@Profile(LegoConstants.LEGO_PROFILE_SWAGGER)
@EnableSwagger2
public class SwaggerConfiguration {

    private final LegoProperties legoProperties;

    private final Logger log = LoggerFactory.getLogger(SwaggerConfiguration.class);

    public SwaggerConfiguration(LegoProperties legoProperties) {
        this.legoProperties = legoProperties;
    }

    /**
     * swagger spring-fox 摘要信息 (作为实体配置文件）
     *
     * @return swagger摘要
     */
    @Bean
    public Docket swaggerSpringfoxApiDocket() {

        log.debug("starting Swagger");
        //计时器
        StopWatch watch = new StopWatch();
        watch.start();
        //spring fox document
        Contact contact = new Contact(
            legoProperties.getSwagger().getContactName(),
            legoProperties.getSwagger().getContactUrl(),
            legoProperties.getSwagger().getContactEmail()
        );
        ApiInfo apiInfo = new ApiInfo(
            legoProperties.getSwagger().getTitle(),
            legoProperties.getSwagger().getDescription(),
            legoProperties.getSwagger().getVersion(),
            legoProperties.getSwagger().getTermsOfServiceUrl(),
            contact,
            legoProperties.getSwagger().getLicense(),
            legoProperties.getSwagger().getLicenseUrl(),
            new ArrayList<>()
        );

        //swagger 摘要注册为实体，作为配置文件
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
            .host(legoProperties.getSwagger().getHost())
            .protocols(new HashSet<>(Arrays.asList(legoProperties.getSwagger().getProtocols())))
            .apiInfo(apiInfo)
            .forCodeGeneration(true)
            .directModelSubstitute(java.nio.ByteBuffer.class, String.class)
            .genericModelSubstitutes(ResponseEntity.class)
            .select()
            .apis(RequestHandlerSelectors.basePackage(legoProperties.getSwagger().getBasePackage()))
            .paths(regex(legoProperties.getSwagger().getDefaultIncludePattern()))
            .build();

        watch.stop();
        log.debug("Started Swagger in {} ms", watch.getTotalTimeMillis());
        return docket;
    }
}
