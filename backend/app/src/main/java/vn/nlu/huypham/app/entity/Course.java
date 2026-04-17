package vn.nlu.huypham.app.entity;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
import lombok.experimental.FieldDefaults;
import vn.nlu.huypham.app.constant.CourseVisibilities;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Include
    UUID id;
    String name;
    String description;
    String thumbnail;
    CourseVisibilities visibility;
    int duration;
    int studentCount;
    int lectureCount;
    float oldPrice;
    float price;

    @OneToMany(mappedBy = "course", orphanRemoval = true, cascade = CascadeType.ALL)
    Set<Section> sections;

    @Column(updatable = false)
    long createdAt;
    long updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now().getEpochSecond();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now().getEpochSecond();
    }
}
