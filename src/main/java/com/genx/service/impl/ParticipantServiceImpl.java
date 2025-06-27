package com.genx.service.impl;

import com.genx.dto.request.KitCodeRequest;
import com.genx.dto.response.NotificationResponse;
import com.genx.dto.response.ParticipantResponse;
import com.genx.entity.Booking;
import com.genx.entity.Participant;
import com.genx.entity.User;
import com.genx.enums.EBookingStatus;
import com.genx.enums.ECollectionMethod;
import com.genx.enums.EParticipantSampleStatus;
import com.genx.mapper.ParticipantMapper;
import com.genx.repository.IParticipantRepository;
import com.genx.repository.IStaffInfoRepository;
import com.genx.repository.IUserRepository;
import com.genx.security.SecurityUtil;
import com.genx.service.interfaces.INotificationService;
import com.genx.service.interfaces.IParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ParticipantServiceImpl implements IParticipantService {

    @Autowired
    private IParticipantRepository participantRepository;

    @Autowired
    private ParticipantMapper participantMapper;

    @Autowired
    private INotificationService notificationService;



    @Override
    public ParticipantResponse sendKitToCustomer(Long participantId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy participant"));

        if (participant.getSampleStatus() != EParticipantSampleStatus.PENDING) {
            throw new IllegalStateException("Không thể gửi kit ở trạng thái hiện tại");
        }

        participant.setSampleStatus(EParticipantSampleStatus.KIT_SENT);
        participant.setKitEnteredAt(LocalDateTime.now());
        Participant saved = participantRepository.save(participant);

        checkAndNotifyIfAllKitSent(saved.getBooking());

        return participantMapper.toResponse(saved);
    }

    private void checkAndNotifyIfAllKitSent(Booking booking) {
        boolean allKitSent = participantRepository.findByBooking_Id(booking.getId())
                .stream()
                .allMatch(p -> p.getSampleStatus() == EParticipantSampleStatus.KIT_SENT);

        if (allKitSent) {
            User customer = booking.getCustomer().getUser();

            notificationService.sendNotification(
                    customer,
                    "Tất cả bộ kit đã được gửi đi",
                    "Tất cả các bộ kit trong đơn đăng ký" +  booking.getCode() + "đã được gửi đến địa chỉ của bạn. \n" +
                            "Vui lòng truy cập trang \"Hướng dẫn thu mẫu\" để chuẩn bị đúng cách khi nhận được bộ kit.\n",
                    booking
            );
        }
    }


    @Override
    public ParticipantResponse enterKitCodeByStaff(Long participantId, KitCodeRequest request) {
        User currentUser = SecurityUtil.getCurrentUser()
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy người dùng đăng nhập"));

        return handleEnterKitCode(participantId, request, currentUser, false);
    }

    @Override
    public ParticipantResponse enterKitCodeByCustomer(Long participantId, KitCodeRequest request) {
        User currentUser = SecurityUtil.getCurrentUser()
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy người dùng đăng nhập"));

        return handleEnterKitCode(participantId, request, currentUser, true);
    }

    private void notifyStaffIfAllKitsEnteredByCustomer(Booking booking) {
        boolean allKitsEntered = participantRepository.findByBooking_Id(booking.getId())
                .stream()
                .allMatch(p -> p.getSampleStatus() == EParticipantSampleStatus.WAITING_FOR_COLLECTION);

        if (allKitsEntered && booking.getCollectionMethod() == ECollectionMethod.HOME) {
            User staff = booking.getRecordStaff().getUser();
            notificationService.sendNotification(
                    staff,
                    "Khách đã thu mẫu xong",
                    "Đơn #" + booking.getCode() + " đã được khách hoàn tất thu mẫu. Vui lòng kiểm tra và xử lý mẫu gửi đến.",
                    booking
            );
        }
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

    private ParticipantResponse handleEnterKitCode(Long participantId, KitCodeRequest request, User currentUser, boolean isCustomer) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người tham gia với ID: " + participantId));

        if (participant.getBooking() == null || participant.getBooking().getStatus() != EBookingStatus.CONFIRMED) {
            throw new IllegalStateException("Không thể nhập mã kit vì booking chưa được xác nhận.");
        }

        if (participant.getKitCode() != null) {
            throw new IllegalStateException("Mã kit đã được nhập trước đó.");
        }

        if (request.getSampleType() == null) {
            throw new IllegalArgumentException("Mẫu không được tìm thấy.");
        }

        if (isCustomer) {
            String ownerUsername = participant.getBooking().getCustomer().getUser().getUsername();
            if (!currentUser.getUsername().equals(ownerUsername)) {
                throw new SecurityException("Bạn không có quyền nhập mã kit cho người tham gia này.");
            }

            ECollectionMethod method = participant.getBooking().getCollectionMethod();
            if (method == ECollectionMethod.HOME) {
                if (participant.getSampleStatus() != EParticipantSampleStatus.KIT_SENT) {
                    throw new IllegalStateException("Bạn chỉ có thể nhập mã kit khi bộ kit đã được gửi.");
                }
            }
        }


        participant.setKitEnteredBy(currentUser);
        participant.setKitCode(request.getKitCode());
        participant.setKitEnteredAt(LocalDateTime.now());
        participant.setSampleType(request.getSampleType());

        if (isCustomer) {
            participant.setSampleStatus(EParticipantSampleStatus.WAITING_FOR_COLLECTION);
        } else {
            ECollectionMethod method = participant.getBooking().getCollectionMethod();
            if (method == null) {
                throw new IllegalStateException("Không xác định được hình thức thu mẫu.");
            }
            switch (method) {
                case HOME -> participant.setSampleStatus(EParticipantSampleStatus.WAITING_FOR_COLLECTION);
                case HOSPITAL -> participant.setSampleStatus(EParticipantSampleStatus.CONFIRMED);
            }
        }

        Participant saved = participantRepository.save(participant);

        if (isCustomer && participant.getBooking().getCollectionMethod() == ECollectionMethod.HOME) {
            notifyStaffIfAllKitsEnteredByCustomer(participant.getBooking());
        }

        return participantMapper.toResponse(saved);
    }

}
