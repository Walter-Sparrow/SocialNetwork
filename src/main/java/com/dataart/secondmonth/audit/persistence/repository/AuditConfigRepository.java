package com.dataart.secondmonth.audit.persistence.repository;

import com.dataart.secondmonth.persistence.entity.AuditConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditConfigRepository extends JpaRepository<AuditConfig, String> {
}
