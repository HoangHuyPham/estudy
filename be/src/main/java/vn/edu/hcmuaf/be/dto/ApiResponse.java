package vn.edu.hcmuaf.be.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {
    private T data;
    private String message;
}
