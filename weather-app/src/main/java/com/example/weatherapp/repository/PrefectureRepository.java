package com.example.weatherapp.repository;

import com.example.weatherapp.entity.Prefecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrefectureRepository extends JpaRepository<Prefecture, Long> {

    // 地域で検索
    List<Prefecture> findByRegion(String region);

    // 名前で検索
    Prefecture findByName(String name);
}
