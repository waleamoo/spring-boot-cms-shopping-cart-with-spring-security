package com.techqwerty.cmsshoppingcart.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.techqwerty.cmsshoppingcart.models.data.Page;
import com.techqwerty.cmsshoppingcart.models.PageRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/pages")
public class AdminPagesController {
	
    @Autowired 
    private PageRepository pageRepo;

    // public AdminPagesController(PageRepository pageRepo){
    //     this.pageRepo = pageRepo;
    // }

    // @GetMapping
	// public void index() {
	// 	List<Page> pages = new ArrayList<Page>();
    //     pages = pageRepo.findAll();
    //     pages.forEach(System.out::println);
	// }
	
	@GetMapping
	public String index(Model model) {
		List<Page> pages = pageRepo.findAllByOrderBySortingAsc();
		model.addAttribute("pages", pages);
		return "admin/pages/index";
	}
	
	// /admin/pages/add
	@GetMapping("/add") 
	public String add(@ModelAttribute Page page) { // or Model model // @ModelAttribute Page page
		// model.addAttribute("page", new Page());
		return "admin/pages/add";
	}
	
	@PostMapping("/add")
	public String add(@Valid Page page, BindingResult bindinResult, RedirectAttributes redirectAttributes, Model model) {  
		// if the model has errors 
		if (bindinResult.hasErrors()) {
			return "admin/pages/add";
		}
		redirectAttributes.addFlashAttribute("message", "Page added");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		// create the slug
		String slug = page.getSlug() == "" ? page.getTitle().toLowerCase().replace(" ", "-") : page.getSlug().toLowerCase().replace(" ", "-");
		// check if the slug already exist 
		Page slugExists = pageRepo.findBySlug(slug);
		if (slugExists != null) {
			redirectAttributes.addFlashAttribute("message", "Slug exist, chose another");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			redirectAttributes.addFlashAttribute("page", page);
		}else {
			page.setSlug(slug);
			page.setSorting(100);
			// save the page 
			pageRepo.save(page);
		}
		return "redirect:/admin/pages/add";
	}
	
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model){
        Page page = pageRepo.getOne(id);
        model.addAttribute("page", page);
        return "admin/pages/edit";
    }

    @PostMapping("/edit")
	public String edit(@Valid Page page, BindingResult bindinResult, RedirectAttributes redirectAttributes, Model model) {  
		Page currentPage = pageRepo.getOne(page.getId());
        
        // if the model has errors 
		if (bindinResult.hasErrors()) {
            model.addAttribute("pageTitle", currentPage.getTitle());
			return "admin/pages/edit";
		}
		redirectAttributes.addFlashAttribute("message", "Page edited");
		redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		// create the slug
		String slug = page.getSlug() == "" ? page.getTitle().toLowerCase().replace(" ", "-") : page.getSlug().toLowerCase().replace(" ", "-");
		// check if the slug already exist 
		Page slugExists = pageRepo.findBySlug(page.getId(), slug);
		if (slugExists != null) {
			redirectAttributes.addFlashAttribute("message", "Slug exist, chose another");
			redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
			redirectAttributes.addFlashAttribute("page", page);
		}else {
			page.setSlug(slug);
			pageRepo.save(page);
		}
		return "redirect:/admin/pages/edit/" + page.getId();
	}


    // /admin/pages/delete/1
	@GetMapping("/delete/{id}") 
	public String delete(@PathVariable int id, RedirectAttributes redirectAttributes) { 
        pageRepo.deleteById(id);
        // redirectAttributes.addFlashAttribute("message", "Page deleted");
		// redirectAttributes.addFlashAttribute("alertClass", "alert-success");
		return "redirect:/admin/pages";
    }

    @PostMapping("/reorder")
    public String reorder(@RequestParam("id[]") int[] id){
        int count = 1;
        Page page;
        for (int pageId : id) {
            page = pageRepo.getOne(pageId);
            page.setSorting(count);
            pageRepo.save(page);
            count++;
        }

        return "ok";
    }



}
