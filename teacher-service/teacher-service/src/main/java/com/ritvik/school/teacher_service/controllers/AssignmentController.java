package com.ritvik.school.teacher_service.controllers;

import com.ritvik.school.teacher_service.dtos.AssignmentDto;
import com.ritvik.school.teacher_service.services.AssignmentService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {


        private final AssignmentService assignmentService;

        public AssignmentController(AssignmentService assignmentService)
        {
            this.assignmentService = assignmentService;
        }


        @PostMapping("/createAssignment")
        public ResponseEntity<AssignmentDto> createAssignment(@RequestBody AssignmentDto assignmentDto){
            return ResponseEntity.ok(assignmentService.createAssignment(assignmentDto));
        }

        @GetMapping("/viewAllAssignments")
        public ResponseEntity<Page<AssignmentDto>> viewAllAssignments(@RequestParam int page,

                                                                   @RequestParam int size,

                                                                   @RequestParam(defaultValue = "studentId") String sortBy,

                                                                   @RequestParam(defaultValue = "asc") String direction){
            return ResponseEntity.ok(assignmentService.viewAllAssignment(page, size, sortBy, direction));
        }


        @GetMapping("/viewAssignmentById")
        public ResponseEntity<AssignmentDto> viewAssignmentById(@RequestParam long assignmentId){
            return ResponseEntity.ok(assignmentService.viewAssignmentById(assignmentId));
        }

        @PutMapping("/updateAssignment")
        public ResponseEntity<AssignmentDto> updateAssignment( @RequestBody AssignmentDto assignmentDto){
            return ResponseEntity.ok(assignmentService.updateAssignment(assignmentDto));
        }

        @DeleteMapping("/deleteAssignment")
        public ResponseEntity<String> deleteAssignment(@RequestParam long assignmentId){
            assignmentService.deleteAssignment(assignmentId);
            return ResponseEntity.ok("Assignment deleted successfully");
        }

    }


