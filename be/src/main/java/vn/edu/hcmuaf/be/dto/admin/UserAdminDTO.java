package vn.edu.hcmuaf.be.dto.admin;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAdminDTO {
    private UUID id;
    private String username;
    private String role;
}
