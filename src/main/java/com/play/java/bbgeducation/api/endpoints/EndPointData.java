package com.play.java.bbgeducation.api.endpoints;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EndPointData {
    String controllerName;
    String methodName;
    String href;
    String httpMethod;
}
