package vn.edu.hcmuaf.be.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
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
@Table(name="instructor")
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
    private List<Course> uploadCourses = new ArrayList<>();

    public void addCourse(Course course){
        if (!uploadCourses.contains(course)){
            uploadCourses.add(course);
            course.setInstructor(this);
        }
    }
}
