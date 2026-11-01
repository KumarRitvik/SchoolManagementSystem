package com.ritvik.school.student_service.fees;

import com.ritvik.school.student_service.dtos.StudentDto;

public interface FeesCalculator {
  public double calculateFees(StudentDto student);
}
