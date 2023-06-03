package com.bezrukov.restproj.repository;

import com.bezrukov.restproj.entity.SystemItemHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SystemItemHistoryRepository extends JpaRepository<SystemItemHistoryEntity, Long> {
    List<SystemItemHistoryEntity> getSystemItemHistoryEntitiesByIdAndDateGreaterThanEqualAndDateLessThan(
            String id, LocalDateTime startDate, LocalDateTime endDate);
    void deleteAllById(String id);
}
