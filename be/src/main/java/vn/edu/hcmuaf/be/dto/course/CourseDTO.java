package vn.edu.hcmuaf.be.dto.course;

import java.util.List;
import java.util.UUID;

import lombok.Data;
import vn.edu.hcmuaf.be.entity.enums.Language;

@Data
public class CourseDTO {
    private UUID id;
    private String name;
    private String description;
    private Language language;
    private double oldPrice, currentPrice;
    private ImageDTO preview;
    private InstructorDTO instructor;
    private List<SectionDTO> sections;

    @Data
    public static class InstructorDTO {
        private UUID id;
        private String description;
        private String displayName;
    }

    @Data
    public static class SectionDTO {
        private UUID id;
        private String name;
        private int ordinal;
        private List<LectureDTO> lectures;
    }

    @Data
    public static class LectureDTO {
        private UUID id;
        private String title;
        private String description;
        private int ordinal;
        private VideoDTO video;
    }

    @Data
    public static class VideoDTO {
        private UUID id;
        private long duration;
    }

    @Data
    public static class ImageDTO {
        private UUID id;
        private String url;
    }
};
