package carzanodev.genuniv.microservices.enrolment.api.enrolment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.InvalidReferenceValueException;
import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.InvalidTargetEntityException;
import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.NonEmptyException;
import carzanodev.genuniv.microservices.common.model.dto.ResponseMeta;
import carzanodev.genuniv.microservices.common.model.dto.StandardResponse;
import carzanodev.genuniv.microservices.common.util.parser.TimeSlotMapper;
import carzanodev.genuniv.microservices.common.util.parser.TimeSlotMapper.ScheduleConflictException;
import carzanodev.genuniv.microservices.common.util.parser.TimeSlotSchedule;
import carzanodev.genuniv.microservices.enrolment.api.enrolment.StudentEnrolmentDTO.EnrolmentDTO;
import carzanodev.genuniv.microservices.enrolment.cache.model.OfferingDTO;
import carzanodev.genuniv.microservices.enrolment.cache.model.ScheduleDTO;
import carzanodev.genuniv.microservices.enrolment.cache.model.StudentDTO;
import carzanodev.genuniv.microservices.enrolment.config.CustomExceptionHandler.DuplicateEnrolmentException;
import carzanodev.genuniv.microservices.enrolment.config.IntraServiceProperties;
import carzanodev.genuniv.microservices.enrolment.persistence.entity.Enrolment;
import carzanodev.genuniv.microservices.enrolment.persistence.repository.EnrolmentRepository;

import static carzanodev.genuniv.microservices.common.util.MetaMessage.CREATE_MSG;
import static carzanodev.genuniv.microservices.common.util.MetaMessage.DELETE_MSG;
import static carzanodev.genuniv.microservices.common.util.MetaMessage.LIST_MSG;
import static carzanodev.genuniv.microservices.common.util.MetaMessage.RETRIEVE_MSG;
import static carzanodev.genuniv.microservices.enrolment.cache.GeneralCacheContext.OFFERING;
import static carzanodev.genuniv.microservices.enrolment.cache.GeneralCacheContext.SCHEDULE;
import static carzanodev.genuniv.microservices.enrolment.cache.GeneralCacheContext.STUDENT;

@Service
class EnrolmentService {

    private final EnrolmentRepository enrolmentRepo;

    @Autowired
    public EnrolmentService(EnrolmentRepository enrolmentRepo, RestTemplate restTemplate, IntraServiceProperties intraProperties) {
        this.enrolmentRepo = enrolmentRepo;
    }

    public StandardResponse<StudentEnrolmentDTO.List> getAllEnrolments(boolean isActiveOnly) {
        Map<Long, Set<Enrolment>> student2EnrolmentMap = new HashMap<>();
        (isActiveOnly ? enrolmentRepo.findAllActive() : enrolmentRepo.findAll())
                .stream()
                .forEach(i -> {
                    long studentId = i.getStudentId();
                    if (!student2EnrolmentMap.containsKey(studentId)) {
                        student2EnrolmentMap.put(studentId, new LinkedHashSet<>());
                    }

                    student2EnrolmentMap.get(studentId).add(i);
                });

        Set<StudentEnrolmentDTO> studentEnrolmentDtos = new HashSet<>();
        for (Entry<Long, Set<Enrolment>> entry : student2EnrolmentMap.entrySet()) {
            long studentId = entry.getKey();
            Set<Enrolment> enrolments = entry.getValue();

            studentEnrolmentDtos.add(entityToDto(studentId, enrolments));
        }

        StudentEnrolmentDTO.List response = new StudentEnrolmentDTO.List(studentEnrolmentDtos);
        ResponseMeta meta = ResponseMeta.createBasicMeta(LIST_MSG.make(studentEnrolmentDtos.size()));

        return new StandardResponse<>(response, meta);
    }

    public StandardResponse<StudentEnrolmentDTO.List> getEnrolmentsByStudentId(long studentId, boolean isActiveOnly) throws InvalidTargetEntityException, InvalidReferenceValueException {
        if (STUDENT.get(studentId) == null) {
            throw new InvalidReferenceValueException("student_id", String.valueOf(studentId));
        }

        Set<Enrolment> enrolments = enrolmentRepo.findAllByStudentIdEqualsAndIsActiveEquals(studentId, isActiveOnly);

        StudentEnrolmentDTO response = entityToDto(studentId, enrolments);
        ResponseMeta meta = ResponseMeta.createBasicMeta(RETRIEVE_MSG.make(studentId));


        return new StandardResponse<>(new StudentEnrolmentDTO.List(Sets.newSet(response)), meta);
    }

