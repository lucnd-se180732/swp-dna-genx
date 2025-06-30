package com.genx.controller;



import com.genx.dto.request.ServiceRequestDto;
import com.genx.dto.response.ServiceResponseDto;
import com.genx.enums.ECaseType;
import com.genx.service.interfaces.IServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/services")
@RequiredArgsConstructor
public class AdminServiceController {

    private final IServiceService serviceService;


    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ServiceResponseDto> createService(@RequestBody ServiceRequestDto dto) {
        return ResponseEntity.ok(serviceService.createService(dto));
    }


    @GetMapping
    public ResponseEntity<List<ServiceResponseDto>> getAll() {
        return ResponseEntity.ok(serviceService.getAllServices());
    }


    @GetMapping("/filter")
    public ResponseEntity<List<ServiceResponseDto>> getByType(@RequestParam ECaseType type) {
        return ResponseEntity.ok(serviceService.getByCaseType(type));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponseDto> update(@PathVariable Long id, @RequestBody ServiceRequestDto dto) {
        return ResponseEntity.ok(serviceService.updateService(id, dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}/enabled")
    public ResponseEntity<Void> toggleEnabled(@PathVariable Long id, @RequestParam boolean enabled) {
        serviceService.toggleEnabled(id, enabled);
        return ResponseEntity.ok().build();
    }
}
