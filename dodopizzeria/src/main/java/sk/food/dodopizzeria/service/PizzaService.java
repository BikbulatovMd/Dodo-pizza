package sk.food.dodopizzeria.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.food.dodopizzeria.dto.PizzaDto;
import sk.food.dodopizzeria.dto.PizzaSizeDto;
import sk.food.dodopizzeria.entity.Ingredient;
import sk.food.dodopizzeria.entity.Pizza;
import sk.food.dodopizzeria.entity.PizzaSize;
import sk.food.dodopizzeria.entity.Tag;
import sk.food.dodopizzeria.exception.ResourceNotFoundException;
import sk.food.dodopizzeria.repository.IngredientRepository;
import sk.food.dodopizzeria.repository.PizzaRepository;
import sk.food.dodopizzeria.repository.PizzaSizeRepository;
import sk.food.dodopizzeria.repository.TagRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PizzaService {

    private final PizzaRepository pizzaRepository;
    private final PizzaSizeRepository pizzaSizeRepository;
    private final IngredientRepository ingredientRepository;
    private final TagRepository tagRepository;

    public Page<Pizza> findAllActive(Pageable pageable) {
        return pizzaRepository.findByActiveTrue(pageable);
    }

    public List<Pizza> findAllActive() {
        return pizzaRepository.findByActiveTrue();
    }

    public Page<Pizza> findAll(Pageable pageable) {
        return pizzaRepository.findAll(pageable);
    }

    public Pizza findById(Long id) {
        return pizzaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pizza", id));
    }

    public Pizza findBySlug(String slug) {
        return pizzaRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Pizza", "slug", slug));
    }

    public Page<Pizza> searchPizzas(String search, String tagSlug, Pageable pageable) {
        return pizzaRepository.searchWithFilters(search, tagSlug, pageable);
    }

    public List<Pizza> searchByName(String name) {
        return pizzaRepository.findByNameSkContainingIgnoreCaseAndActiveTrue(name);
    }

    public Page<Pizza> findByTag(String tagSlug, Pageable pageable) {
        return pizzaRepository.findByTagSlug(tagSlug, pageable);
    }

    @Transactional
    public Pizza createPizza(PizzaDto dto) {
        if (pizzaRepository.existsBySlug(dto.getSlug())) {
            throw new IllegalArgumentException("Pizza so slugom '" + dto.getSlug() + "' už existuje");
        }

        Pizza pizza = new Pizza();
        updatePizzaFromDto(pizza, dto);
        return pizzaRepository.save(pizza);
    }

    @Transactional
    public Pizza updatePizza(Long id, PizzaDto dto) {
        Pizza pizza = findById(id);

        // Check slug uniqueness if changed
        if (!pizza.getSlug().equals(dto.getSlug()) && pizzaRepository.existsBySlug(dto.getSlug())) {
            throw new IllegalArgumentException("Pizza so slugom '" + dto.getSlug() + "' už existuje");
        }

        updatePizzaFromDto(pizza, dto);
        return pizzaRepository.save(pizza);
    }

    private void updatePizzaFromDto(Pizza pizza, PizzaDto dto) {
        pizza.setNameSk(dto.getNameSk());
        pizza.setDescriptionSk(dto.getDescriptionSk());
        pizza.setSlug(dto.getSlug());
        pizza.setImageUrl(dto.getImageUrl());
        pizza.setActive(dto.getActive());

        // Update ingredients
        Set<Ingredient> ingredients = new HashSet<>();
        if (dto.getIngredientIds() != null) {
            for (Long ingId : dto.getIngredientIds()) {
                ingredients.add(ingredientRepository.findById(ingId)
                        .orElseThrow(() -> new ResourceNotFoundException("Ingrediencia", ingId)));
            }
        }
        pizza.setIngredients(ingredients);

        // Update tags
        Set<Tag> tags = new HashSet<>();
        if (dto.getTagIds() != null) {
            for (Long tagId : dto.getTagIds()) {
                tags.add(tagRepository.findById(tagId)
                        .orElseThrow(() -> new ResourceNotFoundException("Tag", tagId)));
            }
        }
        pizza.setTags(tags);
    }

    @Transactional
    public void updatePizzaImage(Long pizzaId, String imageUrl) {
        Pizza pizza = findById(pizzaId);
        pizza.setImageUrl(imageUrl);
        pizzaRepository.save(pizza);
    }

    @Transactional
    public void deletePizza(Long id) {
        Pizza pizza = findById(id);
        pizzaRepository.delete(pizza);
    }

    @Transactional
    public void togglePizzaActive(Long id) {
        Pizza pizza = findById(id);
        pizza.setActive(!pizza.getActive());
        pizzaRepository.save(pizza);
    }

    // Pizza Sizes
    public List<PizzaSize> getPizzaSizes(Long pizzaId) {
        return pizzaSizeRepository.findByPizzaId(pizzaId);
    }

    public List<PizzaSize> getAvailablePizzaSizes(Long pizzaId) {
        return pizzaSizeRepository.findByPizzaIdAndAvailableTrue(pizzaId);
    }

    public PizzaSize findSizeById(Long sizeId) {
        return pizzaSizeRepository.findById(sizeId)
                .orElseThrow(() -> new ResourceNotFoundException("Veľkosť pizze", sizeId));
    }

    @Transactional
    public PizzaSize createPizzaSize(Long pizzaId, PizzaSizeDto dto) {
        Pizza pizza = findById(pizzaId);

        PizzaSize size = new PizzaSize();
        size.setPizza(pizza);
        size.setSizeCode(dto.getSizeCode());
        size.setDiameterCm(dto.getDiameterCm());
        size.setPriceEur(dto.getPriceEur());
        size.setAvailable(dto.getAvailable());

        return pizzaSizeRepository.save(size);
    }

    @Transactional
    public PizzaSize updatePizzaSize(Long sizeId, PizzaSizeDto dto) {
        PizzaSize size = findSizeById(sizeId);
        size.setSizeCode(dto.getSizeCode());
        size.setDiameterCm(dto.getDiameterCm());
        size.setPriceEur(dto.getPriceEur());
        size.setAvailable(dto.getAvailable());
        return pizzaSizeRepository.save(size);
    }

    @Transactional
    public void deletePizzaSize(Long sizeId) {
        PizzaSize size = findSizeById(sizeId);
        pizzaSizeRepository.delete(size);
    }

    public PizzaDto toPizzaDto(Pizza pizza) {
        PizzaDto dto = new PizzaDto();
        dto.setId(pizza.getId());
        dto.setNameSk(pizza.getNameSk());
        dto.setDescriptionSk(pizza.getDescriptionSk());
        dto.setSlug(pizza.getSlug());
        dto.setImageUrl(pizza.getImageUrl());
        dto.setActive(pizza.getActive());

        Set<Long> ingredientIds = new HashSet<>();
        pizza.getIngredients().forEach(i -> ingredientIds.add(i.getId()));
        dto.setIngredientIds(ingredientIds);

        Set<Long> tagIds = new HashSet<>();
        pizza.getTags().forEach(t -> tagIds.add(t.getId()));
        dto.setTagIds(tagIds);

        return dto;
    }
}

