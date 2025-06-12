package vn.edu.hcmuaf.be.dto.login;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequestDTO {
    private String username;
    private String password;
}
