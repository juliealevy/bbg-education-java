package com.play.java.bbgeducation.domain.programs;

import com.play.java.bbgeducation.domain.courses.CourseEntity;
import com.play.java.bbgeducation.domain.programs.ProgramEntity;
import com.play.java.bbgeducation.domain.sessioncourse.SessionCourseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="session")
public class SessionEntity {
    @Id
    @SequenceGenerator(name = "session_id_seq", sequenceName = "SESSION_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "session_id_seq")
    private Long id;

    @Column(unique = true)
    private String name;

    private String description;

    //figure out date only columns... is this ok or not
    private LocalDate startDate;
    private LocalDate endDate;

    private int practicumHours;

    @ManyToOne(cascade = ALL)
    @JoinColumn(name = "program_id", referencedColumnName = "id")
    private ProgramEntity program;

    @OneToMany(mappedBy="session", fetch = FetchType.LAZY)
    private List<SessionCourseEntity> courses;

    //want to set as not insertable or updateable, but then findById doesn't return the dates...
    @Column(name = "created_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @CreationTimestamp
    private OffsetDateTime createdDateTime;

    @Column(name = "updated_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @UpdateTimestamp
    private OffsetDateTime updatedDateTime;

    @Column(name = "deactivated_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime deactivatedDateTime;
}
