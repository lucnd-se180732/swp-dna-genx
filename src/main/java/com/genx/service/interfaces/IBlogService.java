package com.genx.service.interfaces;

import com.genx.dto.BlogDto;

import java.util.List;

public interface IBlogService {
    BlogDto createBlog(BlogDto dto, Long userId);
    List<BlogDto> getAllBlogs();
    void deleteBlog(Long id);
}