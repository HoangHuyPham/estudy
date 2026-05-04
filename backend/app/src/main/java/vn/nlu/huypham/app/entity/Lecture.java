package vn.nlu.huypham.app.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Lecture
{
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Include
	UUID id;
	float weight;
	String name;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "lecture", orphanRemoval = true, cascade = CascadeType.ALL)
	Video video;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "section_id", foreignKey = @ForeignKey(name = "fk_lecture_section"))
	Section section;

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
