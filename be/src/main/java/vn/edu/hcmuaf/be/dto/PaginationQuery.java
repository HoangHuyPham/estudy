package vn.edu.hcmuaf.be.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Builder.Default;

@Builder
@Data
public class PaginationQuery {
    @Default
    private int page=1, limit=10;
}
