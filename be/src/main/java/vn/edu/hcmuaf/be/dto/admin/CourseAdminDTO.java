package vn.edu.hcmuaf.be.dto.admin;

import lombok.*;
import vn.edu.hcmuaf.be.entity.Image;

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
    private ImageDTO preview;

    @Data
    public static class ImageDTO{
        private UUID id;
        private String name;
        private String url;
    }
}