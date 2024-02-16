package com.play.java.bbgeducation.application.courses.results;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Relation(collectionRelation="courses")
public class CourseResult {
    private Long id;
    private String name;
    private String description;
    private Boolean isPublic;
    private Boolean isOnline;
}
