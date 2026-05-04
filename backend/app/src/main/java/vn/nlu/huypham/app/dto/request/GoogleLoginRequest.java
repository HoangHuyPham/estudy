package vn.nlu.huypham.app.dto.request;

import com.google.auto.value.AutoValue.Builder;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GoogleLoginRequest
{
	String token;
}
