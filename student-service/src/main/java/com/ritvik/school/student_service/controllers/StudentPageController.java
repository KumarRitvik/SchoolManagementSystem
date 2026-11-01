package com.ritvik.school.student_service.controllers;

import com.ritvik.school.student_service.dtos.StudentDto;
import com.ritvik.school.student_service.enums.Gender;
import com.ritvik.school.student_service.enums.StudentType;
import com.ritvik.school.student_service.service.StudentServiceImpl;
import com.ritvik.school.student_service.dtos.TeacherDto;
import com.ritvik.school.student_service.dtos.ClassRoomDto;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/student-page")
public class StudentPageController {

    private final StudentServiceImpl studentService;

    public StudentPageController(StudentServiceImpl studentService) {
        this.studentService = studentService;
    }

    @ModelAttribute
    public void common(Model model) {
        model.addAttribute("genders", Gender.values());
        model.addAttribute("studentTypes", StudentType.values());
    }

    @GetMapping
    public String home() {
        return "redirect:/student-page/viewAll";
    }

    @GetMapping("/viewAll")
    public String viewAll(@RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "10") int size,
                          @RequestParam(defaultValue = "studentId") String sortBy,
                          @RequestParam(defaultValue = "asc") String direction,
                          Model model) {
        model.addAttribute("students",
                studentService.viewAllPagewise(page, size, sortBy, direction));
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        return "student/list";
    }

    @GetMapping("/viewAllStudents")
    public String viewAllStudents(Model model) {
        model.addAttribute("students", studentService.viewAllStudents());
        return "student/all";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("student", studentService.viewById(id));
        return "student/view";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("student", new StudentDto());
        return "student/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute StudentDto student) {
        studentService.create(student);
        return "redirect:/student-page/viewAll";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("student", studentService.viewById(id));
        return "student/form";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute StudentDto student) {
        studentService.update(student);
        return "redirect:/student-page/view/" + student.getStudentId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        studentService.delete(id);
        return "redirect:/student-page/viewAll";
    }

    @GetMapping("/search")
    public String search(@RequestParam String name,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "10") int size,
                         @RequestParam(defaultValue = "studentId") String sortBy,
                         @RequestParam(defaultValue = "asc") String direction,
                         Model model) {
        model.addAttribute("students",
                studentService.searchByName(name, page, size, sortBy, direction));
        model.addAttribute("searchName", name);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        return "student/list";
    }

    @GetMapping("/searchByType")
    public String searchByType(@RequestParam StudentType studentType,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "studentId") String sortBy,
                               @RequestParam(defaultValue = "asc") String direction,
                               Model model) {
        model.addAttribute("students",
                studentService.searchByStudentType(studentType, page, size, sortBy, direction));
        return "student/list";
    }

    @GetMapping("/teacher")
    public String teacher(@RequestParam Long teacherId, Model model) {
        TeacherDto teacher = studentService.viewYourTeachersById(teacherId);
        model.addAttribute("teacher", teacher);
        return "student/teacher";
    }

    @GetMapping("/classroom")
    public String classroom(@RequestParam Long classRoomId, Model model) {
        ClassRoomDto classroom = studentService.viewYourClassRoomById(classRoomId);
        model.addAttribute("classroom", classroom);
        return "student/classroom";
    }

    @PostMapping("/{id}/photo")
    public String uploadPhoto(@PathVariable Long id,
                              @RequestParam("photo") MultipartFile file) {
        studentService.uploadPhoto(id, file);
        return "redirect:/student-page/view/" + id;
    }

    @GetMapping("/{id}/photo")
    public ResponseEntity<Resource> viewPhoto(@PathVariable Long id) {
        var file = studentService.viewPhoto(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(file.getContentType()));
        return ResponseEntity.ok().headers(headers).body(file.getResource());
    }

    @GetMapping("/{id}/photo/delete")
    public String deletePhoto(@PathVariable Long id) {
        studentService.deletePhoto(id);
        return "redirect:/student-page/view/" + id;
    }

    @PostMapping("/teacher/{id}/photo")
    public String uploadTeacherPhoto(@PathVariable long id,
                                     @RequestParam("photo") MultipartFile file) {
        studentService.uploadTeacherPhotoById(id, file);
        return "redirect:/student-page/teacher?teacherId=" + id;
    }

    @GetMapping("/teacher/{id}/photo")
    public ResponseEntity<Resource> viewTeacherPhoto(@PathVariable long id) {
        return studentService.viewTeacherPhotoById(id);
    }

    @GetMapping("/teacher/{id}/photo/delete")
    public String deleteTeacherPhoto(@PathVariable long id) {
        studentService.deleteTeacherPhoto(id);
        return "redirect:/student-page/teacher?teacherId=" + id;
    }
}
