package com.onlybuns.onlybuns.infrastructure.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onlybuns.onlybuns.domain.models.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
    
}
