package carzanodev.genuniv.microservices.enrolment.persistence.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.stereotype.Repository;

import carzanodev.genuniv.microservices.enrolment.config.CustomExceptionHandler.CapacityExceededException;
import carzanodev.genuniv.microservices.enrolment.config.CustomExceptionHandler.InexistentCounterException;

@Repository
public class EnrolmentCounterRepository {

    private static final String ENROLMENT_COUNTER_PREFIX = "EC#";
    private static int STARTING_COUNTER_VALUE = 0;

    private final Map<String, RedisAtomicInteger> enrolmentCounterMap = new ConcurrentHashMap<>();
    private final RedisConnectionFactory factory;

    @Autowired
    public EnrolmentCounterRepository(RedisConnectionFactory factory) {
        this.factory = factory;
    }

    public int atomicIncrement(long key, int limit) throws CapacityExceededException {
        RedisAtomicInteger counter = getAtomicCounter(keyify(key));

        int valueNow = counter.incrementAndGet();
        if (valueNow > limit) {
            throw new CapacityExceededException(key, limit);
        } else {
            return valueNow;
        }
    }

    public int atomicDecrement(long key) throws InexistentCounterException {
        RedisAtomicInteger counter = getAtomicCounter(keyify(key), false);

        if (counter == null) {
            throw new InexistentCounterException(key);
        }

        return counter.decrementAndGet();
    }

    private RedisAtomicInteger getAtomicCounter(String key) {
        return getAtomicCounter(key, true);
    }

    private RedisAtomicInteger getAtomicCounter(String key, boolean isCreateOnNull) {
        RedisAtomicInteger counter = enrolmentCounterMap.get(key);

        if (counter == null && isCreateOnNull) {
            counter = new RedisAtomicInteger(key, factory, STARTING_COUNTER_VALUE);
        }

        return counter;
    }

    private String keyify(long id) {
        return ENROLMENT_COUNTER_PREFIX + id;
    }

}
