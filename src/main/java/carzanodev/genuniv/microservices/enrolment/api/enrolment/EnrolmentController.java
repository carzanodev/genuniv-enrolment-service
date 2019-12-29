package carzanodev.genuniv.microservices.enrolment.api.enrolment;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.InvalidReferenceValueException;
import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.InvalidTargetEntityException;
import carzanodev.genuniv.microservices.common.model.dto.StandardResponse;

@RestController
@RequestMapping("/api/v1/enrolment")
class EnrolmentController {

    private final EnrolmentService enrolmentService;

    @Autowired
    EnrolmentController(EnrolmentService enrolmentService) {
        this.enrolmentService = enrolmentService;
    }

    // ↓↓↓ GET ↓↓↓

    @GetMapping
    StandardResponse<StudentEnrolmentDTO.List> getEnrolments(@RequestParam(name = "student_id", defaultValue = StringUtils.EMPTY) String studentId) throws InvalidReferenceValueException, InvalidTargetEntityException {
        return studentId.isEmpty() ? enrolmentService.getAllEnrolments(true) : enrolmentService.getEnrolmentsByStudentId(Long.valueOf(studentId), true);
    }

    // ↓↓↓ POST ↓↓↓

    @PostMapping
    StandardResponse<StudentEnrolmentDTO> postEnrolments(@Valid @NonNull @RequestBody StudentEnrolmentDTO studentEnrolmentDto) throws Exception {
        return enrolmentService.addEnrolmentsByStudentId(studentEnrolmentDto);
    }

     // ↓↓↓ DELETE ↓↓↓

    @DeleteMapping(path = "{id}")
    StandardResponse<StudentEnrolmentDTO> deleteEnrolment(@PathVariable("id") long id) throws InvalidTargetEntityException {
        return enrolmentService.deleteEnrolment(id);
    }

}
