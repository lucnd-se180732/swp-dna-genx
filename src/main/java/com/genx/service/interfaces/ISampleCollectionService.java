package com.genx.service.interfaces;

import com.genx.dto.response.SampleCollectionHistoryResponse;

import java.util.List;

public interface ISampleCollectionService {
    void sendSamplesToLab(Long bookingId);

    List<SampleCollectionHistoryResponse> getSampleCollectionHistory();
     void completeSampleCollection(Long bookingId);
}
