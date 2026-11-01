package com.ritvik.school.student_service.controllers;

import com.ritvik.school.student_service.dtos.EnrollmentDto;
import com.ritvik.school.student_service.service.EnrollmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/enrollment-page")
public class EnrollmentPageController {

    private final EnrollmentService enrollmentService;

    public EnrollmentPageController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public String home(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "enrollmentId") String sortBy,
                       @RequestParam(defaultValue = "asc") String direction,
                       Model model) {
        model.addAttribute("enrollments",
                enrollmentService.viewAllEnrollments(page, size, sortBy, direction));
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        return "enrollment/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("enrollment", new EnrollmentDto());
        return "enrollment/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute EnrollmentDto dto) {
        enrollmentService.createEnrollment(dto);
        return "redirect:/enrollment-page";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable long id, Model model) {
        model.addAttribute("enrollment", enrollmentService.viewEnrollmentById(id));
        return "enrollment/view";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable long id, Model model) {
        model.addAttribute("enrollment", enrollmentService.viewEnrollmentById(id));
        return "enrollment/form";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute EnrollmentDto dto) {
        enrollmentService.updateEnrollment(dto);
        return "redirect:/enrollment-page/view/" + dto.getEnrollmentId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable long id) {
        enrollmentService.deleteEnrollment(id);
        return "redirect:/enrollment-page";
    }

    @GetMapping("/subject")
    public String subject(@RequestParam long subjectId, Model model) {
        model.addAttribute("subject", enrollmentService.viewSubjectById(subjectId));
        return "enrollment/subject";
    }
}
