package com.harini.DSA.repository;

import com.harini.DSA.model.TreeRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TreeRecordRepository extends JpaRepository<TreeRecord, Long> {
    List<TreeRecord> findAllByOrderByIdDesc();
}