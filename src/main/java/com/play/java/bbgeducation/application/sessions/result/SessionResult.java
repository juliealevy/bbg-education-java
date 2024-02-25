package com.play.java.bbgeducation.application.sessions.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.play.java.bbgeducation.application.programs.result.ProgramResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Relation(collectionRelation="sessions")
public class SessionResult implements Serializable {
    private static String DATE_PATTERN = "MM-dd-yyyy";
    private static DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

    private Long id;
    private String name;
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="MM-dd-yyyy")
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="MM-dd-yyyy")
    private LocalDate endDate;

    private int practicumHours;

    private ProgramResult program;


    @JsonIgnore
    public String getStartDateStr(){
        return startDate.format(date_formatter);
    }

    @JsonIgnore
    public String getEndDateStr(){
        return endDate.format(date_formatter);
    }
}
