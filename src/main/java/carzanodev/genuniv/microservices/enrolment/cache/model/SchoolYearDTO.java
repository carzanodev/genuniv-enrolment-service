package carzanodev.genuniv.microservices.enrolment.cache.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SchoolYearDTO {

    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @Data
    @NoArgsConstructor
    public static class List {
        @JsonProperty("school_years")
        private Set<SchoolYearDTO> schoolYears;
    }

}
