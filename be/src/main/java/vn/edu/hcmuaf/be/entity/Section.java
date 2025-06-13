package vn.edu.hcmuaf.be.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
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
@Table(name="section")
public class Section {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Include
    private UUID id;
    private String name;
    private int ordinal;

    @ManyToOne
    @JoinColumn(name = "courseId", foreignKey = @ForeignKey(name = "courseId"))
    private Course course;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
    @Default
    private Set<Lecture> lectures = new HashSet<>();
}
