package com.tungdd.k8sdemo.config;

import com.tungdd.k8sdemo.component.ActiveRequestInterceptor;
import com.tungdd.k8sdemo.component.RequestTimingInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final RequestTimingInterceptor requestTimingInterceptor;
    private final ActiveRequestInterceptor activeRequestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestTimingInterceptor);
        registry.addInterceptor(activeRequestInterceptor);
    }
}

