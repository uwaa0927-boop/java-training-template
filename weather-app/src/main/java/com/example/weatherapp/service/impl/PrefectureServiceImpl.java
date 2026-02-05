package com.example.weatherapp.service.impl;

import com.example.weatherapp.dto.PrefectureDto;
import com.example.weatherapp.entity.Prefecture;
import com.example.weatherapp.exception.ResourceNotFoundException;
import com.example.weatherapp.mapper.PrefectureMapper;
import com.example.weatherapp.repository.PrefectureRepository;
import com.example.weatherapp.service.PrefectureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 都道府県サービスの実装クラス
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class PrefectureServiceImpl implements PrefectureService {

    private final PrefectureRepository prefectureRepository;
    private final PrefectureMapper prefectureMapper;

    /**
     * コンストラクタインジェクション
     */
    public PrefectureServiceImpl(
            PrefectureRepository prefectureRepository,
            PrefectureMapper prefectureMapper
    ) {
        this.prefectureRepository = prefectureRepository;
        this.prefectureMapper = prefectureMapper;
    }

    /**
     * 全都道府県を取得
     *
     * @return 全都道府県のDTOリスト
     */
    @Override
    public List<PrefectureDto> findAll() {
        log.info("全都道府県を取得");

        List<Prefecture> prefectures = prefectureRepository.findAll();

        log.debug("取得件数: {}", prefectures.size());

        return prefectureMapper.toDtoList(prefectures);
    }

    /**
     * IDで都道府県を取得
     *
     * @param id 都道府県ID
     * @return 都道府県DTO
     * @throws ResourceNotFoundException 存在しない場合
     */
    @Override
    public PrefectureDto findById(Long id) {
        log.info("都道府県を取得: id={}", id);

        Prefecture prefecture = prefectureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "都道府県が見つかりません: id=" + id
                ));

        return prefectureMapper.toDto(prefecture);
    }

    /**
     * IDで都道府県Entityを取得（内部用）
     * WeatherServiceから呼ばれる
     *
     * @param id 都道府県ID
     * @return 都道府県Entity
     * @throws ResourceNotFoundException 存在しない場合
     */
    @Override
    public Prefecture findEntityById(Long id) {
        log.debug("都道府県Entityを取得: id={}", id);

        return prefectureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "都道府県が見つかりません: id=" + id
                ));
    }

    /**
     * 地域で都道府県を取得
     *
     * @param region 地域名
     * @return 該当地域の都道府県DTOリスト
     */
    @Override
    public List<PrefectureDto> findByRegion(String region) {
        log.info("地域で都道府県を取得: region={}", region);

        List<Prefecture> prefectures = prefectureRepository
                .findByRegionOrderById(region);

        log.debug("取得件数: {}", prefectures.size());

        return prefectureMapper.toDtoList(prefectures);
    }

    /**
     * 地域別にグループ化して取得
     * トップページで使用
     *
     * @return 地域をキーとしたMap<地域名, 都道府県リスト>
     */
    @Override
    public Map<String, List<PrefectureDto>> findAllGroupedByRegion() {
        log.info("地域別グループ化で全都道府県を取得");

        List<Prefecture> allPrefectures = prefectureRepository.findAll();

        // 地域別にグループ化
        Map<String, List<PrefectureDto>> grouped = allPrefectures.stream()
                .collect(Collectors.groupingBy(
                        Prefecture::getRegion,
                        LinkedHashMap::new,  // 順序を保持
                        Collectors.mapping(
                                prefectureMapper::toDto,
                                Collectors.toList()
                        )
                ));

        log.debug("グループ数: {}", grouped.size());

        return grouped;
    }

    /**
     * 都道府県名で検索
     *
     * @param name 都道府県名
     * @return 都道府県DTO
     * @throws ResourceNotFoundException 存在しない場合
     */
    @Override
    public PrefectureDto findByName(String name) {
        log.info("都道府県名で検索: name={}", name);

        Prefecture prefecture = prefectureRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "都道府県が見つかりません: name=" + name
                ));

        return prefectureMapper.toDto(prefecture);
    }
}
