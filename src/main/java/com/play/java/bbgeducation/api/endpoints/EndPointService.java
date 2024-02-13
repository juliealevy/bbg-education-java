package com.play.java.bbgeducation.api.endpoints;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

@Service
public class EndPointService {
    private final Logger logger = LoggerFactory.getLogger(EndPointService.class);
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private Map<String, List<EndPointData>> endpointsByController = new HashMap<>();

    public EndPointService(ApplicationContext applicationContext) {
        this.requestMappingHandlerMapping = applicationContext
                .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);

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
                                        .href(pathPatterns.iterator().next().getPatternString())
                                        .httpMethod(httpMethods.iterator().next().name())
                                        .build();
                            }
                    )
                    .collect(groupingBy(EndPointData::getControllerName));
            logEndpoints();
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
