package com.example.service.impl;

import com.example.domain.Application;
import com.example.domain.Judgement;
import com.example.dto.JudgementDto.Request;
import com.example.dto.JudgementDto.Response;
import com.example.repository.ApplicationRepository;
import com.example.repository.JudgementRepository;
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
class JudgementServiceImplTest {

    @InjectMocks private JudgementServiceImpl judgementService;
    @Mock private ApplicationRepository applicationRepository;
    @Mock private JudgementRepository judgementRepository;
    @Spy private ModelMapper modelMapper;

    @DisplayName("대출 심사 정보 결과를 입력하여 저장한다.")
    @Test
    void Should_ReturnResponseOfNewJudgmentEntity_When_RequestNewJudgment(){
        // Given
        Request request = Request.builder()
                .applicationId(1L)
                .name("member kim")
                .approvalAmount(BigDecimal.valueOf(5000000))
                .build();

        Judgement judgement = Judgement.builder()
                .applicationId(1L)
                .name("member kim")
                .approvalAmount(BigDecimal.valueOf(5000000))
                .build();

        given(applicationRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(Application.builder().build()));
        given(judgementRepository.save(any(Judgement.class)))
                .willReturn(judgement);

        // When
        Response result = judgementService.create(request);

        // Then
        assertThat(result.getName()).isEqualTo(judgement.getName());
        assertThat(result.getApplicationId()).isEqualTo(judgement.getApplicationId());
        assertThat(result.getApprovalAmount()).isEqualTo(judgement.getApprovalAmount());
    }

    @DisplayName("대출 심사 결과를 조회한다.")
    @Test
    void Should_ReturnResponseOfExistJudgmentEntity_When_RequestExistJudgmentId(){
        // Given
        Long judgementId = 1L;
        Judgement judgement = Judgement.builder()
                .judgementId(judgementId)
                .build();

        given(judgementRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(judgement));

        // When
        Response result = judgementService.get(judgementId);

        // Then
        assertThat(result.getJudgementId()).isEqualTo(judgementId);
    }


    @DisplayName("대출 신청 정보 id 를 사용하여 심사 결과를 조회한다.")
    @Test
    void Should_ReturnResponseOfExistJudgmentEntity_When_RequestExistApplicationId(){
        // Given
        Long applicationId = 1L;
        Long judgementId = 1L;

        Judgement judgement = Judgement.builder()
                .judgementId(judgementId)
                .build();

        Application application = Application.builder()
                .applicationId(applicationId)
                .build();

        given(applicationRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(application));
        given(judgementRepository.findByApplicationId(anyLong()))
                .willReturn(Optional.ofNullable(judgement));

        // When
        Response result = judgementService.getJudgementOfApplication(applicationId);

        // Then
        assertThat(result.getJudgementId()).isEqualTo(judgementId);
    }

    @DisplayName("대출 심사 결과를 수정한다.")
    @Test
    void Should_ReturnUpdatedResponseOfExistJudgmentEntity_When_RequestUpdateExistJudgmentInfo(){
        // Given
        Long judgementId = 1L;
        Request request = Request.builder()
                .name("jyuron")
                .approvalAmount(BigDecimal.valueOf(30000000))
                .build();

        Judgement judgement = Judgement.builder()
                .judgementId(judgementId)
                .name("jyuka")
                .approvalAmount(BigDecimal.valueOf(50000000))
                .build();


        given(judgementRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(judgement));
        given(judgementRepository.save(any(Judgement.class)))
                .willReturn(judgement);

        // When
        Response result = judgementService.update(judgementId, request);

        // Then
        assertThat(result.getJudgementId()).isEqualTo(judgementId);
        assertThat(result.getName()).isEqualTo(request.getName());
        assertThat(result.getApprovalAmount()).isEqualTo(request.getApprovalAmount());
    }

    @DisplayName("대출 심사 결과를 삭제한다.")
    @Test
    void Should_ReturnUpdatedResponseOfExistApplicationEntity_When_RequestGrantApprovalAmountOfJudgmentInfo(){
        // Given
        Long judgementId = 1L;
        Judgement judgement = Judgement.builder()
                .judgementId(judgementId)
                .build();

        given(judgementRepository.findById(judgementId))
                .willReturn(Optional.ofNullable(judgement));
        given(judgementRepository.save(any(Judgement.class)))
                .willReturn(judgement);

        // When
        judgementService.delete(judgementId);

        // Then
        assertThat(judgement.getIsDeleted()).isTrue();
    }

}