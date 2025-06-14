package vn.edu.hcmuaf.be.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ApiResponse<T> {
    private T data;
    private String message;
}
