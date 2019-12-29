package carzanodev.genuniv.microservices.enrolment.cache;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import carzanodev.genuniv.microservices.common.cache.ApiCache;
import carzanodev.genuniv.microservices.common.model.dto.StandardResponse;
import carzanodev.genuniv.microservices.enrolment.cache.model.SchoolPeriodDTO;
import carzanodev.genuniv.microservices.enrolment.cache.model.SchoolPeriodDTO.List;
import carzanodev.genuniv.microservices.enrolment.config.IntraServiceProperties;

@Component
class SchoolPeriodApiCache extends ApiCache<Integer, SchoolPeriodDTO, SchoolPeriodDTO.List> {

    private static final ParameterizedTypeReference<StandardResponse<SchoolPeriodDTO.List>> responseType = new ParameterizedTypeReference<>() {
    };

    @Autowired
    public SchoolPeriodApiCache(RestTemplate restTemplate, IntraServiceProperties properties) {
        super(
                restTemplate,
                properties.getGeneralInfo().getSchoolPeriodApiUrl(),
                properties.getGeneralInfo().getSchoolPeriodApiUrl() + "/info");
    }

    @Override
    protected ParameterizedTypeReference<StandardResponse<List>> getResponseType() {
        return responseType;
    }

    @Override
    protected Collection<SchoolPeriodDTO> getResultData(StandardResponse<SchoolPeriodDTO.List> response) {
        return response.getResponse().getSchoolPeriods();
    }

    @Override
    protected Integer id(SchoolPeriodDTO schoolPeriodDTO) {
        return schoolPeriodDTO.getId();
    }

}
