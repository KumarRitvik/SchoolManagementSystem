package com.ritvik.school.teacher_service.salary;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SalaryConfig {

   @Bean
    public SalaryCalculator permanentTeacherSalaryCalculator() {
       return  new PermanentTeacherSalaryCalculator();
    }

    @Bean
    public SalaryCalculator contractTeacherSalaryCalculator() {
       return  new ContractTeacherSalaryCalculator();
  }
}