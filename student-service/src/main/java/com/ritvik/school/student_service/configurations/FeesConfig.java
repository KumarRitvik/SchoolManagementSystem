package com.ritvik.school.student_service.configurations;

import com.ritvik.school.student_service.fees.DayScholarFeesCalculator;
import com.ritvik.school.student_service.fees.FeesCalculator;
import com.ritvik.school.student_service.fees.HostelerFeesCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeesConfig {

    @Bean
  public FeesCalculator hostelerFeesCalculator(){
     return new HostelerFeesCalculator();
    }

   @Bean
   public FeesCalculator dayScholarFeesCalculator(){
       return  new DayScholarFeesCalculator();
    }
}
