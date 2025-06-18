package vn.edu.hcmuaf.be.dto.user;

import java.util.UUID;

import lombok.Data;

@Data
public class UserInfoDTO {
    private UUID id;
    private String username;
    private RoleDTO role;
    private ProfileDTO profile;
    private CustomerDTO customer;
    private InstructorDTO instructor;

    @Data
    public static class ProfileDTO {
        private UUID id;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private ImageDTO preview;
    }

    @Data
    public static class ImageDTO {
        private UUID id;
        private String name;
        private String url;
    }

    @Data
    public static class RoleDTO {
        private UUID id;
        private String name;
        private String description;
    }

    @Data
    public static class InstructorDTO {
        private UUID id;
        private String description;
        private String displayName;
    }

    @Data
    public static class CustomerDTO {
        private UUID id;
        private String displayName;
        private String description;
    }
}
