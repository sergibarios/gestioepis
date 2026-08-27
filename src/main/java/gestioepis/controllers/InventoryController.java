package gestioepis.controllers;

import gestioepis.models.ClothingItem;
import gestioepis.repositories.CategoryRepository;
import gestioepis.repositories.ClothingItemRepository;
import gestioepis.repositories.LocationRepository;
import gestioepis.repositories.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class InventoryController {

    @Autowired
    private ClothingItemRepository clothingItemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @GetMapping("/inventory")
    public String inventoryTotal(@RequestParam(required = false) String categoryId, Model model) {
        List<ClothingItem> items = clothingItemRepository.findAll();
        return renderInventory(items, "all", categoryId, model);
    }

    @GetMapping("/inventory/{locationName}")
    public String inventoryByLocation(@PathVariable String locationName,
                                       @RequestParam(required = false) String categoryId,
                                       Model model) {
        List<ClothingItem> items = clothingItemRepository.findByLocationNameIgnoreCase(locationName);
        return renderInventory(items, locationName.toLowerCase(), categoryId, model);
    }

    @PostMapping("/inventory")
    public String saveItem(
            @RequestParam String code,
            @RequestParam Long categoryId,
            @RequestParam Long subcategoryId,
            @RequestParam String itemSize,
            @RequestParam String brand,
            @RequestParam double price,
            @RequestParam Long locationId,
            RedirectAttributes redirectAttributes) {

        ClothingItem item = new ClothingItem();
        item.setCode(code);
        item.setCategory(categoryRepository.findById(categoryId).orElseThrow());
        item.setSubcategory(subcategoryRepository.findById(subcategoryId).orElseThrow());
        item.setItemSize(itemSize);
        item.setBrand(brand);
        item.setPrice(price);
        item.setLocation(locationRepository.findById(locationId).orElseThrow());

        clothingItemRepository.save(item);
        redirectAttributes.addFlashAttribute("toastMessage", "Article afegit correctament.");
        return "redirect:/inventory";
    }

    @PostMapping("/inventory/edit/{id}")
    public String updateItem(
            @PathVariable Long id,
            @RequestParam String code,
            @RequestParam Long categoryId,
            @RequestParam Long subcategoryId,
            @RequestParam String itemSize,
            @RequestParam String brand,
            @RequestParam double price,
            @RequestParam Long locationId,
            RedirectAttributes redirectAttributes) {

        ClothingItem item = clothingItemRepository.findById(id).orElseThrow();
        item.setCode(code);
        item.setCategory(categoryRepository.findById(categoryId).orElseThrow());
        item.setSubcategory(subcategoryRepository.findById(subcategoryId).orElseThrow());
        item.setItemSize(itemSize);
        item.setBrand(brand);
        item.setPrice(price);
        item.setLocation(locationRepository.findById(locationId).orElseThrow());

        clothingItemRepository.save(item);
        redirectAttributes.addFlashAttribute("toastMessage", "Article actualitzat correctament.");
        return "redirect:/inventory";
    }

    @PostMapping("/inventory/delete/{id}")
    public String deleteItem(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        clothingItemRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("toastMessage", "Article eliminat.");
        return "redirect:/inventory";
    }

    private String renderInventory(List<ClothingItem> items, String locationKey, String categoryId, Model model) {
        Long selectedCategoryId = (categoryId != null && !categoryId.isBlank()) ? Long.parseLong(categoryId) : null;

        if (selectedCategoryId != null) {
            items = items.stream()
                    .filter(item -> item.getCategory() != null && selectedCategoryId.equals(item.getCategory().getId()))
                    .toList();
        }

        model.addAttribute("items", items);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("subcategories", subcategoryRepository.findAll());
        model.addAttribute("locations", locationRepository.findAll());
        model.addAttribute("currentLocationKey", locationKey);
        model.addAttribute("selectedCategoryId", selectedCategoryId);
        return "inventory";
    }
}
