package vn.nlu.huypham.app.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ApiResponsePageable<T> extends ApiResponse<T>
{
	int page;
	int limit;
	long totalItems;
	int totalPages;
}
