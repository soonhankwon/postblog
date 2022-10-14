package com.assignment.postblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PostblogApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostblogApplication.class, args);
    }

}
