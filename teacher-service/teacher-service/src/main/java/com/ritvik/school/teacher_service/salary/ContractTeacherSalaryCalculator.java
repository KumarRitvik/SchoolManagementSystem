package com.ritvik.school.teacher_service.salary;

import com.ritvik.school.teacher_service.dtos.TeacherDto;
import com.ritvik.school.teacher_service.exceptions.WrongTeacherTypeException;


public class ContractTeacherSalaryCalculator implements SalaryCalculator {

    @Override
    public long calculateSalary(TeacherDto teacher) {
        try {
            if (teacher.getTeacherType().equals("Contract")) {
                return 35000;
            } else {
                throw new WrongTeacherTypeException("The teacher type you entered is wrong. It should be either Permanant or Contract");
            }
        } catch (WrongTeacherTypeException wrongTeacherTypeException) {
            throw wrongTeacherTypeException;
        }
    }
}
