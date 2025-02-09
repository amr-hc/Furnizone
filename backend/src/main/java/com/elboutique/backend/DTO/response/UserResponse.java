package com.elboutique.backend.DTO.response;

import com.elboutique.backend.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Integer id;
    private String full_name;
    private String email;
    private String gender;
    private String image_url;

    public static UserResponse fromUser(User user, String baseUrl) {
        return UserResponse.builder()
            .id(user.getId())
            .full_name(user.getFullName())
            .email(user.getEmail())
            .image_url(user.getImage() != null ? baseUrl + "/" + user.getImage() : null)
            .build();
    }
}
