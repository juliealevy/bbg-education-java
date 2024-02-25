package com.play.java.bbgeducation.domain.sessioncourse;

import com.play.java.bbgeducation.domain.sessioncourse.SessionCourseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="class")
public class ClassEntity implements Serializable {

    @Id
    @SequenceGenerator(name = "class_id_seq", sequenceName = "CLASS_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "class_id_seq")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "session_course_id", referencedColumnName = "id")
    private SessionCourseEntity sessionCourse;

    @Column(name = "class_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime classDateTime;




    //want to set as not insertable or updateable, but then findById doesn't return the dates...
    @Column(name = "created_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @CreationTimestamp
    private OffsetDateTime createdDateTime;

    @Column(name = "updated_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @UpdateTimestamp
    private OffsetDateTime updatedDateTime;
}


