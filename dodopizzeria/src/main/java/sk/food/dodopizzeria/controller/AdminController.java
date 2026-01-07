package sk.food.dodopizzeria.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sk.food.dodopizzeria.dto.IngredientDto;
import sk.food.dodopizzeria.dto.PizzaDto;
import sk.food.dodopizzeria.dto.PizzaSizeDto;
import sk.food.dodopizzeria.entity.*;
import sk.food.dodopizzeria.service.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final PizzaService pizzaService;
    private final IngredientService ingredientService;
    private final TagService tagService;
    private final UserService userService;
    private final OrderService orderService;
    private final FileStorageService fileStorageService;

    // Dashboard
    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("pendingOrders", orderService.countByStatus(OrderStatus.PENDING));
        model.addAttribute("preparingOrders", orderService.countByStatus(OrderStatus.PREPARING));
        model.addAttribute("deliveringOrders", orderService.countByStatus(OrderStatus.DELIVERING));
        model.addAttribute("recentOrders", orderService.searchOrders(null, PageRequest.of(0, 5)));
        return "admin/dashboard";
    }

    // ==================== PIZZAS ====================
    @GetMapping("/pizzas")
    public String listPizzas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Page<Pizza> pizzas = pizzaService.findAll(PageRequest.of(page, size));
        model.addAttribute("pizzas", pizzas);
        return "admin/pizzas/list";
    }

    @GetMapping("/pizzas/new")
    public String newPizzaForm(Model model) {
        model.addAttribute("pizza", new PizzaDto());
        model.addAttribute("ingredients", ingredientService.findAll());
        model.addAttribute("tags", tagService.findAll());
        return "admin/pizzas/form";
    }

    @PostMapping("/pizzas/new")
    public String createPizza(
            @Valid @ModelAttribute("pizza") PizzaDto dto,
            BindingResult result,
            @RequestParam(required = false) MultipartFile image,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("ingredients", ingredientService.findAll());
            model.addAttribute("tags", tagService.findAll());
            return "admin/pizzas/form";
        }

        try {
            // Upload image if provided
            if (image != null && !image.isEmpty()) {
                String imageUrl = fileStorageService.storeFile(image, "pizzas");
                dto.setImageUrl(imageUrl);
            }

            Pizza pizza = pizzaService.createPizza(dto);
            redirectAttributes.addFlashAttribute("success", "Pizza bola vytvorená");
            return "redirect:/admin/pizzas/" + pizza.getId() + "/sizes";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("ingredients", ingredientService.findAll());
            model.addAttribute("tags", tagService.findAll());
            return "admin/pizzas/form";
        }
    }

    @GetMapping("/pizzas/{id}/edit")
    public String editPizzaForm(@PathVariable Long id, Model model) {
        Pizza pizza = pizzaService.findById(id);
        model.addAttribute("pizza", pizzaService.toPizzaDto(pizza));
        model.addAttribute("ingredients", ingredientService.findAll());
        model.addAttribute("tags", tagService.findAll());
        return "admin/pizzas/form";
    }

    @PostMapping("/pizzas/{id}/edit")
    public String updatePizza(
            @PathVariable Long id,
            @Valid @ModelAttribute("pizza") PizzaDto dto,
            BindingResult result,
            @RequestParam(required = false) MultipartFile image,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("ingredients", ingredientService.findAll());
            model.addAttribute("tags", tagService.findAll());
            return "admin/pizzas/form";
        }

        try {
            // Upload new image if provided
            if (image != null && !image.isEmpty()) {
                String imageUrl = fileStorageService.storeFile(image, "pizzas");
                dto.setImageUrl(imageUrl);
            }

            pizzaService.updatePizza(id, dto);
            redirectAttributes.addFlashAttribute("success", "Pizza bola aktualizovaná");
            return "redirect:/admin/pizzas";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("ingredients", ingredientService.findAll());
            model.addAttribute("tags", tagService.findAll());
            return "admin/pizzas/form";
        }
    }

    @PostMapping("/pizzas/{id}/toggle")
    public String togglePizzaActive(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        pizzaService.togglePizzaActive(id);
        redirectAttributes.addFlashAttribute("success", "Stav pizze bol zmenený");
        return "redirect:/admin/pizzas";
    }

    @PostMapping("/pizzas/{id}/delete")
    public String deletePizza(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            pizzaService.deletePizza(id);
            redirectAttributes.addFlashAttribute("success", "Pizza bola vymazaná");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/pizzas";
    }

    // Pizza Sizes
    @GetMapping("/pizzas/{id}/sizes")
    public String pizzaSizes(@PathVariable Long id, Model model) {
        Pizza pizza = pizzaService.findById(id);
        model.addAttribute("pizza", pizza);
        model.addAttribute("sizes", pizzaService.getPizzaSizes(id));
        model.addAttribute("newSize", new PizzaSizeDto());
        return "admin/pizzas/sizes";
    }

    @PostMapping("/pizzas/{pizzaId}/sizes")
    public String addPizzaSize(
            @PathVariable Long pizzaId,
            @Valid @ModelAttribute("newSize") PizzaSizeDto dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            Pizza pizza = pizzaService.findById(pizzaId);
            model.addAttribute("pizza", pizza);
            model.addAttribute("sizes", pizzaService.getPizzaSizes(pizzaId));
            return "admin/pizzas/sizes";
        }

        pizzaService.createPizzaSize(pizzaId, dto);
        redirectAttributes.addFlashAttribute("success", "Veľkosť bola pridaná");
        return "redirect:/admin/pizzas/" + pizzaId + "/sizes";
    }

    @PostMapping("/pizzas/sizes/{sizeId}/delete")
    public String deletePizzaSize(
            @PathVariable Long sizeId,
            @RequestParam Long pizzaId,
            RedirectAttributes redirectAttributes) {
        pizzaService.deletePizzaSize(sizeId);
        redirectAttributes.addFlashAttribute("success", "Veľkosť bola vymazaná");
        return "redirect:/admin/pizzas/" + pizzaId + "/sizes";
    }

    // ==================== INGREDIENTS ====================
    @GetMapping("/ingredients")
    public String listIngredients(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Page<Ingredient> ingredients = ingredientService.searchIngredients(search, PageRequest.of(page, size));
        model.addAttribute("ingredients", ingredients);
        model.addAttribute("search", search);
        return "admin/ingredients/list";
    }

    @GetMapping("/ingredients/new")
    public String newIngredientForm(Model model) {
        model.addAttribute("ingredient", new IngredientDto());
        return "admin/ingredients/form";
    }

    @PostMapping("/ingredients/new")
    public String createIngredient(
            @Valid @ModelAttribute("ingredient") IngredientDto dto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "admin/ingredients/form";
        }

        ingredientService.createIngredient(dto);
        redirectAttributes.addFlashAttribute("success", "Ingrediencia bola vytvorená");
        return "redirect:/admin/ingredients";
    }

    @GetMapping("/ingredients/{id}/edit")
    public String editIngredientForm(@PathVariable Long id, Model model) {
        Ingredient ingredient = ingredientService.findById(id);
        model.addAttribute("ingredient", ingredientService.toIngredientDto(ingredient));
        return "admin/ingredients/form";
    }

    @PostMapping("/ingredients/{id}/edit")
    public String updateIngredient(
            @PathVariable Long id,
            @Valid @ModelAttribute("ingredient") IngredientDto dto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "admin/ingredients/form";
        }

        ingredientService.updateIngredient(id, dto);
        redirectAttributes.addFlashAttribute("success", "Ingrediencia bola aktualizovaná");
        return "redirect:/admin/ingredients";
    }

    @PostMapping("/ingredients/{id}/delete")
    public String deleteIngredient(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            ingredientService.deleteIngredient(id);
            redirectAttributes.addFlashAttribute("success", "Ingrediencia bola vymazaná");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/ingredients";
    }

    // ==================== TAGS ====================
    @GetMapping("/tags")
    public String listTags(Model model) {
        model.addAttribute("tags", tagService.findAll());
        return "admin/tags/list";
    }

    @PostMapping("/tags")
    public String createTag(
            @RequestParam String nameSk,
            @RequestParam String slug,
            RedirectAttributes redirectAttributes) {
        try {
            tagService.createTag(nameSk, slug);
            redirectAttributes.addFlashAttribute("success", "Tag bol vytvorený");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/tags";
    }

    @PostMapping("/tags/{id}/delete")
    public String deleteTag(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            tagService.deleteTag(id);
            redirectAttributes.addFlashAttribute("success", "Tag bol vymazaný");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/tags";
    }

    // ==================== USERS ====================
    @GetMapping("/users")
    public String listUsers(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Page<User> users = userService.searchUsers(search, PageRequest.of(page, size));
        model.addAttribute("users", users);
        model.addAttribute("search", search);
        return "admin/users/list";
    }

    @GetMapping("/users/{id}")
    public String userDetail(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "admin/users/detail";
    }

    @PostMapping("/users/{id}/toggle")
    public String toggleUserEnabled(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.toggleUserEnabled(id);
        redirectAttributes.addFlashAttribute("success", "Stav používateľa bol zmenený");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/role")
    public String assignRole(
            @PathVariable Long id,
            @RequestParam String roleName,
            @RequestParam String action,
            RedirectAttributes redirectAttributes) {

        if ("add".equals(action)) {
            userService.assignRole(id, roleName);
            redirectAttributes.addFlashAttribute("success", "Rola bola priradená");
        } else if ("remove".equals(action)) {
            userService.removeRole(id, roleName);
            redirectAttributes.addFlashAttribute("success", "Rola bola odobraná");
        }

        return "redirect:/admin/users/" + id;
    }

    // ==================== ORDERS ====================
    @GetMapping("/orders")
    public String listOrders(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Page<Order> orders = orderService.searchOrders(search, PageRequest.of(page, size));
        model.addAttribute("orders", orders);
        model.addAttribute("search", search);
        return "admin/orders/list";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Long id, Model model) {
        Order order = orderService.findById(id);
        model.addAttribute("order", order);
        model.addAttribute("allStatuses", OrderStatus.values());
        return "admin/orders/detail";
    }

    @PostMapping("/orders/{id}/status")
    public String updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status,
            RedirectAttributes redirectAttributes) {
        try {
            orderService.updateStatus(id, status);
            redirectAttributes.addFlashAttribute("success", "Stav objednávky bol aktualizovaný");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/orders/" + id;
    }
}

