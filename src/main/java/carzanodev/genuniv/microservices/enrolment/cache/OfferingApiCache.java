package carzanodev.genuniv.microservices.enrolment.cache;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import carzanodev.genuniv.microservices.common.cache.ApiCache;
import carzanodev.genuniv.microservices.common.model.dto.StandardResponse;
import carzanodev.genuniv.microservices.enrolment.cache.model.OfferingDTO;
import carzanodev.genuniv.microservices.enrolment.cache.model.OfferingDTO.List;
import carzanodev.genuniv.microservices.enrolment.config.IntraServiceProperties;

@Component
class OfferingApiCache extends ApiCache<Long, OfferingDTO, OfferingDTO.List> {

    private static final ParameterizedTypeReference<StandardResponse<OfferingDTO.List>> responseType = new ParameterizedTypeReference<>() {
    };

    @Autowired
    public OfferingApiCache(RestTemplate restTemplate, IntraServiceProperties intraServiceProperties) {
        super(
                restTemplate,
                intraServiceProperties.getCollege().getOfferingApiUrl(),
                intraServiceProperties.getCollege().getOfferingApiUrl() + "/info");
    }

    @Override
    protected ParameterizedTypeReference<StandardResponse<List>> getResponseType() {
        return responseType;
    }

    @Override
    protected Collection<OfferingDTO> getResultData(StandardResponse<OfferingDTO.List> response) {
        return response.getResponse().getOfferings();
    }

    @Override
    protected Long id(OfferingDTO facultyDTO) {
        return facultyDTO.getId();
    }

}
