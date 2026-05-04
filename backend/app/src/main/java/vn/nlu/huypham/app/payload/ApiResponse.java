package vn.nlu.huypham.app.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
public class ApiResponse<T>
{
	int code;
	String message;
	T data;
}
