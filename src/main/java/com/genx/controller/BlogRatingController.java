package com.genx.controller;

import com.genx.dto.request.BlogRatingRequestDto;
import com.genx.dto.response.BlogRatingResponseDto;
import com.genx.entity.User;
import com.genx.service.interfaces.IBlogRatingService;
import com.genx.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogRatingController {

    private final IBlogRatingService blogRatingService;
    private final IUserService userService;

    @PostMapping("/rate")
    public ResponseEntity<Void> rateBlog(@RequestBody BlogRatingRequestDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // lấy username từ token

        User user = userService.getUserByUsername(username); // custom method trong UserService
        blogRatingService.rateBlog(user.getId(), dto); // giữ nguyên logic
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{blogId}/rating")
    public ResponseEntity<BlogRatingResponseDto> getRating(@PathVariable Long blogId) {
        return ResponseEntity.ok(blogRatingService.getBlogRating(blogId));
    }
}