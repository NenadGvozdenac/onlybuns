package com.onlybuns.onlybuns.domain.services;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.onlybuns.onlybuns.domain.serviceinterfaces.BloomFilterServiceInterface;
import com.onlybuns.onlybuns.infrastructure.interfaces.UserRepository;

import jakarta.annotation.PostConstruct;

@Service
public class BloomFilterService implements BloomFilterServiceInterface {

    private BloomFilter<String> bloomFilter;

    @Autowired
    private UserRepository userRepository;

    public BloomFilterService() {
        bloomFilter = BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8), 100000, 0.01);
    }

    @PostConstruct
    public void init() {
        List<String> usernames = userRepository.findAll().stream().map(user -> user.getUsername()).collect(Collectors.toList());
        usernames.forEach(username -> bloomFilter.put(username));
    }

    @Override
    public void add(String username) {
        bloomFilter.put(username);
    }

    @Override
    public boolean contains(String username) {
        return bloomFilter.mightContain(username);
    }
}
