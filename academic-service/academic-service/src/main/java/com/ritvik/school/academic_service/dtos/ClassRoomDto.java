package com.ritvik.school.academic_service.dtos;

import com.ritvik.school.academic_service.enums.Section;
import com.ritvik.school.academic_service.enums.Standard;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;


@Data

public class ClassRoomDto {

    private Long clasRoomId;

    @NotNull
    private Standard standard;

    @NotNull
    private Section section;

    @PastOrPresent
    @Min(2000)
    private String academicYear;

    @Max(50)
    @Min(3)
    private Integer capacity;


}
