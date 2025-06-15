package vn.edu.hcmuaf.be.entity;

import java.util.ArrayList;
import java.util.List;
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
@Table(name = "section")
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

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    @Default
    private List<Lecture> lectures = new ArrayList<>();

    public void addLecture(Lecture lecture) {
        lectures.add(lecture);
        lecture.setSection(this);
    }
}
