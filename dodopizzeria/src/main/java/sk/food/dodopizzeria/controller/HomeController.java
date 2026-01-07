package sk.food.dodopizzeria.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import sk.food.dodopizzeria.entity.Pizza;
import sk.food.dodopizzeria.service.IngredientService;
import sk.food.dodopizzeria.service.PizzaService;
import sk.food.dodopizzeria.service.TagService;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PizzaService pizzaService;
    private final TagService tagService;
    private final IngredientService ingredientService;

    @GetMapping("/")
    public String home(Model model) {
        // Show featured pizzas on homepage
        model.addAttribute("featuredPizzas", pizzaService.findAllActive()
                .stream().limit(6).toList());
        model.addAttribute("tags", tagService.findAll());
        return "home";
    }

    @GetMapping("/menu")
    public String menu(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String tag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Pizza> pizzas = pizzaService.searchPizzas(search, tag, pageable);

        model.addAttribute("pizzas", pizzas);
        model.addAttribute("tags", tagService.findAll());
        model.addAttribute("currentSearch", search);
        model.addAttribute("currentTag", tag);
        return "menu/index";
    }

    @GetMapping("/pizza/{slug}")
    public String pizzaDetail(@PathVariable String slug, Model model) {
        Pizza pizza = pizzaService.findBySlug(slug);
        model.addAttribute("pizza", pizza);
        model.addAttribute("availableSizes", pizzaService.getAvailablePizzaSizes(pizza.getId()));
        model.addAttribute("availableExtras", ingredientService.findAvailableExtras());
        return "menu/detail";
    }
}

