package com.ro7rinke.rr_mail_check.repository;

import com.ro7rinke.rr_mail_check.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface EmailRepository extends JpaRepository<Email, Long> {

}

