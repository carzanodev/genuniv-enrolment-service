package carzanodev.genuniv.microservices.enrolment.config;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler;
import carzanodev.genuniv.microservices.common.model.dto.ApiError;
import carzanodev.genuniv.microservices.common.model.dto.StandardResponse;
import carzanodev.genuniv.microservices.common.util.parser.TimeSlotMapper.ScheduleConflictException;

@ControllerAdvice
public class CustomExceptionHandler extends CommonExceptionHandler {

    @ExceptionHandler({DuplicateEnrolmentException.class, ScheduleConflictException.class, CapacityExceededException.class, InexistentCounterException.class})
    public ResponseEntity<StandardResponse<Object>> handleConflictRequests(Exception e, WebRequest request) {
        ApiError apiError = new ApiError(new Timestamp(System.currentTimeMillis()), HttpStatus.BAD_REQUEST.value(), e.getMessage());
        StandardResponse<Object> errorResponse = new StandardResponse(apiError);
        return new ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST);
    }

    public static class DuplicateEnrolmentException extends Exception {
        public DuplicateEnrolmentException(Collection<Long> duplicateEnrolmentIds) {
            super("Already enrolled to the following offerings: " + Arrays.toString(duplicateEnrolmentIds.toArray()));
        }
    }

    public static class CapacityExceededException extends Exception {
        public CapacityExceededException(long offeringId, int capacity) {
            super(String.format("Offering(%d) has exceeded its %d capacity!", offeringId, capacity));
        }
    }

    public static class InexistentCounterException extends Exception {
        public InexistentCounterException(long id) {
            super(String.format("Counter with id:%d does not exist yet!", id));
        }
    }

}
