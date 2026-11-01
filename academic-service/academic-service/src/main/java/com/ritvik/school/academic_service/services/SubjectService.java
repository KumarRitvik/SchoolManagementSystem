package com.ritvik.school.academic_service.services;

import com.ritvik.school.academic_service.dtos.SubjectDto;
import com.ritvik.school.academic_service.entities.Subject;
import com.ritvik.school.academic_service.exceptions.SubjectAlreadyExistsException;
import com.ritvik.school.academic_service.exceptions.SubjectDoesNotExistsException;
import com.ritvik.school.academic_service.repositories.SubjectRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class SubjectService implements SubjectServiceImpl {


        private final SubjectRepo subjectRepo;
        public SubjectService(SubjectRepo subjectRepo) {
            this.subjectRepo = subjectRepo;
        }


        private SubjectDto toSubjectDto(Subject subject) {
            SubjectDto subjectDto = new SubjectDto();

            subjectDto.setSubjectId(subject.getSubjectId());
            subjectDto.setSubjectName(subject.getSubjectName());
            subjectDto.setSubjectCode(subject.getSubjectCode());
            subjectDto.setTheoryMarks(subject.getTheoryMarks());
            subjectDto.setPracticalMarks(subject.getPracticalMarks());

            return subjectDto;
        }

        private Subject toSubject(SubjectDto subjectDto) {
            Subject subject = new Subject();

            subject.setSubjectId(subjectDto.getSubjectId());
            subject.setSubjectName(subjectDto.getSubjectName());
            subject.setSubjectCode(subjectDto.getSubjectCode());
            subject.setTheoryMarks(subjectDto.getTheoryMarks());
            subject.setPracticalMarks(subjectDto.getPracticalMarks());

            return subject;
        }

        @Override
        public SubjectDto createSubject(SubjectDto subjectDto){

            if (subjectRepo.findById(subjectDto.getSubjectId()).isPresent()) {
                throw new SubjectAlreadyExistsException(
                        "Subject already exists with subject ID : "
                                + subjectDto.getSubjectId());
            }


            return this.toSubjectDto(subjectRepo.save(this.toSubject(subjectDto)));
        }


        @Override
        public Page<SubjectDto> viewAllSubjects(int page, int size, String sortBy,
                                                String direction) {

            Sort sort = direction.equalsIgnoreCase("desc")

                    ? Sort.by(sortBy).descending()

                    : Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Subject> subjectPage = subjectRepo.findAll(pageable);

            return subjectPage.map(this::toSubjectDto);


        }

        @Override
        public SubjectDto viewSubjectById(long subjectId) {
            Subject subject = subjectRepo.findById(subjectId)
                    .orElseThrow(() -> new SubjectDoesNotExistsException("Subject not found with id " + subjectId));

            return this.toSubjectDto(subject);
        }

        @Override
        public SubjectDto updateSubject(SubjectDto subjectDto) {
            Subject subject = subjectRepo.findById(subjectDto.getSubjectId())
                    .orElseThrow(() -> new SubjectDoesNotExistsException("Subject not found with id " + subjectDto.getSubjectId()));

            subject.setSubjectName(subjectDto.getSubjectName());
            subject.setSubjectCode(subjectDto.getSubjectCode());
            subject.setTheoryMarks(subjectDto.getTheoryMarks());
            subject.setPracticalMarks(subjectDto.getPracticalMarks());

            return this.toSubjectDto(subjectRepo.save(subject));
        }

        @Override
        public void deleteSubject(long subjectId) {
            subjectRepo.deleteById(subjectId);
        }

    }




