package gestioepis.controllers;

import gestioepis.models.Handover;
import gestioepis.repositories.ClothingItemRepository;
import gestioepis.repositories.HandoverRepository;
import gestioepis.repositories.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDate;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ClothingItemRepository clothingItemRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private HandoverRepository handoverRepository;

    @GetMapping("/")
    public String home(Model model) {

        long totalStock = clothingItemRepository.countByHandoverIsNull();
        long lowStockAlerts = 0;
        long pendingOrders = purchaseOrderRepository.count();
        long deliveriesToday = handoverRepository.countByHandoverDate(LocalDate.now());

        List<Handover> recentActivity = handoverRepository.findTop5ByOrderByHandoverDateDesc();

        model.addAttribute("totalStock", totalStock);
        model.addAttribute("lowStockAlerts", lowStockAlerts);
        model.addAttribute("pendingOrders", pendingOrders);
        model.addAttribute("deliveriesToday", deliveriesToday);
        model.addAttribute("recentActivity", recentActivity);

        return "home";
    }
}
