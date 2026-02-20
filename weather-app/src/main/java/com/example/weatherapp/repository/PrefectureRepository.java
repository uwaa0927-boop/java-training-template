package com.example.weatherapp.repository;

import com.example.weatherapp.entity.Prefecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Prefecture（都道府県）のRepositoryインターフェース
 */
@Repository
public interface PrefectureRepository extends JpaRepository<Prefecture, Long> {

    /**
     * 都道府県名で検索
     *
     * @param name 都道府県名（例: "東京都"）
     * @return 該当する都道府県（Optional）
     */
    Optional<Prefecture> findByName(String name);

    /**
     * 地域で検索
     *
     * @param region 地域名（例: "関東"）
     * @return 該当する都道府県のリスト
     *
     * 例: findByRegion("関東")
     * → SELECT * FROM prefectures WHERE region = '関東' ORDER BY id
     */
    List<Prefecture> findByRegionOrderById(String region);

    /**
     * 英語名で検索
     *
     * @param nameEn 英語名（例: "Tokyo"）
     * @return 該当する都道府県（Optional）
     */
    Optional<Prefecture> findByNameEn(String nameEn);

    /**
     * 都道府県名の部分一致検索
     *
     * @param keyword キーワード（例: "京"）
     * @return 該当する都道府県のリスト
     *
     * 例: findByNameContaining("京")
     * → 東京都、京都府
     */
    List<Prefecture> findByNameContaining(String keyword);

    /**
     * 地域リストで検索
     *
     * @param regions 地域のリスト（例: ["関東", "関西"]）
     * @return 該当する都道府県のリスト
     */
    List<Prefecture> findByRegionIn(List<String> regions);

    /**
     * 緯度・経度の範囲で検索
     * カスタムクエリ使用（メソッド名だと複雑になりすぎるため）
     *
     * @param minLat 最小緯度
     * @param maxLat 最大緯度
     * @param minLon 最小経度
     * @param maxLon 最大経度
     * @return 該当する都道府県のリスト
     */
    @Query("SELECT p FROM Prefecture p " +
            "WHERE p.latitude BETWEEN :minLat AND :maxLat " +
            "AND p.longitude BETWEEN :minLon AND :maxLon " +
            "ORDER BY p.region")
    List<Prefecture> findByCoordinateRange(
            @Param("minLat") BigDecimal minLat,
            @Param("maxLat") BigDecimal maxLat,
            @Param("minLon") BigDecimal minLon,
            @Param("maxLon") BigDecimal maxLon
    );

    /**
     * すべての地域を重複なしで取得
     *
     * @return 地域のリスト（例: ["北海道", "東北", "関東", ...]）
     */
    @Query("SELECT DISTINCT p.region FROM Prefecture p ORDER BY p.region")
    List<String> findAllRegions();

    /**
     * 地域ごとの都道府県数をカウント
     * ネイティブクエリ使用（GROUP BYのため）
     *
     * @return 地域名と件数のマップ
     */
    @Query(value = "SELECT region, COUNT(*) as count " +
            "FROM prefectures " +
            "GROUP BY region " +
            "ORDER BY MIN(id)",
            nativeQuery = true)
    List<Object[]> countByRegion();
}