package com.play.java.bbgeducation.domain.programs;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="program")
public class ProgramEntity {

    @Id
    @SequenceGenerator(name = "program_id_seq", sequenceName = "PROGRAM_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "program_id_seq")
    private Long id;

    @Column(unique = true)
    private String name;

    private String description;

    //want to set as not insertable or updateable, but then findById doesn't return the dates...
    @Column(name = "created_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @CreationTimestamp
    private OffsetDateTime createdDateTime;

    @Column(name = "updated_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @UpdateTimestamp
    private OffsetDateTime updatedDateTime;


}
