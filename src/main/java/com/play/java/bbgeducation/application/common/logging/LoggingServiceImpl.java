package com.play.java.bbgeducation.application.common.logging;

import com.play.java.bbgeducation.application.common.logging.scrubbing.LoggingDataScrubberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LoggingServiceImpl implements LoggingService{

    Logger logger = LoggerFactory.getLogger(LoggingServiceImpl.class);
    private final LoggingDataScrubberService loggingDataScrubberService;

    public LoggingServiceImpl(LoggingDataScrubberService loggingDataScrubberService) {
        this.loggingDataScrubberService = loggingDataScrubberService;
    }

    @Override
    public void logRequest(HttpServletRequest request, Object body) {
        StringBuilder requestMessage = new StringBuilder();

        Map<String, String> params = getParameters(request);
        Object scrubbedBody =  loggingDataScrubberService.scrubBody(body);

        requestMessage.append("REQUEST ");
        requestMessage.append("method [").append(request.getMethod()).append("]");
        requestMessage.append(" path = [").append(request.getRequestURI()).append("] ");

        if(!params.isEmpty()) {
            requestMessage.append(" parameters = [").append(params).append("] ");
        }

        if(!Objects.isNull(body)) {
            requestMessage.append(" body = [").append(scrubbedBody).append("]");
        }

        logger.info("Request: {}", requestMessage);
    }

    @Override
    public void logResponse(HttpServletRequest request, HttpServletResponse response, Object body) {
        StringBuilder responseMessage = new StringBuilder();
        Map<String,String> headers = getHeaders(response);
        Object scrubbedBody =  loggingDataScrubberService.scrubBody(body);
        responseMessage.append("RESPONSE ");
        responseMessage.append(" method = [").append(request.getMethod()).append("]");
        if(!headers.isEmpty()) {
            responseMessage.append(" ResponseHeaders = [").append(headers).append("]");
        }
        responseMessage.append(" responseBody = [").append(scrubbedBody).append("]");

        logger.info("Response: {}", responseMessage);
    }

    private Map<String,String> getHeaders(HttpServletResponse response) {
        Map<String,String> headers = new HashMap<>();
        Collection<String> headerMap = response.getHeaderNames();
        for(String str : headerMap) {
            headers.put(str,response.getHeader(str));
        }
        return headers;
    }
    private Map<String,String> getParameters(HttpServletRequest request) {
        Map<String,String> parameters = new HashMap<>();
        Enumeration<String> params = request.getParameterNames();
        while(params.hasMoreElements()) {
            String paramName = params.nextElement();
            String paramValue = request.getParameter(paramName);
            parameters.put(paramName,paramValue);
        }
        return parameters;
    }


}
