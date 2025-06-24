
package com.genx.controller;
import com.genx.dto.request.AdnResultRequest;
import com.genx.dto.response.ParticipantResponse;
import com.genx.entity.AdnResult;
import com.genx.service.interfaces.IAdnResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/adn-results")
@RequiredArgsConstructor
public class AdnResultController {
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
}