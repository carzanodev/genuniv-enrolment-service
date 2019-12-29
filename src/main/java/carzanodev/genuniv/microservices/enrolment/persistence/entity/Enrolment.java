package carzanodev.genuniv.microservices.enrolment.persistence.entity;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
public class Enrolment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "offering_id")
    private long offeringId;

    @Column(name = "student_id")
    private long studentId;

    @Column(name = "assigned_staff_id")
    private long assignedStaffId;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "inserted_at", insertable = false, updatable = false)
    private Timestamp insertedAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Timestamp updatedAt;

}
