package vn.nlu.huypham.app.dto.response;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMe {
    UUID id;
    String email;
    String displayName;
    String username;
    String avatar;
    boolean isDarkMode;
    List<UserMeRole> roles;
    String phone;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserMeRole {
        UUID id;
        String name;
    }
}
