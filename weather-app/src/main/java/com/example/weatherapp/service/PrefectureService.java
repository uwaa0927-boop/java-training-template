package com.example.weatherapp.service;

import com.example.weatherapp.dto.PrefectureDto;
import com.example.weatherapp.entity.Prefecture;

import java.util.List;
import java.util.Map;

/**
 * 都道府県サービスのインターフェース
 */
public interface PrefectureService {

    /**
     * 全都道府県を取得
     */
    List<PrefectureDto> findAll();

    /**
     * IDで都道府県を取得
     */
    PrefectureDto findById(Long id);

    /**
     * IDで都道府県Entityを取得（内部用）
     */
    Prefecture findEntityById(Long id);

    /**
     * 地域で都道府県を取得
     */
    List<PrefectureDto> findByRegion(String region);

    /**
     * 地域別にグループ化して取得
     */
    Map<String, List<PrefectureDto>> findAllGroupedByRegion();

    /**
     * 都道府県名で検索
     */
    PrefectureDto findByName(String name);
}
