package com.ritvik.school.academic_service.controllers;

import com.ritvik.school.academic_service.dtos.ClassRoomDto;
import com.ritvik.school.academic_service.enums.Section;
import com.ritvik.school.academic_service.enums.Standard;
import com.ritvik.school.academic_service.services.ClassRoomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/classroom-page")
public class ClassRoomPageController {

    private final ClassRoomService classRoomService;

    public ClassRoomPageController(ClassRoomService classRoomService) {
        this.classRoomService = classRoomService;
    }

    @ModelAttribute
    public void common(Model model) {
        model.addAttribute("standards", Standard.values());
        model.addAttribute("sections", Section.values());
    }

    @GetMapping
    public String home(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "classRoomId") String sortBy,
                       @RequestParam(defaultValue = "asc") String direction,
                       Model model) {
        model.addAttribute("classrooms",
                classRoomService.viewAllClassRoom(page, size, sortBy, direction));
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        return "classroom/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("classroom", new ClassRoomDto());
        return "classroom/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute ClassRoomDto dto) {
        classRoomService.createClassRoom(dto);
        return "redirect:/classroom-page";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("classroom", classRoomService.viewClassRoomById(id));
        return "classroom/view";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("classroom", classRoomService.viewClassRoomById(id));
        return "classroom/form";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute ClassRoomDto dto) {
        classRoomService.updateClassRoom(dto);
        return "redirect:/classroom-page/view/" + dto.getClasRoomId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        classRoomService.deleteClassRoom(id);
        return "redirect:/classroom-page";
    }
}
