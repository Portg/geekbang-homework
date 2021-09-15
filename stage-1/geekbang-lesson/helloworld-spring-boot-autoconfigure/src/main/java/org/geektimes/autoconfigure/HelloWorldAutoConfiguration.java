package org.geektimes.autoconfigure;

import org.geektimes.bean.HelloWorldRunnerBean;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ConditionalOnNotWebApplication
public class HelloWorldAutoConfiguration {

    @Bean
    public HelloWorldRunnerBean helloWorldRunnerBean() {
        return new HelloWorldRunnerBean();
    }
}
