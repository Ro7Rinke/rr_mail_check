package com.ro7rinke.rr_mail_check.repository;

import com.ro7rinke.rr_mail_check.model.DisposableDomain;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DisposableDomainRepository extends JpaRepository<DisposableDomain, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO disposable_domains(domain) VALUES (:domain) ON CONFLICT(domain) DO NOTHING", nativeQuery = true)
    void insertIgnoreConflict(@Param("domain") String domain);
}
