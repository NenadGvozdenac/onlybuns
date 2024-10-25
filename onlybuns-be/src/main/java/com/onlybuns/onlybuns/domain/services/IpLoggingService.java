package com.onlybuns.onlybuns.domain.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlybuns.onlybuns.domain.models.IpLog;
import com.onlybuns.onlybuns.infrastructure.interfaces.IpLoggingRepository;

@Service
public class IpLoggingService {

    @Autowired
    private final IpLoggingRepository ipLoggingRepository;

    public IpLoggingService(IpLoggingRepository ipLoggingRepository) {
        this.ipLoggingRepository = ipLoggingRepository;
    }

    public IpLog logIpAddress(String address) {
        // log the IP address of the user
        IpLog ipLog = new IpLog();
        ipLog.setIpAddress(address);
        ipLog.setTimestamp(System.currentTimeMillis());
        ipLog.setCreatedAt(LocalDateTime.now());

        ipLog = ipLoggingRepository.save(ipLog);

        return ipLog;
    }

    /***
     * Get the number of logs for the given IP address
     * @param address
     * @return the number of logs
     */
    public Integer getIpLogCount(String address) {
        return ipLoggingRepository.findAllByIpAddress(address).orElse(List.of()).size();
    }

    /***
     * Get the number of logs for the given IP address in the last given minutes
     * @param address
     * @param minutes
     * @return the number of logs
     */
    public Integer getIpLogCountInPastTime(String address, Integer minutes) {
        return ipLoggingRepository.findAll().stream()
                .filter(ipLog -> ipLog.getIpAddress().equals(address)
                        && ipLog.getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(minutes)))
                .toList().size();
    }
}
