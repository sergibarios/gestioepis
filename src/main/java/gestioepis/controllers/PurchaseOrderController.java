package gestioepis.controllers;

import gestioepis.models.PurchaseOrder;
import gestioepis.repositories.PurchaseOrderRepository;
import gestioepis.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private StorageService storageService;

    @GetMapping("/purchase-orders")
    public String purchaseOrders(
            @RequestParam(required = false, defaultValue = "desc") String sort,
            @RequestParam(required = false) String status,
            Model model) {
        List<PurchaseOrder> orders = "asc".equalsIgnoreCase(sort)
                ? purchaseOrderRepository.findAllByOrderByOrderDateAsc()
                : purchaseOrderRepository.findAllByOrderByOrderDateDesc();

        if ("pending".equalsIgnoreCase(status)) {
            orders = orders.stream()
                    .filter(o -> o.getDeliveryNotes() == null || o.getDeliveryNotes().isEmpty())
                    .toList();
        } else if ("received".equalsIgnoreCase(status)) {
            orders = orders.stream()
                    .filter(o -> o.getDeliveryNotes() != null && !o.getDeliveryNotes().isEmpty())
                    .toList();
        }

        model.addAttribute("orders", orders);
        model.addAttribute("sort", sort);
        model.addAttribute("selectedStatus", status);
        return "purchase-orders";
    }

    @PostMapping("/purchase-orders")
    public String savePurchaseOrder(
            @RequestParam String name,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate orderDate,
            @RequestParam(required = false) String comments,
            @RequestParam(required = false) MultipartFile file,
            RedirectAttributes redirectAttributes) {

        PurchaseOrder order = new PurchaseOrder();
        order.setName(name);
        order.setOrderDate(orderDate);

        if (comments != null) {
            order.setComments(comments);
        }

        if (file != null && !file.isEmpty()) {
            String filePath = storageService.store(file);
            order.setFilePath(filePath);
        }

        purchaseOrderRepository.save(order);
        redirectAttributes.addFlashAttribute("toastMessage", "Comanda afegida correctament.");
        return "redirect:/purchase-orders";
    }

    @PostMapping("/purchase-orders/delete/{id}")
    public String deletePurchaseOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        purchaseOrderRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("toastMessage", "Comanda eliminada.");
        return "redirect:/purchase-orders";
    }

    @PostMapping("/purchase-orders/edit/{id}")
    public String updatePurchaseOrder(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate orderDate,
            @RequestParam(required = false) String comments,
            @RequestParam(required = false) MultipartFile file,
            RedirectAttributes redirectAttributes) {

        PurchaseOrder order = purchaseOrderRepository.findById(id).orElseThrow();
        order.setName(name);
        order.setOrderDate(orderDate);

        if (comments != null) {
            order.setComments(comments);
        }

        if (file != null && !file.isEmpty()) {
            String filePath = storageService.store(file);
            order.setFilePath(filePath);
        }

        purchaseOrderRepository.save(order);
        redirectAttributes.addFlashAttribute("toastMessage", "Comanda actualitzada correctament.");
        return "redirect:/purchase-orders";
    }
}