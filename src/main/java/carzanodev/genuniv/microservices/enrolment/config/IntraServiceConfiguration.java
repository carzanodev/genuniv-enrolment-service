package carzanodev.genuniv.microservices.enrolment.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import carzanodev.genuniv.microservices.common.config.ClientLenientErrorHandler;

@Configuration
public class IntraServiceConfiguration {

    @Bean
    @LoadBalanced
    RestTemplate getRestTemplate() {
        return new RestTemplateBuilder()
                .errorHandler(new ClientLenientErrorHandler())
                .build();
    }

}
