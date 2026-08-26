package gestioepis.controllers;

import gestioepis.models.DeliveryNote;
import gestioepis.models.PurchaseOrder;
import gestioepis.repositories.DeliveryNoteRepository;
import gestioepis.repositories.PurchaseOrderRepository;
import gestioepis.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Controller
public class DeliveryNoteController {

    @Autowired
    private DeliveryNoteRepository deliveryNoteRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private StorageService storageService;

    @PostMapping("/delivery-notes")
    public String saveDeliveryNote(
            @RequestParam Long orderId,
            @RequestParam String reference,
            @RequestParam(required = false) MultipartFile file) {

        PurchaseOrder order = purchaseOrderRepository.findById(orderId).orElseThrow();

        DeliveryNote note = new DeliveryNote();
        note.setReference(reference);
        note.setDeliveryDate(LocalDate.now());
        note.setPurchaseOrder(order);

        if (file != null && !file.isEmpty()) {
            String filePath = storageService.store(file);
            note.setFilePath(filePath);
        }

        deliveryNoteRepository.save(note);

        return "redirect:/purchase-orders";
    }
}