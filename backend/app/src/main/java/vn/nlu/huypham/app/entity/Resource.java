package vn.nlu.huypham.app.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.nlu.huypham.app.constant.ResourceTypes;
import vn.nlu.huypham.app.constant.ResourceVisibilities;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Resource
{
	@Id
	@Include
	UUID id;
	long size;
	@Enumerated(EnumType.STRING)
	ResourceVisibilities visibility;
	String xAccelRedirect;
	String diskPath;
	@Enumerated(EnumType.STRING)
	ResourceTypes type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_resource_user"))
	User owner;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", foreignKey = @ForeignKey(name = "fk_resource_course"))
	Course course;

	@Column(updatable = false)
	long createdAt;

	@PrePersist
	public void onCreate()
	{
		this.createdAt = Instant.now().getEpochSecond();
	}
}
