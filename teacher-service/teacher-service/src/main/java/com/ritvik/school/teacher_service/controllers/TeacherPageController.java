package com.ritvik.school.teacher_service.controllers;

import com.ritvik.school.teacher_service.dtos.TeacherDto;
import com.ritvik.school.teacher_service.enums.TeacherType;
import com.ritvik.school.teacher_service.services.TeacherService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/teacher-page")
public class TeacherPageController {

    private final TeacherService teacherService;

    public TeacherPageController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @ModelAttribute
    public void common(Model model) {
        model.addAttribute("teacherTypes", TeacherType.values());
    }

    @GetMapping
    public String home() {
        return "redirect:/teacher-page/viewAll";
    }

    // =========================
    // VIEW ALL TEACHERS
    // =========================
    @GetMapping("/viewAll")
    public String viewAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "teacherId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {

        model.addAttribute("teachers",
                teacherService.viewAllPagewise(page, size, sortBy, direction));

        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);

        return "teacher/list";
    }

    // =========================
    // VIEW TEACHER BY ID
    // =========================
    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("teacher", teacherService.viewById(id));
        return "teacher/view";
    }

    // =========================
    // CREATE TEACHER
    // =========================
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("teacher", new TeacherDto());
        return "teacher/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute TeacherDto dto) {
        TeacherDto created = teacherService.create(dto);
        return "redirect:/teacher-page/view/" + created.getTeacherId();
    }

    // =========================
    // UPDATE TEACHER
    // =========================
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("teacher", teacherService.viewById(id));
        return "teacher/form";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute TeacherDto dto) {
        teacherService.update(dto);
        return "redirect:/teacher-page/view/" + dto.getTeacherId();
    }

    // =========================
    // DELETE TEACHER
    // =========================
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        teacherService.delete(id);
        return "redirect:/teacher-page/viewAll";
    }

    // =========================
    // SEARCH BY NAME
    // =========================
    @GetMapping("/search")
    public String search(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "teacherId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {

        model.addAttribute("teachers",
                teacherService.searchByName(name, page, size, sortBy, direction));

        model.addAttribute("searchName", name);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);

        return "teacher/list";
    }

    // =========================
    // SEARCH BY TEACHER TYPE
    // =========================
    @GetMapping("/searchByType")
    public String searchByType(
            @RequestParam TeacherType teacherType,
            Model model) {

        model.addAttribute("teachers",
                teacherService.searchByTeacherType(teacherType));

        model.addAttribute("selectedTeacherType", teacherType);

        return "teacher/all";
    }

    // =========================
    // TEACHER PHOTO UPLOAD
    // =========================
    @PostMapping("/{id}/photo")
    public String uploadPhoto(
            @PathVariable Long id,
            @RequestParam("photo") MultipartFile file) {

        teacherService.uploadTeacherPhoto(id, file);
        return "redirect:/teacher-page/view/" + id;
    }

    // =========================
    // VIEW TEACHER PHOTO
    // =========================
    @GetMapping("/{id}/photo")
    public ResponseEntity<Resource> viewPhoto(@PathVariable Long id) {

        var file = teacherService.viewPhoto(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                MediaType.parseMediaType(file.getContentType()));

        return ResponseEntity.ok()
                .headers(headers)
                .body(file.getResource());
    }

    // =========================
    // DELETE TEACHER PHOTO
    // =========================
    @GetMapping("/{id}/photo/delete")
    public String deletePhoto(@PathVariable Long id) {

        teacherService.deletePhoto(id);
        return "redirect:/teacher-page/view/" + id;
    }
}
