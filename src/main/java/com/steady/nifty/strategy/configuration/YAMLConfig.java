package com.steady.nifty.strategy.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

import lombok.Data;

@Configuration
@ConfigurationProperties
@EnableEncryptableProperties
@Data
public class YAMLConfig {
    private String name;
    private String environment;
    private List<String> servers = new ArrayList<>();
}
