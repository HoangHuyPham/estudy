package vn.edu.hcmuaf.be.dto.admin;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseAdminDTO {
    private UUID id;
    private String name;
    private String description;
    private double currentPrice;
    private String instructorName;
}