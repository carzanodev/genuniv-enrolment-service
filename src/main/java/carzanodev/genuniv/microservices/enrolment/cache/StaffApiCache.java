package carzanodev.genuniv.microservices.enrolment.cache;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import carzanodev.genuniv.microservices.common.cache.ApiCache;
import carzanodev.genuniv.microservices.common.model.dto.StandardResponse;
import carzanodev.genuniv.microservices.enrolment.cache.model.StaffDTO;
import carzanodev.genuniv.microservices.enrolment.config.IntraServiceProperties;

@Component
class StaffApiCache extends ApiCache<Long, StaffDTO, StaffDTO.List> {

    private static final ParameterizedTypeReference<StandardResponse<StaffDTO.List>> responseType = new ParameterizedTypeReference<>() {
    };

    @Autowired
    public StaffApiCache(RestTemplate restTemplate, IntraServiceProperties intraServiceProperties) {
        super(
                restTemplate,
                intraServiceProperties.getPersonalRecords().getStaffApiUrl(),
                intraServiceProperties.getPersonalRecords().getStaffApiUrl() + "/info");
    }

    @Override
    protected ParameterizedTypeReference<StandardResponse<StaffDTO.List>> getResponseType() {
        return responseType;
    }

    @Override
    protected Collection<StaffDTO> getResultData(StandardResponse<StaffDTO.List> response) {
        return response.getResponse().getStaffs();
    }

    @Override
    protected Long id(StaffDTO staffDTO) {
        return staffDTO.getId();
    }

}
