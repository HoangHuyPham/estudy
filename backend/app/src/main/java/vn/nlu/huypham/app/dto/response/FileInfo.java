package vn.nlu.huypham.app.dto.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo {
    UUID id;
    String uri;
    long size;
    long createdAt;
    boolean isProtected;
}
