package carzanodev.genuniv.microservices.enrolment.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ConstructorBinding
@ConfigurationProperties(prefix = "intra.service")
public class IntraServiceProperties {

    private final GeneralInfo generalInfo;
    private final College college;
    private final PersonalRecords personalRecords;

    @AllArgsConstructor
    @Getter
    @ConstructorBinding
    @ToString
    public static final class GeneralInfo {
        private final String protocol;
        private final String serviceName;
        private final SchoolYearAPI schoolYearAPI;
        private final SchoolPeriodAPI schoolPeriodAPI;
        private final ScheduleAPI scheduleAPI;

        public String getSchoolYearApiUrl() {
            return protocol + "://" + serviceName + schoolYearAPI.getApiPrefix();
        }

        public String getSchoolPeriodApiUrl() {
            return protocol + "://" + serviceName + schoolPeriodAPI.getApiPrefix();
        }

        public String getScheduleApiUrl() {
            return protocol + "://" + serviceName + scheduleAPI.getApiPrefix();
        }

        @ConstructorBinding
        public static final class SchoolYearAPI extends Api {
            public SchoolYearAPI(String apiPrefix) {
                super(apiPrefix);
            }
        }

        @ConstructorBinding
        public static final class SchoolPeriodAPI extends Api {
            public SchoolPeriodAPI(String apiPrefix) {
                super(apiPrefix);
            }
        }

        @ConstructorBinding
        public static final class ScheduleAPI extends Api {
            public ScheduleAPI(String apiPrefix) {
                super(apiPrefix);
            }
        }
    }

    @AllArgsConstructor
    @Getter
    @ConstructorBinding
    @ToString
    public static final class College {
        private final String protocol;
        private final String serviceName;
        private final CollegeApi collegeApi;
        private final DegreeApi degreeApi;
        private final DegreeTypeApi degreeTypeApi;
        private final CourseApi courseApi;
        private final OfferingApi offeringApi;

        public String getCollegeApiUrl() {
            return protocol + "://" + serviceName + collegeApi.getApiPrefix();
        }

        public String getDegreeApiUrl() {
            return protocol + "://" + serviceName + degreeApi.getApiPrefix();
        }

        public String getDegreeTypeApiUrl() {
            return protocol + "://" + serviceName + degreeTypeApi.getApiPrefix();
        }

        public String getCourseApiUrl() {
            return protocol + "://" + serviceName + courseApi.getApiPrefix();
        }

        public String getOfferingApiUrl() {
            return protocol + "://" + serviceName + offeringApi.getApiPrefix();
        }

        @ConstructorBinding
        public static final class CollegeApi extends Api {
            public CollegeApi(String apiPrefix) {
                super(apiPrefix);
            }
        }

        @ConstructorBinding
        public static final class DegreeApi extends Api {
            public DegreeApi(String apiPrefix) {
                super(apiPrefix);
            }
        }

        @ConstructorBinding
        public static final class DegreeTypeApi extends Api {
            public DegreeTypeApi(String apiPrefix) {
                super(apiPrefix);
            }
        }

        @ConstructorBinding
        public static final class CourseApi extends Api {
            public CourseApi(String apiPrefix) {
                super(apiPrefix);
            }
        }

        @ConstructorBinding
        public static final class OfferingApi extends Api {
            public OfferingApi(String apiPrefix) {
                super(apiPrefix);
            }
        }
    }

    @AllArgsConstructor
    @Getter
    @ConstructorBinding
    @ToString
    public static final class PersonalRecords {
        private final String protocol;
        private final String serviceName;
        private final FacultyApi facultyApi;
        private final StaffApi staffApi;
        private final StudentApi studentApi;

        public String getFacultyApiUrl() {
            return protocol + "://" + serviceName + facultyApi.getApiPrefix();
        }

        public String getStaffApiUrl() {
            return protocol + "://" + serviceName + staffApi.getApiPrefix();
        }

        public String getStudentApiUrl() {
            return protocol + "://" + serviceName + studentApi.getApiPrefix();
        }

        @ConstructorBinding
        public static final class FacultyApi extends Api {
            public FacultyApi(String apiPrefix) {
                super(apiPrefix);
            }
        }

        @ConstructorBinding
        public static final class StaffApi extends Api {
            public StaffApi(String apiPrefix) {
                super(apiPrefix);
            }
        }

        @ConstructorBinding
        public static final class StudentApi extends Api {
            public StudentApi(String apiPrefix) {
                super(apiPrefix);
            }
        }
    }

    @AllArgsConstructor
    @Getter
    public static abstract class Api {
        private final String apiPrefix;
    }

}
