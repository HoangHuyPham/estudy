package vn.nlu.huypham.app.entity;

import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.nlu.huypham.app.constant.VideoStatus;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "video", uniqueConstraints =
{ @UniqueConstraint(name = "uk_video_lecture_id", columnNames = "lecture_id"),
		@UniqueConstraint(name = "uk_video_resource_id", columnNames = "resource_id") })
public class Video
{
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Include
	UUID id;
	int duration;
	@Enumerated(EnumType.STRING)
	VideoStatus status;

	@OneToOne
	@JoinColumn(name = "lecture_id")
	Lecture lecture;

	@OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "resource_id")
	Resource resource;
}
