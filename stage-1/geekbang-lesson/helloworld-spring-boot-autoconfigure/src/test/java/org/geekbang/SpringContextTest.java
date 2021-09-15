package org.geekbang;

import org.geektimes.autoconfigure.HelloWorldAutoConfiguration;
import org.geektimes.bean.HelloWorldRunnerBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class SpringContextTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

    @Test
    public void run() throws Exception {
        this.contextRunner.withUserConfiguration(HelloWorldAutoConfiguration.class).run(context -> {
            HelloWorldRunnerBean helloWorldRunnerBean = context.getBean(HelloWorldRunnerBean.class);
            helloWorldRunnerBean.run(new DefaultApplicationArguments(new String[]{"Hello", "World"}));
        });
    }
}
