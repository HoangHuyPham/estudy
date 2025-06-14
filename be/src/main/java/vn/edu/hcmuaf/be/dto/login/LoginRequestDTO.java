package vn.edu.hcmuaf.be.dto.login;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequestDTO {
    private String username;
    private String password;

    public String getUsername() {return this.username;}
    public void setUsername(String username) {this.username = username;}
    public String getPassword() {return this.password;}
    public void setPassword(String password) {this.password = password;}
}
