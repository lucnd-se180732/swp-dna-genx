package com.genx.controller;


import com.genx.dto.RegistrationDTO;
import com.genx.enums.EPaymentStatus;
import com.genx.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/registrations")
@CrossOrigin(origins = "*")
public class RegistrationController {
    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/register")  // Keeping original endpoint
    public ResponseEntity<RegistrationDTO> createRegistration(@RequestBody RegistrationDTO registrationDTO) {
        RegistrationDTO savedRegistration = registrationService.createRegistration(registrationDTO);
        return ResponseEntity.ok(savedRegistration);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistrationDTO> getRegistration(@PathVariable Long id) {
        RegistrationDTO registration = registrationService.getRegistrationById(id);
        return ResponseEntity.ok(registration);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<RegistrationDTO> cancelRegistration(@PathVariable Long id) {
        RegistrationDTO cancelledRegistration = registrationService.cancelRegistration(id);
        return ResponseEntity.ok(cancelledRegistration);
    }
    @GetMapping
    public ResponseEntity<List<RegistrationDTO>> getAllRegistrations(
            @RequestParam(required = false) EPaymentStatus status) {
        List<RegistrationDTO> registrations;
        if (status != null) {
            registrations = registrationService.getRegistrationsByStatus(status);
        } else {
            registrations = registrationService.getAllRegistrations();
        }
        return ResponseEntity.ok(registrations);
    }
}
