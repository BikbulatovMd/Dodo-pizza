package sk.food.dodopizzeria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import sk.food.dodopizzeria.dto.CartItemDto;
import sk.food.dodopizzeria.entity.Ingredient;
import sk.food.dodopizzeria.entity.Pizza;
import sk.food.dodopizzeria.entity.PizzaSize;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

@Service
@SessionScope
public class CartService implements Serializable {

    private final Map<String, CartItemDto> items = new LinkedHashMap<>();
    private final PizzaService pizzaService;
    private final IngredientService ingredientService;

    @Autowired
    public CartService(PizzaService pizzaService, IngredientService ingredientService) {
        this.pizzaService = pizzaService;
        this.ingredientService = ingredientService;
    }

    public void addItem(Long pizzaId, Long sizeId, Integer quantity, List<Long> extraIngredientIds) {
        Pizza pizza = pizzaService.findById(pizzaId);
        PizzaSize size = pizzaService.findSizeById(sizeId);

        String itemId = generateItemId(pizzaId, sizeId, extraIngredientIds);

        if (items.containsKey(itemId)) {
            // Increase quantity if same item exists
            CartItemDto existing = items.get(itemId);
            existing.setQuantity(existing.getQuantity() + quantity);
        } else {
            // Create new cart item
            CartItemDto item = new CartItemDto();
            item.setItemId(itemId);
            item.setPizzaId(pizzaId);
            item.setPizzaName(pizza.getNameSk());
            item.setPizzaImageUrl(pizza.getImageUrl());
            item.setSizeId(sizeId);
            item.setSizeCode(size.getSizeCode());
            item.setDiameterCm(size.getDiameterCm());
            item.setBasePrice(size.getPriceEur());
            item.setQuantity(quantity);

            // Add extras
            if (extraIngredientIds != null) {
                for (Long ingId : extraIngredientIds) {
                    Ingredient ing = ingredientService.findById(ingId);
                    CartItemDto.CartExtraDto extra = new CartItemDto.CartExtraDto();
                    extra.setIngredientId(ingId);
                    extra.setIngredientName(ing.getNameSk());
                    extra.setPrice(ing.getExtraPriceEur());
                    extra.setQuantity(1);
                    item.getExtras().add(extra);
                }
            }

            items.put(itemId, item);
        }
    }

    public void updateItemQuantity(String itemId, Integer quantity) {
        if (items.containsKey(itemId)) {
            if (quantity <= 0) {
                items.remove(itemId);
            } else {
                items.get(itemId).setQuantity(quantity);
            }
        }
    }

    public void removeItem(String itemId) {
        items.remove(itemId);
    }

    public void clear() {
        items.clear();
    }

    public List<CartItemDto> getItems() {
        return new ArrayList<>(items.values());
    }

    public int getItemCount() {
        return items.values().stream()
                .mapToInt(CartItemDto::getQuantity)
                .sum();
    }

    public BigDecimal getTotalPrice() {
        return items.values().stream()
                .map(CartItemDto::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    private String generateItemId(Long pizzaId, Long sizeId, List<Long> extraIds) {
        StringBuilder sb = new StringBuilder();
        sb.append(pizzaId).append("-").append(sizeId);
        if (extraIds != null && !extraIds.isEmpty()) {
            List<Long> sorted = new ArrayList<>(extraIds);
            Collections.sort(sorted);
            sb.append("-").append(sorted);
        }
        return sb.toString();
    }
}

