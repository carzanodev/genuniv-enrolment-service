package carzanodev.genuniv.microservices.enrolment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class EnrolmentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnrolmentServiceApplication.class, args);
    }

}
