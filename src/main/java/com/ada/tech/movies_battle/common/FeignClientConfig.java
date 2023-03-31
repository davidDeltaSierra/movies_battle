package com.ada.tech.movies_battle.common;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.ada.tech.movies_battle")
@ImportAutoConfiguration(FeignAutoConfiguration.class)
class FeignClientConfig {
}
