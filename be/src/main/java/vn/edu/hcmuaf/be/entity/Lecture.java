package vn.edu.hcmuaf.be.entity;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name="lecture")
public class Lecture {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Include
    private UUID id;
    private String title;
    private String description;
    private int ordinal;

    @ManyToOne
    @JoinColumn(name = "sectionId", foreignKey = @ForeignKey(name = "sectionId"))
    private Section section;

    @OneToOne(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval=true)
    private Video video;
}
