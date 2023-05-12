package com.example.service.impl;

import com.example.domain.Counsel;
import com.example.dto.CounselDto.Request;
import com.example.dto.CounselDto.Response;
import com.example.exception.BaseException;
import com.example.repository.CounselRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CounselServiceImplTest {

    @InjectMocks private CounselServiceImpl counselService;
    @Spy private ModelMapper modelMapper;
    @Mock private CounselRepository counselRepository;

    @DisplayName("상담 신청 내역을 받으면 DB에 저장 후 해당 내용을 반환한다.")
    @Test
    void should_returnResponseOfNewCounselEntity_when_requestCounsel(){
        // Given
        Counsel entity = Counsel.builder()
                .name("쥬카")
                .cellPhone("010-2222-3333")
                .email("jyuka@mail.com")
                .memo("대출 신청합니다.")
                .zipCode("12345")
                .address("서울특별시 종로구")
                .addressDetail("101동 101호")
                .build();

        Request request = Request.builder()
                .name("쥬카")
                .cellPhone("010-2222-3333")
                .email("jyuka@mail.com")
                .memo("대출 신청합니다.")
                .zipCode("12345")
                .address("서울특별시 종로구")
                .addressDetail("101동 101호")
                .build();

        given(counselRepository.save(any(Counsel.class)))
                .willReturn(entity);


        // When
        Response result = counselService.create(request);

        // Then
        assertThat(result.getName()).isSameAs(entity.getName());
    }

    @DisplayName("상담 내역 번호를 입력하면 해당 정보를 리턴한다.")
    @Test
    void Should_ReturnResponseOfExistCounselEntity_When_RequestExistCounselId(){
        // Given
        Long counselId = 1L;
        Counsel entity = Counsel.builder().counselId(counselId).build();
        given(counselRepository.findById(anyLong())).willReturn(Optional.ofNullable(entity));

        // When
        Response result = counselService.get(counselId);

        // Then
        assertThat(result.getCounselId()).isEqualTo(entity.getCounselId());
    }

    @DisplayName("상담 내역이 존재하지 않는 번호를 입력하면 예외를 발생시킨다.")
    @Test
    void Should_ThrowException_When_RequestNotExistCounselId(){
        // Given
        Long counselId = 2L;
        given(counselRepository.findById(anyLong())).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> counselService.get(counselId));

        // Then
        assertThat(t)
                .isInstanceOf(BaseException.class);
    }

    @DisplayName("수정된 상담신청 내역을 받아 저장한다.")
    @Test
    void Should_ReturnUpdatedResponseOfExistCounselEntity_When_RequestUpdateExistCounselInfo(){
        // Given
        Long counselId = 1L;
        Counsel entity = Counsel.builder()
                .counselId(counselId)
                .name("jyuka")
                .build();

        Request request = Request.builder()
                .name("jyuron")
                .build();

        given(counselRepository.findById(anyLong())).willReturn(Optional.ofNullable(entity));
        given(counselRepository.save(any(Counsel.class))).willReturn(entity);

        // When
        Response result = counselService.update(counselId, request);

        // Then
        assertThat(result.getCounselId()).isEqualTo(counselId);
        assertThat(result.getName()).isEqualTo(request.getName());
    }

    @DisplayName("상담 내역을 soft delete 한다.")
    @Test
    void Should_DeletedCounselEntity_When_RequestDeleteExistCounselInfo(){
        // Given
        Long counselId = 1L;
        Counsel entity = Counsel.builder()
                .counselId(counselId)
                .build();
        given(counselRepository.findById(anyLong())).willReturn(Optional.ofNullable(entity));
        given(counselRepository.save(any(Counsel.class))).willReturn(entity);

        // When
        counselService.delete(counselId);

        // Then
        assertThat(entity.getIsDeleted()).isTrue();
    }
}