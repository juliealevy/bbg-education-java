package com.play.java.bbgeducation.application.programs;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgramResult {
    private Long id;
    private String name;
    private String description;
}
