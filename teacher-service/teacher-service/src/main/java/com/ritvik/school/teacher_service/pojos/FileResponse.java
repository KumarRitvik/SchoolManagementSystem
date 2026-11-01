package com.ritvik.school.teacher_service.pojos;

import lombok.Data;
import org.springframework.core.io.Resource;

@Data
public class FileResponse {


        private final Resource resource;
        private final String contentType;


}


