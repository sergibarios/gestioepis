package gestioepis.controllers;

import gestioepis.repositories.HandoverRepository;
import gestioepis.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HandoverController {
    @Autowired
    private HandoverRepository handoverRepository;

    @Autowired
    private StorageService storageService;

    @GetMapping("/handovers")
    public String handovers(Model model){

    }


}
