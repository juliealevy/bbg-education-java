package com.play.java.bbgeducation.api.sessions;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionRequest {

    public static DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    private String name;
    private String description;

    @JsonFormat(pattern="MM-dd-yyyy")
    private LocalDate startDate;
    @JsonFormat(pattern="MM-dd-yyyy")
    private LocalDate endDate;

    private Integer practicumHours;

    @JsonIgnore
    public String getStartDateStr(){
        return this.startDate.format(date_formatter);
    }

    @JsonIgnore
    public String getEndDateStr(){
        return this.endDate.format(date_formatter);
    }
    //future:  list of sessioncourses

}
