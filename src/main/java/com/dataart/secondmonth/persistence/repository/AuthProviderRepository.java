package com.dataart.secondmonth.persistence.repository;

import com.dataart.secondmonth.persistence.entity.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthProviderRepository extends JpaRepository<AuthProvider, Long> {

    AuthProvider getByName(String name);

}
