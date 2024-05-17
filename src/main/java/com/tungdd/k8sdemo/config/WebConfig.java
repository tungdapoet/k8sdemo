package com.tungdd.k8sdemo.config;

import com.tungdd.k8sdemo.component.RequestMetricsFilter;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public FilterRegistrationBean<RequestMetricsFilter> loggingFilter(RequestMetricsFilter filter) {
        FilterRegistrationBean<RequestMetricsFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}

