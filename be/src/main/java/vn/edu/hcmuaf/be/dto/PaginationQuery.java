package vn.edu.hcmuaf.be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaginationQuery {
    private int page;
    private int limit;
}
