package com.pinup.repository;

import com.pinup.entity.Place;
import com.pinup.repository.querydsl.PlaceRepositoryQueryDsl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long>, PlaceRepositoryQueryDsl {
    Optional<Place> findByKakaoMapId(String kakaoMapId);
}
