package com.example.weatherapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 都道府県マスタEntity
 * prefectures テーブルとマッピング
 */
@Entity
@Table(name = "prefectures")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prefecture {

    /**
     * 都道府県ID（主キー）
     * AUTO_INCREMENTで自動採番
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 都道府県名（例: 東京都）
     * NOT NULL制約、最大10文字
     */
    @Column(nullable = false, length = 10)
    private String name;

    /**
     * 英語名（例: Tokyo）
     * NOT NULL制約、最大50文字
     */
    @Column(name = "name_en", nullable = false, length = 50)
    private String nameEn;

    /**
     * 緯度（例: 35.689487）
     * DECIMAL(9,6)型
     */
    @Column(nullable = false, precision = 9, scale = 6)
    private Double latitude;

    /**
     * 経度（例: 139.691706）
     * DECIMAL(9,6)型
     */
    @Column(nullable = false, precision = 9, scale = 6)
    private Double longitude;

    /**
     * 地域区分（例: 関東）
     * NOT NULL制約、最大20文字
     */
    @Column(nullable = false, length = 20)
    private String region;

    /**
     * 作成日時
     * INSERT時に自動設定
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新日時
     * INSERT/UPDATE時に自動設定
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * INSERT前に実行されるメソッド
     * 作成日時・更新日時を現在時刻で初期化
     */
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    /**
     * UPDATE前に実行されるメソッド
     * 更新日時を現在時刻で更新
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}