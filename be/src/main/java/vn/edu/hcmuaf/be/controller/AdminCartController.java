package vn.edu.hcmuaf.be.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.be.dto.ApiPaginationResponse;
import vn.edu.hcmuaf.be.dto.PaginationQuery;
import vn.edu.hcmuaf.be.dto.admin.CartAdminDTO;
import vn.edu.hcmuaf.be.dto.course.CourseDTO;
import vn.edu.hcmuaf.be.entity.Cart;
import vn.edu.hcmuaf.be.entity.Course;
import vn.edu.hcmuaf.be.repository.CartRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/carts")
public class AdminCartController {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiPaginationResponse<?>> getAllCarts(PaginationQuery query) {
        Pageable pageable = PageRequest.of(query.getPage(), query.getLimit());
        Page<Cart> pageResult = cartRepository.findAll(pageable);
        List<CartAdminDTO> result = pageResult.getContent().stream().map(e->modelMapper.map(e, CartAdminDTO.class)).toList();
        return new ResponseEntity<>(
                ApiPaginationResponse.builder()
                        .limit(query.getLimit())
                        .page(query.getPage())
                        .total((int) pageResult.getTotalElements())
                        .data(result)
                        .message("success")
                        .build(),
                HttpStatus.OK);
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