package com.play.java.bbgeducation.domain.sessioncourse;

import com.play.java.bbgeducation.domain.courses.CourseEntity;
import com.play.java.bbgeducation.domain.programs.SessionEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="session_course", uniqueConstraints = {@UniqueConstraint(name = "session_id_course_id_unique", columnNames = {"session_id", "course_id"})})
public class SessionCourseEntity implements Serializable {
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

    @OneToMany(mappedBy = "sessionCourse",fetch = FetchType.LAZY)
    private Set<ClassEntity> classes;

    //want to set as not insertable or updatable, but then findById doesn't return the dates...
    @Column(name = "created_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @CreationTimestamp
    private OffsetDateTime createdDateTime;

    @Column(name = "updated_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @UpdateTimestamp
    private OffsetDateTime updatedDateTime;

    public SessionCourseEntity(){

    }

    public static SessionCourseEntity create(SessionEntity session, CourseEntity course){
        return build(null, session, course);
    }

    public static SessionCourseEntity build(Long id, SessionEntity session, CourseEntity course){
        SessionCourseEntity courseEntity = new SessionCourseEntity();
        courseEntity.id = id;
        courseEntity.session = session;
        courseEntity.course = course;

        return courseEntity;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (id == null){
            return false;
        }
        SessionCourseEntity that = (SessionCourseEntity) obj;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(14);
    }

    @Override
    public String toString() {
        return "SessionCourseEntity{" +
                "id=" + id +
                '}';
    }
}
