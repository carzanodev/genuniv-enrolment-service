package carzanodev.genuniv.microservices.enrolment.cache.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

import carzanodev.genuniv.microservices.common.util.parser.TimeSlotParser;
import carzanodev.genuniv.microservices.common.util.parser.TimeSlotSchedule;

@Data
@NoArgsConstructor
public class ScheduleDTO {

    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("time_slot")
    private String timeSlot;

    private transient Set<TimeSlotSchedule> timeSlotSchedules;

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
        this.timeSlotSchedules = TimeSlotParser.toTimeSlotSchedules(timeSlot);
    }

    @Data
    @NoArgsConstructor
    public static class List {
        @JsonProperty("schedules")
        private Set<ScheduleDTO> schedules;
    }

}
