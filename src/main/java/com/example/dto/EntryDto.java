package com.example.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EntryDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class Request{
        private BigDecimal entryAmount;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class Response{
        private Long entryId;
        private Long applicationId;
        private BigDecimal entryAmount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class UpdateResponse{
        private Long entryId;
        private Long applicationId;
        private BigDecimal beforeEntryAmount;
        private BigDecimal afterEntryAmount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}