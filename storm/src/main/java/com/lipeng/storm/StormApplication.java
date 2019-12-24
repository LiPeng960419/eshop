package com.lipeng.storm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StormApplication {

    public static void main(String[] args) {
        SpringApplication.run(StormApplication.class, args);
    }

//    public static void main(String... args) {
//        SpringApplication app = new SpringApplication(StormApplication.class);
//        //我们并不需要web servlet功能，所以设置为WebApplicationType.NONE
//        //app.setWebApplicationType(WebApplicationType.NONE);
//        //忽略掉banner输出
//        //app.setBannerMode(Banner.Mode.OFF);
//        //忽略Spring启动信息日志
//        //app.setLogStartupInfo(false);
//        app.run(args);
//    }

}