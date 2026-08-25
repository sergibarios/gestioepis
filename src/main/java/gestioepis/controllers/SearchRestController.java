package gestioepis.controllers;

import gestioepis.dto.SearchResultDTO;
import gestioepis.models.ClothingItem;
import gestioepis.models.PurchaseOrder;
import gestioepis.repositories.ClothingItemRepository;
import gestioepis.repositories.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchRestController {

    @Autowired
    private ClothingItemRepository clothingItemRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @GetMapping
    public List<SearchResultDTO> search(@RequestParam String q) {
        List<SearchResultDTO> results = new ArrayList<>();

        List<ClothingItem> items = clothingItemRepository.findByCodeContainingIgnoreCaseOrSubcategoryContainingIgnoreCase(q, q);
        for (ClothingItem item : items) {
            results.add(new SearchResultDTO(
                    item.getSubcategory() + " - " + item.getBrand(),
                    "Code: " + item.getCode() + " | Size: " + item.getItemSize(),
                    "Item",
                    "/items/" + item.getId()
            ));
        }

        List<PurchaseOrder> orders = purchaseOrderRepository.findByNameContainingIgnoreCase(q);
        for (PurchaseOrder order : orders) {
            results.add(new SearchResultDTO(
                    order.getName(),
                    "Date: " + order.getOrderDate(),
                    "Order",
                    "/orders/" + order.getId()
            ));
        }

        return results;
    }
}