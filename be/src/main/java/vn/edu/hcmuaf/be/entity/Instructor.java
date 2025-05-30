package vn.edu.hcmuaf.be.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode.Include;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Instructor {
    @Id
    @Include
    private UUID id;
    private String description;

    @OneToOne
    @JoinColumn(name="userId", nullable = true)
    @MapsId
    private User user;

    @Default
    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Course> uploadCourses = new HashSet<>();
}
