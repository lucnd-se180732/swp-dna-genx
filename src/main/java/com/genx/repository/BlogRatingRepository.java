package com.genx.repository;

import com.genx.entity.BlogRate.Blog;
import com.genx.entity.BlogRate.BlogRating;
import com.genx.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface BlogRatingRepository extends JpaRepository<BlogRating, Long> {
    Optional<BlogRating> findByBlogAndUser(Blog blog, User user);

    List<BlogRating> findByBlog(Blog blog);
}