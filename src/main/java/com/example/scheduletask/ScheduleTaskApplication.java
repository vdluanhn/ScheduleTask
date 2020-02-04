package com.example.scheduletask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//Bạn có thể kích hoạt chức năng lên lịch trình một cách đơn giản bằng việt thêm annotation @EnableScheduling vào trong main application class
// hay trong 1 lớp class nào đó mà bạn đăt annotation @Configuration
@SpringBootApplication
//1. Kích hoạt Scheduling trong Spring Boot
@EnableScheduling
public class ScheduleTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScheduleTaskApplication.class, args);
    }

}
