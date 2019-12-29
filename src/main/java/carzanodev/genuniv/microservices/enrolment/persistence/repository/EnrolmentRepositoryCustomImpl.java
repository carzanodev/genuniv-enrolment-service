package carzanodev.genuniv.microservices.enrolment.persistence.repository;

import java.util.Collection;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;

import carzanodev.genuniv.microservices.enrolment.cache.model.OfferingDTO;
import carzanodev.genuniv.microservices.enrolment.config.CustomExceptionHandler.CapacityExceededException;
import carzanodev.genuniv.microservices.enrolment.config.CustomExceptionHandler.InexistentCounterException;
import carzanodev.genuniv.microservices.enrolment.persistence.entity.Enrolment;

import static carzanodev.genuniv.microservices.enrolment.cache.GeneralCacheContext.OFFERING;

public class EnrolmentRepositoryCustomImpl implements EnrolmentRepositoryCustom {

    private final EntityManager entityManager;
    private final EnrolmentCounterRepository counterRepo;

    @Autowired
    public EnrolmentRepositoryCustomImpl(EntityManager entityManager, EnrolmentCounterRepository counterRepo) {
        this.entityManager = entityManager;
        this.counterRepo = counterRepo;
    }

    @Override
    public void saveEnrolments(Collection<Enrolment> enrolmentSet) throws CapacityExceededException {
        for (Enrolment e : enrolmentSet) {
            OfferingDTO offering = OFFERING.get(e.getOfferingId());
            long idKey = offering.getId();
            int capacity = offering.getCapacity();

            counterRepo.atomicIncrement(idKey, capacity);
            entityManager.persist(e);
        }

        entityManager.flush();
    }

    @Override
    public void deleteEnrolment(Enrolment enrolment) throws InexistentCounterException {
        OfferingDTO offering = OFFERING.get(enrolment.getOfferingId());
        long idKey = offering.getId();

        counterRepo.atomicDecrement(idKey);
        entityManager.remove(enrolment);

        entityManager.flush();
    }

}
