package vn.nlu.huypham.app.entity;

import java.time.Instant;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.nlu.huypham.app.entity.embeded_id.EnrollmentId;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints =
{ @UniqueConstraint(name = "uk_enrollment_user_course", columnNames =
{ "user_id", "course_id" }) })
public class Enrollment
{
	@EmbeddedId
	EnrollmentId id;
	@ManyToOne
	@MapsId("userId")
	@JoinColumn(name = "user_id")
	User user;
	@ManyToOne
	@MapsId("courseId")
	@JoinColumn(name = "course_id")
	Course course;

	@Column(updatable = false)
	long createdAt;

	@PrePersist
	public void onCreate()
	{
		this.createdAt = Instant.now().getEpochSecond();
	}
}
