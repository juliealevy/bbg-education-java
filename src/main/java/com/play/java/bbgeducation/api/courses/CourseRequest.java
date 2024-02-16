package com.play.java.bbgeducation.api.courses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseRequest {
    private String name;
    private String description;
    private Boolean isPublic;
    private Boolean isOnline;
}
