package vn.edu.hcmuaf.be.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import vn.edu.hcmuaf.be.dto.ApiResponse;
import vn.edu.hcmuaf.be.dto.cart.AddCartItemDTO;
import vn.edu.hcmuaf.be.dto.cart.CartDTO;
import vn.edu.hcmuaf.be.dto.cart.PatchCartItemDTO;
import vn.edu.hcmuaf.be.entity.Cart;
import vn.edu.hcmuaf.be.entity.CartItem;
import vn.edu.hcmuaf.be.entity.Course;
import vn.edu.hcmuaf.be.entity.User;
import vn.edu.hcmuaf.be.entity.CartItem.CartItemId;
import vn.edu.hcmuaf.be.repository.CartItemRepository;
import vn.edu.hcmuaf.be.repository.CartRepository;
import vn.edu.hcmuaf.be.repository.CourseRepository;

@RestController
@RequestMapping("/api/self-cart/")
public class SelfCartController {
        @Autowired
        private CartRepository repoCart;

        @Autowired
        private CourseRepository repoCourse;

        @Autowired
        private CartItemRepository repoCartItem;

        @Autowired
        private ModelMapper modelMapper;

        @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<ApiResponse<?>> getSelf() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User user = (User) authentication.getPrincipal();
                Cart existCart = repoCart.findById(user.getId()).orElse(null);

                if (existCart == null) {
                        return new ResponseEntity<>(
                                        ApiResponse.builder()
                                                        .data(null)
                                                        .message("success")
                                                        .build(),
                                        HttpStatus.NO_CONTENT);
                }

                CartDTO cartDTO = modelMapper.map(existCart, CartDTO.class);

                return new ResponseEntity<>(
                                ApiResponse.builder()
                                                .data(cartDTO)
                                                .message("success")
                                                .build(),
                                HttpStatus.OK);
        }

        @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
        @Transactional
        public ResponseEntity<ApiResponse<?>> addCartItem(@RequestBody AddCartItemDTO dto) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User user = (User) authentication.getPrincipal();
                Cart existCart = repoCart.findById(user.getId()).orElse(null);
                Course existCourse = repoCourse.findById(dto.getCourseId()).orElse(null);

                if (existCart == null) {
                        return new ResponseEntity<>(
                                        ApiResponse.builder()
                                                        .data(null)
                                                        .message("not found cart")
                                                        .build(),
                                        HttpStatus.NOT_FOUND);
                }

                if (existCourse == null) {
                        return new ResponseEntity<>(
                                        ApiResponse.builder()
                                                        .data(null)
                                                        .message("not found course")
                                                        .build(),
                                        HttpStatus.NOT_FOUND);
                }

                CartItem newCartItem = CartItem.builder()
                                .cart(existCart)
                                .course(existCourse)
                                .id(new CartItem.CartItemId(existCart.getId(), existCourse.getId()))
                                .isSelected(true)
                                .build();

                if (repoCartItem.existsById(newCartItem.getId())) {
                        return new ResponseEntity<>(
                                        ApiResponse.builder()
                                                        .data(null)
                                                        .message("course already exists in cart")
                                                        .build(),
                                        HttpStatus.CONFLICT);
                }

                existCart.addCartItem(newCartItem);
                existCart = repoCart.save(existCart);

                CartDTO cartDTO = modelMapper.map(existCart, CartDTO.class);

                return new ResponseEntity<>(
                                ApiResponse.builder()
                                                .data(cartDTO)
                                                .message("success")
                                                .build(),
                                HttpStatus.OK);

        }

        @DeleteMapping(value = "{courseId}", produces = MediaType.APPLICATION_JSON_VALUE)
        @Transactional
        public ResponseEntity<ApiResponse<?>> deleteCartItem(@PathVariable UUID courseId) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User user = (User) authentication.getPrincipal();
                UUID cartId = user.getId();

                CartItemId id = new CartItem.CartItemId(cartId, courseId);

                if (!repoCartItem.existsById(id)) {
                        return new ResponseEntity<>(
                                        ApiResponse.builder()
                                                        .data(null)
                                                        .message("cart item not found")
                                                        .build(),
                                        HttpStatus.NOT_FOUND);
                }

                repoCartItem.deleteById(id);

                return new ResponseEntity<>(
                                ApiResponse.builder()
                                                .data(null)
                                                .message("success")
                                                .build(),
                                HttpStatus.OK);
        }

        @PatchMapping(value = "/{courseId}", produces = MediaType.APPLICATION_JSON_VALUE)
        @Transactional
        public ResponseEntity<ApiResponse<?>> updateIsSelected(
                        @PathVariable UUID courseId,
                        @RequestBody PatchCartItemDTO dto) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User user = (User) authentication.getPrincipal();

                Cart cart = repoCart.findById(user.getId()).orElse(null);
                if (cart == null) {
                        return new ResponseEntity<>(
                                        ApiResponse.builder().data(null).message("Cart not found").build(),
                                        HttpStatus.NOT_FOUND);
                }

                CartItemId cartItemId = new CartItem.CartItemId(user.getId(), courseId);
                CartItem cartItem = repoCartItem.findById(cartItemId).orElse(null);
                if (cartItem == null) {
                        return new ResponseEntity<>(
                                        ApiResponse.builder().data(null).message("cart item not found").build(),
                                        HttpStatus.NOT_FOUND);
                }

                
                cartItem.setSelected(dto.isSelected());
                cart = repoCart.save(cart);

                CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
                return new ResponseEntity<>(
                                ApiResponse.builder().data(cartDTO).message("success").build(),
                                HttpStatus.OK);
        }
}
