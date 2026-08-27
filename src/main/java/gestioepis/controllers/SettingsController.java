package gestioepis.controllers;

import gestioepis.models.Category;
import gestioepis.models.Location;
import gestioepis.models.Person;
import gestioepis.models.Subcategory;
import gestioepis.repositories.CategoryRepository;
import gestioepis.repositories.LocationRepository;
import gestioepis.repositories.PersonRepository;
import gestioepis.repositories.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SettingsController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/settings")
    public String settings(@RequestParam(required = false) String error, Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("subcategories", subcategoryRepository.findAll());
        model.addAttribute("locations", locationRepository.findAll());
        model.addAttribute("people", personRepository.findAll());
        model.addAttribute("error", error);
        return "settings";
    }

    @PostMapping("/settings/categories")
    public String addCategory(@RequestParam String name, RedirectAttributes redirectAttributes) {
        Category category = new Category();
        category.setName(name);
        categoryRepository.save(category);
        redirectAttributes.addFlashAttribute("toastMessage", "Categoria afegida.");
        return "redirect:/settings";
    }

    @PostMapping("/settings/categories/edit/{id}")
    public String updateCategory(@PathVariable Long id, @RequestParam String name, RedirectAttributes redirectAttributes) {
        Category category = categoryRepository.findById(id).orElseThrow();
        category.setName(name);
        categoryRepository.save(category);
        redirectAttributes.addFlashAttribute("toastMessage", "Categoria actualitzada.");
        return "redirect:/settings";
    }

    @PostMapping("/settings/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("toastMessage", "Categoria eliminada.");
        } catch (DataIntegrityViolationException e) {
            return "redirect:/settings?error=category-in-use";
        }
        return "redirect:/settings";
    }

    @PostMapping("/settings/subcategories")
    public String addSubcategory(@RequestParam String name, @RequestParam Long categoryId, RedirectAttributes redirectAttributes) {
        Subcategory subcategory = new Subcategory();
        subcategory.setName(name);
        subcategory.setCategory(categoryRepository.findById(categoryId).orElseThrow());
        subcategoryRepository.save(subcategory);
        redirectAttributes.addFlashAttribute("toastMessage", "Subcategoria afegida.");
        return "redirect:/settings";
    }

    @PostMapping("/settings/subcategories/edit/{id}")
    public String updateSubcategory(@PathVariable Long id, @RequestParam String name, @RequestParam Long categoryId, RedirectAttributes redirectAttributes) {
        Subcategory subcategory = subcategoryRepository.findById(id).orElseThrow();
        subcategory.setName(name);
        subcategory.setCategory(categoryRepository.findById(categoryId).orElseThrow());
        subcategoryRepository.save(subcategory);
        redirectAttributes.addFlashAttribute("toastMessage", "Subcategoria actualitzada.");
        return "redirect:/settings";
    }

    @PostMapping("/settings/subcategories/delete/{id}")
    public String deleteSubcategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            subcategoryRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("toastMessage", "Subcategoria eliminada.");
        } catch (DataIntegrityViolationException e) {
            return "redirect:/settings?error=subcategory-in-use";
        }
        return "redirect:/settings";
    }

    @PostMapping("/settings/locations")
    public String addLocation(@RequestParam String name, @RequestParam(required = false) String description, RedirectAttributes redirectAttributes) {
        Location location = new Location();
        location.setName(name);
        location.setDescription(description);
        locationRepository.save(location);
        redirectAttributes.addFlashAttribute("toastMessage", "Ubicació afegida.");
        return "redirect:/settings";
    }

    @PostMapping("/settings/locations/edit/{id}")
    public String updateLocation(@PathVariable Long id, @RequestParam String name, @RequestParam(required = false) String description, RedirectAttributes redirectAttributes) {
        Location location = locationRepository.findById(id).orElseThrow();
        location.setName(name);
        location.setDescription(description);
        locationRepository.save(location);
        redirectAttributes.addFlashAttribute("toastMessage", "Ubicació actualitzada.");
        return "redirect:/settings";
    }

    @PostMapping("/settings/locations/delete/{id}")
    public String deleteLocation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            locationRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("toastMessage", "Ubicació eliminada.");
        } catch (DataIntegrityViolationException e) {
            return "redirect:/settings?error=location-in-use";
        }
        return "redirect:/settings";
    }

    @PostMapping("/settings/people")
    public String addPerson(@RequestParam String name, RedirectAttributes redirectAttributes) {
        Person person = new Person();
        person.setName(name);
        personRepository.save(person);
        redirectAttributes.addFlashAttribute("toastMessage", "Persona afegida.");
        return "redirect:/settings";
    }

    @PostMapping("/settings/people/edit/{id}")
    public String updatePerson(@PathVariable Long id, @RequestParam String name, RedirectAttributes redirectAttributes) {
        Person person = personRepository.findById(id).orElseThrow();
        person.setName(name);
        personRepository.save(person);
        redirectAttributes.addFlashAttribute("toastMessage", "Persona actualitzada.");
        return "redirect:/settings";
    }

    @PostMapping("/settings/people/delete/{id}")
    public String deletePerson(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            personRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("toastMessage", "Persona eliminada.");
        } catch (DataIntegrityViolationException e) {
            return "redirect:/settings?error=person-in-use";
        }
        return "redirect:/settings";
    }
}
