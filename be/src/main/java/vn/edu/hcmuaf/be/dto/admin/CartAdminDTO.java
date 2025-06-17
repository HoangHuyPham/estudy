package vn.edu.hcmuaf.be.dto.admin;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartAdminDTO {
    private UUID customerId;
    private String customerUsername;
    private String courseName;
    private double price;
}