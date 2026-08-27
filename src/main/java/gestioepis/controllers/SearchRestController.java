package gestioepis.controllers;

import gestioepis.dto.SearchResultDTO;
import gestioepis.models.ClothingItem;
import gestioepis.models.DeliveryNote;
import gestioepis.models.Handover;
import gestioepis.models.PurchaseOrder;
import gestioepis.repositories.ClothingItemRepository;
import gestioepis.repositories.DeliveryNoteRepository;
import gestioepis.repositories.HandoverRepository;
import gestioepis.repositories.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
public class SearchRestController {

    @Autowired
    private ClothingItemRepository clothingItemRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private HandoverRepository handoverRepository;

    @Autowired
    private DeliveryNoteRepository deliveryNoteRepository;

    @GetMapping("/api/search")
    public List<SearchResultDTO> search(@RequestParam(required = false, defaultValue = "") String q) {
        if (q == null || q.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String query = q.trim();

        List<ScoredResult> results = new ArrayList<>();

        List<ClothingItem> items = clothingItemRepository.findByCodeContainingIgnoreCaseOrSubcategoryNameContainingIgnoreCase(query, query);
        for (ClothingItem item : items) {
            String subcategoryName = item.getSubcategory() != null ? item.getSubcategory().getName() : "-";
            int score = Math.min(relevanceScore(item.getCode(), query), relevanceScore(subcategoryName, query));
            results.add(new ScoredResult(score, new SearchResultDTO(
                    subcategoryName + " - " + item.getBrand(),
                    "Codi: " + item.getCode() + " | Size: " + item.getItemSize(),
                    "Article",
                    "/inventory#item-" + item.getId()
            )));
        }

        List<PurchaseOrder> orders = purchaseOrderRepository.findByNameContainingIgnoreCase(query);
        for (PurchaseOrder order : orders) {
            results.add(new ScoredResult(relevanceScore(order.getName(), query), new SearchResultDTO(
                    order.getName(),
                    "Data: " + order.getOrderDate(),
                    "Comanda",
                    "/purchase-orders#order-" + order.getId()
            )));
        }

        List<Handover> handovers = handoverRepository.findByPersonNameContainingIgnoreCase(query);
        for (Handover handover : handovers) {
            String personName = handover.getPerson() != null ? handover.getPerson().getName() : "-";
            results.add(new ScoredResult(relevanceScore(personName, query), new SearchResultDTO(
                    personName,
                    "Data: " + handover.getHandoverDate(),
                    "Entrega",
                    "/handovers#handover-" + handover.getId()
            )));
        }

        List<DeliveryNote> deliveryNotes = deliveryNoteRepository.findByReferenceContainingIgnoreCase(query);
        for (DeliveryNote note : deliveryNotes) {
            results.add(new ScoredResult(relevanceScore(note.getReference(), query), new SearchResultDTO(
                    note.getReference(),
                    "Data: " + note.getDeliveryDate(),
                    "Albara",
                    "/purchases#note-" + note.getId()
            )));
        }

        return results.stream()
                .sorted(Comparator.comparingInt(ScoredResult::score))
                .map(ScoredResult::dto)
                .toList();
    }

    private int relevanceScore(String field, String query) {
        if (field == null) {
            return 3;
        }
        String normalizedField = field.toLowerCase();
        String normalizedQuery = query.toLowerCase();
        if (normalizedField.equals(normalizedQuery)) {
            return 0;
        }
        if (normalizedField.startsWith(normalizedQuery)) {
            return 1;
        }
        if (normalizedField.contains(normalizedQuery)) {
            return 2;
        }
        return 3;
    }

    private record ScoredResult(int score, SearchResultDTO dto) {
    }
}
