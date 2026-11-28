package com.quanlykhov.dao;

import com.quanlykhov.model.VaiTro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaiTroRepository extends JpaRepository<VaiTro, String> {
    boolean existsByTenVTIgnoreCase(String tenVT);
}