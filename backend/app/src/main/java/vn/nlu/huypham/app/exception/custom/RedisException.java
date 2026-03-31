package vn.nlu.huypham.app.exception.custom;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data
public class RedisException extends RuntimeException {
    int code;
    public RedisException(String msg, int code) {
        super(msg);
        this.code = code;
    }
}
