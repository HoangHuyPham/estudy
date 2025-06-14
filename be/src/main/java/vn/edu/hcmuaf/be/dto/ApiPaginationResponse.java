package vn.edu.hcmuaf.be.dto;

import lombok.experimental.SuperBuilder;


@SuperBuilder
public class ApiPaginationResponse<T> extends ApiResponse<T> {
    private int total, page, limit;
}
