package carzanodev.genuniv.microservices.enrolment.persistence.repository;

import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import carzanodev.genuniv.microservices.enrolment.config.CustomExceptionHandler.CapacityExceededException;
import carzanodev.genuniv.microservices.enrolment.config.CustomExceptionHandler.InexistentCounterException;
import carzanodev.genuniv.microservices.enrolment.persistence.entity.Enrolment;

public interface EnrolmentRepositoryCustom {

    @Transactional
    void saveEnrolments(Collection<Enrolment> enrolment) throws CapacityExceededException;

    @Transactional
    void deleteEnrolment(Enrolment enrolment) throws InexistentCounterException;

}
