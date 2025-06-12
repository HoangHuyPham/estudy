package vn.edu.hcmuaf.be.entity;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name="gift")
public class Gift {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Include
    private UUID id;
    private String message;
    @Default
    private long createdAt = Instant.now().toEpochMilli();

    @ManyToOne
    @JoinColumn(name = "senderId", foreignKey = @ForeignKey(name = "senderId"))
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiverId", foreignKey = @ForeignKey(name = "receiverId"))
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "courseId", foreignKey = @ForeignKey(name = "courseId"))
    private Course course;
}
