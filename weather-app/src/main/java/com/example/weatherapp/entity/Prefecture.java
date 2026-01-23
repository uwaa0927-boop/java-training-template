package com.example.weatherapp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter //Lombok(Getter/Setterを自動で作ってくれる)
@Entity //データベースと対応づけるため
@Table(name = "prefectures") //Prefecturesテーブルと対応づけるため
public class Prefecture {
    //主キーをJPAに教える@Id
    @Id
    private Long id;
    private String name;
    private String region;
}
