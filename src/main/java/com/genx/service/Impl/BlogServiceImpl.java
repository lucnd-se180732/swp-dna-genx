package com.genx.service.Impl;

import com.genx.dto.BlogDto;
import com.genx.entity.BlogRate.Blog;
import com.genx.entity.User;
import com.genx.mapper.BlogMapper;
import com.genx.repository.BlogRepository;
import com.genx.repository.UserRepository;
import com.genx.service.interfaces.IBlogService;
import lombok.RequiredArgsConstructor;
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
    public BlogDto createBlog(BlogDto dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Blog blog = blogMapper.toEntity(dto);
        blog.setBlogId(null); // để đảm bảo JPA tạo mới
        blog.setCreatedAt(LocalDateTime.now());
        blog.setCreatedBy(user);

        return blogMapper.toDTO(blogRepository.save(blog));
    }

    @Override
    public List<BlogDto> getAllBlogs() {
        return blogRepository.findAll().stream()
                .map(blogMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBlog(Long id) {
        if (!blogRepository.existsById(id)) {
            throw new RuntimeException("Blog not found");
        }
        blogRepository.deleteById(id);
    }
}