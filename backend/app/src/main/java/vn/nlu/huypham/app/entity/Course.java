package vn.nlu.huypham.app.entity;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;
import lombok.experimental.FieldDefaults;
import vn.nlu.huypham.app.constant.CourseStatus;
import vn.nlu.huypham.app.constant.CourseVisibilities;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Course
{
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Include
	UUID id;
	String name;
	String description;
	String thumbnail;
	@Default
	@Enumerated(EnumType.STRING)
	CourseStatus status = CourseStatus.DRAFT;
	@Default
	@Enumerated(EnumType.STRING)
	CourseVisibilities visibility = CourseVisibilities.PRIVATE;
	int duration;
	int enrollmentCount;
	int lectureCount;
	float oldPrice;
	float price;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id", foreignKey = @ForeignKey(name = "fk_course_user"))
	User owner;

	@OneToMany(mappedBy = "course", orphanRemoval = true, cascade = CascadeType.ALL)
	Set<Section> sections;

	@OneToMany(mappedBy = "course")
	Set<Resource> resources;

	@Column(updatable = false)
	long createdAt;
	long updatedAt;

	@PrePersist
	public void prePersist()
	{
		this.createdAt = Instant.now().getEpochSecond();
	}

	@PreUpdate
	public void preUpdate()
	{
		this.updatedAt = Instant.now().getEpochSecond();
	}
}
