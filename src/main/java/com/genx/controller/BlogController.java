package com.genx.controller;

import com.genx.dto.BlogDto;
import com.genx.service.interfaces.IBlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173") // <-- Để Vue gọi được API
@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final IBlogService blogService;

    @PostMapping
    public ResponseEntity<BlogDto> create(@RequestBody BlogDto dto, @RequestParam Long userId) {
        return ResponseEntity.ok(blogService.createBlog(dto, userId));
    }

    @GetMapping
    public ResponseEntity<List<BlogDto>> getAll() {
        return ResponseEntity.ok(blogService.getAllBlogs());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return ResponseEntity.noContent().build();
    }
}