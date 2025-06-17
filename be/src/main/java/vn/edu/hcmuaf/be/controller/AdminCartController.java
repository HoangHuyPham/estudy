package vn.edu.hcmuaf.be.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.be.entity.Cart;
import vn.edu.hcmuaf.be.repository.CartRepository;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/carts")
public class AdminCartController {

    @Autowired
    private CartRepository cartRepository;

    @GetMapping
    public ResponseEntity<Page<Cart>> getAllCarts(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(cartRepository.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<Cart> createCart(@RequestBody Cart cart) {
        return ResponseEntity.ok(cartRepository.save(cart));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cart> updateCart(@PathVariable UUID id, @RequestBody Cart cart) {
        Optional<Cart> optional = cartRepository.findById(id);
        if (optional.isEmpty()) return ResponseEntity.notFound().build();

        Cart existing = optional.get();
        existing.setUser(cart.getUser());
        existing.setCartItems(cart.getCartItems());
        return ResponseEntity.ok(cartRepository.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable UUID id) {
        if (!cartRepository.existsById(id)) return ResponseEntity.notFound().build();
        cartRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}