
package com.genx.controller;
import com.genx.dto.request.AdnResultRequest;
import com.genx.dto.response.ParticipantResponse;
import com.genx.entity.AdnResult;
import com.genx.entity.Participant;
import com.genx.repository.IParticipantRepository;
import com.genx.service.interfaces.IAdnResultService;
import com.genx.service.interfaces.IUploadImageFile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping("/api/adn-results")
@RequiredArgsConstructor
public class AdnResultController {
    @Autowired
    private IParticipantRepository participantRepository;

    @Autowired
    private IUploadImageFile uploadImageFile;
    private final IAdnResultService IAdnResultService;

    @PostMapping
    public ResponseEntity<AdnResult> saveResult(@RequestBody AdnResultRequest request) {
        return ResponseEntity.ok(IAdnResultService.saveAdnResult(request));
    }

    @GetMapping("/export/{bookingId}")
    public ResponseEntity<byte[]> exportResult(@PathVariable Long bookingId) throws Exception {
        byte[] pdf = IAdnResultService.exportResultToPdf(bookingId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename("adn_result_" + bookingId + ".pdf").build());
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    @GetMapping("/booking/{id}/participants")
    public ResponseEntity<List<ParticipantResponse>> getParticipants(@PathVariable Long id) {
        return ResponseEntity.ok(IAdnResultService.getParticipantsByBookingId(id));
    }

    @PostMapping("/participants/{id}/fingerprint")
    public ResponseEntity<String> uploadFingerprintImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        try {
            String url = uploadImageFile.uploadImageFile(file);  // service upload ảnh lên Cloudinary
            Participant participant = participantRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy participant"));

            participant.setFingerprintImageUrl(url);
            participantRepository.save(participant);

            return ResponseEntity.ok(url);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image: " + e.getMessage());
        }
    }
}