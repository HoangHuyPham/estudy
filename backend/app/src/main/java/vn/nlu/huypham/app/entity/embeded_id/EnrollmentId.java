package vn.nlu.huypham.app.entity.embeded_id;

import java.util.UUID;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentId {
    UUID userId;
    UUID courseId;
}
