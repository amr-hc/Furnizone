package com.elboutique.backend.DTO.request;

import org.springframework.web.multipart.MultipartFile;

import com.elboutique.backend.model.User;
import com.elboutique.backend.validation.Unique;
import com.elboutique.backend.validation.ValidEnum;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Full name is required.")
    @Size(min = 4, message = "Full name must be at least 4 characters long.")
    private String fullName;

    @NotBlank(message = "Email is required.")
    @Email(message = "Please provide a valid email address.")
    @Unique(entity = User.class, fieldName = "email", message = "Email must be unique.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
        message = "Password must contain at least one uppercase letter, one lowercase letter, and one digit."
    )
    private String password;

    @NotBlank(message="Gender is required")
    @ValidEnum(enumClass = Gender.class, message = "Gender must be either 'male' or 'female'.")
    private String gender;

    public enum Gender {
        male,
        female
    }

    private MultipartFile image;


}
