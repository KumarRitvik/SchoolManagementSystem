package com.ritvik.school.student_service.fees;

import com.ritvik.school.student_service.dtos.StudentDto;

public class HostelerFeesCalculator implements FeesCalculator {

    @Override
   public double calculateFees(StudentDto student) {
      return student.getHostelerFees()+student.getSemFees();

   }
}
