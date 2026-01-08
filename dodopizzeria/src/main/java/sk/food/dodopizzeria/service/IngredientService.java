package sk.food.dodopizzeria.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.food.dodopizzeria.dto.IngredientDto;
import sk.food.dodopizzeria.entity.Ingredient;
import sk.food.dodopizzeria.exception.ResourceNotFoundException;
import sk.food.dodopizzeria.repository.IngredientRepository;

import java.util.List;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredient> findAll() {
        return ingredientRepository.findAll();
    }

    public Page<Ingredient> findAll(Pageable pageable) {
        return ingredientRepository.findAll(pageable);
    }

    public Ingredient findById(Long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingrediencia", id));
    }

    public List<Ingredient> findAvailableExtras() {
        return ingredientRepository.findAvailableExtras();
    }

    public Page<Ingredient> searchIngredients(String search, Pageable pageable) {
        if (search == null || search.isBlank()) {
            return ingredientRepository.findAll(pageable);
        }
        return ingredientRepository.findByNameSkContainingIgnoreCase(search, pageable);
    }

    @Transactional
    public Ingredient createIngredient(IngredientDto dto) {
        Ingredient ingredient = new Ingredient();
        updateIngredientFromDto(ingredient, dto);
        return ingredientRepository.save(ingredient);
    }

    @Transactional
    public Ingredient updateIngredient(Long id, IngredientDto dto) {
        Ingredient ingredient = findById(id);
        updateIngredientFromDto(ingredient, dto);
        return ingredientRepository.save(ingredient);
    }

    private void updateIngredientFromDto(Ingredient ingredient, IngredientDto dto) {
        ingredient.setNameSk(dto.getNameSk());
        ingredient.setAllergenInfoSk(dto.getAllergenInfoSk());
        ingredient.setVegan(dto.getVegan());
        ingredient.setSpicy(dto.getSpicy());
        ingredient.setExtraPriceEur(dto.getExtraPriceEur());
    }

    @Transactional
    public void deleteIngredient(Long id) {
        Ingredient ingredient = findById(id);
        if (!ingredient.getPizzas().isEmpty()) {
            throw new IllegalStateException("Nemožno vymazať ingredienciu, ktorá je použitá v pizzách");
        }
        ingredientRepository.delete(ingredient);
    }

    public IngredientDto toIngredientDto(Ingredient ingredient) {
        IngredientDto dto = new IngredientDto();
        dto.setId(ingredient.getId());
        dto.setNameSk(ingredient.getNameSk());
        dto.setAllergenInfoSk(ingredient.getAllergenInfoSk());
        dto.setVegan(ingredient.getVegan());
        dto.setSpicy(ingredient.getSpicy());
        dto.setExtraPriceEur(ingredient.getExtraPriceEur());
        return dto;
    }
}

