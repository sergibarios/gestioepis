package gestioepis.controllers;

import gestioepis.models.*;
import gestioepis.repositories.ClothingItemRepository;
import gestioepis.repositories.HandoverRepository;
import gestioepis.repositories.PersonRepository;
import gestioepis.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HandoverController {
    @Autowired
    private HandoverRepository handoverRepository;

    @Autowired
    private ClothingItemRepository clothingItemRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private StorageService storageService;

    @GetMapping("/handovers")
    public String handovers(
            @RequestParam(required = false, defaultValue = "desc") String sort,
            @RequestParam(required = false) Long personId,
            Model model){
        List<Handover> handovers = "asc".equalsIgnoreCase(sort)
                ? handoverRepository.findAllByOrderByHandoverDateAsc()
                : handoverRepository.findAllByOrderByHandoverDateDesc();

        if (personId != null) {
            handovers = handovers.stream()
                    .filter(h -> h.getPerson() != null && personId.equals(h.getPerson().getId()))
                    .toList();
        }

        model.addAttribute("handovers", handovers);
        model.addAttribute("sort", sort);
        model.addAttribute("selectedPersonId", personId);


        List<ClothingItem> clothingItems = clothingItemRepository.findByHandoverIsNull();
        System.out.println("--> clothingItems " + clothingItems.size());
        // Agrupa por Subcategoría y luego por Talla
        Map<Subcategory, Map<Talla, List<ClothingItem>>> agrupados = clothingItems.stream()
                .collect(Collectors.groupingBy(
                        ClothingItem::getSubcategory,
                        Collectors.groupingBy(ClothingItem::getItemSize)
                ));

        model.addAttribute("clothingItems", clothingItems);
        model.addAttribute("items", agrupados);

        List<Person> people = personRepository.findAll();
        model.addAttribute("people", people);
        return "handovers";
    }

    @PostMapping("/handovers")
    public String saveHandover(
        @RequestParam Long personId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate handoverDate,
        @RequestParam List<Long> itemIds,
        RedirectAttributes redirectAttributes
    ){
        Handover handover = new Handover();
        Person person = personRepository.findById(personId).orElseThrow();
        handover.setPerson(person);
        handover.setHandoverDate(handoverDate);

        handoverRepository.save(handover);

        for(Long id : itemIds){
            ClothingItem item = clothingItemRepository.findById(id).orElseThrow();
            item.setHandover(handover);
            clothingItemRepository.save(item);
        }

        redirectAttributes.addFlashAttribute("toastMessage", "Entrega registrada correctament.");
        return "redirect:/handovers";
    }

    @PostMapping("/handovers/delete")
    public String deleteHandover(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        Handover handover = handoverRepository.findById(id).orElseThrow();

        // Libera las prendas asignadas poníendoles handover = null
        if (handover.getHandedItems() != null) {
            for (ClothingItem item : handover.getHandedItems()) {
                item.setHandover(null);
                clothingItemRepository.save(item);
            }
        }

        handoverRepository.delete(handover);
        redirectAttributes.addFlashAttribute("toastMessage", "Entrega eliminada correctament.");
        return "redirect:/handovers";
    }
}
