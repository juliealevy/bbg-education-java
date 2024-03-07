package com.play.java.bbgeducation.domain.sessioncourse;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

@Setter
@Getter
@Entity
@Table(name="class")
public class ClassEntity implements Serializable {

    @Id
    @SequenceGenerator(name = "class_id_seq", sequenceName = "CLASS_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "class_id_seq")
    private Long id;

    @Column(name = "class_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime classDateTime;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="session_course_id",referencedColumnName = "id")
    private SessionCourseEntity sessionCourse;

    //want to set as not insertable or updateable, but then findById doesn't return the dates...
    @Column(name = "created_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @CreationTimestamp
    private OffsetDateTime createdDateTime;

    @Column(name = "updated_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @UpdateTimestamp
    private OffsetDateTime updatedDateTime;

    public ClassEntity(){

    }

    public static ClassEntity create(SessionCourseEntity sessionCourse, OffsetDateTime classDateTime){
        return build(null,sessionCourse,classDateTime);
    }

    public static ClassEntity build(Long id, SessionCourseEntity sessionCourse, OffsetDateTime classDateTime){
        ClassEntity classEntity = new ClassEntity();
        classEntity.id = id;
        classEntity.sessionCourse = sessionCourse;
        classEntity.classDateTime = classDateTime;

        return classEntity;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()){
            return false;
        }
        if (id == null){
            return false;
        }
        ClassEntity that = (ClassEntity) obj;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(15);
    }

    @Override
    public String toString() {
        return "ClassEntity{" +
                "id=" + id +
                '}';
    }
}


