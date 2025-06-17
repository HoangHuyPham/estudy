package vn.edu.hcmuaf.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmuaf.be.entity.Cart;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
}
