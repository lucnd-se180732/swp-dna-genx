package com.genx.service.interfaces;

import com.genx.dto.request.KitCodeRequest;
import com.genx.dto.response.ParticipantResponse;

public interface IParticipantService{
    ParticipantResponse enterKitCode(Long participantId, KitCodeRequest request);
    ParticipantResponse confirmCollectedSample(Long participantId);
}
