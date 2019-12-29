package carzanodev.genuniv.microservices.enrolment.api.enrolment;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentEnrolmentDTO {

    @JsonProperty("student_id")
    private long studentId;

    @JsonProperty("enrolments")
    private EnrolmentDTO[] enrolments;

    @Data
    @AllArgsConstructor
    public static class EnrolmentDTO {

        @JsonProperty("id")
        private long id;

        @JsonProperty("offering_id")
        private long offeringId;

    }

    @Data
    @AllArgsConstructor
    public static class List {

        @JsonProperty("student_enrolments")
        private Collection<StudentEnrolmentDTO> studentEnrolments;

    }

}
