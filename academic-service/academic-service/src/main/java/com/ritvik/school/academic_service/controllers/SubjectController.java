package com.ritvik.school.academic_service.controllers;

import com.ritvik.school.academic_service.dtos.SubjectDto;
import com.ritvik.school.academic_service.services.SubjectService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    private final SubjectService subjectService;
        public SubjectController(SubjectService subjectService) {
            this.subjectService = subjectService;
        }

        @PostMapping("/createSubject")
        public ResponseEntity<SubjectDto> createSubject(@RequestBody SubjectDto subjectDto) {
            return ResponseEntity.ok(subjectService.createSubject(subjectDto));
        }

        @GetMapping("/viewAllSubjects")
        public ResponseEntity<Page<SubjectDto>> viewAllSubjects(@RequestParam int page,

                                                                 @RequestParam int size,

                                                                 @RequestParam(defaultValue = "studentId") String sortBy,

                                                                 @RequestParam(defaultValue = "asc") String direction){
            return ResponseEntity.ok(subjectService.viewAllSubjects(page, size, sortBy, direction));
        }


        @GetMapping("/viewSubjectById")
        public ResponseEntity<SubjectDto>viewSubjectById(@RequestParam long subjectId){
            return ResponseEntity.ok(subjectService.viewSubjectById(subjectId));
        }

        @PutMapping("/updateSubject")
        public ResponseEntity<SubjectDto> updateSubject( @RequestBody SubjectDto subjectDto){
            return ResponseEntity.ok(subjectService.updateSubject(subjectDto));
        }

        @DeleteMapping("/deleteSubject")
        public ResponseEntity<String> deleteSubject(@RequestParam long subjectId){
            subjectService.deleteSubject(subjectId);
            return ResponseEntity.ok("Subject deleted successfully");
        }

    }




