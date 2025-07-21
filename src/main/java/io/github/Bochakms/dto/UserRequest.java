package io.github.Bochakms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
	@Schema(description = "User's first name", example = "Alex")
	@NotBlank(message = "First name is required")
    private String firstName;
	
	@Schema(description = "User's last name", example = "Smith")
    @NotBlank(message = "Last name is required")
    private String lastName;
    
	@Schema(description = "User's email", example = "AlexSmith@gmail.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    
	@Schema(description = "User's password. Password must be at least 6 characters", example = "qwerty")
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}