package com.elboutique.backend.mapper;


import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Value;

import com.elboutique.backend.DTO.response.UserResponse;
import com.elboutique.backend.model.User;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Value("${app.base-url}")
    protected String baseUrl;

    @Mapping(target = "image_url", source = "image", qualifiedByName = "withBaseUrl")
    @Mapping(target = "full_name", source = "fullName")
    public abstract UserResponse toDto(User user);

    @Named("withBaseUrl")
    protected String getImageWithBaseUrl(String image) {
        return image != null ? baseUrl + image : null;
    }
}

