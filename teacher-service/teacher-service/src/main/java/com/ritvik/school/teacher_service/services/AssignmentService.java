package com.ritvik.school.teacher_service.services;

import com.ritvik.school.teacher_service.dtos.AssignmentDto;
import com.ritvik.school.teacher_service.entities.Assignment;
import com.ritvik.school.teacher_service.exceptions.AssignmentAlreadyExistsException;
import com.ritvik.school.teacher_service.repositories.AssignmentRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AssignmentService implements AssignmentServiceImpl {


        private final AssignmentRepo assignmentRepo;
        public AssignmentService(AssignmentRepo assignmentRepo) {
            this.assignmentRepo = assignmentRepo;
        }


        private AssignmentDto toAssignmentDto(Assignment assignment) {
            AssignmentDto assignmentDto = new AssignmentDto();

            assignmentDto.setAssignmentId(assignment.getAssignmentId());
            assignmentDto.setActive(assignment.getActive());
            assignmentDto.setAcademicYear(assignment.getAcademicYear());



            return assignmentDto;
        }

        private Assignment toAssignment(AssignmentDto assignmentDto) {
            Assignment assignment = new Assignment();

            assignment.setAssignmentId(assignmentDto.getAssignmentId());
            assignment.setActive(assignmentDto.getActive());
            assignment.setAcademicYear(assignmentDto.getAcademicYear());

            return assignment;
        }


        @Override
        public AssignmentDto createAssignment(AssignmentDto assignmentDto){

            Assignment assignment = this.toAssignment(assignmentDto);

            if (assignmentRepo.findById(assignmentDto.getAssignmentId()).isPresent()) {
                throw new AssignmentAlreadyExistsException(
                        "Assignment already exists with assignment ID : "
                                + assignment.getAssignmentId());
            }


            return this.toAssignmentDto(assignmentRepo.save(this.toAssignment(assignmentDto)));
        }


        @Override
        public Page<AssignmentDto> viewAllAssignment(int page, int size, String sortBy,
                                                     String direction) {

            Sort sort = direction.equalsIgnoreCase("desc")

                    ? Sort.by(sortBy).descending()

                    : Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Assignment> assignmentPage = assignmentRepo.findAll(pageable);

            Page<AssignmentDto> dtoPage =
                    assignmentPage.map(this::toAssignmentDto);

            return dtoPage;


        }

        @Override
        public AssignmentDto viewAssignmentById(long assignmentId) {
            return this.toAssignmentDto(assignmentRepo.findById(assignmentId).orElseThrow(() -> new RuntimeException("Assignment not found")));
        }

        @Override
        public AssignmentDto updateAssignment(AssignmentDto assignmentDto) {
            Assignment assignment = new Assignment();

            assignment.setAssignmentId(assignmentDto.getAssignmentId());
            assignment.setActive(assignmentDto.getActive());
            assignment.setAcademicYear(assignmentDto.getAcademicYear());


            return this.toAssignmentDto(assignmentRepo.save(assignment));
        }

        @Override
        public void deleteAssignment(long assignmentId) {
            assignmentRepo.deleteById(assignmentId);
        }

    }
