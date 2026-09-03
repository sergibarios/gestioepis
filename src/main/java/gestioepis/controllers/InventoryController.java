package gestioepis.controllers;

import gestioepis.models.ClothingItem;
import gestioepis.models.Subcategory;
import gestioepis.models.Talla;
import gestioepis.repositories.CategoryRepository;
import gestioepis.repositories.ClothingItemRepository;
import gestioepis.repositories.LocationRepository;
import gestioepis.repositories.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        List<ClothingItem> items = clothingItemRepository.findByHandoverIsNull();
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
            @RequestParam Long categoryId,
            @RequestParam Long subcategoryId,
            @RequestParam Talla itemSize,
            @RequestParam double price,
            @RequestParam Long locationId,
            @RequestParam(defaultValue = "1") int quantity,
            RedirectAttributes redirectAttributes) {

        var category = categoryRepository.findById(categoryId).orElseThrow();
        var subcategory = subcategoryRepository.findById(subcategoryId).orElseThrow();
        var location = locationRepository.findById(locationId).orElseThrow();
        int itemsToCreate = Math.max(1, quantity);

        for (int i = 0; i < itemsToCreate; i++) {
            ClothingItem item = new ClothingItem();
            item.setCategory(category);
            item.setSubcategory(subcategory);
            item.setItemSize(itemSize);
            item.setPrice(price);
            item.setLocation(location);
            clothingItemRepository.save(item);
        }

        redirectAttributes.addFlashAttribute("toastMessage", itemsToCreate > 1
                ? itemsToCreate + " articles afegits correctament."
                : "Article afegit correctament.");
        return "redirect:/inventory";
    }

    @PostMapping("/inventory/edit/{id}")
    public String updateItem(
            @PathVariable Long id,
            @RequestParam Long categoryId,
            @RequestParam Long subcategoryId,
            @RequestParam Talla itemSize,
            @RequestParam double price,
            @RequestParam Long locationId,
            RedirectAttributes redirectAttributes) {

        ClothingItem item = clothingItemRepository.findById(id).orElseThrow();
        item.setCategory(categoryRepository.findById(categoryId).orElseThrow());
        item.setSubcategory(subcategoryRepository.findById(subcategoryId).orElseThrow());
        item.setItemSize(itemSize);
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
        model.addAttribute("groupedItems", groupBySubcategoryAndSize(items));
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("subcategories", subcategoryRepository.findAll());
        model.addAttribute("locations", locationRepository.findAll());
        model.addAttribute("allSizes", Talla.values());
        model.addAttribute("currentLocationKey", locationKey);
        model.addAttribute("selectedCategoryId", selectedCategoryId);
        return "inventory";
    }

    private List<SubcategoryGroup> groupBySubcategoryAndSize(List<ClothingItem> items) {
        Map<Long, Subcategory> subcategoriesById = new LinkedHashMap<>();
        Map<Long, Map<Talla, List<ClothingItem>>> itemsBySubcategoryId = new LinkedHashMap<>();

        items.stream()
                .filter(item -> item.getSubcategory() != null)
                .sorted(Comparator.comparing(item -> item.getSubcategory().getName(), String.CASE_INSENSITIVE_ORDER))
                .forEach(item -> {
                    Long subcategoryId = item.getSubcategory().getId();
                    subcategoriesById.putIfAbsent(subcategoryId, item.getSubcategory());
                    itemsBySubcategoryId
                            .computeIfAbsent(subcategoryId, id -> new EnumMap<>(Talla.class))
                            .computeIfAbsent(item.getItemSize(), size -> new ArrayList<>())
                            .add(item);
                });

        return subcategoriesById.entrySet().stream()
                .map(entry -> new SubcategoryGroup(entry.getValue(), itemsBySubcategoryId.get(entry.getKey())))
                .toList();
    }

    public static class SubcategoryGroup {
        private final Subcategory subcategory;
        private final Map<Talla, List<ClothingItem>> sizeGroups;

        public SubcategoryGroup(Subcategory subcategory, Map<Talla, List<ClothingItem>> sizeGroups) {
            this.subcategory = subcategory;
            this.sizeGroups = sizeGroups;
        }

        public Subcategory getSubcategory() {
            return subcategory;
        }

        public Map<Talla, List<ClothingItem>> getSizeGroups() {
            return sizeGroups;
        }

        public int getTotalCount() {
            return sizeGroups.values().stream().mapToInt(List::size).sum();
        }
    }
}
