package com.play.java.bbgeducation.domain.courses;

import com.play.java.bbgeducation.domain.sessioncourse.SessionCourseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="course")
public class CourseEntity implements Serializable {

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
    private Boolean isPublic = true;

    @NotNull
    @Column(columnDefinition ="BOOLEAN DEFAULT FALSE")
    private Boolean isOnline = false;

    @OneToMany(mappedBy = "course")
    Set<SessionCourseEntity> sessionCourses = new HashSet<>();

    //want to set as not insertable or updateable, but then findById doesn't return the dates...
    @Column(name = "created_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @CreationTimestamp
    private OffsetDateTime createdDateTime;

    @Column(name = "updated_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @UpdateTimestamp
    private OffsetDateTime updatedDateTime;

    @Column(name = "inactivated_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime inactivatedDateTime;

    public static CourseEntity create(String name, String description, Boolean isPublic, Boolean isOnline){
        return build(null, name, description,isPublic,isOnline);
    }

    public static CourseEntity build(Long id, String name, String description, Boolean isPublic, Boolean isOnline){
        CourseEntity course = new CourseEntity();
        course.id = id;
        course.name = name;
        course.description = description;
        course.isPublic = isPublic;
        course.isOnline = isOnline;

        return course;
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
        CourseEntity that = (CourseEntity) obj;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(12);
    }

    @Override
    public String toString() {
        return "CourseEntity{" +
                "id=" + id +
                '}';
    }
}
