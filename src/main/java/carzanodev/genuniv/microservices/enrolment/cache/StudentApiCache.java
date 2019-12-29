package carzanodev.genuniv.microservices.enrolment.cache;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import carzanodev.genuniv.microservices.common.cache.ApiCache;
import carzanodev.genuniv.microservices.common.model.dto.StandardResponse;
import carzanodev.genuniv.microservices.enrolment.cache.model.StudentDTO;
import carzanodev.genuniv.microservices.enrolment.cache.model.StudentDTO.List;
import carzanodev.genuniv.microservices.enrolment.config.IntraServiceProperties;

@Component
class StudentApiCache extends ApiCache<Long, StudentDTO, StudentDTO.List> {

    private static final ParameterizedTypeReference<StandardResponse<StudentDTO.List>> responseType = new ParameterizedTypeReference<>() {
    };

    @Autowired
    public StudentApiCache(RestTemplate restTemplate, IntraServiceProperties intraServiceProperties) {
        super(restTemplate, intraServiceProperties.getPersonalRecords().getStudentApiUrl(), intraServiceProperties.getPersonalRecords().getStudentApiUrl() + "/info");
    }

    @Override
    protected ParameterizedTypeReference<StandardResponse<List>> getResponseType() {
        return responseType;
    }

    @Override
    protected Collection<StudentDTO> getResultData(StandardResponse<StudentDTO.List> response) {
        return response.getResponse().getStudents();
    }

    @Override
    protected Long id(StudentDTO studentDTO) {
        return studentDTO.getId();
    }

}
