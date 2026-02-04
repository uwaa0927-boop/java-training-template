package com.example.weatherapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 都道府県情報DTO
 * 画面表示・API応答用
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrefectureDto {

    /** 都道府県ID */
    private Long id;

    /** 都道府県名 */
    private String name;

    /** 英語名 */
    private String nameEn;

    /** 緯度 */
    private Double latitude;

    /** 経度 */
    private Double longitude;

    /** 地域 */
    private String region;

    // createdAt, updatedAtは含めない（不要なため）
}
