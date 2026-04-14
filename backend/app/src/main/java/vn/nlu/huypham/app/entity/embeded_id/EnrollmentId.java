package vn.nlu.huypham.app.entity.embeded_id;

import java.util.UUID;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentId {
    UUID userId;
    UUID courseId;
}
