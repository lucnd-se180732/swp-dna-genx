package com.genx.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyController {
    @GetMapping("/dummy")
    public ResponseEntity<String> dummy() {
        return ResponseEntity.ok("OK");
    }
}
