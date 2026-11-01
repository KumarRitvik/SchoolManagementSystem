package com.ritvik.school.student_service.pojos;

import org.springframework.core.io.Resource;

public class FileResponse {

        private final Resource resource;
        private final String contentType;

        public FileResponse(Resource resource, String contentType) {
            this.resource = resource;
            this.contentType = contentType;
        }

        public Resource getResource() {
            return resource;
        }

        public String getContentType() {
            return contentType;
        }
    }

