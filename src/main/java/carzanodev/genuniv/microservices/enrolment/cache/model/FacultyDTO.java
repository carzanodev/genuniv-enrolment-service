package carzanodev.genuniv.microservices.enrolment.cache.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FacultyDTO {

    @JsonProperty("id")
    private long id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("middle_name")
    private String middleName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("college_id")
    private int collegeId;

    @Data
    @NoArgsConstructor
    public static class List {
        @JsonProperty("faculties")
        private Set<FacultyDTO> faculties;
    }

}
