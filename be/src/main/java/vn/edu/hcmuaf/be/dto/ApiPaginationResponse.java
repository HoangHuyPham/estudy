package vn.edu.hcmuaf.be.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public class ApiPaginationResponse<T> extends ApiResponse<T> {
    private int total, page, limit;
}
