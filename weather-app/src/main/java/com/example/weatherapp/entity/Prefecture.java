package com.example.weatherapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter //Lombok(Getter/Setterを自動で作ってくれる)
@Entity //データベースと対応づけるため
@Table(name = "prefectures") //prefecturesテーブルと対応づけるため
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prefecture {
    //主キーをJPAに教える@Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(name = "name_en", nullable = false, length = 50)
    private String nameEn;

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(nullable = false, length = 20)
    private String region;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
