package com.company.restproj.repository;

import com.company.restproj.modeldb.SystemItemDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SystemItemRepository extends JpaRepository<SystemItemDB, String> {
    List<SystemItemDB> getSystemItemDBByParentId(String parentId);
    List<SystemItemDB> getSystemItemDBByDateBetween(Date date, Date date2);
}
