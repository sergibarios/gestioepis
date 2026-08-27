package gestioepis.controllers;

import gestioepis.models.ClothingItem;
import gestioepis.models.Handover;
import gestioepis.models.Person;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    public String handovers(Model model){
        List<Handover> handovers = handoverRepository.findAllByOrderByHandoverDateDesc();
        model.addAttribute("handovers", handovers);

        List<ClothingItem> clothingItems = clothingItemRepository.findByHandoverIsNull();
        model.addAttribute("items", clothingItems);

        List<Person> people = personRepository.findAll();
        model.addAttribute("people", people);
        return "handovers";
    }

    @PostMapping("/handovers")
    public String saveHandover(
        @RequestParam Long personId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate handoverDate,
        @RequestParam List<Long> itemIds
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

        return "redirect:/handovers";
    }

}
