package vn.edu.hcmuaf.be.dto.register;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class RegisterRequestDTO {
    private String username;
    private String password;

}