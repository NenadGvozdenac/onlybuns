package com.onlybuns.onlybuns.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onlybuns.onlybuns.domain.models.EmailAuthentication;

public interface EmailRepository extends JpaRepository<EmailAuthentication, Long> {
    public EmailAuthentication findByEmail(String email);
}
