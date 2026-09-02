package gestioepis.controllers;

import gestioepis.dto.ActivityDTO;
import gestioepis.models.DeliveryNote;
import gestioepis.models.Handover;
import gestioepis.models.PurchaseOrder;
import gestioepis.repositories.DeliveryNoteRepository;
import gestioepis.repositories.HandoverRepository;
import gestioepis.repositories.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
public class HomeController {

    private static final int RECENT_ACTIVITY_LIMIT = 6;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private HandoverRepository handoverRepository;

    @Autowired
    private DeliveryNoteRepository deliveryNoteRepository;

    @GetMapping("/")
    public String home(Model model) {
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
