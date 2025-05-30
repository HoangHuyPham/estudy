package vn.edu.hcmuaf.be.entity;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
public class User {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Include
    private UUID id;
    private String username;
    private String password;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="roleId", foreignKey = @ForeignKey(name="roleId"))
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval=true)
    private Profile profile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval=true)
    private Cart cart;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval=true)
    private WishList wishList;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval=true)
    private Instructor instructor;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval=true)
    private Customer customer;

    public void addRole(Role role){
        this.role = role;
        if (!role.getUsers().contains(this)){
            role.getUsers().add(this);
        }
    }

    public void addProfile(Profile profile){
        this.profile = profile;
        profile.setUser(this);
    }
}