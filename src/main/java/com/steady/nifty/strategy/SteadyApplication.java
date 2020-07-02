package com.steady.nifty.strategy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.steady.nifty.strategy" })
public class SteadyApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(SteadyApplication.class, args);
    }
}
