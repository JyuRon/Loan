package com.example.service.impl;

import com.example.domain.AcceptTerms;
import com.example.domain.Application;
import com.example.domain.Terms;
import com.example.dto.ApplicationDto;
import com.example.dto.ApplicationDto.Request;
import com.example.dto.ApplicationDto.Response;
import com.example.exception.BaseException;
import com.example.repository.AcceptTermsRepository;
import com.example.repository.ApplicationRepository;
import com.example.repository.TermsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceImplTest {

    @InjectMocks private ApplicationServiceImpl applicationService;
    @Mock private ApplicationRepository applicationRepository;
    @Mock private TermsRepository termsRepository;
    @Mock private AcceptTermsRepository acceptTermsRepository;
    @Spy private ModelMapper modelMapper;

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

    @DisplayName("신창자가 동의한 약관이 서버에서 제공한 약관을 모두 동의하였는지 판단 후 결과를 저장한다.")
    @Test
    void Should_AddAcceptTerms_When_RequestAcceptTermsOfApplication(){
        // Given
        Terms entityA = Terms.builder()
                .termsId(1L)
                .name("대출 이용 약관 1")
                .termsDetailUrl("https://abc-storage.acc/dslfjdlsfjlsdddads")
                .build();

        Terms entityB = Terms.builder()
                .termsId(2L)
                .name("대출 이용 약관 2")
                .termsDetailUrl("https://abc-storage.acc/dslfjdlsfjlsdweqwq")
                .build();

        List<Long> acceptTerms = Arrays.asList(1L, 2L);

        ApplicationDto.AcceptTerms request = ApplicationDto.AcceptTerms.builder()
                .acceptTermsIds(acceptTerms)
                .build();

        Long findId = 1L;

        given(applicationRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(Application.builder().build()));
        given(termsRepository.findAll(Sort.by(Sort.Direction.ASC, "termsId")))
                .willReturn(Arrays.asList(entityA, entityB));
        given(acceptTermsRepository.save(any(AcceptTerms.class)))
                .willReturn(AcceptTerms.builder().build());

        // When
        Boolean actual = applicationService.acceptTerms(findId, request);

        // Then
        assertThat(actual).isTrue();
    }

    @DisplayName("사용자가 약관 동의를 모두 하지 않은 경우 예외를 반한한다.")
    @Test
    void Should_ThrowException_When_RequestNotAllAcceptTermsOfApplication() {
        // Given
        Terms entityA = Terms.builder()
                .termsId(1L)
                .name("대출 이용 약관 1")
                .termsDetailUrl("https://abc-storage.acc/dslfjdlsfjlsdddads")
                .build();

        Terms entityB = Terms.builder()
                .termsId(2L)
                .name("대출 이용 약관 2")
                .termsDetailUrl("https://abc-storage.acc/dslfjdlsfjlsdweqwq")
                .build();

        List<Long> acceptTerms = Arrays.asList(1L);

        ApplicationDto.AcceptTerms request = ApplicationDto.AcceptTerms.builder()
                .acceptTermsIds(acceptTerms)
                .build();

        Long findId = 1L;

        given(applicationRepository.findById(findId)).willReturn(
                Optional.ofNullable(Application.builder().build()));
        given(termsRepository.findAll(Sort.by(Sort.Direction.ASC, "termsId")))
                .willReturn(Arrays.asList(entityA, entityB));

        // When
        Throwable t = catchThrowable(() -> applicationService.acceptTerms(1L, request));

        // Then
        assertThat(t).isInstanceOf(BaseException.class);
    }

    @DisplayName("존재하지 않는 약관을 동의한 경우 예외를 발생시킨다.")
    @Test
    void Should_ThrowException_When_RequestNotExistAcceptTermsOfApplication() {
        // Given
        Terms entityA = Terms.builder()
                .termsId(1L)
                .name("대출 이용 약관 1")
                .termsDetailUrl("https://abc-storage.acc/dslfjdlsfjlsdddads")
                .build();

        Terms entityB = Terms.builder()
                .termsId(2L)
                .name("대출 이용 약관 2")
                .termsDetailUrl("https://abc-storage.acc/dslfjdlsfjlsdweqwq")
                .build();

        List<Long> acceptTerms = Arrays.asList(1L, 3L);

        ApplicationDto.AcceptTerms request = ApplicationDto.AcceptTerms.builder()
                .acceptTermsIds(acceptTerms)
                .build();

        Long findId = 1L;

        given(applicationRepository.findById(findId))
                .willReturn(Optional.ofNullable(Application.builder().build()));
        given(termsRepository.findAll(Sort.by(Sort.Direction.ASC, "termsId")))
                .willReturn(Arrays.asList(entityA, entityB));

        // When
        Throwable t = catchThrowable(() -> applicationService.acceptTerms(1L, request));

        // Then
        assertThat(t).isInstanceOf(BaseException.class);
    }


}