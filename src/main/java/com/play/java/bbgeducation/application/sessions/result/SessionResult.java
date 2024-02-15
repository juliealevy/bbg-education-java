package com.play.java.bbgeducation.application.sessions.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Relation(collectionRelation="sessions")
public class SessionResult {
    private Long id;
    private String name;
    private String description;
    @JsonFormat(pattern="MM-dd-yyyy")
    private LocalDate startDate;
    @JsonFormat(pattern="MM-dd-yyyy")
    private LocalDate endDate;
    private int practicumHours;
}
