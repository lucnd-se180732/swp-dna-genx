package com.genx.controller;

import com.genx.dto.request.BlogRatingRequestDto;
import com.genx.dto.response.BlogRatingResponseDto;
import com.genx.service.interfaces.IBlogRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogRatingController {

    private final IBlogRatingService blogRatingService;


    @PostMapping("/rate")
    public ResponseEntity<Void> rateBlog(@RequestBody BlogRatingRequestDto dto,
                                         @RequestParam Long userId) {
        blogRatingService.rateBlog(userId, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{blogId}/rating")
    public ResponseEntity<BlogRatingResponseDto> getRating(@PathVariable Long blogId) {
        return ResponseEntity.ok(blogRatingService.getBlogRating(blogId));
    }
}
