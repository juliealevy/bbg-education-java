package com.play.java.bbgeducation.application.programs;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Relation(collectionRelation="programs")
public class ProgramResult  {
    private Long id;
    private String name;
    private String description;
}
