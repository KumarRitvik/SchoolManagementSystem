package com.ritvik.school.academic_service.controllers;

import com.ritvik.school.academic_service.dtos.SubjectDto;
import com.ritvik.school.academic_service.enums.SubjectType;
import com.ritvik.school.academic_service.services.SubjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/subject-page")
public class SubjectPageController {

    private final SubjectService subjectService;

    public SubjectPageController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @ModelAttribute
    public void common(Model model) {
        model.addAttribute("subjectTypes", SubjectType.values());
    }

    @GetMapping
    public String home(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "subjectId") String sortBy,
                       @RequestParam(defaultValue = "asc") String direction,
                       Model model) {
        model.addAttribute("subjects",
                subjectService.viewAllSubjects(page, size, sortBy, direction));
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        return "subject/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("subject", new SubjectDto());
        return "subject/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute SubjectDto dto) {
        subjectService.createSubject(dto);
        return "redirect:/subject-page";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable long id, Model model) {
        model.addAttribute("subject", subjectService.viewSubjectById(id));
        return "subject/view";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable long id, Model model) {
        model.addAttribute("subject", subjectService.viewSubjectById(id));
        return "subject/form";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute SubjectDto dto) {
        subjectService.updateSubject(dto);
        return "redirect:/subject-page/view/" + dto.getSubjectId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable long id) {
        subjectService.deleteSubject(id);
        return "redirect:/subject-page";
    }
}
