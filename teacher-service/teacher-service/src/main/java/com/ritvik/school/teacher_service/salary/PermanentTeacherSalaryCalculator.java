package com.ritvik.school.teacher_service.salary;

import com.ritvik.school.teacher_service.dtos.TeacherDto;
import com.ritvik.school.teacher_service.exceptions.WrongTeacherTypeException;

public class PermanentTeacherSalaryCalculator implements SalaryCalculator {

    @Override
    public long calculateSalary(TeacherDto teacherDto) {
        try {
            if (teacherDto.getTeacherType().equals("permanent")) {
                return 50000;
            } else {
                throw new WrongTeacherTypeException("The teacher type you entered is wrong. It should be either Permanant or Contract");
            }
        }catch (WrongTeacherTypeException wtte) {
            throw wtte;
        }
        }
    }

