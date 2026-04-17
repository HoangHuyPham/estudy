package vn.nlu.huypham.app.entity;

import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Include
    UUID id;
    int duration;
    VideoStatus status;
    boolean isDraft;

    @OneToOne
    @JoinColumn(name = "lecture_id", foreignKey = @ForeignKey(name = "fk_video_lecture"))
    Lecture lecture;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "resource_id", foreignKey = @ForeignKey(name = "fk_video_resource"))
    Resource resource;
}
