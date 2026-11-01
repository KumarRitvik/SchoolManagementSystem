package com.ritvik.school.academic_service.controllers;

import com.ritvik.school.academic_service.dtos.ClassRoomDto;
import com.ritvik.school.academic_service.services.ClassRoomService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("classrooms")
public class ClassRoomController {


        private final ClassRoomService classRoomService;

        public ClassRoomController(ClassRoomService classRoomService) {
            this.classRoomService = classRoomService;
        }


        @PostMapping("/createClassRoom")
        public ResponseEntity<ClassRoomDto> createClassRoom(@RequestBody ClassRoomDto classRoomDto) {
            return ResponseEntity.ok(classRoomService.createClassRoom(classRoomDto));
        }

        @GetMapping("/viewAllClassRooms")
        public ResponseEntity<Page<ClassRoomDto>> viewAllClassRoom(@RequestParam int page,

                                                                   @RequestParam int size,

                                                                   @RequestParam(defaultValue = "studentId") String sortBy,

                                                                   @RequestParam(defaultValue = "asc") String direction){
            return ResponseEntity.ok(classRoomService.viewAllClassRoom(page, size, sortBy, direction));
        }


        @GetMapping("/viewClassRoomById")
        public ResponseEntity<ClassRoomDto> viewClassRoomById(@RequestParam Long classRoomId){
            return ResponseEntity.ok(classRoomService.viewClassRoomById(classRoomId));
        }

        @PutMapping("/updateClassRoom")
        public ResponseEntity<ClassRoomDto> updateClassRoom( @RequestBody ClassRoomDto classRoomDto){
            return ResponseEntity.ok(classRoomService.updateClassRoom(classRoomDto));
        }

        @DeleteMapping("/deleteClassRoom")
        public ResponseEntity<String> deleteClassRoom(@RequestParam int classRoomId){
            classRoomService.deleteClassRoom(classRoomId);
            return ResponseEntity.ok("Class room deleted successfully");
        }

}
