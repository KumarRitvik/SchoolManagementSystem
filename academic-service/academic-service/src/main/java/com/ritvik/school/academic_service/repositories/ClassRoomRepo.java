package com.ritvik.school.academic_service.repositories;

import com.ritvik.school.academic_service.entities.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassRoomRepo extends JpaRepository<ClassRoom, Long> {
}
