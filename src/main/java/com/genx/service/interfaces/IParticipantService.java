package com.genx.service.interfaces;

import com.genx.dto.request.KitCodeRequest;
import com.genx.dto.response.ParticipantResponse;

public interface IParticipantService{
    ParticipantResponse sendKitToCustomer(Long participantId);
    ParticipantResponse confirmCollectedSample(Long participantId);
    ParticipantResponse enterKitCodeByStaff(Long participantId, KitCodeRequest request);
    ParticipantResponse enterKitCodeByCustomer(Long participantId, KitCodeRequest request);
}
