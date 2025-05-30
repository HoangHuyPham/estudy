package vn.edu.hcmuaf.be.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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
public class Role {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Include
    private UUID id;
    private String name;
    private String description;

    @OneToMany(mappedBy="role")
    @Default
    private Set<User> users = new HashSet<>();
}
