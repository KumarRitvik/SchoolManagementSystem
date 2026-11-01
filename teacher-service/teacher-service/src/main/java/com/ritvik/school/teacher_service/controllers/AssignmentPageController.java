package com.ritvik.school.teacher_service.controllers;

import com.ritvik.school.teacher_service.dtos.AssignmentDto;
import com.ritvik.school.teacher_service.services.AssignmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/assignment-page")
public class AssignmentPageController {

    private final AssignmentService assignmentService;

    public AssignmentPageController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping
    public String home(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "assignmentId") String sortBy,
                       @RequestParam(defaultValue = "asc") String direction,
                       Model model) {
        model.addAttribute("assignments",
                assignmentService.viewAllAssignment(page, size, sortBy, direction));
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        return "assignment/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("assignment", new AssignmentDto());
        return "assignment/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute AssignmentDto dto) {
        assignmentService.createAssignment(dto);
        return "redirect:/assignment-page";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable long id, Model model) {
        model.addAttribute("assignment", assignmentService.viewAssignmentById(id));
        return "assignment/view";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable long id, Model model) {
        model.addAttribute("assignment", assignmentService.viewAssignmentById(id));
        return "assignment/form";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute AssignmentDto dto) {
        assignmentService.updateAssignment(dto);
        return "redirect:/assignment-page/view/" + dto.getAssignmentId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable long id) {
        assignmentService.deleteAssignment(id);
        return "redirect:/assignment-page";
    }
}
