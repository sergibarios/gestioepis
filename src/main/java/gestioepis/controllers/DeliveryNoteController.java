package gestioepis.controllers;

import gestioepis.models.DeliveryNote;
import gestioepis.models.PurchaseOrder;
import gestioepis.repositories.*;
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
public class DeliveryNoteController {

    @Autowired
    private DeliveryNoteRepository deliveryNoteRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private StorageService storageService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private LocationRepository locationRepository;

    @GetMapping("/purchases")
    public String purchases(
            @RequestParam(required = false, defaultValue = "desc") String sort,
            @RequestParam(required = false) Long orderId,
            Model model) {
        List<DeliveryNote> deliveryNotes = "asc".equalsIgnoreCase(sort)
                ? deliveryNoteRepository.findAllByOrderByDeliveryDateAsc()
                : deliveryNoteRepository.findAllByOrderByDeliveryDateDesc();

        if (orderId != null) {
            deliveryNotes = deliveryNotes.stream()
                    .filter(n -> n.getPurchaseOrder() != null && orderId.equals(n.getPurchaseOrder().getId()))
                    .toList();
        }

        List<PurchaseOrder> orders = purchaseOrderRepository.findAllByOrderByOrderDateDesc();
        model.addAttribute("deliveryNotes", deliveryNotes);
        model.addAttribute("orders", orders);
        model.addAttribute("sort", sort);
        model.addAttribute("selectedOrderId", orderId);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("subcategories", subcategoryRepository.findAll());
        model.addAttribute("locations", locationRepository.findAll());
        model.addAttribute("allSizes", gestioepis.models.Talla.values());
        return "purchases";
    }

    @PostMapping("/delivery-notes")
    public String saveDeliveryNote(
            @RequestParam(required = false) Long orderId,
            @RequestParam String reference,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deliveryDate,
            @RequestParam(required = false) MultipartFile file,
            @RequestParam(required = false, defaultValue = "purchases") String redirectSource,
            RedirectAttributes redirectAttributes) {

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
        redirectAttributes.addFlashAttribute("toastMessage", "Albarà afegit correctament.");

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
            @RequestParam(required = false) MultipartFile file,
            RedirectAttributes redirectAttributes) {

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
        redirectAttributes.addFlashAttribute("toastMessage", "Albarà actualitzat correctament.");
        return "redirect:/purchases";
    }

    @PostMapping("/delivery-notes/delete/{id}")
    public String deleteDeliveryNote(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        deliveryNoteRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("toastMessage", "Albarà eliminat.");
        return "redirect:/purchases";
    }
}