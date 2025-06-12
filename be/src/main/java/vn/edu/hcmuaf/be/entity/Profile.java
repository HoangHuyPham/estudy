package vn.edu.hcmuaf.be.entity;

import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
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
@Table(name="profile")
public class Profile {
    @Id
    @Include
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    @OneToOne
    @JoinColumn(name="userId")
    @MapsId
    private User user;

    @OneToOne(mappedBy="profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private Image avatar;
}