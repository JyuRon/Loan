package com.example.service;

import com.example.domain.Terms;
import com.example.dto.TermsDto.Request;
import com.example.dto.TermsDto.Response;
import com.example.repository.TermsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TermsServiceImplTest {

    @InjectMocks private TermsServiceImpl termsService;
    @Mock private TermsRepository termsRepository;
    @Spy private ModelMapper modelMapper;

    @DisplayName("신규 약관을 등록한다.")
    @Test
    void Should_ReturnResponseOfNewTermsEntity_When_RequestTerm(){
        // Given
        Request request = Request.builder()
                .name("테스트 약관")
                .termsDetailUrl("https://www.naver.com")
                .build();
        Terms terms = Terms.builder()
                .name("테스트 약관")
                .termsDetailUrl("https://www.naver.com")
                .build();

        given(termsRepository.save(any(Terms.class)))
                .willReturn(terms);

        // When
        Response result = termsService.create(request);

        // Then
        assertThat(result.getTermsDetailUrl()).isEqualTo(terms.getTermsDetailUrl());
        assertThat(result.getName()).isEqualTo(terms.getName());
    }

    @DisplayName("존재하는 모든 약관을 조회한다.")
    @Test
    void Should_ReturnAllResponseOfExistTermsEntities_When_RequestTermsList(){
        // Given
        Terms terms1 = Terms.builder()
                .termsId(1L)
                .name("신규 약관 1")
                .termsDetailUrl("https://www.google.com")
                .build();
        Terms terms2 = Terms.builder()
                .termsId(2L)
                .name("신규 약관 2")
                .termsDetailUrl("https://www.naver.com")
                .build();

        List<Terms> termsList = List.of(terms1, terms2);
        given(termsRepository.findAll()).willReturn(termsList);

        // When
        List<Response> result = termsService.getAll();

        // Then
        assertThat(result.size()).isEqualTo(result.size());
    }
}