package com.bezrukov.restproj.repository;

import com.bezrukov.restproj.entity.SystemItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SystemItemRepository extends JpaRepository<SystemItemEntity, String> {
    List<SystemItemEntity> getSystemItemEntitiesByParentId(String parentId);
    List<SystemItemEntity> getSystemItemEntitiesByDateGreaterThanEqualAndDateLessThanEqual
            (LocalDateTime date, LocalDateTime date2);
}
