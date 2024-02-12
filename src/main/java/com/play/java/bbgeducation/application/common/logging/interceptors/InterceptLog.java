package com.play.java.bbgeducation.application.common.logging.interceptors;

import com.play.java.bbgeducation.application.common.logging.LoggingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class InterceptLog  implements HandlerInterceptor {
    @Autowired
    LoggingService loggingService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals(HttpMethod.GET.name())
                || request.getMethod().equals(HttpMethod.DELETE.name())
                || (request.getContentLength() <= 0 && request.getMethod().equals(HttpMethod.POST.name()))
        ) {
            loggingService.logRequest(request, null);
        }
        return true;
    }
}
