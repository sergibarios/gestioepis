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

import java.time.LocalDate;
import java.util.List;

@Controller
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private StorageService storageService;

    @GetMapping("/purchase-orders")
    public String purchaseOrders(Model model) {
        List<PurchaseOrder> orders = purchaseOrderRepository.findAllByOrderByOrderDateDesc();
        model.addAttribute("orders", orders);
        return "purchase-orders";
    }

    @PostMapping("/purchase-orders")
    public String savePurchaseOrder(
            @RequestParam String name,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate orderDate,
            @RequestParam(required = false) String comments,
            @RequestParam(required = false) MultipartFile file) {

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
        return "redirect:/purchase-orders";
    }

    @GetMapping("/purchase-orders/delete/{id}")
    public String deletePurchaseOrder(@PathVariable Long id) {
        purchaseOrderRepository.deleteById(id);
        return "redirect:/purchase-orders";
    }

    @PostMapping("/purchase-orders/edit/{id}")
    public String updatePurchaseOrder(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate orderDate,
            @RequestParam(required = false) String comments,
            @RequestParam(required = false) MultipartFile file) {

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
        return "redirect:/purchase-orders";
    }
}