    public StandardResponse<StudentEnrolmentDTO> addEnrolmentsByStudentId(StudentEnrolmentDTO studentEnrolmentDto) throws Exception {
        Collection<Enrolment> newEnrolments = dtoToNewEntity(studentEnrolmentDto);

        validateEnrolment(studentEnrolmentDto.getStudentId(), newEnrolments);
        enrolmentRepo.saveEnrolments(newEnrolments);

        ResponseMeta meta = ResponseMeta.createBasicMeta(CREATE_MSG.make(studentEnrolmentDto.getStudentId()));

        return new StandardResponse<>(studentEnrolmentDto, meta);
    }

    public StandardResponse<StudentEnrolmentDTO> deleteEnrolment(long enrolmentId) throws InvalidTargetEntityException {
        Optional<Enrolment> enrolmentFromDb = enrolmentRepo.findById(enrolmentId);

        if (enrolmentFromDb.isEmpty()) {
            throw new InvalidTargetEntityException("enrolment", String.valueOf(enrolmentId));
        }

        Enrolment enrolment = enrolmentFromDb.get();
        enrolmentRepo.delete(enrolment);

        StudentEnrolmentDTO response = entityToDto(enrolment.getStudentId(), Sets.newSet(enrolment));
        ResponseMeta meta = ResponseMeta.createBasicMeta(DELETE_MSG.make(enrolmentId));

        return new StandardResponse<>(response, meta);
    }

    private StudentEnrolmentDTO entityToDto(long studentId, Set<Enrolment> enrolments) {
        ArrayList<EnrolmentDTO> enrolmentDTOs = new ArrayList<>();
        for (Enrolment enrolment : enrolments) {
            enrolmentDTOs.add(new EnrolmentDTO(enrolment.getId(), enrolment.getOfferingId()));
        }

        StudentEnrolmentDTO studentEnrolmentDTO = new StudentEnrolmentDTO();
        studentEnrolmentDTO.setStudentId(studentId);
        studentEnrolmentDTO.setEnrolments(enrolmentDTOs.toArray(new EnrolmentDTO[enrolmentDTOs.size()]));

        return studentEnrolmentDTO;
    }

    private Collection<Enrolment> dtoToNewEntity(StudentEnrolmentDTO studentEnrolment) throws InvalidReferenceValueException, NonEmptyException, InvalidTargetEntityException {

        long studentId = studentEnrolment.getStudentId();
        Optional<StudentDTO> student = Optional.ofNullable(STUDENT.get(studentId));
        if (student.isEmpty()) {
            throw new InvalidReferenceValueException("student_id", String.valueOf(studentId));
        }

        List<Enrolment> enrolments = new ArrayList<>();
        for (EnrolmentDTO e : studentEnrolment.getEnrolments()) {
            long offeringId = e.getOfferingId();
            if (offeringId > 0) {
                Optional<OfferingDTO> offering = Optional.ofNullable(OFFERING.get(offeringId));

                if (offering.isEmpty()) {
                    throw new InvalidReferenceValueException("offering_id", String.valueOf(offeringId));
                }

                Enrolment enrolment = new Enrolment();
                enrolment.setStudentId(studentId);
                enrolment.setOfferingId(offering.get().getId());

                enrolments.add(enrolment);
            } else {
                throw new NonEmptyException("offering_id");
            }
        }

        return enrolments;
    }

    private void validateEnrolment(long studentId, Collection<Enrolment> newEnrolments) throws ScheduleConflictException, DuplicateEnrolmentException {
        Set<Long> currentEnrolmentOfferings = enrolmentRepo.findAllByStudentIdEqualsAndIsActiveEquals(studentId, true)
                .stream()
                .map(Enrolment::getOfferingId)
                .collect(Collectors.toSet());

        Set<TimeSlotSchedule> currentSchedule = new LinkedHashSet<>();
        currentEnrolmentOfferings
                .stream()
                .map(i -> OFFERING.get(i))
                .map(j -> SCHEDULE.get(j.getScheduleId()))
                .map(ScheduleDTO::getTimeSlotSchedules)
                .forEach(currentSchedule::addAll);

        TimeSlotMapper mapper = new TimeSlotMapper(currentSchedule);
        List<Long> duplicateEnrolments = new ArrayList<>();
        for (Enrolment ne : newEnrolments) {
            OfferingDTO o = OFFERING.get(ne.getOfferingId());
            ScheduleDTO s = SCHEDULE.get(o.getScheduleId());

            if (currentEnrolmentOfferings.contains(ne.getOfferingId())) {
                duplicateEnrolments.add(ne.getOfferingId());
            } else {
                mapper.putSchedules(s.getTimeSlotSchedules());
            }
        }

        if (duplicateEnrolments.size() > 0) {
            throw new DuplicateEnrolmentException(duplicateEnrolments);
        }
    }

}