package vn.nlu.huypham.app.dto.request;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest
{
	String displayName;
	@Length(min = 3, max = 255)
	String username;
	@Length(min = 8, max = 255)
	String password;
	String reCaptchaToken;
	String avatar;
	@Email
	@NotBlank
	String email;
}
