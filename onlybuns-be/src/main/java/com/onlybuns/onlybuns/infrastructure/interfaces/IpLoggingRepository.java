package com.onlybuns.onlybuns.infrastructure.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onlybuns.onlybuns.domain.models.IpLog;

public interface IpLoggingRepository extends JpaRepository<IpLog, Long> {
    Optional<IpLog> findByIpAddress(String ipAddress);
    Optional<List<IpLog>> findAllByIpAddress(String ipAddress);
}
