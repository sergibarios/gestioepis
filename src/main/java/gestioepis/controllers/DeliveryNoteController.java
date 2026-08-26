package gestioepis.controllers;

import gestioepis.models.DeliveryNote;
import gestioepis.models.PurchaseOrder;
import gestioepis.repositories.DeliveryNoteRepository;
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
public class DeliveryNoteController {

    @Autowired
    private DeliveryNoteRepository deliveryNoteRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private StorageService storageService;

    @GetMapping("/purchases")
    public String purchases(Model model) {
        List<DeliveryNote> deliveryNotes = deliveryNoteRepository.findAllByOrderByDeliveryDateDesc();
        List<PurchaseOrder> orders = purchaseOrderRepository.findAllByOrderByOrderDateDesc();
        model.addAttribute("deliveryNotes", deliveryNotes);
        model.addAttribute("orders", orders);
        return "purchases";
    }

    @PostMapping("/delivery-notes")
    public String saveDeliveryNote(
            @RequestParam(required = false) Long orderId,
            @RequestParam String reference,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deliveryDate,
            @RequestParam(required = false) MultipartFile file,
            @RequestParam(required = false, defaultValue = "purchases") String redirectSource) {

        DeliveryNote note = new DeliveryNote();
        note.setReference(reference);
        note.setDeliveryDate(deliveryDate != null ? deliveryDate : LocalDate.now());

        if (orderId != null) {
            PurchaseOrder order = purchaseOrderRepository.findById(orderId).orElse(null);
            note.setPurchaseOrder(order);
        }

        if (file != null && !file.isEmpty()) {
            String filePath = storageService.store(file);
            note.setFilePath(filePath);
        }

        deliveryNoteRepository.save(note);

        if ("orders".equalsIgnoreCase(redirectSource)) {
            return "redirect:/purchase-orders";
        }
        return "redirect:/purchases";
    }

    @PostMapping("/delivery-notes/edit/{id}")
    public String updateDeliveryNote(
            @PathVariable Long id,
            @RequestParam(required = false) Long orderId,
            @RequestParam String reference,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deliveryDate,
            @RequestParam(required = false) MultipartFile file) {

        DeliveryNote note = deliveryNoteRepository.findById(id).orElseThrow();
        note.setReference(reference);
        note.setDeliveryDate(deliveryDate);

        if (orderId != null) {
            PurchaseOrder order = purchaseOrderRepository.findById(orderId).orElse(null);
            note.setPurchaseOrder(order);
        } else {
            note.setPurchaseOrder(null);
        }

        if (file != null && !file.isEmpty()) {
            String filePath = storageService.store(file);
            note.setFilePath(filePath);
        }

        deliveryNoteRepository.save(note);
        return "redirect:/purchases";
    }

    @GetMapping("/delivery-notes/delete/{id}")
    public String deleteDeliveryNote(@PathVariable Long id) {
        deliveryNoteRepository.deleteById(id);
        return "redirect:/purchases";
    }
}