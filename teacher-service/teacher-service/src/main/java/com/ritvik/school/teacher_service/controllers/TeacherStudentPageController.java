package com.ritvik.school.teacher_service.controllers;

import com.ritvik.school.teacher_service.dtos.StudentDto;
import com.ritvik.school.teacher_service.services.TeacherService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/teacher-student-page")
public class TeacherStudentPageController {

    private final TeacherService teacherService;

    public TeacherStudentPageController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    // Student page home: enter a student ID.
    @GetMapping
    public String home() {
        return "teacher/student";
    }

    // =========================
    // VIEW STUDENT BY ID
    // Teacher Service endpoint:
    // GET /teachers/student/{studentId}
    // =========================
    @GetMapping("/view")
    public String viewStudent(
            @RequestParam Long studentId,
            Model model) {

        model.addAttribute(
                "student",
                teacherService.getStudentById(studentId));

        return "teacher/student-view";
    }

    // =========================
    // CREATE STUDENT
    // Teacher Service endpoint:
    // POST /teachers/student
    // =========================
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("student", new StudentDto());
        return "teacher/student-form";
    }

    @PostMapping("/create")
    public String createStudent(@ModelAttribute StudentDto dto) {

        StudentDto created = teacherService.createStudent(dto);

        return "redirect:/teacher-student-page/view/"
                + created.getStudentId();
    }

    // =========================
    // UPDATE STUDENT
    // Teacher Service endpoint:
    // PUT /teachers/student
    // =========================
    @GetMapping("/edit/{studentId}")
    public String editStudent(
            @PathVariable Long studentId,
            Model model) {

        model.addAttribute(
                "student",
                teacherService.getStudentById(studentId));

        return "teacher/student-form";
    }

    @PostMapping("/update")
    public String updateStudent(@ModelAttribute StudentDto dto) {

        teacherService.updateStudent(dto);

        return "redirect:/teacher-student-page/view/"
                + dto.getStudentId();
    }

    // =========================
    // DELETE STUDENT
    // Teacher Service endpoint:
    // DELETE /teachers/student/{studentId}
    // =========================
    @GetMapping("/delete/{studentId}")
    public String deleteStudent(@PathVariable Long studentId) {

        teacherService.deleteStudent(studentId);

        return "redirect:/teacher-student-page";
    }

    // =========================
    // UPLOAD STUDENT PHOTO
    // Teacher Service endpoint:
    // POST /teachers/student/{id}/photo
    // =========================
    @PostMapping("/{id}/photo")
    public String uploadStudentPhoto(
            @PathVariable Long id,
            @RequestParam("photo") MultipartFile file)
            throws IOException {

        teacherService.uploadStudentPhoto(id, file);

        return "redirect:/teacher-student-page/view/" + id;
    }

    // =========================
    // VIEW STUDENT PHOTO
    // Teacher Service endpoint:
    // GET /teachers/student/{id}/photo
    // =========================
    @GetMapping("/{id}/photo")
    public ResponseEntity<Resource> viewStudentPhoto(
            @PathVariable Long id) {

        var file = teacherService.viewStudentPhoto(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                MediaType.parseMediaType(file.getContentType()));

        return ResponseEntity.ok()
                .headers(headers)
                .body(file.getResource());
    }

    // =========================
    // DELETE STUDENT PHOTO
    // Teacher Service endpoint:
    // DELETE /teachers/student/{id}/photo
    // =========================
    @GetMapping("/{id}/photo/delete")
    public String deleteStudentPhoto(@PathVariable Long id) {

        teacherService.deleteStudentPhoto(id);

        return "redirect:/teacher-student-page/view/" + id;
    }
}
