package vn.edu.hcmuaf.be.dto.admin;
import lombok.*;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleAdminDTO {
    private UUID id;
    private String name;
    private String description;
}