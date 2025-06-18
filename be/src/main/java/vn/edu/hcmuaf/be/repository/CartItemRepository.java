package vn.edu.hcmuaf.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.edu.hcmuaf.be.entity.CartItem;
import vn.edu.hcmuaf.be.entity.CartItem.CartItemId;

public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {}
