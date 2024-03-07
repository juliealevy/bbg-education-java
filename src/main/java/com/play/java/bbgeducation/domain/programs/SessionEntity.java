package com.play.java.bbgeducation.domain.programs;

import com.play.java.bbgeducation.domain.sessioncourse.SessionCourseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="session")
public class SessionEntity implements Serializable {
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

    @ManyToOne()
    @JoinColumn(name = "program_id", referencedColumnName = "id")
    private ProgramEntity program;

    @OneToMany(mappedBy="session")
    private Set<SessionCourseEntity> sessionCourses = new HashSet<>();

    //want to set as not insertable or updatable, but then findById doesn't return the dates...
    @Column(name = "created_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @CreationTimestamp
    private OffsetDateTime createdDateTime;

    @Column(name = "updated_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @UpdateTimestamp
    private OffsetDateTime updatedDateTime;

    @Column(name = "deactivated_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime deactivatedDateTime;

    public SessionEntity(){

    }
    public static SessionEntity create(String name, String description, ProgramEntity program,
                                       LocalDate startDate, LocalDate endDate, int practicumHours){
        return build(null, name, description, program, startDate, endDate,practicumHours);
    }
    public static SessionEntity build(Long id, String name, String description, ProgramEntity program,
                                      LocalDate startDate, LocalDate endDate, int practicumHours){
        SessionEntity session = new SessionEntity();
        session.id = id;
        session.name = name;
        session.description  = description;
        session.program = program;
        session.startDate = startDate;
        session.endDate = endDate;
        session.practicumHours = practicumHours;

        return session;

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (id == null) {
            return false;
        }
        SessionEntity other = (SessionEntity) obj;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(11);
    }

    @Override
    public String toString() {
        return "SessionEntity{" +
                "program=" + program +
                '}';
    }
}
