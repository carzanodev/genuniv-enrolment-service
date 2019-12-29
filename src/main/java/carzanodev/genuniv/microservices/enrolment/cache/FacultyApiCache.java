package carzanodev.genuniv.microservices.enrolment.cache;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

import carzanodev.genuniv.microservices.common.cache.ApiCache;
import carzanodev.genuniv.microservices.common.model.dto.StandardResponse;
import carzanodev.genuniv.microservices.enrolment.cache.model.FacultyDTO;
import carzanodev.genuniv.microservices.enrolment.cache.model.FacultyDTO.List;
import carzanodev.genuniv.microservices.enrolment.config.IntraServiceProperties;

@Component
@Slf4j
class FacultyApiCache extends ApiCache<Long, FacultyDTO, FacultyDTO.List> {

    private static final ParameterizedTypeReference<StandardResponse<FacultyDTO.List>> responseType = new ParameterizedTypeReference<>() {
    };

    @Autowired
    public FacultyApiCache(RestTemplate restTemplate, IntraServiceProperties intraServiceProperties) {
        super(
                restTemplate,
                intraServiceProperties.getPersonalRecords().getFacultyApiUrl(),
                intraServiceProperties.getPersonalRecords().getFacultyApiUrl() + "/info");
    }

    @Override
    protected ParameterizedTypeReference<StandardResponse<List>> getResponseType() {
        return responseType;
    }

    @Override
    protected Collection<FacultyDTO> getResultData(StandardResponse<FacultyDTO.List> response) {
        return response.getResponse().getFaculties();
    }

    @Override
    protected Long id(FacultyDTO facultyDTO) {
        return facultyDTO.getId();
    }
}
