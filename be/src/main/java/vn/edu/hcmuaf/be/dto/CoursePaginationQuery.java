package vn.edu.hcmuaf.be.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class CoursePaginationQuery extends PaginationQuery {
    private String keyword;
    private Double minPrice;
    private Double maxPrice;
}
