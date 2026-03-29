package vn.nlu.huypham.app.dto.request;

import org.hibernate.validator.constraints.Length;

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
public class LoginBasic {
    @Length(min = 3, max = 255)
    String username;
    @Length(min = 8, max = 255)
    String password;
}
