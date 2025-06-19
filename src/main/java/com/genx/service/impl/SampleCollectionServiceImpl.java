package com.genx.service.impl;

import com.genx.entity.SampleCollection;
import com.genx.enums.EParticipantSampleStatus;
import com.genx.enums.ESampleCollectionStatus;
import com.genx.repository.IParticipantRepository;
import com.genx.repository.ISampleCollectionRepository;
import com.genx.service.interfaces.ISampleCollectionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SampleCollectionServiceImpl implements ISampleCollectionService {

    @Autowired
    private ISampleCollectionRepository sampleCollectionRepository;

    @Autowired
    private IParticipantRepository participantRepository;

    @Transactional
    @Override
    public void sendSamplesToLab(Long bookingId) {

        SampleCollection sampleCollection = sampleCollectionRepository.findByBooking_Id(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy quá trình thu mẫu cho booking ID: " + bookingId));

        if (sampleCollection.getStatus() != ESampleCollectionStatus.COLLECTING) {
            throw new IllegalStateException("Không thể gửi mẫu tới lab khi trạng thái hiện tại không phải là COLLECTING.");
        }

        boolean allConfirmed = participantRepository.findByBooking_Id(bookingId)
                .stream()
                .allMatch(p -> p.getSampleStatus() == EParticipantSampleStatus.CONFIRMED);

        if (!allConfirmed) {
            throw new IllegalStateException("Tất cả mẫu chưa được xác nhận. Không thể gửi đến phòng lab.");
        }

        sampleCollection.setStatus(ESampleCollectionStatus.SENT_TO_LAB);
        sampleCollection.setConfirmedAt(LocalDateTime.now());
        sampleCollectionRepository.save(sampleCollection);
    }
}
