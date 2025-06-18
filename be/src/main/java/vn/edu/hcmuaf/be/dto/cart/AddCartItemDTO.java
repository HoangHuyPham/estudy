package vn.edu.hcmuaf.be.dto.cart;

import java.util.UUID;

import lombok.Data;

@Data
public class AddCartItemDTO {
    private UUID courseId;
    private boolean isSelected;
}
