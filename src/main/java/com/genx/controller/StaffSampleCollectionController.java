package com.genx.controller;


import com.genx.dto.request.KitCodeRequest;
import com.genx.dto.response.ApiResponse;
import com.genx.dto.response.ParticipantResponse;
import com.genx.service.interfaces.IParticipantService;
import com.genx.service.interfaces.ISampleCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/staff/sample-collection")
public class StaffSampleCollectionController {

    @Autowired
    private IParticipantService participantService;

    @Autowired
    private ISampleCollectionService sampleCollectionService;


    @PutMapping("/participants/{id}/kit-code")
    public ParticipantResponse enterKitCode(@PathVariable("id") Long participantId,
                                            @RequestBody KitCodeRequest request) {
        return participantService.enterKitCode(participantId, request);
    }

    @PutMapping("/{participantId}/confirm")
    public ParticipantResponse confirmSampleCollection(@PathVariable Long participantId) {
        return participantService.confirmCollectedSample(participantId);
    }

    @PutMapping("/bookings/{bookingId}/send-to-lab")
    public ApiResponse<String> sendToLab(@PathVariable Long bookingId) {
        sampleCollectionService.sendSamplesToLab(bookingId);
        return ApiResponse.<String>builder()
                .message("Gửi mẫu đến phòng lab thành công.")
                .result("OK")
                .build();
    }

}
