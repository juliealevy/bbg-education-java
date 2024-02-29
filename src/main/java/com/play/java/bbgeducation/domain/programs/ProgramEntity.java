package com.play.java.bbgeducation.domain.programs;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="program")
public class ProgramEntity implements Serializable {

    @Id
    @SequenceGenerator(name = "program_id_seq", sequenceName = "PROGRAM_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "program_id_seq")
    private Long id;

    @Column(unique = true)
    private String name;

    private String description;

    @OneToMany(mappedBy = "program",fetch = FetchType.LAZY)
    private Set<SessionEntity> sessions = new HashSet<>();

    //want to set as not insertable or updateable, but then findById doesn't return the dates...
    @Column(name = "created_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @CreationTimestamp
    private OffsetDateTime createdDateTime;

    @Column(name = "updated_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @UpdateTimestamp
    private OffsetDateTime updatedDateTime;

    @Column(name = "deactivated_date_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime deactivatedDateTime;

    public ProgramEntity(){

    }

    public static ProgramEntity create(String name, String description){
        return ProgramEntity.build(null, name, description);
    }

    public static ProgramEntity build(Long id, String name, String description){
        ProgramEntity programEntity = new ProgramEntity();
        programEntity.setId(id);
        programEntity.setName(name);
        programEntity.setDescription(description);

        return programEntity;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProgramEntity other = (ProgramEntity) obj;
        if (id == null) {
            return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return 10;
    }

    @Override
    public String toString() {
        return "ProgramEntity{" +
                "id=" + id +
                '}';
    }
}
