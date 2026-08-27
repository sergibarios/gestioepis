package gestioepis.controllers;

import gestioepis.models.ClothingItem;
import gestioepis.repositories.ClothingItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class PageController {

    @Autowired
    private ClothingItemRepository clothingItemRepository;

    @GetMapping("/inventory")
    public String inventoryTotal(Model model) {
        List<ClothingItem> items = clothingItemRepository.findAll();
        model.addAttribute("items", items);
        model.addAttribute("currentLocation", "Tot l'Inventari");
        return "inventory";
    }

    @GetMapping("/items/{locationName}")
    public String inventoryByLocation(@PathVariable String locationName, Model model) {
        List<ClothingItem> items = clothingItemRepository.findByLocationNameIgnoreCase(locationName);

        model.addAttribute("items", items);

        String title = locationName.substring(0, 1).toUpperCase() + locationName.substring(1).toLowerCase();
        model.addAttribute("currentLocation", title);

        return "inventory";
    }
}