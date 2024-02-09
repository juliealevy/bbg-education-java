package com.play.java.bbgeducation.application.common.logging.interceptors;


import com.play.java.bbgeducation.application.common.logging.interceptors.InterceptLog;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class CustomWebConfigurer implements WebMvcConfigurer {

    private InterceptLog logInterceptor;

    public CustomWebConfigurer(InterceptLog logInterceptor) {
        this.logInterceptor = logInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor);
    }
}