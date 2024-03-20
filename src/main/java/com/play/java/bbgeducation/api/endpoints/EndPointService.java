package com.play.java.bbgeducation.api.endpoints;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.pattern.PathPattern;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;

@Service
public class EndPointService {
    private final Logger logger = LoggerFactory.getLogger(EndPointService.class);
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private Map<String, List<EndPointData>> endpointsByController = new HashMap<>();

    private final HttpServletRequest httpRequest;

    public EndPointService(ApplicationContext applicationContext, HttpServletRequest httpServletRequest) {

        this.requestMappingHandlerMapping = applicationContext
                .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        this.requestMappingHandlerMapping.getUrlPathHelper().setAlwaysUseFullPath(true);
        this.httpRequest = httpServletRequest;

    }

    public Optional<EndPointData> get(String controllerName, String methodName){
        LoadEndpoints();
        List<EndPointData> controllerEndpoints = endpointsByController.get(controllerName.toLowerCase());
        if (controllerEndpoints == null || controllerEndpoints.isEmpty())
            return Optional.empty();

        return controllerEndpoints.stream().filter(e -> e.methodName.equals(methodName)).findFirst();
    }

    private void LoadEndpoints() {
        if (endpointsByController.isEmpty()) {
            endpointsByController = requestMappingHandlerMapping.getHandlerMethods().entrySet().stream()
                    .filter((entry) ->
                            entry.getValue().getBeanType().isAnnotationPresent(HasApiEndpoints.class) &&
                            !entry.getKey().getPathPatternsCondition().getPatterns().isEmpty() &&
                            !entry.getKey().getMethodsCondition().getMethods().isEmpty()
                    ).map((entry) -> {
                                Set<PathPattern> pathPatterns = entry.getKey().getPathPatternsCondition().getPatterns();
                                Set<RequestMethod> httpMethods = entry.getKey().getMethodsCondition().getMethods();
                                return EndPointData.builder()
                                        .controllerName(entry.getValue().getBean().toString().toLowerCase())
                                        .methodName(entry.getValue().getMethod().getName())
                                        .href(buildHref(pathPatterns.iterator().next().getPatternString()))
                                        .httpMethod(httpMethods.iterator().next().name())
                                        .build();
                            }
                    )
                    .collect(groupingBy(EndPointData::getControllerName));
            logEndpoints();
        }
    }

    private String buildHref(String path) {
        try {
            //using this builder so url doesn't get encoded..this is for hal api
            return UriComponentsBuilder.fromPath(path)
                    .scheme(httpRequest.getScheme())
                    .host(httpRequest.getServerName())
                    .port(httpRequest.getServerPort())
                    .build()
                    .toString();
        } catch (Exception ex) {
            return path;
        }

    }
    private void logEndpoints() {
        endpointsByController.forEach((key, valueList) -> {
            logger.info("Endpoints for " + key);
            valueList.forEach(v -> {
                logger.info(v.toString());
            });
        });
    }
}
