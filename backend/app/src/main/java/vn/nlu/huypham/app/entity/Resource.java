package vn.nlu.huypham.app.entity;

import java.time.Instant;
import java.util.UUID;

import org.springframework.core.io.ResourceEditor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Resource {
    @Id
    @Include
    UUID id;
    long size;
    ResourceVisibilities visibility;
    String xAccelRedirect;
    String diskPath;
    ResourceTypes type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    Course course;

    @Column(updatable = false)
    long createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now().getEpochSecond();
    }
}
