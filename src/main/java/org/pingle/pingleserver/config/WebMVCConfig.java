package org.pingle.pingleserver.config;

import lombok.RequiredArgsConstructor;
import org.pingle.pingleserver.interceptor.pre.GUserIdArgumentResolver;
import org.pingle.pingleserver.interceptor.pre.UserIdArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
@RequiredArgsConstructor
public class WebMVCConfig implements WebMvcConfigurer {
    private final UserIdArgumentResolver userIdArgumentResolver;

    private final GUserIdArgumentResolver gUserIdArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
        resolvers.add(this.userIdArgumentResolver);
        resolvers.add(this.gUserIdArgumentResolver);
    }
}