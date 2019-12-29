package carzanodev.genuniv.microservices.enrolment.persistence.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import carzanodev.genuniv.microservices.enrolment.persistence.entity.Enrolment;

@Repository
public interface EnrolmentRepository extends JpaRepository<Enrolment, Long>, EnrolmentRepositoryCustom {

    Set<Enrolment> findAllByStudentIdEqualsAndIsActiveEquals(long id, boolean isActive);

//    Set<Enrolment> findAllByStudentIdEqualsAndIsActiveEquals(long id, boolean isActive);

    @Query("SELECT e FROM Enrolment e WHERE e.isActive = TRUE")
    Set<Enrolment> findAllActive();

}
