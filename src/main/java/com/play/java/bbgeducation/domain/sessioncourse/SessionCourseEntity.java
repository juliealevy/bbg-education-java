package com.play.java.bbgeducation.domain.sessioncourse;

import com.play.java.bbgeducation.domain.courses.CourseEntity;
import com.play.java.bbgeducation.domain.programs.SessionEntity;
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
@Table(name="session_course", uniqueConstraints = {@UniqueConstraint(name = "session_id_course_id_unique", columnNames = {"session_id", "course_id"})})
public class SessionCourseEntity {
    @Id
    @SequenceGenerator(name = "session_course_id_seq", sequenceName = "SESSION_COURSE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "session_course_id_seq")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "session_id", referencedColumnName = "id")
    private SessionEntity session;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id",referencedColumnName = "id")
    private CourseEntity course;


    //want to set as not insertable or updateable, but then findById doesn't return the dates...
    @Column(name = "created_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @CreationTimestamp
    private OffsetDateTime createdDateTime;

    @Column(name = "updated_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @UpdateTimestamp
    private OffsetDateTime updatedDateTime;

}
