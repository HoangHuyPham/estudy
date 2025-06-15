package vn.edu.hcmuaf.be.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name="owned-course")
public class OwnedCourse {
    @Id
    @Include
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "courseId", foreignKey = @ForeignKey(name = "courseId"))
    private Course course;

    @ManyToOne
    @JoinColumn(name = "customerId", foreignKey = @ForeignKey(name = "customerId"))
    private Customer customer;
}
