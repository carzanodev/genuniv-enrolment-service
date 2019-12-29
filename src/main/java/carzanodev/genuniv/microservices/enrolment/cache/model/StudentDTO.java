package carzanodev.genuniv.microservices.enrolment.cache.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudentDTO {

    @JsonProperty("id")
    private long id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("middle_name")
    private String middleName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("birthdate")
    private String birthDate;

    @JsonProperty("address")
    private String address;

    @JsonProperty("degree_id")
    private int degreeId;

    @Data
    @NoArgsConstructor
    public static class List {
        @JsonProperty("students")
        private Set<StudentDTO> students;
    }

}
