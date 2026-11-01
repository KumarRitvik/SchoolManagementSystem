package com.ritvik.school.teacher_service.salary;

import com.ritvik.school.teacher_service.dtos.TeacherDto;

public interface SalaryCalculator {
   public long calculateSalary(TeacherDto teacherDto);
}
