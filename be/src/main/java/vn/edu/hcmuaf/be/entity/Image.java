package vn.edu.hcmuaf.be.entity;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode.Include;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Image {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Include
    private UUID id;
    private String name;
    private String url;

    @OneToOne
    @JoinColumn(name="profileId", nullable = true)
    @MapsId
    private Profile profile;
}
