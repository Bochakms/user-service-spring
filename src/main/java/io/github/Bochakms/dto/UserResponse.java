package io.github.Bochakms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "UserResponse", description = "User response with HATEOAS links")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
	@Schema(description = "Unique identifier of the user", example = "1")
	private Long id;
	@Schema(description = "User's first name", example = "Alex")
	private String firstName;
	@Schema(description = "User's last name", example = "Smith")
	private String lastName;
	@Schema(description = "User's email", example = "AlexSmith@gmail.com")
	private String email;
}