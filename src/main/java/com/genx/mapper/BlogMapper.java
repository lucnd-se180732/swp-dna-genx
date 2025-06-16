package com.genx.mapper;

import com.genx.dto.BlogDto;
import com.genx.entity.BlogRate.Blog;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BlogMapper {

    @Mapping(target = "id", source = "blogId")
    @Mapping(target = "createdById", source = "createdBy.id")
    @Mapping(target = "createdByName", source = "createdBy.fullName")
    BlogDto toDTO(Blog blog);

    @InheritInverseConfiguration(name = "toDTO")
    @Mapping(target = "createdBy", ignore = true) // sẽ set thủ công trong service
    Blog toEntity(BlogDto dto);
}