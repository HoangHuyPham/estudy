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
public class Course {
    @Id
    @Include
    private UUID id;
    private String name;
    private String description;
    private String language;
    private long duration;

    @ManyToOne
    @JoinColumn(name="instructorId", foreignKey = @ForeignKey(name="instructorId"))
    private Instructor instructor;

    @ManyToOne
    @JoinColumn(name="customerId", foreignKey = @ForeignKey(name="customerId"))
    private Customer customer;
}
