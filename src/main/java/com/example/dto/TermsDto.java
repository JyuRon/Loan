package com.example.dto;

import lombok.*;

import java.time.LocalDateTime;

public class TermsDto {
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class Request {
        private String name;
        private String termsDetailUrl;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class Response {
        private Long termsId;
        private String name;
        private String termsDetailUrl;
        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;
    }
}
