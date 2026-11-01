package com.ritvik.school.academic_service.services;

import com.ritvik.school.academic_service.dtos.ClassRoomDto;
import com.ritvik.school.academic_service.entities.ClassRoom;
import com.ritvik.school.academic_service.exceptions.ClassRoomAlreadyExistsException;
import com.ritvik.school.academic_service.exceptions.ClassRoomDoesNotExistsException;
import com.ritvik.school.academic_service.repositories.ClassRoomRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClassRoomService implements ClassRoomServiceImpl {

        private final ClassRoomRepo classRoomRepo;
        public ClassRoomService(ClassRoomRepo classRoomRepo) {
            this.classRoomRepo = classRoomRepo;
        }


        private ClassRoomDto toClassRoomDto(ClassRoom classRoom) {
            ClassRoomDto classRoomDto = new ClassRoomDto();

            classRoomDto.setClasRoomId(classRoom.getClassRoomId());
            classRoomDto.setStandard(classRoom.getStandard());
            classRoomDto.setSection(classRoom.getSection());
            classRoomDto.setAcademicYear(classRoom.getAcademicYear());
            classRoomDto.setCapacity(classRoom.getCapacity());

            return classRoomDto;
        }

        private ClassRoom toClassRoom(ClassRoomDto classRoomDto) {
            ClassRoom classRoom = new ClassRoom();

            classRoom.setClassRoomId(classRoomDto.getClasRoomId());
            classRoom.setStandard(classRoomDto.getStandard());
            classRoom.setSection(classRoomDto.getSection());
            classRoom.setAcademicYear(classRoomDto.getAcademicYear());
            classRoom.setCapacity(classRoomDto.getCapacity());

            return classRoom;
        }



        @Override
        public ClassRoomDto createClassRoom(ClassRoomDto classRoomDto){

            if (classRoomRepo.findById(classRoomDto.getClasRoomId()).isPresent()) {
                throw new ClassRoomAlreadyExistsException(
                        "ClassRoom already exists with class ID : "
                                + classRoomDto.getClasRoomId());
            }


            return this.toClassRoomDto(classRoomRepo.save(this.toClassRoom(classRoomDto)));
        }


        @Override
        public Page<ClassRoomDto> viewAllClassRoom(int page, int size, String sortBy,
                                                   String direction) {

            Sort sort = direction.equalsIgnoreCase("desc")

                    ? Sort.by(sortBy).descending()

                    : Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(page, size, sort);

            Page<ClassRoom> classRoomPage = classRoomRepo.findAll(pageable);

            Page<ClassRoomDto> dtoPage =
                    classRoomPage.map(this::toClassRoomDto);

            return dtoPage;


        }

        @Override
        public ClassRoomDto viewClassRoomById(long classRoomId) {

            return classRoomRepo.findById(classRoomId).map(this::toClassRoomDto).orElseThrow(() ->
                    new ClassRoomDoesNotExistsException(
                            "ClassRoom does not exist with class ID: "
                                    + classRoomId));
        }

        @Override
        public ClassRoomDto updateClassRoom(ClassRoomDto classRoomDto) {
            ClassRoom classRoom = new ClassRoom();

            classRoom.setClassRoomId(classRoomDto.getClasRoomId());
            classRoom.setStandard(classRoomDto.getStandard());
            classRoom.setSection(classRoomDto.getSection());
            classRoom.setAcademicYear(classRoomDto.getAcademicYear());
            classRoom.setCapacity(classRoomDto.getCapacity());

            return this.toClassRoomDto(classRoomRepo.save(classRoom));
        }

        @Override
        public void deleteClassRoom(long classRoomId) {
            classRoomRepo.deleteById(classRoomId);
        }

    }


