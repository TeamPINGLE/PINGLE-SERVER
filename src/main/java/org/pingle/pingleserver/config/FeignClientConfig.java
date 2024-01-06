package org.pingle.pingleserver.config;

import org.pingle.pingleserver.PingleserverApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = PingleserverApplication.class)
public class FeignClientConfig {
}