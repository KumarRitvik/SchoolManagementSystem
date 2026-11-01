package com.ritvik.school.academic_service.services;

import com.ritvik.school.academic_service.dtos.ClassRoomDto;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ClassRoomServiceImpl {
    ClassRoomDto createClassRoom(ClassRoomDto classRoomDto);

    Page<ClassRoomDto> viewAllClassRoom(int page, int size, String sortBy,
                                        String direction);


    ClassRoomDto viewClassRoomById(long classRoomId);

    ClassRoomDto updateClassRoom(ClassRoomDto classRoomDto);

    void deleteClassRoom(long classRoomId);
}
