package com.genx.service.Impl;



import com.genx.dto.request.BlogRequestDto;
import com.genx.dto.response.BlogResponseDto;
import com.genx.entity.BlogRate.Blog;
import com.genx.entity.User;
import com.genx.mapper.BlogMapper;
import com.genx.repository.BlogRepository;
import com.genx.repository.UserRepository;
import com.genx.service.interfaces.IBlogService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements IBlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final BlogMapper blogMapper;

    @Override
    public BlogResponseDto createBlog(BlogRequestDto dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Blog blog = blogMapper.toEntity(dto);
        blog.setCreatedAt(LocalDateTime.now());
        blog.setCreatedBy(user);

        return blogMapper.toResponseDto(blogRepository.save(blog));
    }

    @Override
    public List<BlogResponseDto> getAllBlogs() {
        return blogRepository.findAll().stream()
                .map(blogMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BlogResponseDto getBlogById(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found with id: " + id));

        blog.setViewCount(blog.getViewCount() + 1); // Tăng lượt xem
        blogRepository.save(blog); // Lưu lại

        return blogMapper.toResponseDto(blog); // Trả về DTO đã có viewCount
    }


    private String generateSlug(String title) {
        return title == null ? null :
                title.toLowerCase()
                        .replaceAll("[^a-z0-9\\s]", "")
                        .replaceAll("\\s+", "-");
    }
    @Override
    public BlogResponseDto updateBlog(Long id, BlogRequestDto dto) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        blog.setTitle(dto.getTitle());
        blog.setShortDescription(dto.getShortDescription());
        blog.setThumbnailUrl(dto.getThumbnailUrl());
        blog.setContent(dto.getContent());
        blog.setSlug(generateSlug(dto.getTitle()));
        blog.setCreatedAt(LocalDateTime.now());

        return blogMapper.toResponseDto(blogRepository.save(blog));
    }

    @Override
    public void deleteBlog(Long id) {
        if (!blogRepository.existsById(id)) {
            throw new RuntimeException("Blog not found");
        }
        blogRepository.deleteById(id);
    }

    @Override
    public Page<BlogResponseDto> getAllBlogs(Pageable pageable) {
        return blogRepository.findAll(pageable)
                .map(blogMapper::toResponseDto);
    }


}
