package sk.food.dodopizzeria.dto;

import sk.food.dodopizzeria.entity.PizzaSize;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartItemDto {

    private String itemId;
    private Long pizzaId;
    private String pizzaName;
    private String pizzaImageUrl;
    private Long sizeId;
    private PizzaSize.SizeCode sizeCode;
    private Integer diameterCm;
    private BigDecimal basePrice;
    private Integer quantity;
    private List<CartExtraDto> extras = new ArrayList<>();

    public CartItemDto() {
    }

    public CartItemDto(String itemId, Long pizzaId, String pizzaName, String pizzaImageUrl,
                       Long sizeId, PizzaSize.SizeCode sizeCode, Integer diameterCm,
                       BigDecimal basePrice, Integer quantity, List<CartExtraDto> extras) {
        this.itemId = itemId;
        this.pizzaId = pizzaId;
        this.pizzaName = pizzaName;
        this.pizzaImageUrl = pizzaImageUrl;
        this.sizeId = sizeId;
        this.sizeCode = sizeCode;
        this.diameterCm = diameterCm;
        this.basePrice = basePrice;
        this.quantity = quantity;
        this.extras = extras;
    }

    public BigDecimal getTotalPrice() {
        BigDecimal extrasTotal = extras.stream()
                .map(e -> e.getPrice().multiply(BigDecimal.valueOf(e.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return basePrice.add(extrasTotal).multiply(BigDecimal.valueOf(quantity));
    }

    // Getters and Setters
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Long getPizzaId() {
        return pizzaId;
    }

    public void setPizzaId(Long pizzaId) {
        this.pizzaId = pizzaId;
    }

    public String getPizzaName() {
        return pizzaName;
    }

    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
    }

    public String getPizzaImageUrl() {
        return pizzaImageUrl;
    }

    public void setPizzaImageUrl(String pizzaImageUrl) {
        this.pizzaImageUrl = pizzaImageUrl;
    }

    public Long getSizeId() {
        return sizeId;
    }

    public void setSizeId(Long sizeId) {
        this.sizeId = sizeId;
    }

    public PizzaSize.SizeCode getSizeCode() {
        return sizeCode;
    }

    public void setSizeCode(PizzaSize.SizeCode sizeCode) {
        this.sizeCode = sizeCode;
    }

    public Integer getDiameterCm() {
        return diameterCm;
    }

    public void setDiameterCm(Integer diameterCm) {
        this.diameterCm = diameterCm;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public List<CartExtraDto> getExtras() {
        return extras;
    }

    public void setExtras(List<CartExtraDto> extras) {
        this.extras = extras;
    }

    public static class CartExtraDto {
        private Long ingredientId;
        private String ingredientName;
        private BigDecimal price;
        private Integer quantity;

        public CartExtraDto() {
        }

        public CartExtraDto(Long ingredientId, String ingredientName, BigDecimal price, Integer quantity) {
            this.ingredientId = ingredientId;
            this.ingredientName = ingredientName;
            this.price = price;
            this.quantity = quantity;
        }

        public Long getIngredientId() {
            return ingredientId;
        }

        public void setIngredientId(Long ingredientId) {
            this.ingredientId = ingredientId;
        }

        public String getIngredientName() {
            return ingredientName;
        }

        public void setIngredientName(String ingredientName) {
            this.ingredientName = ingredientName;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }
}
