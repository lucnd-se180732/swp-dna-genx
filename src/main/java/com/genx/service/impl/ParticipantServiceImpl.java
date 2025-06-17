package com.genx.service.impl;

import com.genx.dto.request.KitCodeRequest;
import com.genx.dto.response.ParticipantResponse;
import com.genx.entity.Participant;
import com.genx.enums.EBookingStatus;
import com.genx.enums.EParticipantSampleStatus;
import com.genx.mapper.ParticipantMapper;
import com.genx.repository.IParticipantRepository;
import com.genx.repository.IUserRepository;
import com.genx.service.interfaces.IParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ParticipantServiceImpl implements IParticipantService {

    @Autowired
    private IParticipantRepository participantRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ParticipantMapper participantMapper;


    @Override
    public ParticipantResponse enterKitCode(Long participantId, KitCodeRequest request) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người tham gia với ID: " + participantId));

        if (participant.getBooking() == null || participant.getBooking().getStatus() != EBookingStatus.CONFIRMED) {
            throw new IllegalStateException("Không thể nhập mã kit vì booking chưa được xác nhận.");
        }
        // TODO: sau này lấy từ người dùng đăng nhập
        Long fakeLoggedInUserId = 1L;

        participant.setKitCode(request.getKitCode());
        participant.setKitEnteredAt(LocalDateTime.now());
        participant.setKitEnteredBy(userRepository.findById(fakeLoggedInUserId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên nhập kit")));

        // Tự động cập nhật sampleStatus theo hình thức thu mẫu
        var method = participant.getBooking().getCollectionOption();
        if (method == null) {
            throw new IllegalStateException("Không xác định được hình thức thu mẫu.");
        }
        switch (method) {
            case HOME -> participant.setSampleStatus(EParticipantSampleStatus.WAITING_FOR_COLLECTION);
            case HOSPITAL -> participant.setSampleStatus(EParticipantSampleStatus.CONFIRMED);
        }

        return participantMapper.toResponse(participantRepository.save(participant));
    }

    @Override
    public ParticipantResponse confirmCollectedSample(Long participantId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người tham gia với ID: " + participantId));

        if (participant.getSampleStatus() != EParticipantSampleStatus.WAITING_FOR_COLLECTION) {
            throw new IllegalStateException("Không thể xác nhận thu mẫu vì trạng thái hiện tại không phải là CHỜ THU MẪU.");
        }

        participant.setSampleStatus(EParticipantSampleStatus.CONFIRMED);
        return participantMapper.toResponse(participantRepository.save(participant));
    }
}
