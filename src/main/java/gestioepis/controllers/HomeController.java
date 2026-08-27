package gestioepis.controllers;

import gestioepis.dto.ActivityDTO;
import gestioepis.models.ClothingItem;
import gestioepis.models.DeliveryNote;
import gestioepis.models.Handover;
import gestioepis.models.PurchaseOrder;
import gestioepis.repositories.ClothingItemRepository;
import gestioepis.repositories.DeliveryNoteRepository;
import gestioepis.repositories.HandoverRepository;
import gestioepis.repositories.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    // No hi ha un camp d'estoc mínim configurable al model: es fa servir un llindar
    // fix per SKU (subcategoria + talla) fins que es defineixi una regla de negoci.
    private static final long LOW_STOCK_THRESHOLD = 5;

    private static final int RECENT_ACTIVITY_LIMIT = 6;

    @Autowired
    private ClothingItemRepository clothingItemRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private HandoverRepository handoverRepository;

    @Autowired
    private DeliveryNoteRepository deliveryNoteRepository;

    @GetMapping("/")
    public String home(Model model) {

        List<ClothingItem> availableItems = clothingItemRepository.findByHandoverIsNull();

        long totalStock = availableItems.size();

        long lowStockAlerts = availableItems.stream()
                .collect(Collectors.groupingBy(
                        item -> (item.getSubcategory() != null ? item.getSubcategory().getId() : -1L) + "|" + item.getItemSize(),
                        Collectors.counting()))
                .values().stream()
                .filter(count -> count < LOW_STOCK_THRESHOLD)
                .count();

        long pendingOrders = purchaseOrderRepository.countByDeliveryNotesIsEmpty();
        long deliveriesToday = handoverRepository.countByHandoverDate(LocalDate.now());

        model.addAttribute("totalStock", totalStock);
        model.addAttribute("lowStockAlerts", lowStockAlerts);
        model.addAttribute("pendingOrders", pendingOrders);
        model.addAttribute("deliveriesToday", deliveriesToday);
        model.addAttribute("recentActivity", buildRecentActivity());

        return "home";
    }

    private List<ActivityDTO> buildRecentActivity() {
        List<ActivityDTO> activity = new ArrayList<>();

        for (Handover h : handoverRepository.findTop5ByOrderByHandoverDateDesc()) {
            String personName = h.getPerson() != null ? h.getPerson().getName() : "-";
            activity.add(new ActivityDTO(h.getHandoverDate(), "Entrega a " + personName, "Entrega"));
        }

        for (PurchaseOrder o : purchaseOrderRepository.findTop5ByOrderByOrderDateDesc()) {
            activity.add(new ActivityDTO(o.getOrderDate(), "Comanda " + o.getName(), "Comanda"));
        }

        for (DeliveryNote n : deliveryNoteRepository.findTop5ByOrderByDeliveryDateDesc()) {
            activity.add(new ActivityDTO(n.getDeliveryDate(), "Albarà " + n.getReference(), "Albarà"));
        }

        return activity.stream()
                .filter(a -> a.getDate() != null)
                .sorted(Comparator.comparing(ActivityDTO::getDate).reversed())
                .limit(RECENT_ACTIVITY_LIMIT)
                .toList();
    }
}
