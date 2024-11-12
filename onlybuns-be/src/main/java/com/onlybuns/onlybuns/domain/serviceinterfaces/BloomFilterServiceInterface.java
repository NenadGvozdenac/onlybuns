package com.onlybuns.onlybuns.domain.serviceinterfaces;

public interface BloomFilterServiceInterface {
    public void add(String username);
    public boolean contains(String username);
}
