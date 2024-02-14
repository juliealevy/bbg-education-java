package com.play.java.bbgeducation.api.links;

import com.play.java.bbgeducation.api.endpoints.EndPointData;
import com.play.java.bbgeducation.api.endpoints.EndPointService;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Optional;

@Service
public class ApiLinkService {
    private final EndPointService endPointService;


    public ApiLinkService(EndPointService endPointService) {
        this.endPointService = endPointService;
    }

    public Optional<ApiLink> get(String linkRelation, Class<?> controller, Method method){
        return get(linkRelation, controller.getSimpleName(), method.getName(), null);
    }

    public Optional<ApiLink> get(String linkRelation, Class<?> controller, Method method, Object requestBody){
        return get(linkRelation, controller.getSimpleName(), method.getName(), requestBody);
    }

    private Optional<ApiLink> get(String linkRelation,String controllerName, String methodName, Object requestBody){
        Optional<EndPointData> endPointData = endPointService.get(controllerName, methodName);
        return endPointData
                .map(linkData -> new ApiLink(linkRelation, linkData.getHref(), linkData.getHttpMethod(), requestBody));

    }

}
