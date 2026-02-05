package com.example.weatherapp.service;

import com.example.weatherapp.dto.PrefectureDto;
import com.example.weatherapp.entity.Prefecture;
import com.example.weatherapp.exception.ResourceNotFoundException;
import com.example.weatherapp.mapper.PrefectureMapper;
import com.example.weatherapp.repository.PrefectureRepository;
import com.example.weatherapp.service.impl.PrefectureServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * PrefectureServiceのテスト
 * Mockitoを使った単体テスト
 */
@ExtendWith(MockitoExtension.class)
class PrefectureServiceTest {

    @Mock
    private PrefectureRepository prefectureRepository;

    @Mock
    private PrefectureMapper prefectureMapper;

    @InjectMocks
    private PrefectureServiceImpl prefectureService;

    private Prefecture tokyoPrefecture;
    private PrefectureDto tokyoDto;

    @BeforeEach
    void setUp() {
        // テストデータ準備
        tokyoPrefecture = Prefecture.builder()
                .id(13L)
                .name("東京都")
                .nameEn("Tokyo")
                .latitude(BigDecimal.valueOf(35.689))
                .longitude(BigDecimal.valueOf(139.692))
                .region("関東")
                .build();

        tokyoDto = PrefectureDto.builder()
                .id(13L)
                .name("東京都")
                .nameEn("Tokyo")
                .latitude(35.689)
                .longitude(139.692)
                .region("関東")
                .build();
    }

    @Test
    @DisplayName("全都道府県を取得できる")
    void testFindAll() {
        // Given
        List<Prefecture> prefectures = Arrays.asList(tokyoPrefecture);
        List<PrefectureDto> dtos = Arrays.asList(tokyoDto);

        when(prefectureRepository.findAll()).thenReturn(prefectures);
        when(prefectureMapper.toDtoList(prefectures)).thenReturn(dtos);

        // When
        List<PrefectureDto> result = prefectureService.findAll();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("東京都");

        verify(prefectureRepository, times(1)).findAll();
        verify(prefectureMapper, times(1)).toDtoList(prefectures);
    }

    @Test
    @DisplayName("IDで都道府県を取得できる")
    void testFindById() {
        // Given
        when(prefectureRepository.findById(13L))
                .thenReturn(Optional.of(tokyoPrefecture));
        when(prefectureMapper.toDto(tokyoPrefecture))
                .thenReturn(tokyoDto);

        // When
        PrefectureDto result = prefectureService.findById(13L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(13L);
        assertThat(result.getName()).isEqualTo("東京都");

        verify(prefectureRepository, times(1)).findById(13L);
    }

    @Test
    @DisplayName("存在しないIDで例外が発生する")
    void testFindById_NotFound() {
        // Given
        when(prefectureRepository.findById(999L))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> prefectureService.findById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("都道府県が見つかりません");

        verify(prefectureRepository, times(1)).findById(999L);
        verify(prefectureMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("地域で都道府県を取得できる")
    void testFindByRegion() {
        // Given
        List<Prefecture> kantoList = Arrays.asList(tokyoPrefecture);
        List<PrefectureDto> kantoDtos = Arrays.asList(tokyoDto);

        when(prefectureRepository.findByRegionOrderById("関東"))
                .thenReturn(kantoList);
        when(prefectureMapper.toDtoList(kantoList))
                .thenReturn(kantoDtos);

        // When
        List<PrefectureDto> result = prefectureService.findByRegion("関東");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRegion()).isEqualTo("関東");
    }

    @Test
    @DisplayName("地域別にグループ化して取得できる")
    void testFindAllGroupedByRegion() {
        // Given
        Prefecture osaka = Prefecture.builder()
                .id(27L)
                .name("大阪府")
                .region("関西")
                .build();

        List<Prefecture> allPrefectures = Arrays.asList(
                tokyoPrefecture,
                osaka
        );

        when(prefectureRepository.findAll()).thenReturn(allPrefectures);
        when(prefectureMapper.toDto(tokyoPrefecture)).thenReturn(tokyoDto);
        when(prefectureMapper.toDto(osaka)).thenReturn(
                PrefectureDto.builder().id(27L).name("大阪府").region("関西").build()
        );

        // When
        Map<String, List<PrefectureDto>> result =
                prefectureService.findAllGroupedByRegion();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get("関東")).hasSize(1);
        assertThat(result.get("関西")).hasSize(1);
    }
}