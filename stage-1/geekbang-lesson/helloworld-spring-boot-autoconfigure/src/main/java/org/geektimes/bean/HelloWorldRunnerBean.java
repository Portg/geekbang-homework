package org.geektimes.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class HelloWorldRunnerBean implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(HelloWorldRunnerBean.class);

    @Override
    public void run(ApplicationArguments arg0) throws Exception {
        String strArgs = Arrays.stream(arg0.getSourceArgs()).collect(Collectors.joining(" "));
        logger.info("Application started with arguments:" + strArgs);
    }
}
