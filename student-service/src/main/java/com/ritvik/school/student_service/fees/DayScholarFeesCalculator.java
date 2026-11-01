package com.ritvik.school.student_service.fees;

import com.ritvik.school.student_service.dtos.StudentDto;

public class DayScholarFeesCalculator implements FeesCalculator {
   @Override
   public double calculateFees(StudentDto student) {
       return student.getSemFees();
   }

    }
