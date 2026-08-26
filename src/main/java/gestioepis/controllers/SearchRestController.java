package gestioepis.controllers;

import gestioepis.dto.SearchResultDTO;
import gestioepis.models.ClothingItem;
import gestioepis.models.Handover;
import gestioepis.models.PurchaseOrder;
import gestioepis.repositories.ClothingItemRepository;
import gestioepis.repositories.HandoverRepository;
import gestioepis.repositories.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SearchRestController {

    @Autowired
    private ClothingItemRepository clothingItemRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private HandoverRepository handoverRepository;

    @GetMapping("/api/search")
    public List<SearchResultDTO> search(@RequestParam(required = false, defaultValue = "") String q) {
        List<SearchResultDTO> results = new ArrayList<>();

        if (q == null || q.trim().isEmpty()) {
            return results;
        }

        List<ClothingItem> items = clothingItemRepository.findByCodeContainingIgnoreCaseOrSubcategoryContainingIgnoreCase(q, q);
        for (ClothingItem item : items) {
            results.add(new SearchResultDTO(
                    item.getSubcategory() + " - " + item.getBrand(),
                    "Codi: " + item.getCode() + " | Size: " + item.getItemSize(),
                    "Article",
                    "/items/" + item.getId()
            ));
        }

        List<PurchaseOrder> orders = purchaseOrderRepository.findByNameContainingIgnoreCase(q);
        for (PurchaseOrder order : orders) {
            results.add(new SearchResultDTO(
                    order.getName(),
                    "Data: " + order.getOrderDate(),
                    "Comanda",
                    "/purchase-orders"
            ));
        }

        List<Handover> handovers = handoverRepository.findByPersonNameContainingIgnoreCase(q);
        for (Handover handover : handovers) {
            results.add(new SearchResultDTO(
                    handover.getPerson().getName(),
                    "Data: " + handover.getHandoverDate(),
                    "Entrega",
                    "/handovers"
            ));
        }

        return results;
    }
}