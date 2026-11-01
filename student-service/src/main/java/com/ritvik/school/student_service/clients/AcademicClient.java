package com.ritvik.school.student_service.clients;

import com.ritvik.school.student_service.dtos.ClassRoomDto;
import com.ritvik.school.student_service.dtos.SubjectDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ACADEMIC-SERVICE")
    public interface AcademicClient {

        @GetMapping("/subjects/viewSubjectById")
        SubjectDto getSubject(@RequestParam("subjectId") Long subjectId);

        @GetMapping("/classrooms/viewClassRoomById")
       ClassRoomDto viewYourClassRoomById(@RequestParam("classRoomId") long classRoomId);
}
