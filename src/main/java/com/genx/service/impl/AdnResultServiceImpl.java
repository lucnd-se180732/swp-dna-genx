
package com.genx.service.impl;

import com.genx.dto.request.AdnResultRequest;
import com.genx.dto.response.ParticipantResponse;
import com.genx.entity.AdnResult;
import com.genx.entity.Booking;
import com.genx.entity.SampleCollection;
import com.genx.enums.ESampleCollectionStatus;
import com.genx.mapper.AdnResultMapper;
import com.genx.repository.IAdnResultRepository;
import com.genx.repository.IBookingRepository;
import com.genx.repository.ISampleCollectionRepository;
import com.genx.service.interfaces.IAdnResultService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdnResultServiceImpl implements IAdnResultService {

    @Autowired
    private IAdnResultRepository adnResultRepository;

    @Autowired
    private IBookingRepository bookingRepository;

    @Override
    public AdnResult saveAdnResult(AdnResultRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        AdnResult result = AdnResult.builder()
                .booking(booking)
                .createdAt(LocalDateTime.now())
                .lociResults(request.getLociResults())
                .conclusion(request.getConclusion())
                .build();
        return adnResultRepository.save(result);
    }

    public byte[] exportResultToPdf(Long bookingId) throws Exception {
        AdnResult result = adnResultRepository.findByBooking_Id(bookingId)
                .orElseThrow(() -> new RuntimeException("Result not found"));
        Booking booking = result.getBooking();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();

        BaseFont baseFont = BaseFont.createFont("fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font titleFont = new Font(baseFont, 16, Font.BOLD);
        Font headerFont = new Font(baseFont, 12, Font.BOLD);
        Font normalFont = new Font(baseFont, 11);

        // Header
        Paragraph govTitle = new Paragraph("CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM", headerFont);
        govTitle.setAlignment(Element.ALIGN_CENTER);
        document.add(govTitle);

        Paragraph slogan = new Paragraph("Độc lập - Tự do - Hạnh phúc", headerFont);
        slogan.setAlignment(Element.ALIGN_CENTER);
        slogan.setSpacingAfter(15f);
        document.add(slogan);

        Paragraph mainTitle = new Paragraph("PHIẾU KẾT QUẢ XÉT NGHIỆM ADN", titleFont);
        mainTitle.setAlignment(Element.ALIGN_CENTER);
        mainTitle.setSpacingAfter(20f);
        document.add(mainTitle);

        // Representative info
        document.add(new Paragraph("THÔNG TIN NGƯỜI ĐẠI DIỆN", headerFont));
        document.add(new Paragraph("Họ tên: " + booking.getCustomer().getUser().getFullName(), normalFont));
        document.add(new Paragraph("Số điện thoại: " + booking.getPhoneNumber(), normalFont));
        document.add(new Paragraph("Email: " + booking.getEmail(), normalFont));
        document.add(new Paragraph("Ngày hẹn: " + (booking.getAppointmentDate() != null ? booking.getAppointmentDate().toString() : "Không rõ"), normalFont));
        document.add(Chunk.NEWLINE);

        String caseType = booking.getService().getCaseType().name();
        document.add(new Paragraph("Loại hồ sơ: " + (caseType != null ? caseType : "Không rõ"), normalFont));
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("DANH SÁCH NGƯỜI THAM GIA", headerFont));
        PdfPTable participantTable;
        if ("ADMINISTRATIVE".equalsIgnoreCase(caseType)) {
            participantTable = new PdfPTable(5);
            participantTable.setWidthPercentage(100);
            participantTable.setSpacingBefore(10f);
            participantTable.addCell(new PdfPCell(new Phrase("Họ tên", normalFont)));
            participantTable.addCell(new PdfPCell(new Phrase("Kit Code", normalFont)));
            participantTable.addCell(new PdfPCell(new Phrase("Mối quan hệ", normalFont)));
            participantTable.addCell(new PdfPCell(new Phrase("CMND/CCCD", normalFont)));
            participantTable.addCell(new PdfPCell(new Phrase("Vân tay", normalFont)));

            for (var p : booking.getParticipants()) {
                participantTable.addCell(new PdfPCell(new Phrase(p.getFullName(), normalFont)));
                participantTable.addCell(new PdfPCell(new Phrase(p.getKitCode(), normalFont)));
                participantTable.addCell(new PdfPCell(new Phrase(p.getRelationship(), normalFont)));
                participantTable.addCell(new PdfPCell(new Phrase(p.getIdentityNumber() != null ? p.getIdentityNumber() : "Không có", normalFont)));

                if (p.getFingerprintImageUrl() != null && !p.getFingerprintImageUrl().isEmpty()) {
                    try {
                        Image fingerprintImg = Image.getInstance(new URL(p.getFingerprintImageUrl()));
                        fingerprintImg.scaleToFit(50, 50);
                        PdfPCell imgCell = new PdfPCell(fingerprintImg, true);
                        imgCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        participantTable.addCell(imgCell);
                    } catch (Exception e) {
                        participantTable.addCell(new PdfPCell(new Phrase("Lỗi ảnh", normalFont)));
                    }
                } else {
                    participantTable.addCell(new PdfPCell(new Phrase("Không", normalFont)));
                }
            }
        } else {
            participantTable = new PdfPTable(3);
            participantTable.setWidthPercentage(100);
            participantTable.setSpacingBefore(10f);
            participantTable.addCell(new PdfPCell(new Phrase("Họ tên", normalFont)));
            participantTable.addCell(new PdfPCell(new Phrase("Kit Code", normalFont)));
            participantTable.addCell(new PdfPCell(new Phrase("Mối quan hệ", normalFont)));

            for (var p : booking.getParticipants()) {
                participantTable.addCell(new PdfPCell(new Phrase(p.getFullName(), normalFont)));
                participantTable.addCell(new PdfPCell(new Phrase(p.getKitCode(), normalFont)));
                participantTable.addCell(new PdfPCell(new Phrase(p.getRelationship(), normalFont)));
            }
        }
        document.add(participantTable);
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("KẾT QUẢ XÉT NGHIỆM (THEO LOCUS)", headerFont));
        PdfPTable lociTable = new PdfPTable(2);
        lociTable.setWidthPercentage(100);
        lociTable.setSpacingBefore(10f);
        lociTable.addCell("Locus");
        lociTable.addCell(new Phrase("Kết quả", normalFont));

        for (var entry : result.getLociResults().entrySet()) {
            lociTable.addCell(entry.getKey());
            lociTable.addCell(entry.getValue());
        }

        document.add(lociTable);
        document.add(Chunk.NEWLINE);

        Paragraph conclusion = new Paragraph("KẾT LUẬN:", headerFont);
        conclusion.setSpacingBefore(15f);
        document.add(conclusion);

        Paragraph conclusionText = new Paragraph(result.getConclusion().toUpperCase(), titleFont);
        conclusionText.setSpacingBefore(5f);
        conclusionText.setAlignment(Element.ALIGN_LEFT);
        document.add(conclusionText);

        document.close();
        return out.toByteArray();
    }


    @Override
    public List<ParticipantResponse> getParticipantsByBookingId(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy booking"));

        SampleCollection collection = booking.getSampleCollection();

        if (collection == null || collection.getStatus() != ESampleCollectionStatus.SENT_TO_LAB) {
            throw new RuntimeException("Chỉ lấy participants khi mẫu đã được gửi đến phòng lab");
        }

        return booking.getParticipants().stream()
                .map(p -> {
                    ParticipantResponse dto = new ParticipantResponse();
                    dto.setFullName(p.getFullName());
                    dto.setKitCode(p.getKitCode());
                    dto.setRelationship(p.getRelationship());
                    return dto;
                })
                .collect(Collectors.toList());
    }

}