package com.swanhtetaungphyo.article_approval.dto;

import jakarta.validation.constraints.*;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.swanhtetaungphyo.article_approval.models.User}
 */
@Value
public class UserDto {
    @NotNull
    @Size(max = 200)
    String username;
    @NotNull(message = "email must not be null")
    @Size(max = 100)
    @Email
    @NotEmpty
    @NotBlank
    String email;
}