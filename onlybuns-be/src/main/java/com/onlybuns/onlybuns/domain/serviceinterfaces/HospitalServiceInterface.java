package com.onlybuns.onlybuns.domain.serviceinterfaces;

import java.util.List;

import com.onlybuns.onlybuns.core.misc.Result;
import com.onlybuns.onlybuns.domain.models.HospitalInformation;

public interface HospitalServiceInterface {
    Result<List<HospitalInformation>> connectToNodeServer();
}
