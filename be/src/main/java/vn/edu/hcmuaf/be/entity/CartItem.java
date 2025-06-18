package vn.edu.hcmuaf.be.entity;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder.Default;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "cart_item")
public class CartItem {
    @EmbeddedId
    @Include
    @Default
    private CartItemId id = CartItemId.builder().build();
    private boolean isSelected;

    @ManyToOne
    @JoinColumn(name = "cartId", foreignKey = @ForeignKey(name = "cartId"))
    @MapsId("cartId")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "courseId", foreignKey = @ForeignKey(name = "courseId"))
    @MapsId("courseId")
    private Course course;

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    @EqualsAndHashCode
    public static class CartItemId implements Serializable {
        private UUID cartId;
        private UUID courseId;
    }
}
