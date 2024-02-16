package com.play.java.bbgeducation.domain.courses;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name="course")
public class CourseEntity {

    public CourseEntity(){
        isPublic = true;
        isOnline = false;
    }

    @Id
    @SequenceGenerator(name = "course_id_seq", sequenceName = "COURSE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_id_seq")
    private Long id;

    @Column(unique = true)
    private String name;

    private String description;

    @NotNull
    @Column(columnDefinition ="BOOLEAN DEFAULT TRUE" )
    @Builder.Default
    private Boolean isPublic = true;

    @NotNull
    @Column(columnDefinition ="BOOLEAN DEFAULT FALSE")
    @Builder.Default
    private Boolean isOnline = false;


    //want to set as not insertable or updateable, but then findById doesn't return the dates...
    @Column(name = "created_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @CreationTimestamp
    private OffsetDateTime createdDateTime;

    @Column(name = "updated_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @UpdateTimestamp
    private OffsetDateTime updatedDateTime;

    @Column(name = "inactivated_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @UpdateTimestamp
    private OffsetDateTime inactivatedDateTime;
}
