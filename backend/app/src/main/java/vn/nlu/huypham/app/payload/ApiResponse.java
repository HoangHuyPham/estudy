package vn.nlu.huypham.app.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {
    int code;
    String message;
    T data;
}
