package vn.edu.hcmuaf.be.entity;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name="video")
public class Video {
    @Id
    @Include
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    private String name;
    private String url;
    private long duration;

    @OneToOne
    @JoinColumn(name="lectureId", foreignKey = @ForeignKey(name="lectureId"))
    private Lecture lecture;
}
