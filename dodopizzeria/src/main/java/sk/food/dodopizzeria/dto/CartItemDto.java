package sk.food.dodopizzeria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sk.food.dodopizzeria.entity.PizzaSize;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {

    private String itemId; // Unique identifier for cart item
    private Long pizzaId;
    private String pizzaName;
    private String pizzaImageUrl;
    private Long sizeId;
    private PizzaSize.SizeCode sizeCode;
    private Integer diameterCm;
    private BigDecimal basePrice;
    private Integer quantity;
    private List<CartExtraDto> extras = new ArrayList<>();

    public BigDecimal getTotalPrice() {
        BigDecimal extrasTotal = extras.stream()
                .map(e -> e.getPrice().multiply(BigDecimal.valueOf(e.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return basePrice.add(extrasTotal).multiply(BigDecimal.valueOf(quantity));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartExtraDto {
        private Long ingredientId;
        private String ingredientName;
        private BigDecimal price;
        private Integer quantity;
    }
}

