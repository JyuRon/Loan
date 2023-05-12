package com.example.service;

import com.example.domain.Application;
import com.example.dto.ApplicationDto.Response;
import com.example.dto.ApplicationDto.Request;
import com.example.repository.ApplicationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceImplTest {

    @InjectMocks
    private ApplicationServiceImpl applicationService;
    @Mock
    private ApplicationRepository applicationRepository;
    @Spy
    private ModelMapper modelMapper;

    @DisplayName("대출 신청 양식을 받아 DB에 저장한다.")
    @Test
    void Should_ReturnResponseOfNewApplyEntity_When_RequestApply() {
        // Given
        Request request = Request.builder()
                .name("Member Kim")
                .cellPhone("010-1111-2222")
                .email("mail@abc.de")
                .hopeAmount(BigDecimal.valueOf(50000000))
                .build();

        Application entity = Application.builder()
                .name("Member Kim")
                .cellPhone("010-1111-2222")
                .email("mail@abc.de")
                .hopeAmount(BigDecimal.valueOf(50000000))
                .build();

        given(applicationRepository.save(any(Application.class))).willReturn(entity);

        // When
        Response result = applicationService.create(request);

        // Then
        assertThat(result.getHopeAmount()).isEqualTo(entity.getHopeAmount());
        assertThat(result.getName()).isEqualTo(entity.getName());
    }


    @DisplayName("대출 신청 기록이 존재하는 경우 해당 내용을 반환한다.")
    @Test
    void Should_ReturnResponseOfExistApplicationEntity_When_RequestExistApplicationId() {
        // Given
        Long applicationId = 1L;
        Application entity = Application.builder()
                .applicationId(applicationId)
                .build();

        given(applicationRepository.findById(anyLong())).willReturn(Optional.ofNullable(entity));

        // When
        Response result = applicationService.get(applicationId);

        // Then
        assertThat(result.getApplicationId()).isEqualTo(applicationId);
    }

    @DisplayName("대출 신청기록이 존재하는 경우 변경될 내용을 받아 반영한다.")
    @Test
    void Should_ReturnUpdatedResponseOfExistApplicationEntity_When_RequestUpdateExistApplicationInfo() {
        // Given
        Long applicationId = 1L;
        Request request = Request.builder()
                .hopeAmount(BigDecimal.valueOf(50000000))
                .build();
        Application application = Application.builder()
                .applicationId(applicationId)
                .hopeAmount(BigDecimal.valueOf(500000000))
                .build();

        given(applicationRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(application));
        given(applicationRepository.save(any(Application.class)))
                .willReturn(application);

        // When
        Response result = applicationService.update(applicationId, request);

        // Then
        assertThat(result.getApplicationId()).isEqualTo(applicationId);
        assertThat(result.getHopeAmount()).isEqualTo(request.getHopeAmount());
    }

    @DisplayName("존재하는 대출 신청 내용을 삭제한다.")
    @Test
    void Should_DeletedApplicationEntity_When_RequestDeleteExistApplicationInfo() {
        // Given
        Long applicationId = 1L;
        Application application = Application.builder()
                .applicationId(applicationId)
                .build();

        given(applicationRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(application));
        given(applicationRepository.save(any(Application.class)))
                .willReturn(application);

        // When
        applicationService.delete(applicationId);

        // Then
        assertThat(application.getIsDeleted()).isTrue();
    }


}