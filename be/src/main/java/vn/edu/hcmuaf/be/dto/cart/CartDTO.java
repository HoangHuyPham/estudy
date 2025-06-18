package vn.edu.hcmuaf.be.dto.cart;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Data;
import vn.edu.hcmuaf.be.dto.course.CourseDTO;
import vn.edu.hcmuaf.be.entity.CartItem.CartItemId;

@Data
public class CartDTO {
    private UUID id;
    private List<CartItemDTO> cartItems = new ArrayList<>();

    @Data
    public static class CartItemDTO {
        private CartItemId id;
        private boolean isSelected;
        private CourseDTO course;
    }
}
