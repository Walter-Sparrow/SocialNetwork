package com.dataart.secondmonth.audit.persistence.repository;

import com.dataart.secondmonth.audit.persistence.document.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuditLogRepository extends MongoRepository<AuditLog, String> {
}
