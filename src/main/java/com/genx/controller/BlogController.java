package com.genx.controller;



import com.genx.dto.request.BlogRequestDto;
import com.genx.dto.response.BlogResponseDto;
import com.genx.service.interfaces.IBlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final IBlogService blogService;

    @PostMapping
    public ResponseEntity<BlogResponseDto> create(@RequestBody BlogRequestDto dto, @RequestParam Long userId) {
        return ResponseEntity.ok(blogService.createBlog(dto, userId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<BlogResponseDto>> getAll() {
        return ResponseEntity.ok(blogService.getAllBlogs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(blogService.getBlogById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogResponseDto> update(@PathVariable Long id, @RequestBody BlogRequestDto dto) {
        return ResponseEntity.ok(blogService.updateBlog(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<BlogResponseDto>> getAllBlogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(blogService.getAllBlogs(pageable));
    }
}